package leave.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import leave.interfaces.EmployeeRepository;
import leave.interfaces.LeaveApplicant;
import leave.interfaces.LeaveRepository;
import leave.interfaces.LeaveType;
import leave.model.Employee;
import leave.model.LeaveRequest;

// Handles an employee applying for leave. It only knows the LeaveType interface,
// so new leave types need no change here.
public class LeaveService implements LeaveApplicant {
    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveBalanceService balanceService;
    private final Map<String, LeaveType> leaveTypes = new HashMap<>(); // lookup by lower-case name

    public LeaveService(LeaveRepository leaveRepository,
                        EmployeeRepository employeeRepository,
                        LeaveBalanceService balanceService,
                        List<LeaveType> availableTypes) {
        this.leaveRepository = leaveRepository;
        this.employeeRepository = employeeRepository;
        this.balanceService = balanceService;
        for (LeaveType type : availableTypes) {
            leaveTypes.put(type.getName().toLowerCase(), type);
        }
    }

    @Override
    public LeaveRequest applyLeave(int employeeId, String leaveTypeName, LocalDate from, LocalDate to) {
        Employee employee = employeeRepository.findById(employeeId);
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found: " + employeeId);
        }

        LeaveType type = leaveTypes.get(leaveTypeName.toLowerCase());
        if (type == null) {
            throw new IllegalArgumentException("Unknown leave type: " + leaveTypeName);
        }

        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        LeaveRequest request = new LeaveRequest(employeeId, type.getName(), from, to);

        // The leave type decides its own rules
        if (!type.isValid(request)) {
            throw new IllegalArgumentException(type.getRuleDescription());
        }

        int remaining = balanceService.getRemainingBalance(employeeId, type);
        if (request.getDays() > remaining) {
            throw new IllegalArgumentException("Not enough " + type.getName()
                    + " leave balance. Requested " + request.getDays() + ", remaining " + remaining);
        }

        leaveRepository.save(request);
        return request;
    }
}

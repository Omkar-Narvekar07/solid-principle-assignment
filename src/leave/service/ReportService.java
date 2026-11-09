package leave.service;

import java.util.List;

import leave.interfaces.EmployeeRepository;
import leave.interfaces.LeaveType;
import leave.interfaces.ReportGenerator;
import leave.model.Employee;
import leave.model.LeaveStatus;
import leave.model.Role;

// HR-only report: approved, pending and remaining days per employee and leave type
public class ReportService implements ReportGenerator {
    private final EmployeeRepository employeeRepository;
    private final LeaveBalanceService balanceService;
    private final List<LeaveType> leaveTypes;

    public ReportService(EmployeeRepository employeeRepository,
                         LeaveBalanceService balanceService,
                         List<LeaveType> leaveTypes) {
        this.employeeRepository = employeeRepository;
        this.balanceService = balanceService;
        this.leaveTypes = leaveTypes;
    }

    @Override
    public void generateReport(int requesterId) {
        Employee requester = employeeRepository.findById(requesterId);
        if (requester == null || requester.getRole() != Role.HR) {
            throw new IllegalArgumentException("Only HR can generate leave reports");
        }

        System.out.println(String.format("%-8s %-8s %9s %9s %10s %10s",
                "Employee", "Type", "Approved", "Pending", "Remaining", "Allowance"));

        for (Employee employee : employeeRepository.findAll()) {
            for (LeaveType type : leaveTypes) {
                int approved = balanceService.getDaysByStatus(employee.getId(), type.getName(), LeaveStatus.APPROVED);
                int pending = balanceService.getDaysByStatus(employee.getId(), type.getName(), LeaveStatus.PENDING);
                int remaining = balanceService.getRemainingBalance(employee.getId(), type);

                System.out.println(String.format("%-8s %-8s %9d %9d %10d %10d",
                        employee.getName(), type.getName(), approved, pending,
                        remaining, type.getYearlyAllowance()));
            }
        }
    }
}

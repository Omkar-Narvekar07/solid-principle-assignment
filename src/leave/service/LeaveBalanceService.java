package leave.service;

import leave.interfaces.LeaveRepository;
import leave.interfaces.LeaveType;
import leave.model.LeaveRequest;
import leave.model.LeaveStatus;

// Only calculates leave balances, shared by LeaveService and ReportService
public class LeaveBalanceService {
    private final LeaveRepository leaveRepository;

    public LeaveBalanceService(LeaveRepository leaveRepository) {
        this.leaveRepository = leaveRepository;
    }

    public int getDaysByStatus(int employeeId, String leaveTypeName, LeaveStatus status) {
        int total = 0;
        for (LeaveRequest request : leaveRepository.findByEmployeeId(employeeId)) {
            if (request.getLeaveTypeName().equals(leaveTypeName) && request.getStatus() == status) {
                total += request.getDays();
            }
        }
        return total;
    }

    // Pending days are counted as used so an employee cannot over-apply
    // while earlier requests are still waiting for a decision.
    public int getRemainingBalance(int employeeId, LeaveType leaveType) {
        int approved = getDaysByStatus(employeeId, leaveType.getName(), LeaveStatus.APPROVED);
        int pending = getDaysByStatus(employeeId, leaveType.getName(), LeaveStatus.PENDING);
        return leaveType.getYearlyAllowance() - approved - pending;
    }
}

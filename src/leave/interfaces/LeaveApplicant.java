package leave.interfaces;

import java.time.LocalDate;

import leave.model.LeaveRequest;

// What an employee is allowed to do
public interface LeaveApplicant {
    LeaveRequest applyLeave(int employeeId, String leaveTypeName, LocalDate from, LocalDate to);
}

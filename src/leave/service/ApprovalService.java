package leave.service;

import leave.interfaces.EmployeeRepository;
import leave.interfaces.LeaveApprover;
import leave.interfaces.LeaveRepository;
import leave.interfaces.Notifier;
import leave.model.Employee;
import leave.model.LeaveRequest;
import leave.model.LeaveStatus;
import leave.model.Role;

// Handles manager approve/reject and tells the employee the outcome.
// The notification channel is injected, so it can be swapped without changes here.
public class ApprovalService implements LeaveApprover {
    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;
    private final Notifier notifier;

    public ApprovalService(LeaveRepository leaveRepository,
                           EmployeeRepository employeeRepository,
                           Notifier notifier) {
        this.leaveRepository = leaveRepository;
        this.employeeRepository = employeeRepository;
        this.notifier = notifier;
    }

    @Override
    public void approve(int requestId, int approverId) {
        decide(requestId, approverId, LeaveStatus.APPROVED);
    }

    @Override
    public void reject(int requestId, int approverId) {
        decide(requestId, approverId, LeaveStatus.REJECTED);
    }

    // Shared by approve and reject, since both follow the same checks
    private void decide(int requestId, int approverId, LeaveStatus newStatus) {
        LeaveRequest request = leaveRepository.findById(requestId);
        if (request == null) {
            throw new IllegalArgumentException("Leave request not found: " + requestId);
        }
        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalArgumentException("Request " + requestId + " is already " + request.getStatus());
        }

        Employee approver = employeeRepository.findById(approverId);
        Employee employee = employeeRepository.findById(request.getEmployeeId());
        if (approver == null) {
            throw new IllegalArgumentException("Approver not found: " + approverId);
        }

        // Only the employee's own manager may decide the request
        if (approver.getRole() != Role.MANAGER || employee.getManagerId() != approverId) {
            throw new IllegalArgumentException(approver.getName()
                    + " is not the manager of " + employee.getName());
        }

        request.setStatus(newStatus);
        leaveRepository.save(request);

        notifier.send(employee, "Your " + request.getLeaveTypeName() + " leave request #"
                + request.getId() + " (" + request.getFrom() + " to " + request.getTo()
                + ") was " + newStatus);
    }
}

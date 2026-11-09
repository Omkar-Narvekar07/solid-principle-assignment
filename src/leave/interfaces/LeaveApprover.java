package leave.interfaces;


// What a manager is allowed to do
public interface LeaveApprover {
    void approve(int requestId, int approverId);

    void reject(int requestId, int approverId);
}

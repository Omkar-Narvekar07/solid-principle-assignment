package leave.interfaces;

import java.util.List;

import leave.model.LeaveRequest;

public interface LeaveRepository {
    void save(LeaveRequest request);

    LeaveRequest findById(int id);

    List<LeaveRequest> findByEmployeeId(int employeeId);

    List<LeaveRequest> findAll();
}

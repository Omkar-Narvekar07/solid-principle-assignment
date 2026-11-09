package leave.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import leave.interfaces.LeaveRepository;
import leave.model.LeaveRequest;

// Stands in for the database. A SQL version would implement the same interface.
public class InMemoryLeaveRepository implements LeaveRepository {
    private final Map<Integer, LeaveRequest> requests = new LinkedHashMap<>();
    private int nextId = 1;

    @Override
    public void save(LeaveRequest request) {
        if (request.getId() == 0) {
            request.setId(nextId);
            nextId++;
        }
        requests.put(request.getId(), request);
    }

    @Override
    public LeaveRequest findById(int id) {
        return requests.get(id);
    }

    @Override
    public List<LeaveRequest> findByEmployeeId(int employeeId) {
        List<LeaveRequest> result = new ArrayList<>();
        for (LeaveRequest request : requests.values()) {
            if (request.getEmployeeId() == employeeId) {
                result.add(request);
            }
        }
        return result;
    }

    @Override
    public List<LeaveRequest> findAll() {
        return new ArrayList<>(requests.values());
    }
}

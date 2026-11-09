package leave.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import leave.interfaces.EmployeeRepository;
import leave.model.Employee;

public class InMemoryEmployeeRepository implements EmployeeRepository {
    private final Map<Integer, Employee> employees = new LinkedHashMap<>();

    @Override
    public void save(Employee employee) {
        employees.put(employee.getId(), employee);
    }

    @Override
    public Employee findById(int id) {
        return employees.get(id);
    }

    @Override
    public List<Employee> findAll() {
        return new ArrayList<>(employees.values());
    }
}

package leave.interfaces;

import java.util.List;

import leave.model.Employee;

public interface EmployeeRepository {
    void save(Employee employee);

    Employee findById(int id);

    List<Employee> findAll();
}

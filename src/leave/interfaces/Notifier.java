package leave.interfaces;

import leave.model.Employee;

public interface Notifier {
    void send(Employee employee, String message);
}

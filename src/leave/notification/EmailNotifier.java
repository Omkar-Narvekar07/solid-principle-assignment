package leave.notification;

import leave.interfaces.Notifier;
import leave.model.Employee;

public class EmailNotifier implements Notifier {

    @Override
    public void send(Employee employee, String message) {
        System.out.println("[EMAIL to " + employee.getName() + "] " + message);
    }
}

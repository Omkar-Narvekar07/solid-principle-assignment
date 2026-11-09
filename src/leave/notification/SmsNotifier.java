package leave.notification;

import leave.interfaces.Notifier;
import leave.model.Employee;

public class SmsNotifier implements Notifier {

    @Override
    public void send(Employee employee, String message) {
        System.out.println("[SMS to " + employee.getName() + "] " + message);
    }
}

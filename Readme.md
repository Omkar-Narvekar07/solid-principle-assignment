# Employee Leave Management System

A small Java program where employees apply for leave, managers approve or reject it, and HR can see a report. Everything is stored in memory, so nothing is saved after the program ends.

## What it does

- Employees can apply for Casual, Sick or Earned leave.
- Each leave type has its own yearly limit and its own rule.
- The remaining balance is worked out from the approved and pending requests.
- Only the employee's own manager can approve or reject a request.
- The employee gets a message (email style, printed on the console) when the status changes.
- Only HR can generate the leave report.

## How to run

You need Java 8 or above. Open a terminal in the project folder and run:

```
javac -d out -sourcepath src src/leave/Main.java
java -cp out leave.Main
```

`Main` runs a short demo with four sample people (an employee, a manager, HR and another employee) and prints what happens at each step.

## Folder structure

```
src/leave/
  Main.java        sets everything up and runs the demo
  interfaces/      all the interfaces
  model/           Employee, LeaveRequest, Role, LeaveStatus
  leavetypes/      CasualLeave, SickLeave, EarnedLeave
  repository/      in-memory storage (stands in for a database)
  notification/    EmailNotifier, SmsNotifier
  service/         the main logic
```

## How SOLID is used

**Single Responsibility.** Each class does one job. `LeaveRequest` only holds data. `LeaveService` handles applying. `ApprovalService` handles approving and rejecting. `LeaveBalanceService` only does the balance maths. `ReportService` only makes the report.

**Open/Closed.** Leave types and notifiers are interfaces. To add a new one, you write a new class. You do not edit the services. See the next section.

**Liskov Substitution.** Any `LeaveType` can be used in place of another, and any `Notifier` can be used in place of another. The services work the same with all of them.

**Interface Segregation.** The interfaces are small. `LeaveApplicant` has only apply, `LeaveApprover` has only approve and reject, and `ReportGenerator` has only the report. Nobody has to depend on methods they do not use.

**Dependency Inversion.** The services work with interfaces like `LeaveRepository` and `Notifier`, not with the real classes. Only `Main` knows which real classes are used. To use a real database later, write a new class that implements `LeaveRepository`.

## Adding a new leave type

Say we want Maternity leave. No existing class needs to change, except for one line in `Main`.

1. Create `src/leave/leavetypes/MaternityLeave.java`:

```java
package leave.leavetypes;

import leave.interfaces.LeaveType;
import leave.model.LeaveRequest;

public class MaternityLeave implements LeaveType {

    @Override
    public String getName() {
        return "Maternity";
    }

    @Override
    public int getYearlyAllowance() {
        return 90;
    }

    @Override
    public boolean isValid(LeaveRequest request) {
        return true;
    }

    @Override
    public String getRuleDescription() {
        return "No special rule for maternity leave";
    }
}
```

2. In `Main.java`, add the import and one line where the other leave types are added:

```java
leaveTypes.add(new MaternityLeave());
```

That is all. `LeaveService`, `ApprovalService` and `ReportService` pick it up on their own, and it shows up in the report.

A new notification method works the same way. Write a class that implements `Notifier` (for example `SlackNotifier`) and change `new EmailNotifier()` in `Main` to use it.



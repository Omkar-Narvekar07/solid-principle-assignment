package leave.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LeaveRequest {
    private int id; // assigned by the repository on first save
    private int employeeId;
    private String leaveTypeName;
    private LocalDate from;
    private LocalDate to;
    private LeaveStatus status;

    public LeaveRequest(int employeeId, String leaveTypeName, LocalDate from, LocalDate to) {
        this.employeeId = employeeId;
        this.leaveTypeName = leaveTypeName;
        this.from = from;
        this.to = to;
        this.status = LeaveStatus.PENDING;
    }

    // Both the first and the last day are counted
    public int getDays() {
        return (int) ChronoUnit.DAYS.between(from, to) + 1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getLeaveTypeName() {
        return leaveTypeName;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

    public LeaveStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveStatus status) {
        this.status = status;
    }
}

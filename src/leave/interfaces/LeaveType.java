package leave.interfaces;

import leave.model.LeaveRequest;

public interface LeaveType {
    String getName();

    int getYearlyAllowance();

    // Each leave type decides what a valid request looks like
    boolean isValid(LeaveRequest request);

    // Shown to the employee when isValid returns false
    String getRuleDescription();
}

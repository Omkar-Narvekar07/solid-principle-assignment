package leave.leavetypes;

import leave.interfaces.LeaveType;
import leave.model.LeaveRequest;

public class SickLeave implements LeaveType {

    @Override
    public String getName() {
        return "Sick";
    }

    @Override
    public int getYearlyAllowance() {
        return 10;
    }

    @Override
    public boolean isValid(LeaveRequest request) {
        return request.getDays() <= 5;
    }

    @Override
    public String getRuleDescription() {
        return "Sick leave can be at most 5 days at a time";
    }
}

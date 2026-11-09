package leave.leavetypes;

import leave.interfaces.LeaveType;
import leave.model.LeaveRequest;

public class CasualLeave implements LeaveType {

    @Override
    public String getName() {
        return "Casual";
    }

    @Override
    public int getYearlyAllowance() {
        return 12;
    }

    @Override
    public boolean isValid(LeaveRequest request) {
        return request.getDays() <= 3;
    }

    @Override
    public String getRuleDescription() {
        return "Casual leave can be at most 3 days at a time";
    }
}

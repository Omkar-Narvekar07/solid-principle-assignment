package leave.leavetypes;

import java.time.LocalDate;

import leave.interfaces.LeaveType;
import leave.model.LeaveRequest;

public class EarnedLeave implements LeaveType {

    @Override
    public String getName() {
        return "Earned";
    }

    @Override
    public int getYearlyAllowance() {
        return 15;
    }

    @Override
    public boolean isValid(LeaveRequest request) {
        LocalDate earliestStart = LocalDate.now().plusDays(7);
        return !request.getFrom().isBefore(earliestStart);
    }

    @Override
    public String getRuleDescription() {
        return "Earned leave must be applied at least 7 days in advance";
    }
}

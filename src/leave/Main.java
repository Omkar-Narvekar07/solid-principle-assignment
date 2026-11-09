package leave;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import leave.interfaces.EmployeeRepository;
import leave.interfaces.LeaveApplicant;
import leave.interfaces.LeaveApprover;
import leave.interfaces.LeaveRepository;
import leave.interfaces.LeaveType;
import leave.interfaces.Notifier;
import leave.interfaces.ReportGenerator;
import leave.leavetypes.CasualLeave;
import leave.leavetypes.EarnedLeave;
import leave.leavetypes.SickLeave;
import leave.model.Employee;
import leave.model.LeaveRequest;
import leave.model.Role;
import leave.notification.EmailNotifier;
import leave.repository.InMemoryEmployeeRepository;
import leave.repository.InMemoryLeaveRepository;
import leave.service.ApprovalService;
import leave.service.LeaveBalanceService;
import leave.service.LeaveService;
import leave.service.ReportService;

public class Main {

    //basic demo of all functionalities
    public static void main(String[] args) {
        
        EmployeeRepository employeeRepository = new InMemoryEmployeeRepository();
        LeaveRepository leaveRepository = new InMemoryLeaveRepository();

        List<LeaveType> leaveTypes = new ArrayList<>();
        leaveTypes.add(new CasualLeave());
        leaveTypes.add(new SickLeave());
        leaveTypes.add(new EarnedLeave());

        Notifier notifier = new EmailNotifier(); // We can switch to SMSNotifier if we want

        LeaveBalanceService balanceService = new LeaveBalanceService(leaveRepository);
        LeaveApplicant leaveService = new LeaveService(leaveRepository, employeeRepository, balanceService, leaveTypes);
        LeaveApprover approvalService = new ApprovalService(leaveRepository, employeeRepository, notifier);
        ReportGenerator reportService = new ReportService(employeeRepository, balanceService, leaveTypes);

        // Sample data: Asha and Kiran report to Ravi (manager). Meera is HR.
        employeeRepository.save(new Employee(1, "Asha", Role.EMPLOYEE, 2));
        employeeRepository.save(new Employee(2, "Ravi", Role.MANAGER, 0));
        employeeRepository.save(new Employee(3, "Meera", Role.HR, 0));
        employeeRepository.save(new Employee(4, "Kiran", Role.EMPLOYEE, 2));

        LocalDate today = LocalDate.now();

        System.out.println(" 1. Asha applies for 2 days of casual leave, Ravi approves ");
        LeaveRequest casual = leaveService.applyLeave(1, "Casual", today.plusDays(1), today.plusDays(2));
        approvalService.approve(casual.getId(), 2);

        System.out.println();
        System.out.println(" 2. Asha applies for 6 days of sick leave (breaks the 5 day rule)");
        try {
            leaveService.applyLeave(1, "Sick", today.plusDays(1), today.plusDays(6));
        } catch (IllegalArgumentException e) {
            System.out.println("Request refused: " + e.getMessage());
        }

        System.out.println();
        System.out.println(" 3. Asha applies for earned leave, Ravi rejects ");
        LeaveRequest earned = leaveService.applyLeave(1, "Earned", today.plusDays(10), today.plusDays(12));
        approvalService.reject(earned.getId(), 2);

        System.out.println();
        System.out.println(" 4. HR tries to approve Asha's request, then her manager does ");
        LeaveRequest another = leaveService.applyLeave(1, "Casual", today.plusDays(5), today.plusDays(5));
        try {
            approvalService.approve(another.getId(), 3);
        } catch (IllegalArgumentException e) {
            System.out.println("Not allowed: " + e.getMessage());
        }
        approvalService.approve(another.getId(), 2);

        System.out.println();
        System.out.println(" 5. Asha's remaining leaves ");
        for (LeaveType type : leaveTypes) {
            System.out.println(type.getName() + ": " + balanceService.getRemainingBalance(1, type) + " days");
        }

        System.out.println();
        System.out.println(" 6. Kiran tries to get the report, then HR generates it ");
        try {
            reportService.generateReport(4);
        } catch (IllegalArgumentException e) {
            System.out.println("Not allowed: " + e.getMessage());
        }
        reportService.generateReport(3);
    }
}

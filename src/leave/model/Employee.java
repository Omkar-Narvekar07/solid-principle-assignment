package leave.model;


public class Employee {
    private int id;
    private String name;
    private Role role;
    private int managerId; // 0 means this person has no manager

    public Employee(int id, String name, Role role, int managerId) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.managerId = managerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }
}

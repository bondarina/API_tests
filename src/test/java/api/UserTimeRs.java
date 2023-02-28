package api;

public class UserTimeRs extends UserTime {
    private String updatedAt;
    public UserTimeRs(String name, String job, String updatedAt) {
        super(name, job);
        this.updatedAt = updatedAt;
    }

    public UserTimeRs() {
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}

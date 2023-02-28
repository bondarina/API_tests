package api;

public class UnSuccessReg {
    public UnSuccessReg() {
    }

    private String error;

    public UnSuccessReg(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}

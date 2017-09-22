package org.lokra.weed.content;

public class ErrorMessage {

    private String error;
    private int status;
    private String reason;

    public ErrorMessage() {
    }

    public ErrorMessage(String error) {
        this.error = error;
    }

    public ErrorMessage(String error, int status, String reason) {
        this.error = error;
        this.status = status;
        this.reason = reason;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "error='" + error + '\'' +
                ", status=" + status +
                ", reason='" + reason + '\'' +
                '}';
    }
}

package es.nitaur;

public class ExceptionDetails {

    private long timestamp;
    private String method = "";
    private String path = "";
    private int status;
    private String statusText = "";
    private String exceptionClass = "";
    private String exceptionMessage = "";

    public ExceptionDetails() {
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(final String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(final int status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(final String statusText) {
        this.statusText = statusText;
    }

    public String getExceptionClass() {
        return exceptionClass;
    }

    public void setExceptionClass(final String exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(final String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

}

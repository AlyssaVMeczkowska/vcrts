package model;

import java.time.LocalDateTime;

public class Request {
    private int requestId;
    private String requestType; 
    private int userId;
    private String userName;
    private String timestamp;
    private RequestStatus status;
    private String data; 
    private String rejectionReason;
    
    public Request(int requestId, String requestType, int userId, String userName, 
                   String timestamp, RequestStatus status, String data) {
        this.requestId = requestId;
        this.requestType = requestType;
        this.userId = userId;
        this.userName = userName;
        this.timestamp = timestamp;
        this.status = status;
        this.data = data;
        this.rejectionReason = "";
    }
    

    public int getRequestId() { 
        return requestId; }
    public String getRequestType() { 
        return requestType; }
    public int getUserId() { 
        return userId; }
    public String getUserName() { 
        return userName; }
    public String getTimestamp() { 
        return timestamp; }
    public RequestStatus getStatus() { 
        return status; }
    public String getData() { 
        return data; }
    public String getRejectionReason() { 
        return rejectionReason; }
    

    public void setStatus(RequestStatus status) { 
        this.status = status; }
    public void setRejectionReason(String reason) { 
        this.rejectionReason = reason; }
}
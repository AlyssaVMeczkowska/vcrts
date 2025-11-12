package data;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import model.Request;
import model.RequestStatus;

public class RequestDataManager {
    private static final String REQUESTS_FILE = "data/pending_requests.txt";
    
    /**
     * Get the next request ID
     */
    private int getNextRequestId() {
        int maxId = 0;
        File file = new File(REQUESTS_FILE);
        if (!file.exists()) {
            return 1;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("request_id:")) {
                    try {
                        int currentId = Integer.parseInt(line.split(":")[1].trim());
                        if (currentId > maxId) {
                            maxId = currentId;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Could not parse request ID from line: " + line);
                    }
                }
            }
        } catch (IOException ex) {
            System.err.println("Error reading requests file: " + ex.getMessage());
        }
        return maxId + 1;
    }
    
    /**
     * Add a new pending request
     */
    public int addRequest(String requestType, int userId, String userName, String data) {
        int requestId = getNextRequestId();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(REQUESTS_FILE, true))) {
            writer.write("request_id: " + requestId);
            writer.newLine();
            writer.write("request_type: " + requestType);
            writer.newLine();
            writer.write("user_id: " + userId);
            writer.newLine();
            writer.write("user_name: " + userName);
            writer.newLine();
            writer.write("timestamp: " + timestamp);
            writer.newLine();
            writer.write("status: PENDING");
            writer.newLine();
            writer.write("data: " + data.replace("\n", "\\n"));
            writer.newLine();
            writer.write("rejection_reason: ");
            writer.newLine();
            writer.write("---");
            writer.newLine();
            return requestId;
        } catch (IOException ex) {
            System.err.println("Error writing to requests file: " + ex.getMessage());
            return -1;
        }
    }
    
    /**
     * Get all pending requests
     */
    public List<Request> getPendingRequests() {
        List<Request> requests = new ArrayList<>();
        File file = new File(REQUESTS_FILE);
        if (!file.exists()) {
            return requests;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Map<String, String> requestData = new HashMap<>();
            
            while ((line = reader.readLine()) != null) {
                if (line.equals("---")) {
                    if (!requestData.isEmpty() && "PENDING".equals(requestData.get("status"))) {
                        try {
                            Request request = new Request(
                                Integer.parseInt(requestData.get("request_id")),
                                requestData.get("request_type"),
                                Integer.parseInt(requestData.get("user_id")),
                                requestData.get("user_name"),
                                requestData.get("timestamp"),
                                RequestStatus.PENDING,
                                requestData.get("data").replace("\\n", "\n")
                            );
                            requests.add(request);
                        } catch (Exception e) {
                            System.err.println("Failed to parse request: " + e.getMessage());
                        }
                    }
                    requestData.clear();
                } else {
                    String[] parts = line.split(": ", 2);
                    if (parts.length == 2) {
                        requestData.put(parts[0].trim(), parts[1].trim());
                    } else if (parts.length == 1 && line.contains(":")) {
                        // Handle empty values
                        requestData.put(parts[0].replace(":", "").trim(), "");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return requests;
    }
    
    /**
     * Update request status (accept or reject)
     */
    public boolean updateRequestStatus(int requestId, RequestStatus newStatus, String rejectionReason) {
        File file = new File(REQUESTS_FILE);
        if (!file.exists()) {
            return false;
        }
        
        List<String> lines = new ArrayList<>();
        boolean updated = false;
        boolean inTargetRequest = false;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("request_id:")) {
                    int currentId = Integer.parseInt(line.split(":")[1].trim());
                    inTargetRequest = (currentId == requestId);
                }
                
                if (inTargetRequest && line.startsWith("status:")) {
                    lines.add("status: " + newStatus.toString());
                    updated = true;
                } else if (inTargetRequest && line.startsWith("rejection_reason:")) {
                    lines.add("rejection_reason: " + (rejectionReason != null ? rejectionReason : ""));
                } else {
                    lines.add(line);
                }
                
                if (line.equals("---")) {
                    inTargetRequest = false;
                }
            }
        } catch (IOException ex) {
            System.err.println("Error reading requests file: " + ex.getMessage());
            return false;
        }
        
        if (updated) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
                return true;
            } catch (IOException ex) {
                System.err.println("Error writing to requests file: " + ex.getMessage());
                return false;
            }
        }
        
        return false;
    }
    
    /**
     * Get request by ID
     */
    public Request getRequestById(int requestId) {
        File file = new File(REQUESTS_FILE);
        if (!file.exists()) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Map<String, String> requestData = new HashMap<>();
            
            while ((line = reader.readLine()) != null) {
                if (line.equals("---")) {
                    if (!requestData.isEmpty()) {
                        try {
                            int currentId = Integer.parseInt(requestData.get("request_id"));
                            if (currentId == requestId) {
                                RequestStatus status = RequestStatus.valueOf(requestData.get("status"));
                                Request request = new Request(
                                    currentId,
                                    requestData.get("request_type"),
                                    Integer.parseInt(requestData.get("user_id")),
                                    requestData.get("user_name"),
                                    requestData.get("timestamp"),
                                    status,
                                    requestData.get("data").replace("\\n", "\n")
                                );
                                String rejectionReason = requestData.get("rejection_reason");
                                if (rejectionReason != null && !rejectionReason.isEmpty()) {
                                    request.setRejectionReason(rejectionReason);
                                }
                                return request;
                            }
                        } catch (Exception e) {
                            System.err.println("Failed to parse request: " + e.getMessage());
                        }
                    }
                    requestData.clear();
                } else {
                    String[] parts = line.split(": ", 2);
                    if (parts.length == 2) {
                        requestData.put(parts[0].trim(), parts[1].trim());
                    } else if (parts.length == 1 && line.contains(":")) {
                        requestData.put(parts[0].replace(":", "").trim(), "");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
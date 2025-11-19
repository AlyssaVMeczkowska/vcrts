package data;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import model.Request;
import model.RequestStatus;

public class RequestDataManager {
    private static final String REQUESTS_FILE = "data/pending_requests.txt";

    private int getNextRequestId() {
        int maxId = 0;
        File file = new File(REQUESTS_FILE);
        if (!file.exists()) return 1;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("request_id:")) {
                    try {
                        int currentId = Integer.parseInt(line.split(":")[1].trim());
                        if (currentId > maxId) maxId = currentId;
                    } catch (NumberFormatException e) { }
                }
            }
        } catch (IOException ex) {
            System.err.println("Error reading requests file: " + ex.getMessage());
        }
        return maxId + 1;
    }
    
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
     * Returns ONLY requests with status PENDING (For Controller)
     */
    public List<Request> getPendingRequests() {
        List<Request> requests = new ArrayList<>();
        List<Request> all = getAllRequests();
        for(Request r : all) {
            if(r.getStatus() == RequestStatus.PENDING) {
                requests.add(r);
            }
        }
        return requests;
    }

    /**
     * Returns ALL requests regardless of status (For Client History)
     */
    public List<Request> getAllRequests() {
        List<Request> requests = new ArrayList<>();
        File file = new File(REQUESTS_FILE);
        if (!file.exists()) return requests;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Map<String, String> requestData = new HashMap<>();
            
            while ((line = reader.readLine()) != null) {
                if (line.equals("---")) {
                    if (!requestData.isEmpty()) {
                        try {
                             RequestStatus status = RequestStatus.PENDING;
                             try {
                                 status = RequestStatus.valueOf(requestData.getOrDefault("status", "PENDING"));
                             } catch (Exception e) {}

                             Request request = new Request(
                                Integer.parseInt(requestData.get("request_id")),
                                requestData.get("request_type"),
                                Integer.parseInt(requestData.get("user_id")),
                                requestData.get("user_name"),
                                requestData.get("timestamp"),
                                status,
                                requestData.get("data").replace("\\n", "\n")
                            );
                            String rejection = requestData.get("rejection_reason");
                            if(rejection != null) request.setRejectionReason(rejection);
                            
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
     * Updates the status of a request instead of deleting it.
     */
    public boolean updateRequestStatus(int requestId, RequestStatus newStatus, String rejectionReason) {
        File file = new File(REQUESTS_FILE);
        if (!file.exists()) return false;
        
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

    public Request getRequestById(int requestId) {
        List<Request> all = getAllRequests();
        for(Request r : all) {
            if(r.getRequestId() == requestId) return r;
        }
        return null;
    }
}
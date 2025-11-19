package ClientServer_client;

import data.RequestDataManager;
import java.io.*;
import java.net.Socket;

public class ClientJobHandler implements Runnable {

    private final Socket socket;

    public ClientJobHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("[Server-6000] Client connected: " + socket.getInetAddress());
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String receivedData = in.readLine();
            if (receivedData == null) return;
            
            StringBuilder fullPayload = new StringBuilder();
            fullPayload.append(receivedData).append("\n");
            
            String line;
            while(in.ready() && (line = in.readLine()) != null) {
                fullPayload.append(line).append("\n");
                if(line.equals("---")) break;
            }

            System.out.println("\n[Server-6000] Job Submission Received.");

            RequestDataManager requestManager = new RequestDataManager();
            
            int userId = 0;
            String[] lines = fullPayload.toString().split("\n");
            for (String l : lines) {
                if (l.startsWith("user_id:")) {
                    try { userId = Integer.parseInt(l.split(":")[1].trim());
                    } catch(Exception e){}
                }
            }

            requestManager.addRequest(
                "JOB_SUBMISSION", 
                userId, 
                "Job Client " + userId, 
                fullPayload.toString()
            );

            out.println("ACK: Job Submission Received");
            out.println("Status: ACCEPTED");
            System.out.println("[Server-6000] Job saved to pending requests.\n");

        } catch (IOException e) {
            System.err.println("Job handler error: " + e.getMessage());
        }
    }
}
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

            // 1. Parse the incoming data
            // We need to read the multi-line string sent by ClientJobSender
            // Note: Because readLine only reads one line, we need to ensure the sender
            // sends the data in a way we can capture, OR we assume the sender sends 
            // newlines replaced by a token. 
            // HOWEVER, based on your ClientJobSender, it sends a payload string. 
            // If payload has newlines, readLine() only gets the first line "type: job_submission".
            // To fix this simply without changing the Sender, we will read lines until "---"
            
            StringBuilder fullPayload = new StringBuilder();
            fullPayload.append(receivedData).append("\n");
            
            String line;
            while(in.ready() && (line = in.readLine()) != null) {
                fullPayload.append(line).append("\n");
                if(line.equals("---")) break;
            }

            System.out.println("[Server-6000] Received Job Data.");

            // 2. Save to Pending Requests for the Controller
            RequestDataManager requestManager = new RequestDataManager();
            
            // Extract User ID
            int userId = 0;
            String[] lines = fullPayload.toString().split("\n");
            for (String l : lines) {
                if (l.startsWith("user_id:")) {
                    try { userId = Integer.parseInt(l.split(":")[1].trim()); } catch(Exception e){}
                }
            }

            requestManager.addRequest(
                "JOB_SUBMISSION", 
                userId, 
                "Job Client " + userId, 
                fullPayload.toString()
            );

            // 3. Send ACK to Client so their UI updates
            out.println("ACK: Job Received");
            out.println("Status: ACCEPTED");
            System.out.println("[Server-6000] Job saved to pending requests.\n");

        } catch (IOException e) {
            System.err.println("Job handler error: " + e.getMessage());
        }
    }
}
package Server;

import data.RequestDataManager;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class VCserver
{
    private static final int PORT = 5000;
    private ServerSocket serverSocket;
    private volatile boolean running = true;

    public void start()
    {
        System.out.println("=== VCRTS Server Started on Port " + PORT + " ===");

        try
        {
            serverSocket = new ServerSocket(PORT);

            while (running)
            {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket);
                new Thread(handler).start();
            }
        }
        catch (IOException e)
        {
            if (running)
            {
                System.err.println("Server failure: " + e.getMessage());
            }
        }
    }

    public void stop()
    {
        running = false;
        try
        {
            if (serverSocket != null && !serverSocket.isClosed())
            {
                serverSocket.close();
            }
        }
        catch (IOException e)
        {
            System.err.println("Error stopping server: " + e.getMessage());
        }
    }


    private static class ClientHandler implements Runnable
    {
        private final Socket socket;

        public ClientHandler(Socket socket)
        {
            this.socket = socket;
        }

        @Override
        public void run()
        {
            System.out.println("[Server-5000] Client connected: " + socket.getInetAddress());

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true))
            {


                String firstLine = in.readLine();
                if (firstLine == null) return;


                StringBuilder payload = new StringBuilder();
                payload.append(firstLine).append("\n");

                String line;
                while (in.ready() && (line = in.readLine()) != null)
                {
                    payload.append(line).append("\n");
                    if (line.equals("---")) break;
                }

                String fullPayload = payload.toString();


                String requestType = extractRequestType(fullPayload);

                if ("vehicle_availability".equals(requestType))
                {
                    handleVehicleSubmission(fullPayload, out);
                }
                else if ("job_submission".equals(requestType))
                {
                    handleJobSubmission(fullPayload, out);
                }
                else
                {
                    out.println("ERROR: Unknown request type");
                    System.err.println("[Server-5000] Unknown request type received");
                }

            }
            catch (IOException e)
            {
                System.err.println("[Server-5000] Handler error: " + e.getMessage());
            }
        }

        private String extractRequestType(String payload)
        {
            String[] lines = payload.split("\n");
            for (String line : lines)
            {
                if (line.trim().startsWith("type:"))
                {
                    return line.split(":")[1].trim();
                }
            }
            return "unknown";
        }

        private void handleVehicleSubmission(String payload, PrintWriter out)
        {
            System.out.println("\n[Server-5000] Vehicle Availability Received.");

            RequestDataManager requestManager = new RequestDataManager();

            int userId = extractUserId(payload);

            requestManager.addRequest(
                    "VEHICLE_SUBMISSION",
                    userId,
                    "Vehicle Owner " + userId,
                    payload
            );

            out.println("ACK: Vehicle Availability Received");
            out.println("Status: ACCEPTED");
            System.out.println("[Server-5000] Vehicle submission saved to pending requests.\n");
        }

        private void handleJobSubmission(String payload, PrintWriter out)
        {
            System.out.println("\n[Server-5000] Job Submission Received.");

            RequestDataManager requestManager = new RequestDataManager();

            int userId = extractUserId(payload);

            requestManager.addRequest(
                    "JOB_SUBMISSION",
                    userId,
                    "Job Client " + userId,
                    payload
            );

            out.println("ACK: Job Submission Received");
            out.println("Status: ACCEPTED");
            System.out.println("[Server-5000] Job submission saved to pending requests.\n");
        }

        private int extractUserId(String payload)
        {
            String[] lines = payload.split("\n");
            for (String line : lines) {
                if (line.startsWith("user_id:")) {
                    try {
                        return Integer.parseInt(line.split(":")[1].trim());
                    } catch (Exception e) {
                        System.err.println("Error parsing user_id: " + e.getMessage());
                    }
                }
            }
            return 0;
        }
    }
}

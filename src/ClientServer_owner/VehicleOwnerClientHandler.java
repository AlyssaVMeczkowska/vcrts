package ClientServer_owner;

import java.io.*;
import java.net.Socket;

// Connects to Running VCControllerServer and sends information and receives a response
public class VehicleOwnerClientHandler implements Runnable
{
    private final Socket socket;
    private final RequestApprovalCallback callback;

    public VehicleOwnerClientHandler(Socket socket, RequestApprovalCallback callback)
    {
        this.socket = socket;
        this.callback = callback;
    }

    @Override
    public void run()
    {
        System.out.println("Client connected: " + socket.getInetAddress());
        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(), true
                )
        )
        {
            // Read the full payload (handling multi-line input)
            StringBuilder payloadBuilder = new StringBuilder();
            String line;
            // Read the first line to start
            String firstLine = in.readLine();
            if (firstLine == null) return;
            
            payloadBuilder.append(firstLine).append("\n");
            
            // Read rest of lines if available, loop until "---" or buffer empty
            while (in.ready() && (line = in.readLine()) != null) {
                payloadBuilder.append(line).append("\n");
                if (line.equals("---")) break;
            }

            String receivedData = payloadBuilder.toString();
            
            // UPDATED: Specific print statement for Vehicle Availability
            System.out.println("\n[Server-5000] Vehicle Availability Received.");

            // UPDATED: Specific ACK message
            out.println("ACK: Vehicle Availability Received");

            // Wait for decision from App.java (Auto-save) or Controller UI
            String decision = callback.waitForDecision(receivedData);

            if ("ACCEPT".equals(decision))
            {
                // Case 1: Real Controller clicked "Accept"
                AcceptedVehicleFileStorage.appendToFile("data/vcrts_data.txt", receivedData);
                out.println("Status: ACCEPTED");
                System.out.println("Request ACCEPTED and saved to active database.\n");
            }
            else if ("PENDING".equals(decision))
            {
                // Case 2: App.java auto-saved to Pending
                // Do NOT write to active file yet.
                // But send "ACCEPTED" to client so they know it was submitted successfully.
                out.println("Status: ACCEPTED");
                System.out.println("Request saved to PENDING queue.\n");
            }
            else
            {
                // Case 3: Rejected
                out.println("Status: REJECTED");
                System.out.println("Request REJECTED.\n");
            }

        }
        catch (IOException e)
        {
            System.err.println("Error handling client: " + e.getMessage());
        }
    }

    // Callback interface for UI integration
    public interface RequestApprovalCallback
    {
        String waitForDecision(String requestData);
    }
}
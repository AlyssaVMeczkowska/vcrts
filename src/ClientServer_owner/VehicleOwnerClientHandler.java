package ClientServer_owner;

import java.io.*;
import java.net.Socket;

//Connects to Running VCControllerServer and sends information and receives a response
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
            String receivedData = in.readLine();

            if (receivedData == null)
            {
                return;
            }

            System.out.println("\n--- Incoming Request ---");
            System.out.println(receivedData);

            out.println("ACK: Request Received");

            // Wait for UI decision via callback
            String decision = callback.waitForDecision(receivedData);

            if ("ACCEPT".equals(decision))
            {
                AcceptedVehicleFileStorage.appendToFile("data/vcrts_data.txt", receivedData);
                out.println("Status: ACCEPTED");
                System.out.println("Request ACCEPTED and saved.\n");
            }
            else
            {
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
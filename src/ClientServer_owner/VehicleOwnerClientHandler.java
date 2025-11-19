package ClientServer_owner;

import java.io.*;
import java.net.Socket;


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

            StringBuilder payloadBuilder = new StringBuilder();
            String line;

            String firstLine = in.readLine();
            if (firstLine == null) return;
            
            payloadBuilder.append(firstLine).append("\n");
            

            while (in.ready() && (line = in.readLine()) != null) {
                payloadBuilder.append(line).append("\n");
                if (line.equals("---")) break;
            }

            String receivedData = payloadBuilder.toString();
            

            System.out.println("\n[Server-5000] Vehicle Availability Received.");


            out.println("ACK: Vehicle Availability Received");


            String decision = callback.waitForDecision(receivedData);

            if ("ACCEPT".equals(decision))
            {

                AcceptedVehicleFileStorage.appendToFile("data/vcrts_data.txt", receivedData);
                out.println("Status: ACCEPTED");
                System.out.println("Request ACCEPTED and saved to active database.\n");
            }
            else if ("PENDING".equals(decision))
            {

                out.println("Status: ACCEPTED");
                System.out.println("Request saved to PENDING queue.\n");
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


    public interface RequestApprovalCallback
    {
        String waitForDecision(String requestData);
    }
}
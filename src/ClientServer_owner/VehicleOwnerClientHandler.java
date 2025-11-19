package ClientServer_owner;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

//Connects to Running VCControllerServer and sends information and recieves a response
public class VehicleOwnerClientHandler implements Runnable
{
    private final Socket socket;

    public VehicleOwnerClientHandler(Socket socket)
    {
        this.socket = socket;
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


            System.out.println("Accept or Reject this request? (A/R): ");
            Scanner scanner = new Scanner(System.in);
            String decision = scanner.nextLine().trim().toUpperCase();

            if (decision.equals("A"))
            {
                AcceptedVehicleFileStorage.appendToFile("vehicle_storage.txt", receivedData);
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
}

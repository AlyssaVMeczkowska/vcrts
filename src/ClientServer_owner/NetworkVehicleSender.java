package ClientServer_owner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

//Sends New info to the VCControllerServer
public class NetworkVehicleSender
{
    public static boolean sendVehiclePayload(String payload)
    {
        try (Socket socket = new Socket("localhost", 5000);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())))
        {

            // Send vehicle info
            out.println(payload);

            // Get Acknoledgement
            String ack = in.readLine();
            System.out.println("SERVER: " + ack);

            // Read final result
            String result = in.readLine();
            System.out.println("SERVER: " + result);

            return result != null && result.contains("ACCEPTED");

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}


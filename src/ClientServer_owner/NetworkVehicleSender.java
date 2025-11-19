package ClientServer_owner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class NetworkVehicleSender
{
    public static boolean sendVehiclePayload(String payload)
    {
        try (Socket socket = new Socket("localhost", 5000);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())))
        {


            out.println(payload);

            String ack = in.readLine();
            System.out.println("SERVER: " + ack);

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


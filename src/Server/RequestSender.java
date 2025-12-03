package Server;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class RequestSender
{
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5050;


    public static boolean sendRequest(String payload)
    {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
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
            System.err.println("Error sending request to server: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public static boolean sendVehicleSubmission(String payload)
    {
        return sendRequest(payload);
    }


    public static boolean sendJobSubmission(String payload)
    {
        return sendRequest(payload);
    }
}


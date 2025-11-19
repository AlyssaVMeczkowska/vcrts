package ClientServer_owner;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//Starts Server For Owner Communication
public class VCControllerServer
{
    private static final int PORT = 5000;

    public static void main(String[] args)
    {
        System.out.println("VC Controller Server started on port " + PORT);

        //Uses Threading
        try (ServerSocket serverSocket = new ServerSocket(PORT))
        {
            while (true)
            {
                Socket clientSocket = serverSocket.accept();

                VehicleOwnerClientHandler handler =
                        new VehicleOwnerClientHandler(clientSocket);

                new Thread(handler).start();
            }
        }
        catch (IOException e)
        {
            System.err.println("Server failure: " + e.getMessage());
        }
    }
}

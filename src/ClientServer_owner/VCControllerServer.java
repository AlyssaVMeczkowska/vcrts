package ClientServer_owner;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class VCControllerServer implements Runnable
{
    private static final int PORT = 5000;
    private final VehicleOwnerClientHandler.RequestApprovalCallback callback;
    private ServerSocket serverSocket;
    private volatile boolean running = true;

    public VCControllerServer(VehicleOwnerClientHandler.RequestApprovalCallback callback)
    {
        this.callback = callback;
        System.out.println(">>> VCControllerServer instance created <<<");

    }

    @Override
    public void run()
    {
        System.out.println("VC Controller Server started on port " + PORT);

        try
        {
            serverSocket = new ServerSocket(PORT);
            
            while (running)
            {
                Socket clientSocket = serverSocket.accept();

                VehicleOwnerClientHandler handler =
                        new VehicleOwnerClientHandler(clientSocket, callback);

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
}
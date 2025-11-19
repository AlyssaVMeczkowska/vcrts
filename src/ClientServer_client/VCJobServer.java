package ClientServer_client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class VCJobServer {

    private static final int PORT = 6000; 

    public static void main(String[] args) {
        System.out.println("VC Job Server started on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientJobHandler handler = new ClientJobHandler(clientSocket);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.err.println("Job Server Failure: " + e.getMessage());
        }
    }
}

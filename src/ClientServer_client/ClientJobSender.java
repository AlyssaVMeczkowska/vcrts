package ClientServer_client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientJobSender {

    public static boolean sendJobPayload(String payload) {
        try (Socket socket = new Socket("localhost", 6000);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send the multi-line payload
            out.println(payload);
            
            // Important: If the payload doesn't end with a newline that forces a flush, 
            // or if logic requires it, ensure we flush. 
            // (PrintWriter with autoFlush=true usually handles println well).
            
            // Wait for response
            String ack = in.readLine();
            System.out.println("SERVER: " + ack);

            String result = in.readLine();
            System.out.println("SERVER: " + result);
            
            return result != null && result.contains("ACCEPTED");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
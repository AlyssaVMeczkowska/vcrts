package ClientServer_client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientJobHandler implements Runnable {

    private final Socket socket;

    public ClientJobHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("Client connected: " + socket.getInetAddress());

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String receivedData = in.readLine();
            if (receivedData == null) return;

            System.out.println("\n--- Incoming Job Request ---");
            System.out.println(receivedData);

            out.println("ACK: Job Received");

            System.out.print("Accept or Reject this job? (A/R): ");
            Scanner scanner = new Scanner(System.in);
            String decision = scanner.nextLine().trim().toUpperCase();

            if (decision.equals("A")) {
                AcceptedJobFileStorage.appendToFile("job_storage.txt", receivedData);
                out.println("Status: ACCEPTED");
                System.out.println("Job ACCEPTED and saved.\n");
            } else {
                out.println("Status: REJECTED");
                System.out.println("Job REJECTED.\n");
            }

        } catch (IOException e) {
            System.err.println("Job handler error: " + e.getMessage());
        }
    }
}

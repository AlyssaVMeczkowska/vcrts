import ClientServer_client.ClientJobHandler;
import ClientServer_owner.VCControllerServer;
import ClientServer_owner.VehicleOwnerClientHandler;
import data.RequestDataManager;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.SwingUtilities;
import ui.LoginPage;

public class App {

    public static void main(String[] args) {
        // 1. Start Vehicle Server (Port 5000)
        new Thread(App::startVehicleServer).start();

        // 2. Start Job Server (Port 6000)
        new Thread(App::startJobServer).start();

        // 3. Launch GUI
        SwingUtilities.invokeLater(() -> {
            LoginPage loginPage = new LoginPage();
            loginPage.setVisible(true);
        });
    }

    private static void startVehicleServer() {
        VehicleOwnerClientHandler.RequestApprovalCallback autoSaveCallback = (vehiclePayload) -> {
            System.out.println("[Server-5000] Auto-saving vehicle request to Pending.");
            
            RequestDataManager requestManager = new RequestDataManager();
            
            // Extract User ID for the log
            int userId = 0;
            try {
                String[] lines = vehiclePayload.split("\n");
                for (String line : lines) {
                    if (line.startsWith("user_id:")) {
                        userId = Integer.parseInt(line.split(":")[1].trim());
                        break;
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }

            // Save to PENDING database
            requestManager.addRequest(
                "VEHICLE_SUBMISSION", 
                userId, 
                "Vehicle Owner " + userId, 
                vehiclePayload
            );

            // Return "PENDING" so the handler knows we handled it, 
            // but it shouldn't write to the active file yet.
            return "PENDING"; 
        };

        VCControllerServer server = new VCControllerServer(autoSaveCallback);
        server.run();
    }

    private static void startJobServer() {
        int port = 6000;
        System.out.println("VC Job Server started on port " + port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
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
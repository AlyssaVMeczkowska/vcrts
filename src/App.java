import Server.VCserver;
import data.DatabaseManager;
import javax.swing.SwingUtilities;
import ui.LoginPage;

public class App {

    public static void main(String[] args) {
        
        // 1. Initialize Database (Run SQL Script)
        // This ensures tables exist before the app tries to use them
        System.out.println("--- Booting VCRTS ---");
        DatabaseManager dbManager = DatabaseManager.getInstance();
        
        // Ensure this path matches your project structure
        // If your 'scripts' folder is in the root of the project:
        dbManager.initializeDatabase("scripts/vcrts_schema.sql");

        // 2. Start the Background Server
        new Thread(App::startVCServer).start();

        // 3. Launch the GUI
        SwingUtilities.invokeLater(() -> {
            LoginPage loginPage = new LoginPage();
            loginPage.setVisible(true);
        });
    }

    private static void startVCServer() {
        VCserver server = new VCserver();
        server.start();
    }
}
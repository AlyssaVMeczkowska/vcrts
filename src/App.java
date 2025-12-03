import Server.VCserver;
import data.DatabaseManager;
import javax.swing.SwingUtilities;
import ui.LoginPage;

public class App {

    public static void main(String[] args) {
        
        System.out.println("--- Booting VCRTS ---");
        DatabaseManager dbManager = DatabaseManager.getInstance();
        
        dbManager.initializeDatabase("scripts/vcrts_schema.sql");

        new Thread(App::startVCServer).start();

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
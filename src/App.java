import ClientServer.VCserver;
import data.RequestDataManager;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.SwingUtilities;
import ui.LoginPage;

public class App {

    public static void main(String[] args)
    {

        new Thread(App::startVCServer).start();

        SwingUtilities.invokeLater(() -> {
            LoginPage loginPage = new LoginPage();
            loginPage.setVisible(true);
        });
    }

    private static void startVCServer()
    {
        VCserver server = new VCserver();
        server.start();
    }
}
import javax.swing.SwingUtilities;
import ui.LoginPage;

public class App {


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginPage loginPage = new LoginPage();
            loginPage.setVisible(true);
        });
    }
}

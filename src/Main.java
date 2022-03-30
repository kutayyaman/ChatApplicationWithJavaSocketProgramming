import GUIS.LoginGUI;
import Repository.Impl.UserRepositoryPostgre;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginGUI loginGUI = new LoginGUI(new UserRepositoryPostgre());
                loginGUI.setVisible(true);
            }
        });
    }
}

import GUIS.LoginGUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginGUI loginGUI = new LoginGUI();
                loginGUI.setVisible(true);
            }
        });
    }
}

package GUIS;

import javax.swing.*;

public class LoginGUI extends JFrame {
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameTextField;
    private JTextField passwordTextField;
    private JPanel panel;
    private JButton loginButton;
    private JButton registerButton;

    public LoginGUI(){
        add(panel);
        setSize(400,200);
        setTitle("Login Screen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}

package GUIS;

import Entity.User;
import Repository.UserRepository;
import Utils.StringUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameTextField;
    private JTextField passwordTextField;
    private JPanel panel;
    private JButton loginButton;
    private JButton registerButton;
    private final UserRepository userRepository;

    public LoginGUI(UserRepository userRepository) {
        this.userRepository = userRepository;
        add(panel);
        setSize(400, 200);
        setTitle("Login Screen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = usernameTextField.getText();
                String password = passwordTextField.getText();
                Boolean isTheUserExist = userRepository.isTheUserExist(userName, password);
                User user = userRepository.getByUserName(userName);
                if (isTheUserExist) {
                    ClientChatGUI clientChatGUI = new ClientChatGUI(user);
                    dispose();
                    clientChatGUI.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Check your username and password");
                }
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = usernameTextField.getText();
                String password = passwordTextField.getText();
                Boolean isUserNameValid = !StringUtil.isNullOrEmpty(userName);
                Boolean isPasswordValid = !StringUtil.isNullOrEmpty(password);
                if (!isUserNameValid) {
                    JOptionPane.showMessageDialog(null, "Username cannot be empty");
                    return;
                } else if (!isPasswordValid) {
                    JOptionPane.showMessageDialog(null, "Password cannot be empty");
                    return;
                }
                User user = userRepository.add(new User(userName, password));
                if (user == null || user.getId() == null || user.getId() == 0) {
                    return;
                }
                JOptionPane.showMessageDialog(null, "User created");
            }
        });
    }
}

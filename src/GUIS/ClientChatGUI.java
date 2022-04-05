package GUIS;

import Entity.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientChatGUI extends JFrame {
    private JPanel panel1;
    private JLabel labelUsername;
    private JTextField textFieldMessage;
    private JButton buttonSend;
    private JTextArea textAreaChat;
    private User user;

    public ClientChatGUI(User user) {
        this.user = user;
        add(panel1);
        setSize(800, 800);
        setTitle(String.format("%d %s", user.getId(), user.getUserName()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        labelUsername.setText(user.getUserName());
        buttonSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

    }
}

package GUIS;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerChatGUI extends JFrame {
    private JPanel panel1;
    private JTextArea textAreaChat;
    private JButton buttonSend;
    private JTextField textFieldMessage;

    public ServerChatGUI() {
        add(panel1);
        setSize(800, 800);
        setTitle("Server Chat Screen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        buttonSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String args[]) {
        ServerChatGUI serverChatGUI = new ServerChatGUI();
        serverChatGUI.setVisible(true);
    }
}

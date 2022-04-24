package GUIS;

import Entity.GlobalChatMessage;
import Entity.User;
import Repository.GlobalChatMessageRepository;
import Socket.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GlobalChatGUI extends JFrame {
    private JPanel panel;
    private JTextArea textAreaGlobalChat;
    private JTextField textFieldMessageToSend;
    private JButton buttonSend;
    private JTextArea textAreaOnlineUsers;
    private User user;
    private Client client;
    private GlobalChatMessageRepository globalChatMessageRepository;

    public GlobalChatGUI(User user, Client client, GlobalChatMessageRepository globalChatMessageRepository) {
        add(panel);
        setSize(400, 200);
        setTitle("Global Chat");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.user = user;
        this.client = client;
        this.globalChatMessageRepository = globalChatMessageRepository;

        updateChat();
        client.sendGetOnlineUserRequestToServer();

        buttonSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String messageBody = textFieldMessageToSend.getText();
                GlobalChatMessage globalChatMessage = new GlobalChatMessage(messageBody, user.getId(), user.getUserName());
                globalChatMessageRepository.add(globalChatMessage);
                String message = String.format("%s: %s\n", globalChatMessage.getSenderUserName(), globalChatMessage.getBody());
                textAreaGlobalChat.setText(textAreaGlobalChat.getText() + message);
                client.sendMessage(messageBody, 0);

                textFieldMessageToSend.setText("");
            }
        });
    }

    public void updateChat() {
        List<GlobalChatMessage> allMessages = globalChatMessageRepository.getAll();
        String allMessagesAsString = "";
        for (GlobalChatMessage globalChatMessage : allMessages) {
            String message = String.format("%s: %s\n", globalChatMessage.getSenderUserName(), globalChatMessage.getBody());
            allMessagesAsString += message;
        }
        textAreaGlobalChat.setText(allMessagesAsString);
    }

    public void updateOnlineUserTextArea(String onlineUsers) {
        String onlineUsersTextAreaString = "";
        String[] tagAndUsers = onlineUsers.split(":");
        String[] onlineUsersArray = tagAndUsers[1].split(",");

        for (String onlineUser : onlineUsersArray) {
            onlineUsersTextAreaString += String.format("%s\n", onlineUser);
        }
        textAreaOnlineUsers.setText(onlineUsersTextAreaString);
    }

}

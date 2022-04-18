package GUIS;

import Entity.Chat;
import Entity.Message;
import Entity.User;
import Repository.ChatRepository;
import Repository.MessageRepository;
import Socket.Client;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ClientChatGUI extends JFrame {
    private JPanel panel1;
    private JLabel labelUsername;
    private JTextField textFieldMessage;
    private JButton buttonSend;
    private JTextArea textAreaChat;
    private JList chatJList;
    private JButton buttonCreateChat;
    private User user;
    private ChatRepository chatRepository;
    private MessageRepository messageRepository;
    private List<Chat> chats;
    Integer selectedChatId = null;
    Client client;
    ClientChatGUI clientChatGUI;


    public ClientChatGUI(User user, ChatRepository chatRepository, MessageRepository messageRepository) {
        this.clientChatGUI = this;
        this.user = user;
        add(panel1);
        setSize(450, 450);
        setTitle(String.format("%d %s", user.getId(), user.getUserName()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        labelUsername.setText(user.getUserName());
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        updateChatList();

        try {
            Socket socket = new Socket("localhost", 1234);
            this.client = new Client(socket, user.getUserName(), this);
            this.client.listenForMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }


        buttonSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessageToTheSelectedChat(textFieldMessage.getText());
                //client.sendMessage(selectedChatId);
                textFieldMessage.setText("");
            }
        });

        chatJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateSelectedChatTextAreaByJListSelectedItem();
            }
        });

        buttonCreateChat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChatCreateGUI chatCreateGUI = new ChatCreateGUI(user, clientChatGUI);
                chatCreateGUI.setVisible(true);
            }
        });
    }

    public void sendMessageToAChat(String messageBody, Integer chatId) {
        Message message = new Message(null, messageBody, user.getId(), chatId, user.getUserName());
        messageRepository.add(message);
        /*updateChatList();
        updateSelectedChatTextArea();*/
        client.sendMessage(messageBody, selectedChatId);
    }

    private void sendMessageToTheSelectedChat(String messageBody) {
        sendMessageToAChat(messageBody, this.selectedChatId);
    }

    public void updateSelectedChatTextArea() {
        if (selectedChatId == null || selectedChatId == 0) {
            return;
        }
        Chat selectedChat = chats.stream().filter(chat -> chat.getId() == selectedChatId).collect(Collectors.toList()).get(0);
        List<Message> messages = selectedChat.getMessages().stream().sorted(Comparator.comparingInt(Message::getId)).collect(Collectors.toList());
        String textAreaChatContent = messages.size() > 0 ? "" : "This chat has no message yet";
        for (Message message : messages) {
            textAreaChatContent += String.format("%s : %s\n", message.getSenderUserName(), message.getBody());
        }
        textAreaChat.setText(textAreaChatContent);
    }

    private void updateSelectedChatTextAreaByJListSelectedItem() {
        Object selectedValue = chatJList.getSelectedValue();
        if (selectedValue == null) {
            return;
        }
        String selectedChatString = selectedValue.toString();
        if (!selectedChatString.contains("-")) {
            return;
        }
        selectedChatId = Integer.valueOf(selectedChatString.split("-")[0]);
        updateSelectedChatTextArea();
    }

    public void updateChatList() {
        this.chats = chatRepository.getAllByAccountIdWithMessages(user.getId());
        DefaultListModel model = new DefaultListModel();
        for (Chat chat : chats) {
            model.addElement(chat.toString());
        }
        chatJList.setModel(model);
    }

    public void updateChatListAndSelectedChatTextArea() {
        updateChatList();
        updateSelectedChatTextArea();
    }
}

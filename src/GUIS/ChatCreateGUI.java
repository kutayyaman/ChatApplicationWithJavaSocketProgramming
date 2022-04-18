package GUIS;

import Entity.Chat;
import Entity.User;
import Repository.ChatRepository;
import Repository.Impl.ChatRepositoryImpl;
import Repository.Impl.UserRepositoryPostgre;
import Repository.UserRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ChatCreateGUI extends JFrame {
    private JPanel panel;
    private JTable userTable;
    private JButton createButton;
    private JButton cancelButton;
    private JTextField textFieldChatName;
    private JLabel chatNameLabel;
    private UserRepository userRepository;
    private ChatRepository chatRepository;
    private User creatorUser;

    public ChatCreateGUI(User creatorUser, ClientChatGUI clientChatGUI) {
        add(panel);
        setSize(400, 200);
        setTitle("Create Chat Screen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        userRepository = new UserRepositoryPostgre();
        chatRepository = new ChatRepositoryImpl();
        this.creatorUser = creatorUser;

        DefaultTableModel model = new DefaultTableModel() {
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return Boolean.class;
                    case 1:
                        return String.class;
                    case 2:
                        return Integer.class;
                    default:
                        return String.class;
                }
            }
        };

        userTable.setModel(model);
        model.addColumn("Select");
        model.addColumn("User Name");
        model.addColumn("Id");

        List<User> users = userRepository.getAll();

        int i = 0;
        for (User user : users) {
            if(user.getUserName().equals(creatorUser.getUserName())){
                continue;
            }
            model.addRow(new Object[0]);
            model.setValueAt(false, i, 0);
            model.setValueAt(user.getUserName(), i, 1);
            model.setValueAt(user.getId(),i,2);
            i++;
        }
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<User> selectedUsers = new ArrayList<>();
                for(int i =0;i<userTable.getRowCount();i++){
                    Boolean checked = Boolean.valueOf(userTable.getValueAt(i,0).toString());
                    if(checked){
                        String userName = userTable.getValueAt(i,1).toString();
                        Integer id = (Integer) userTable.getValueAt(i,2);
                        User user = new User(userName,id);
                        selectedUsers.add(user);
                    }
                }
                Chat newChat = chatRepository.createAChat(selectedUsers, creatorUser, textFieldChatName.getText());
                int input = JOptionPane.showConfirmDialog(null, "Chat has been created", "Success", JOptionPane.DEFAULT_OPTION);
                //clientChatGUI.updateChatListAndSelectedChatTextArea();
                clientChatGUI.selectedChatId = newChat.getId();
                clientChatGUI.sendMessageToAChat("Chat is created by me", newChat.getId());
                dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

}

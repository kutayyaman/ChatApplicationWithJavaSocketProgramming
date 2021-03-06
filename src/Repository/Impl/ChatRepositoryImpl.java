package Repository.Impl;

import DatabaseConnection.Database;
import Entity.Chat;
import Entity.Message;
import Entity.User;
import Repository.ChatRepository;
import Utils.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatRepositoryImpl implements ChatRepository {
    @Override
    public List<Chat> getAllByAccountIdWithMessages(Integer accountId) {
        String SQL = "Select c.id, m.id, m.body, m.sender_account_id, m.sender_user_name, c.chat_name from Chat c " +
                "INNER JOIN Account_Chat AC on c.id = AC.chat_id " +
                "LEFT OUTER JOIN Message M on c.id = M.chat_id " +
                "WHERE AC.account_id = ? " +
                "ORDER BY c.id DESC";
        List<Chat> result = new ArrayList<>();
        try {
            Connection connection = Database.connect();
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            pstmt.setInt(1, accountId);
            ResultSet rst = pstmt.executeQuery();

            result = getAllChatFromResultSet(rst);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.disconnect();
        }
        return result;
    }

    @Override
    public Chat createAChat(List<User> users, User creatorUser, String chatName) {
        users.add(creatorUser);
        String SQL = "insert into chat(chat_name) values (?)";
        String accountChatSQL = "INSERT INTO account_chat(account_id, chat_id) VALUES (?,?)";
        Integer chatId = null;
        try {
            Connection connection = Database.connect();
            PreparedStatement pstmt = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, chatName);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                chatId = DatabaseUtils.getIdFromAddResult(pstmt);
            } else {
                throw new IllegalArgumentException("chat could not create");
            }
            for (User user : users) {
                Integer userId = user.getId();
                pstmt = connection.prepareStatement(accountChatSQL);
                pstmt.setInt(1, userId);
                pstmt.setInt(2, chatId);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.disconnect();
        }
        return new Chat(chatId);
    }

    private List<Chat> getAllChatFromResultSet(ResultSet rst) throws SQLException {
        List<Chat> result = new ArrayList<>();

        Chat chat = new Chat();
        while (rst.next()) {
            Integer chatIdFromDB = rst.getInt(1);
            Integer messageId = rst.getInt(2);
            String messageBody = rst.getString(3);
            Integer senderAccountId = rst.getInt(4);
            String senderUserName = rst.getString(5);
            String chatName = rst.getString(6);
            Message message = new Message(messageId, messageBody, senderAccountId, chatIdFromDB,senderUserName);

            if (chat.getId() == null) {
                chat.setId(chatIdFromDB);
                chat.setChatName(chatName);
            }
            if (chat.getId() != chatIdFromDB) {
                result.add(chat);
                chat = new Chat();
                chat.setId(chatIdFromDB);
                chat.setChatName(chatName);
            }
            List<Message> messages = chat.getMessages();
            if (messages == null) {
                messages = new ArrayList<>();
            }
            if(messageId!=0){
                messages.add(message);
            }
            chat.setMessages(messages);
        }
        if(chat.getId()!=null && chat.getId()!=0){
            result.add(chat);
        }
        return result;
    }
}

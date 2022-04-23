package Repository.Impl;

import DatabaseConnection.Database;
import Entity.GlobalChatMessage;
import Entity.User;
import Repository.GlobalChatMessageRepository;
import Utils.DatabaseUtils;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GlobalChatMessageRepositoryImpl implements GlobalChatMessageRepository {

    @Override
    public GlobalChatMessage add(GlobalChatMessage globalChatMessage) {
        int id = 0;
        try {
            Connection connection = Database.connect();
            PreparedStatement pstmt = getAddPreparedStatement(globalChatMessage, connection);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                id = DatabaseUtils.getIdFromAddResult(pstmt);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.out.println(e.getMessage());
        } finally {
            Database.disconnect();
        }
        globalChatMessage.setId(id);
        return globalChatMessage;
    }

    @Override
    public List<GlobalChatMessage> getAll() {
        List<GlobalChatMessage> globalChatMessages = new ArrayList<>();
        String SQL = "SELECT * from global_chat_messages ";
        try {
            Connection connection = Database.connect();
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            ResultSet rst = pstmt.executeQuery();
            while (rst.next()) {
                GlobalChatMessage globalChatMessage = getGlobalChatMessageFromResultSet(rst);
                globalChatMessages.add(globalChatMessage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.disconnect();
        }
        return globalChatMessages;
    }

    private GlobalChatMessage getGlobalChatMessageFromResultSet(ResultSet rst) throws SQLException {
        GlobalChatMessage globalChatMessage;
        Integer id = rst.getInt(1);
        String body = rst.getString(2);
        Integer senderAccountId = rst.getInt(3);
        String senderUserName = rst.getString(4);
        globalChatMessage = new GlobalChatMessage(id,body,senderAccountId,senderUserName);
        return globalChatMessage;
    }

    private PreparedStatement getAddPreparedStatement(GlobalChatMessage globalChatMessage, Connection connection) throws SQLException {
        String SQL = "insert into global_chat_messages(body, sender_account_id, sender_user_name) values (?,?,?)";
        PreparedStatement pstmt = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, globalChatMessage.getBody());
        pstmt.setInt(2, globalChatMessage.getSender_account_id());
        pstmt.setString(3, globalChatMessage.getSenderUserName());
        return pstmt;
    }

}

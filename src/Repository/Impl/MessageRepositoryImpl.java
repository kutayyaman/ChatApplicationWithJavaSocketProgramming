package Repository.Impl;

import DatabaseConnection.Database;
import Entity.Message;
import Repository.MessageRepository;
import Utils.DatabaseUtils;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class MessageRepositoryImpl implements MessageRepository {

    @Override
    public Message add(Message message) {
        int id = 0;
        try {
            Connection connection = Database.connect();
            PreparedStatement pstmt = getAddPreparedStatement(message, connection);
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
        message.setId(id);
        return message;
    }

    private PreparedStatement getAddPreparedStatement(Message message, Connection connection) throws SQLException {
        String SQL = "insert into message(body, sender_account_id, chat_id) values (?,?,?)";
        PreparedStatement pstmt = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, message.getBody());
        pstmt.setInt(2, message.getSender_account_id());
        pstmt.setInt(3, message.getChat_id());
        return pstmt;
    }
}

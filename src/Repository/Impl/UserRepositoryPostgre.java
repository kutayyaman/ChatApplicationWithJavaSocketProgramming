package Repository.Impl;

import DatabaseConnection.Database;
import Entity.User;
import Repository.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryPostgre implements UserRepository {

    @Override
    public User add(User user) {
        int id = 0;
        try {
            Connection connection = Database.connect();
            PreparedStatement pstmt = getAddPreparedStatement(user, connection);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                id = getIdFromAddResult(pstmt);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            Database.disconnect();
        }
        user.setId(id);
        return user;
    }

    @Override
    public User getByUserName(String userName) {
        User user = null;
        String SQL = "SELECT * from Account " +
                "Where user_name = ?";
        try {
            Connection connection = Database.connect();
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            pstmt.setString(1, userName);
            ResultSet rst = pstmt.executeQuery();
            while (rst.next()) {
                user = getUserFromResultSet(rst);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.disconnect();
        }
        return user;
    }

    @Override
    public Boolean isTheUserExist(String userName, String password) {
        String SQL = "SELECT * from Account " +
                "Where user_name = ? " +
                "And password = ?";
        try {
            Connection connection = Database.connect();
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            pstmt.setString(1, userName);
            pstmt.setString(2, password);
            ResultSet rst = pstmt.executeQuery();
            if (rst.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.disconnect();
        }
        return false;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String SQL = "SELECT * from Account ";
        try {
            Connection connection = Database.connect();
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            ResultSet rst = pstmt.executeQuery();
            while (rst.next()) {
                User user = getUserFromResultSet(rst);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.disconnect();
        }
        return users;
    }

    private User getUserFromResultSet(ResultSet rst) throws SQLException {
        User user;
        String userName;
        int id = rst.getInt(1);
        userName = rst.getString(2);
        String password = rst.getString(3);
        user = new User(id, userName, password);
        return user;
    }

    private PreparedStatement getAddPreparedStatement(User user, Connection connection) throws SQLException {
        String SQL = "INSERT INTO Account(user_name,password) "
                + "VALUES(?,?)";

        PreparedStatement pstmt = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, user.getUserName());
        pstmt.setString(2, user.getPassword());
        return pstmt;
    }

    private int getIdFromAddResult(PreparedStatement pstmt) {
        int id = 0;
        try (ResultSet rs = pstmt.getGeneratedKeys()) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }
}

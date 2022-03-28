package DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private final String jdbcURL = "jdbc:postgresql://localhost:5432/ChatApplication";
    private final String username = "postgres";
    private final String password = "admin";
    private Connection connection;

    public void connect(){
        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connected to PostgreSQL server");
        } catch (SQLException e) {
            System.out.println("Error in connecting to PostgreSQL server");
            e.printStackTrace();
        }
    }

    public void disconnect(){
        try {
            connection.close();
            System.out.println("Disconnected from PostgreSQL server");
        } catch (SQLException e) {
            System.out.println("An error occurred while terminating the connection. ");
            e.printStackTrace();
        }
    }
}

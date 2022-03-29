package DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String jdbcURL = "jdbc:postgresql://localhost:5432/ChatApplication";
    private static final String username = "postgres";
    private static final String password = "admin";
    private static Connection connection = null;

    public static Connection connect() {
        try {
            if (connection == null) {
                connection = DriverManager.getConnection(jdbcURL, username, password);
                System.out.println("Connected to PostgreSQL server");
            } else {
                System.out.println("You have already established connection");
            }

        } catch (SQLException e) {
            System.out.println("Error in connecting to PostgreSQL server");
            e.printStackTrace();
        }

        return connection;
    }

    public static void disconnect() {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
                System.out.println("Disconnected from PostgreSQL server");
            } else {
                System.out.println("You didn't already have an established connection");
            }

        } catch (SQLException e) {
            System.out.println("An error occurred while terminating the connection. ");
            e.printStackTrace();
        }
    }
}

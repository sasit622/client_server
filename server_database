package chat.database_send;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/receiver_db"; // Receiver database URL
    private static final String USER = "root";  // Replace with your DB username
    private static final String PASS = "(@Sasi_0077)";  // Replace with your DB password

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASS);
        conn.setAutoCommit(true);  // Ensure auto-commit is on
        System.out.println("Database connection to receiver_db established.");
        return conn;
    }
}

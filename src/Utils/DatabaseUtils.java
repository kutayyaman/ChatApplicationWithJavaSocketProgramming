package Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUtils {
    public static Integer getIdFromAddResult(PreparedStatement pstmt) {
        Integer id = null;
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

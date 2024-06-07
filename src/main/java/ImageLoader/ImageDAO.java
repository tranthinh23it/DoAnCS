package ImageLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ImageDAO {
    public void saveImageToDatabase(byte[] imageBytes) {
        String insertSQL = "INSERT INTO Users (AvatarURL) VALUES (?)";
        //chua co where de update
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setBytes(1, imageBytes);
            pstmt.executeUpdate();
            System.out.println("Image saved to database.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void getImageFromDatabase() {
        String selectSQL = "SELECT AvatarURL FROM Users";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {

            pstmt.executeQuery();
            System.out.println("Image get from database.");

        } catch (SQLException e) {
            e.printStackTrace();
            }
    }
}

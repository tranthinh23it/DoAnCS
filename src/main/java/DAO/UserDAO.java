package DAO;

import DTO.Users;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private static SessionFactory sessionFactory = Connected.getSessionFactory();

    // JDBC
    public static void saveImageToDatabase(String username, byte[] image) {
        String sql = "UPDATE Users SET AvatarURL = ? WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=ChattingApplication;user=sa;password=12345;encrypt=false;");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBytes(1, image);
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lưu hình ảnh vào cơ sở dữ liệu", e);
        }
    }

    public static Image getImage(String username) {
        String sql = "SELECT AvatarURL FROM Users WHERE username = ?";
        Image image = null;
        try (Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=ChattingApplication;user=sa;password=12345;encrypt=false;");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    byte[] imageData = rs.getBytes("AvatarURL");
                    if (imageData != null) {
                        ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
                        image = new Image(bis);
                    }
                }
            }
            if (image == null) {
                image = new Image("file:E:/KY2/nhaps/DoAnCS/src/main/resources/Img/null_avt.png");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi truy xuất hình ảnh từ cơ sở dữ liệu", e);
        }
        return image;
    }

    // Hibernate
}

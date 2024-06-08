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
    private static SessionFactory sessionFactory;

    static {
        Connected.connection(Users.class);
        sessionFactory = Connected.getSessionFactory();
    }

    // JDBC
    public static void saveImageToDatabase(String username, byte[] image) {
        String sql = "UPDATE Users SET AvatarURL = ? WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=ChattingApplication;user=sa;password=123;encrypt=false;");
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
                "jdbc:sqlserver://localhost:1433;databaseName=ChattingApplication;user=sa;password=123;encrypt=false;");
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
    public static void saveImageToDatabaseHibernate(String username, byte[] image) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Users user = session.get(Users.class, username);
                if (user != null) {
                    user.setAvatarUrl(image);
                    session.update(user);
                    transaction.commit();
                }
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw new RuntimeException("Lỗi khi lưu hình ảnh vào cơ sở dữ liệu bằng Hibernate", e);
            }
        }
    }

    public static Image getImageHibernate(String username) {
        Image image = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Users user = session.get(Users.class, username);
                if (user != null) {
                    byte[] imageData = user.getAvatarUrl();
                    if (imageData != null) {
                        ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
                        image = new Image(bis);
                    }
                }
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw new RuntimeException("Lỗi khi truy xuất hình ảnh từ cơ sở dữ liệu bằng Hibernate", e);
            }
        }
        return image != null ? image : new Image("file:E:/KY2/nhaps/DoAnCS/src/main/resources/Img/null_avt.png");
    }
    public static void deleteAvatarUrl(String username) {
        String sql = "UPDATE Users SET AvatarURL = NULL WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=ChattingApplication;user=sa;password=123;encrypt=false;");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi xóa AvatarURL từ cơ sở dữ liệu", e);
        }
    }
}

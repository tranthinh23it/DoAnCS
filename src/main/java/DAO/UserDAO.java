package DAO;

import DTO.Users;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.ByteArrayInputStream;
import java.sql.*;

public class UserDAO {
    private static SessionFactory sessionFactory;

    // JDBC
    public static void saveImageToDatabase(String username, byte[] image) {
        String sql = "UPDATE Users SET AvatarURL = ? WHERE username = ?";
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=ChattingApplication;user=sa;password=12345;encrypt=true;trustServerCertificate=true");
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setBytes(1, image);
            ps.setString(2, username);
            ps.executeUpdate();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Image getImage(String username) {
        String sql = "SELECT AvatarURL FROM Users WHERE username = ?";
        Image image = null;
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=ChattingApplication;user=sa;password=12345;encrypt=false;");
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                byte[] imageData = rs.getBytes("AvatarURL");
                if (imageData != null) {
                    ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
                    image = new Image(bis);
                }
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return image;
    }

    // Hibernate
    public static void saveImageToDatabaseHibernate(String username, byte[] image) {
        Connected.connection(Users.class);
        sessionFactory = Connected.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
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
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static Image getImageHibernate(String username) {
        Connected.connection(Users.class);
        sessionFactory = Connected.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Image image = null;
        try {
            transaction = session.beginTransaction();
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
            e.printStackTrace();
        } finally {
            session.close();
        }
        return image;
    }

}

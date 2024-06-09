package DAO;

import DTO.Users;
import Security_java.PasswordUtil;
import javafx.scene.control.Alert;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class ResetPassword {
    private static SessionFactory sessionFactory = Connected.getSessionFactory();

    public static void changePassword(String username, String email, String newPassword) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Query<Users> query = session.createQuery("SELECT u FROM Users u WHERE u.username = :username AND u.email = :email", Users.class);
                query.setParameter("username", username);
                query.setParameter("email", email);
                Users user = query.uniqueResult();

                if (user != null) {
                    String hashedPassword = PasswordUtil.hashPassword(newPassword);

                    user.setPassword(newPassword);
                    user.setHashed_Password(hashedPassword);

                    session.update(user);
                    transaction.commit();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Notification");
                    alert.setHeaderText(null);
                    alert.setContentText("Password has been changed successfully!");
                    alert.showAndWait();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("User not found or email does not match!");
                    errorAlert.showAndWait();
                }
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }
    }
}

package DAO;

import DTO.Users;
import Security_java.PasswordUtil;
import javafx.scene.control.Alert;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class ResetPassword {
    private static SessionFactory sessionFactory;

    public static void changePassword(String username, String email, String newPassword) {

        Connected.connection(Users.class);
        sessionFactory = Connected.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            Query<Users> query = session.createQuery("select u from Users u WHERE u.username = :username AND u.email = :email", Users.class);
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
        } finally {
            session.close();
        }
    }

    public static void main(String[] args) {
        changePassword("hai", "bihai2k5@gmail.com", "newPassword123");
    }
}

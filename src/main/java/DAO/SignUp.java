package DAO;

import DTO.Users;
import Security_java.PasswordUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.LinkedList;
import java.util.List;

public class SignUp {
    private static SessionFactory sessionFactory;

    public static List<Users> CheckSignUp(String usname) {
        List<Users> userList = new LinkedList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<Users> query = session.createQuery("SELECT u FROM Users u WHERE u.username = :username", Users.class);
            query.setParameter("username", usname);
            userList = query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    public static void SignUpAccount(String username, String password, String email, String hashedPass) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Users user = new Users();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setFullName("");
            user.setAvatarUrl(null);
            user.setNickName(" ");
            user.setHashed_Password(hashedPass);
            user.setPlace(" ");
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Users> getUsersFromDatabase() {
        List<Users> usersList = new LinkedList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            usersList = session.createQuery("SELECT u FROM Users u", Users.class).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usersList;
    }

//    public static void main(String[] args) {
//        String hased = PasswordUtil.hashPassword("letuankhai23");
//        SignUpAccount("Le Tuan Khai","letuankhai23","khailt.23it@vku.udn.vn",hased);
//    }
}

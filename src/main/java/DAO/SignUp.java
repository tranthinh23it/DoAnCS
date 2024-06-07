package DAO;

import DTO.Users;
import Security_java.PasswordUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.mindrot.jbcrypt.BCrypt;

import java.util.LinkedList;
import java.util.List;

public class SignUp {
    private static SessionFactory sessionFactory;

    public static List<Users> CheckSignUp(String usname) {
        Connected.connection(Users.class);
        sessionFactory = Connected.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<Users> listCheck = new LinkedList<>();

        try {
            transaction = session.beginTransaction();
            Query<Users> query = session.createQuery("select u from Users u WHERE u.username = :username", Users.class);
            query.setParameter("username", usname);
            listCheck = query.list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return listCheck;
    }

    public static void SignUpAccount(String username, String password, String email, String hashedPass) {
        Connected.connection(Users.class);
        sessionFactory = Connected.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            Users us = new Users();
            us.setUsername(username);
            us.setPassword(password);
            us.setEmail(email);
            us.setFullName("");
            us.setAvatarUrl(null);
            us.setNickName(" ");
            us.setHashed_Password(hashedPass);
            us.setPlace(" ");

            session.save(us);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static List<Users> getUsersFromDatabase() {
        Connected.connection(Users.class);
        sessionFactory = Connected.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<Users> getUsers = new LinkedList<>();

        try {
            transaction = session.beginTransaction();
            getUsers = session.createQuery("select u from Users u", Users.class).list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return getUsers;
    }

    public static void main(String[] args) {
        String hased = PasswordUtil.hashPassword("letuankhai23");
        SignUpAccount("Le Tuan Khai","letuankhai23","khailt.23it@vku.udn.vn",hased);
    }
}

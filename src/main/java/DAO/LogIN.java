package DAO;

import DTO.Users;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.LinkedList;
import java.util.List;

public class LogIN {
    private static SessionFactory sessionFactory = Connected.getSessionFactory();

    public static List<Users> checkLogin(String email1,String passWord){
        List<Users> listCheck = new LinkedList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<Users> query = session.createQuery("SELECT u FROM Users u WHERE u.email = :email AND u.password = :password", Users.class);
            query.setParameter("email", email1);
            query.setParameter("password", passWord);
            listCheck = query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listCheck;
    }

    public static List<Users> getInfor(String email){
        List<Users> userInfor = new LinkedList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<Users> query = session.createQuery("SELECT u FROM Users u WHERE u.email = :email", Users.class);
            query.setParameter("email", email);
            userInfor = query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfor;
    }
    public static String getHasedCode(String email){
        String hashedPassword = "";
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<String> query = session.createQuery("SELECT u.hashed_Password FROM Users u WHERE u.email = :email", String.class);
            query.setParameter("email", email);
            hashedPassword = query.uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }

}

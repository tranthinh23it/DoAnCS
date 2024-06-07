package DAO;

import DTO.Users;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.LinkedList;
import java.util.List;

public class LogIN {
    private static SessionFactory sessionFactory;
    public static List<Users> checkLogin(String email1,String passWord){
        Connected.connection(Users.class);
        sessionFactory = Connected.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<Users> listCheck = new LinkedList<>();
        Query query = session.createQuery("select u from Users u WHERE u.email = :email AND u.password= :password", Users.class);
        query.setParameter("email",email1);
        query.setParameter("password",passWord);
        listCheck = query.list();

        return listCheck;
    }
    public static List<Users> getInfor(String email){
        Connected.connection(Users.class);
        sessionFactory = Connected.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<Users> userInfor = new LinkedList<>();
        Query query = session.createQuery("select u from Users u WHERE u.email = :email", Users.class);
        query.setParameter("email",email);
        userInfor = query.list();

        return userInfor;
    }
    public static String getHasedCode(String email){
        Connected.connection(Users.class);
        sessionFactory = Connected.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery("SELECT u.hashed_Password from Users u where u.email= :email");
        query.setParameter("email",email);
        String hashedPassword = query.uniqueResult().toString();
        session.getTransaction().commit();
        System.out.println(hashedPassword);
        // Close session and session factory
        session.close();
        sessionFactory.close();

        return hashedPassword;
    }

}

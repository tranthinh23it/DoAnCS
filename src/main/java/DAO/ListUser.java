package DAO;

import DTO.Friends;
import DTO.Users;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class ListUser {
    private static SessionFactory sessionFactory;
    //lay friend
    public static List<Users> loadName(String username) {
        Connected.connection(Friends.class);
        sessionFactory = Connected.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        String hql = "Select u From Users u "
                + "where u.userId IN (Select f.userID2 from Friends f "
                + "Where f.friendShipStatus='accepted' and f.userID1 = (SELECT u1.userId from Users u1 where u1.username= :username))";

         List<Users> users = session.createQuery(hql, Users.class)
                .setParameter("username", username) // Đặt tham số username
                .list();
        return users;
    }

    //lay id tu ten nguoi dung dang nhap vao de chuyen id
    public static int getIDUser(String username){
        int id =0;
        Connected.connection(Users.class);
        sessionFactory = Connected.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        String hql = "SELECT u.userId FROM Users u WHERE u.username = :username";
        Query query = session.createQuery(hql);
        query.setParameter("username", username);
        List<Integer> results = query.list();

        if (!results.isEmpty()) {
            id = results.get(0);
        }
        return id;
    }
    public static List<Users> getUSer(String username){
        List<Users> list = new ArrayList<>();
        Connected.connection(Users.class);
        sessionFactory = Connected.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery("SELECT u FROM Users u WHERE u.username = :username", Users.class);
        query.setParameter("username", username );
        list = query.list();
        return list;
    }
    //lay username bang id
    public static String getUsername(int userId){
        String username ="";
        Connected.connection(Users.class);
        sessionFactory = Connected.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        String hql = "SELECT u.username FROM Users u WHERE u.userId = : userId";
        Query query = session.createQuery(hql);
        query.setParameter("userId", userId);
        List<String> results = query.list();

        if (!results.isEmpty()) {
            username = results.get(0);
        }
        return username;
    }
    public static void updateInfo(String username,String fullname,String email,String nickname,byte[] avaURL,String place) {
        Transaction transaction = null;
        Session session = null;
        SessionFactory sessionFactory = null;

        try {
            Connected.connection(Users.class);
            sessionFactory = Connected.getSessionFactory();
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            int usid = getIDUser(username);
                Query query = session.createQuery("UPDATE Users u SET u.username= :username, u.fullName = :fullname,u.avatarUrl =: avaURL, u.email =:email, u.nickName =:nickname,u.place=:place where u.id=:usid");
                query.setParameter("username",username);
                query.setParameter("fullname",fullname);
                query.setParameter("avaURL",avaURL);
                query.setParameter("email",email);
                query.setParameter("nickname",nickname);
                query.setParameter("usid",usid);
                query.setParameter("place",place);

                int res = query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }
    }
    public static List<Users> searchByUsername(String username) {
        List<Users> results = new ArrayList<>();
        Session session = null;
        SessionFactory sessionFactory = null;
        try {
            Connected.connection(Users.class);
            sessionFactory = Connected.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            Query<Users> query = session.createQuery("SELECT u FROM Users u WHERE u.username LIKE :username", Users.class);
            query.setParameter("username", "%" + username + "%");
            results = query.list();

            session.getTransaction().commit();
        } catch (HibernateException e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }
        return results;
    }
    public static void main(String[] args) {

        updateInfo("Thịnh Trần","admin","admin","admin",null,"admin");
    }
}





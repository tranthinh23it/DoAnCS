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
    private static SessionFactory sessionFactory = Connected.getSessionFactory();
    //lay friend
    public static List<Users> loadName(String username) {
        List<Users> users = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String hql = "SELECT u FROM Users u WHERE u.userId IN (SELECT f.userID2 FROM Friends f WHERE f.friendShipStatus = 'accepted' AND f.userID1 = (SELECT u1.userId FROM Users u1 WHERE u1.username = :username))";
            users = session.createQuery(hql, Users.class)
                    .setParameter("username", username)
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    //lay id tu ten nguoi dung dang nhap vao de chuyen id
    public static int getIDUser(String username){
        int id = 0;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String hql = "SELECT u.userId FROM Users u WHERE u.username = :username";
            Query<Integer> query = session.createQuery(hql, Integer.class);
            query.setParameter("username", username);
            List<Integer> results = query.list();
            if (!results.isEmpty()) {
                id = results.get(0);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
    public static List<Users> getUSer(String username){
        List<Users> list = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<Users> query = session.createQuery("SELECT u FROM Users u WHERE u.username = :username", Users.class);
            query.setParameter("username", username);
            list = query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    //lay username bang id
    public static String getUsername(int userId){
        String username = "";
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String hql = "SELECT u.username FROM Users u WHERE u.userId = :userId";
            Query<String> query = session.createQuery(hql, String.class);
            query.setParameter("userId", userId);
            List<String> results = query.list();
            if (!results.isEmpty()) {
                username = results.get(0);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return username;
    }
    public static void updateInfo(String username,String fullname,String email,String nickname,byte[] avaURL,String place) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            int usid = getIDUser(username);
            Query query = session.createQuery("UPDATE Users u SET u.username = :username, u.fullName = :fullname, u.avatarUrl = :avaURL, u.email = :email, u.nickName = :nickname, u.place = :place WHERE u.userId = :usid");
            query.setParameter("username", username);
            query.setParameter("fullname", fullname);
            query.setParameter("avaURL", avaURL);
            query.setParameter("email", email);
            query.setParameter("nickname", nickname);
            query.setParameter("usid", usid);
            query.setParameter("place", place);
            int res = query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    public static List<Users> searchByUsername(String username) {
        List<Users> results = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<Users> query = session.createQuery("SELECT u FROM Users u WHERE u.username LIKE :username", Users.class);
            query.setParameter("username", "%" + username + "%");
            results = query.list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return results;
    }
//    public static void main(String[] args) {
//
//        updateInfo("Thịnh Trần","admin","admin","admin",null,"admin");
//    }
}





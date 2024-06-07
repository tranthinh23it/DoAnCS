package DAO;

import DTO.Attachments;
import DTO.GroupMessages;
import DTO.Messages;
import DTO.Users;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Message_DAO {

    private static SessionFactory sessionFactory;
    public static List<Messages> loadMessages(int sendId, int receiptId) {
        Connected.connection(Users.class);
        sessionFactory = Connected.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<Messages> messagesList = new ArrayList<>();

        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery(
                    "select m from Messages m where m.senderId in (:sendId, :receiptId) and m.receiverId in (:sendId, :receiptId) ORDER BY m.sentTime ASC",
                    Messages.class
            );
            query.setParameter("sendId", sendId);
            query.setParameter("receiptId", receiptId);
            messagesList = query.list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return messagesList;
    }

    public static void SaveMessage(Messages messages){
        Connected.connection(Messages.class);
        sessionFactory = Connected.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(messages);
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
    public static int getMessageId() {
        int id = 0;
        Connected.connection(Messages.class); // Ensure the correct class is used here
        sessionFactory = Connected.getSessionFactory();
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            // Corrected HQL query
            String hql = "FROM Messages ORDER BY messageId DESC";
            Query<Messages> query = session.createQuery(hql, Messages.class);
            query.setMaxResults(1);

            List<Messages> results = query.list();

            if (!results.isEmpty()) {
                id = results.get(0).getMessageId() + 1;
            } else {
                id = 1;  // If no messages found, start from 1
            }

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
        }
        return id;
    }



}

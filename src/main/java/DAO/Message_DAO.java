package DAO;

import DTO.Messages;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class Message_DAO {

    private static SessionFactory sessionFactory = Connected.getSessionFactory();
    public static List<Messages> loadMessages(int sendId, int receiptId) {
        List<Messages> messagesList = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Query<Messages> query = session.createQuery(
                        "SELECT m FROM Messages m WHERE m.senderId IN (:sendId, :receiptId) AND m.receiverId IN (:sendId, :receiptId) ORDER BY m.sentTime ASC",
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
            }
        }
        return messagesList;
    }

    public static void SaveMessage(Messages messages){
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.save(messages);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }
    }
    public static int getMessageId() {
        int id = 0;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                String hql = "FROM Messages ORDER BY messageId DESC";
                Query<Messages> query = session.createQuery(hql, Messages.class);
                query.setMaxResults(1);
                List<Messages> results = query.list();
                if (!results.isEmpty()) {
                    id = results.get(0).getMessageId() + 1;
                } else {
                    id = 1; // If no messages found, start from 1
                }
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }
        return id;
    }
}

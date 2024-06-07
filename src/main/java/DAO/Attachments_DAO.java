package DAO;

import DTO.Attachments;
import DTO.Messages;
import DTO.Users;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class Attachments_DAO {
    private static SessionFactory sessionFactory;
    public static Attachments loadMessFile(int messageId){
        Connected.connection(Users.class);
        sessionFactory = Connected.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<Attachments> messagesList = new ArrayList<>();
        Attachments attachments = null;
        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery(
                    "select m from Attachments m where m.messageId = :messageId",
                    Attachments.class
            );
            query.setParameter("messageId", messageId);
            messagesList = query.list();

            if (!messagesList.isEmpty()) {
                attachments = messagesList.get(0);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return attachments;
    }
    public static void saveMessFile(Attachments attachments){
        Connected.connection(Attachments.class);
        sessionFactory = Connected.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(attachments);
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

    public static void main(String[] args) {
        Attachments attachments = new Attachments();
        attachments.setMessageId(34);
        attachments.setFileName("file.txt");
        attachments.setFileType("txt");
        attachments.setFileSize(2);
        attachments.setFileUrl("aaaaaaaaaa");
        attachments.setSenderId(1);
        attachments.setReceiverId(2);
        attachments.setGroupId(-1);
        attachments.setDataFile(new byte[0]);
        saveMessFile(attachments);
    }
}

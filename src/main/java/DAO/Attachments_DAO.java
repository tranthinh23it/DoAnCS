package DAO;

import DTO.Attachments;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class Attachments_DAO {
    private static SessionFactory sessionFactory = Connected.getSessionFactory();

    public static Attachments loadMessFile(int messageId) {
        Attachments attachments = null;
        try (Session session = sessionFactory.openSession()) {
            Query<Attachments> query = session.createQuery(
                    "select m from Attachments m where m.messageId = :messageId",
                    Attachments.class
            );
            query.setParameter("messageId", messageId);
            List<Attachments> messagesList = query.list();

            if (!messagesList.isEmpty()) {
                attachments = messagesList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attachments;
    }

    public static void saveMessFile(Attachments attachments) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.save(attachments);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                e.printStackTrace();
            }
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

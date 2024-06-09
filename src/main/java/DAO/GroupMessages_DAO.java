package DAO;

import DTO.GroupMessages;
import DTO.Groups;
import javafx.scene.image.Image;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupMessages_DAO {
    private static SessionFactory sessionFactory = Connected.getSessionFactory();

    public static List<GroupMessages> loadGroupMessages(int groupId) {
        List<GroupMessages> messagesList = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Query<GroupMessages> query = session.createQuery(
                    "select m from GroupMessages m where m.groupId = :groupId order by m.sentTime ASC",
                    GroupMessages.class
            );
            query.setParameter("groupId", groupId);
            messagesList = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messagesList;
    }

    public static List<Groups> loadGroup() {
        List<Groups> groups = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Query<Groups> query = session.createQuery("SELECT g from Groups g", Groups.class);
            groups = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return groups;
    }
    public static void SaveMessageGroup(GroupMessages groupMessages){
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession();){
            transaction = session.beginTransaction();
            session.save(groupMessages);
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    public static int getMessageGrId() {
        int id = 1;
        try (Session session = sessionFactory.openSession();){

            // Corrected HQL query
            String hql = "FROM GroupMessages ORDER BY groupMessageId DESC";
            Query<GroupMessages> query = session.createQuery(hql, GroupMessages.class);
            query.setMaxResults(1);

            List<GroupMessages> results = query.list();

            if (!results.isEmpty()) {
                id = results.get(0).getGroupMessageId() + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
    // JDBC
    public static void saveImageToDatabase(String groupID, byte[] image) {
        String sql = "UPDATE Groups SET GroupImage = ? WHERE GroupID = ?";
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=ChattingApplication;user=sa;password=12345;encrypt=false;");
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setBytes(1, image);
            ps.setString(2, groupID);
            ps.executeUpdate();

            ps.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Image getImage(String groupID) {
        String sql = "SELECT GroupImage FROM Groups WHERE GroupID = ?";
        Image image = null;
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=ChattingApplication;user=sa;password=12345;encrypt=false;");
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, groupID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                byte[] imageData = rs.getBytes("GroupImage");
                if (imageData != null) {
                    ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
                    image = new Image(bis);
                }
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return image;
    }
}

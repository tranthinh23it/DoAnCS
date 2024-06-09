package DAO;

import DTO.Friends;
import DTO.Users;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FriendDAO {

    private static SessionFactory sessionFactory = Connected.getSessionFactory();

    public static List<Friends> listFriend() {

        List<Friends> listFriend = null;

        try (Session session = sessionFactory.openSession()) {
            Query<Friends> query = session.createQuery("select f from Friends f", Friends.class);
            listFriend = query.list();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listFriend;
    }

    //sendRequest
    public static void sendRequest(int userID1, int userID2) {

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            // Kiểm tra xem cặp userID đã tồn tại với trạng thái unaccepted chưa
            Query<Friends> query = session.createQuery(
                    "select f from Friends f " +
                            "where (f.userID1 = :userID1 and f.userID2 = :userID2) " +
                            "and f.friendShipStatus = :status", Friends.class
            );
            query.setParameter("userID1", userID1);
            query.setParameter("userID2", userID2);
            query.setParameter("status", "unaccepted");

            List<Friends> existingRequests = query.list();

            if (existingRequests.isEmpty()) {
                Friends fr = new Friends();
                fr.setUserID1(userID1);
                fr.setUserID2(userID2);
                fr.setFriendShipStatus("unaccepted");

                Friends fr2 = new Friends();
                fr2.setUserID1(userID2);
                fr2.setUserID2(userID1);
                fr2.setFriendShipStatus("unaccepted");

                session.save(fr);
                session.save(fr2);
            } else {
                System.out.println("Existing.");
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //receivedRequest
    public static List<Friends> receivedRequest(int userID) {
        List<Friends> listRequest = null;

        try (Session session = sessionFactory.openSession()) {
            Query<Friends> query = session.createQuery("select f from Friends f where f.userID2 = :userID and f.friendShipStatus = :status", Friends.class);
            query.setParameter("userID", userID);
            query.setParameter("status", "unaccepted");
            listRequest = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listRequest;
    }

    //acceptRequest
    public static void acceptRequest(int userID1, int userID2) {

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            Query query = session.createQuery(
                    "update Friends set friendShipStatus = :status " +
                            "where (userID1 = :userID1 and userID2 = :userID2) " +
                            "or (userID1 = :userID2 and userID2 = :userID1)"
            );

            query.setParameter("status", "accepted");
            query.setParameter("userID1", userID1);
            query.setParameter("userID2", userID2);

            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //gain the user list by id from friend list
    public static List<Users> convert(List<Friends> friendsList) {
        List<Users> usersList = new ArrayList<>();
        Set<Integer> userIds = new HashSet<>();
        try (Session session = sessionFactory.openSession()) {
            for (Friends friend : friendsList) {
                int userId = friend.getUserID1();
                if (!userIds.contains(userId)) {
                    Users user = session.get(Users.class, userId);
                    if (user != null) {
                        usersList.add(user);
                        userIds.add(userId);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usersList;
    }
}

package DTO;

import java.util.Objects;

public class Friends {
    private int friendShipId;
    private int UserID1;
    private int UserID2;
    private String friendShipStatus;

    public int getFriendShipId() {
        return friendShipId;
    }

    public void setFriendShipId(int friendShipId) {
        this.friendShipId = friendShipId;
    }

    public int getUserID1() {
        return UserID1;
    }

    public void setUserID1(int userID1) {
        UserID1 = userID1;
    }

    public int getUserID2() {
        return UserID2;
    }

    public void setUserID2(int userID2) {
        UserID2 = userID2;
    }

    public String getFriendShipStatus() {
        return friendShipStatus;
    }

    public void setFriendShipStatus(String friendShipStatus) {
        this.friendShipStatus = friendShipStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friends friends = (Friends) o;
        return friendShipId == friends.friendShipId && UserID1 == friends.UserID1 && UserID2 == friends.UserID2 && Objects.equals(friendShipStatus, friends.friendShipStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friendShipId, UserID1, UserID2, friendShipStatus);
    }
}

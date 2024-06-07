package DTO;

import java.sql.Date;
import java.util.Objects;

public class Notifications {
    private int notificationId;
    private Integer userId;
    private String notificationContent;
    private Date sentTime;
    private Boolean isRead;

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNotificationContent() {
        return notificationContent;
    }

    public void setNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
    }

    public Date getSentTime() {
        return sentTime;
    }

    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }

    public boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notifications that = (Notifications) o;
        return notificationId == that.notificationId && Objects.equals(userId, that.userId) && Objects.equals(notificationContent, that.notificationContent) && Objects.equals(sentTime, that.sentTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notificationId, userId, notificationContent, sentTime);
    }
}

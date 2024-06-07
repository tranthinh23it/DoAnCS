package DTO;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

public class GroupMessages {
    private int groupMessageId;
    private int groupId;
    private int senderId;
    private String messageContent;
    private Timestamp sentTime;

    public int getGroupMessageId() {
        return groupMessageId;
    }

    public void setGroupMessageId(int groupMessageId) {
        this.groupMessageId = groupMessageId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Timestamp getSentTime() {
        return sentTime;
    }

    public void setSentTime(Timestamp sentTime) {
        this.sentTime = sentTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupMessages that = (GroupMessages) o;
        return groupMessageId == that.groupMessageId && groupId == that.groupId && senderId == that.senderId && Objects.equals(messageContent, that.messageContent) && Objects.equals(sentTime, that.sentTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupMessageId, groupId, senderId, messageContent, sentTime);
    }
}

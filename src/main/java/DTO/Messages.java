package DTO;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

public class Messages {
    private int messageId;
    private int senderId;
    private int receiverId;
    private String messageContent;
    private Timestamp sentTime;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public  String getMessageContent() {
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
        Messages messages = (Messages) o;
        return messageId == messages.messageId && senderId == messages.senderId && receiverId == messages.receiverId && Objects.equals(messageContent, messages.messageContent) && Objects.equals(sentTime, messages.sentTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId, senderId, receiverId, messageContent, sentTime);
    }
}

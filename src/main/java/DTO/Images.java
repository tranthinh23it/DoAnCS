package DTO;

import java.util.Arrays;
import java.util.Objects;

public class Images {
    private int imageId;
    private int messageId;
    private int senderId;
    private int receiverId;
    private int groupId;
    private byte[] imageUrl;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

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

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public byte[] getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(byte[] imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Images images = (Images) o;
        return imageId == images.imageId && messageId == images.messageId && senderId == images.senderId && receiverId == images.receiverId && groupId == images.groupId && Arrays.equals(imageUrl, images.imageUrl);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(imageId, messageId, senderId, receiverId, groupId);
        result = 31 * result + Arrays.hashCode(imageUrl);
        return result;
    }
}

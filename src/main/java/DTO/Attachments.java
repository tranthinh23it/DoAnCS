package DTO;

import java.util.Arrays;
import java.util.Objects;

public class Attachments {
    private int attachmentId;
    private int messageId;
    private String fileName;
    private String fileType;
    private int fileSize;
    private String fileUrl;
    private int senderId;
    private Integer receiverId;  // Optional: Use Integer to allow null values
    private Integer groupId;     // Optional: Use Integer to allow null values
    private byte[] dataFile;     // Byte array to store file data

    public int getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public byte[] getDataFile() {
        return dataFile;
    }

    public void setDataFile(byte[] dataFile) {
        this.dataFile = dataFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attachments that = (Attachments) o;
        return attachmentId == that.attachmentId && messageId == that.messageId && fileSize == that.fileSize && senderId == that.senderId && Objects.equals(fileName, that.fileName) && Objects.equals(fileType, that.fileType) && Objects.equals(fileUrl, that.fileUrl) && Objects.equals(receiverId, that.receiverId) && Objects.equals(groupId, that.groupId) && Arrays.equals(dataFile, that.dataFile);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(attachmentId, messageId, fileName, fileType, fileSize, fileUrl, senderId, receiverId, groupId);
        result = 31 * result + Arrays.hashCode(dataFile);
        return result;
    }
}

package DTO;

import java.util.Arrays;
import java.util.Objects;

public class Groups {
    private int groupId;
    private String groupName;
    private byte[] groupImage;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public byte[] getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(byte[] groupImage) {
        this.groupImage = groupImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Groups groups = (Groups) o;
        return groupId == groups.groupId && Objects.equals(groupName, groups.groupName) && Objects.deepEquals(groupImage, groups.groupImage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, groupName, Arrays.hashCode(groupImage));
    }
}

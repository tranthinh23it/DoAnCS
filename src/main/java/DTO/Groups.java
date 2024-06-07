package DTO;

import java.util.Objects;

public class Groups {
    private int groupId;
    private String groupName;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Groups groups = (Groups) o;
        return groupId == groups.groupId && Objects.equals(groupName, groups.groupName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, groupName);
    }
}

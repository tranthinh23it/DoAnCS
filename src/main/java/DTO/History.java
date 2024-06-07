package DTO;

import java.sql.Date;
import java.util.Objects;

public class History {
    private int historyId;
    private Integer userId;
    private String action;
    private Date actionTime;

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getActionTime() {
        return actionTime;
    }

    public void setActionTime(Date actionTime) {
        this.actionTime = actionTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        History history = (History) o;
        return historyId == history.historyId && Objects.equals(userId, history.userId) && Objects.equals(action, history.action) && Objects.equals(actionTime, history.actionTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(historyId, userId, action, actionTime);
    }
}

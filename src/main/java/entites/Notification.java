package entites;

import java.sql.Timestamp;

public class Notification {
    private int id;
    private int type;
    private int userId;
    private String message;
    private Timestamp timeStamp;
    private boolean isRead;

    public Notification() {
    }

    public Notification(int id, int type, int userId, String message, Timestamp timeStamp, boolean isRead) {
        this.id = id;
        this.type = type;
        this.userId = userId;
        this.message = message;
        this.timeStamp = timeStamp;
        this.isRead = isRead;
    }

    public Notification(int type, int userId, String message, Timestamp timeStamp, boolean isRead) {
        this.type = type;
        this.userId = userId;
        this.message = message;
        this.timeStamp = timeStamp;
        this.isRead = isRead;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", type=" + type +
                ", userId=" + userId +
                ", message='" + message + '\'' +
                ", timeStamp=" + timeStamp +
                ", isRead=" + isRead +
                '}';
    }
}

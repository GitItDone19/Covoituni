package entities;

import java.util.Date;

public class Reclamation {
    private int id;
    private String subject;
    private String description;
    private User user; // The user who made the reclamation
    private String state; // State of the reclamation (e.g., pending, resolved)
    private Date date; // Date of the reclamation
    private String reply;  // Add this new field

    public Reclamation(int id, String subject, String description, User user, String state, Date date, String reply) {
        this.id = id;
        this.subject = subject;
        this.description = description;
        this.user = user;
        this.state = state;
        this.date = date;
        this.reply = reply;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "subject='" + subject + '\'' +
                ", description='" + description + '\'' +
                ", state='" + state + '\'' +
                ", date=" + date +
                '}';
    }
} 
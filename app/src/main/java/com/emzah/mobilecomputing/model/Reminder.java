package com.emzah.mobilecomputing.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reminders")
public class Reminder {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "creator_id")
    private long creatorId;
    @ColumnInfo(name = "message")
    private String message ;
    @ColumnInfo(name = "location_x")
    private double locationX;
    @ColumnInfo(name = "location_y")
    private double locationY;
    @ColumnInfo(name = "reminder_time")
    private long reminderTime;
    @ColumnInfo(name = "creation_time")
    private String creationTime;
    @ColumnInfo(name = "reminder_seen")
    private String reminderSeen;

    public Reminder() {
    }

    public Reminder(long creatorId, String message, double locationX, double locationY, long reminderTime, String creationTime, String reminderSeen) {
        this.creatorId = creatorId;
        this.message = message;
        this.locationX = locationX;
        this.locationY = locationY;
        this.reminderTime = reminderTime;
        this.creationTime = creationTime;
        this.reminderSeen = reminderSeen;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getLocationX() {
        return locationX;
    }

    public void setLocationX(double locationX) {
        this.locationX = locationX;
    }

    public double getLocationY() {
        return locationY;
    }

    public void setLocationY(double locationY) {
        this.locationY = locationY;
    }

    public long getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(long reminderTime) {
        this.reminderTime = reminderTime;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getReminderSeen() {
        return reminderSeen;
    }

    public void setReminderSeen(String reminderSeen) {
        this.reminderSeen = reminderSeen;
    }
}

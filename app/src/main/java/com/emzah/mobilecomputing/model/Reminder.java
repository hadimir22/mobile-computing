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
    private String locationX;
    @ColumnInfo(name = "location_y")
    private String locationY;
    @ColumnInfo(name = "reminder_time")
    private String reminderTime;
    @ColumnInfo(name = "creation_time")
    private String creationTime;
    @ColumnInfo(name = "reminder_seen")
    private String reminderSeen;

    public Reminder() {
    }

    public Reminder(long creatorId, String message, String locationX, String locationY, String reminderTime, String creationTime, String reminderSeen) {
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

    public String getLocationX() {
        return locationX;
    }

    public void setLocationX(String locationX) {
        this.locationX = locationX;
    }

    public String getLocationY() {
        return locationY;
    }

    public void setLocationY(String locationY) {
        this.locationY = locationY;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
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

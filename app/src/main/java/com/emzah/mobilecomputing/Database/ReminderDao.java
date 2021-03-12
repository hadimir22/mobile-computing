package com.emzah.mobilecomputing.Database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.emzah.mobilecomputing.model.Reminder;

import java.util.List;

@Dao
public interface ReminderDao {

    @Query("SELECT * FROM reminders WHERE reminder_time <= :time")
    LiveData<List<Reminder>> getAllReminders(long time);

    @Update
     void updateReminder(Reminder reminder);

    @Insert
    void insertReminder(Reminder reminder);

   @Query("DELETE FROM reminders WHERE creator_id = :creatorId")
    void deleteReminder(long creatorId);
}

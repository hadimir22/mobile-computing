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

    @Query("SELECT * FROM reminders")
    LiveData<List<Reminder>> getAllReminders();

//    @Query("UPDATE reminders SET message = message where creator_id = :creatorId")
//    void updateReminder(long creatorId,String message);
    @Update
     void updateReminder(Reminder reminder);

    @Insert
    void insertReminder(Reminder reminder);

   @Query("DELETE FROM reminders WHERE creator_id = :creatorId")
    void deleteReminder(long creatorId);
}

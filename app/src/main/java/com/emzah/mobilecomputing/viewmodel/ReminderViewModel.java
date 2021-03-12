package com.emzah.mobilecomputing.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.emzah.mobilecomputing.Database.AppDatabase;
import com.emzah.mobilecomputing.model.Reminder;
import com.emzah.mobilecomputing.utils.AppExecutors;

import java.util.List;

public class ReminderViewModel extends AndroidViewModel {


    private LiveData<List<Reminder>> reminderLiveData;
    AppDatabase appDatabase;

    public ReminderViewModel(@NonNull Application application) {
        super(application);
         appDatabase = AppDatabase.getInstance(application);
        reminderLiveData = appDatabase.reminderDao().getAllReminders(System.currentTimeMillis());
    }

    public LiveData<List<Reminder>> getReminderLiveData() {
        return reminderLiveData;
    }

    public void getDataFromDb() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                reminderLiveData = appDatabase.reminderDao().getAllReminders(System.currentTimeMillis());
            }
        });

    }
}

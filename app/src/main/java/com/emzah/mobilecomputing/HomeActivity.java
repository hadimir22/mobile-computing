package com.emzah.mobilecomputing;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.emzah.mobilecomputing.Database.AppDatabase;
import com.emzah.mobilecomputing.model.Reminder;
import com.emzah.mobilecomputing.utils.AppExecutors;
import com.emzah.mobilecomputing.viewmodel.ReminderViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private String m_Text = "";
    private AppDatabase appDatabase;
    private ReminderViewModel reminderViewModel;
    public static final String WORKER_TAG = "notification_worker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        appDatabase = AppDatabase.getInstance(getApplicationContext());
        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Add Reminder");

                LayoutInflater inflater= LayoutInflater.from(HomeActivity.this);
                View layout=inflater.inflate(R.layout.dialog_layout,null);
                EditText input = layout.findViewById(R.id.msg_et);
                TimePicker timePicker = layout.findViewById(R.id.simpleTimePicker);
                timePicker.setIs24HourView(false);

                builder.setView(layout);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         Date date = new Date();
                        Calendar calAlarm = Calendar.getInstance();
                        Calendar calNow = Calendar.getInstance();

                        calNow.setTime(date);
                        calAlarm.setTime(date);

                        calAlarm.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                        calAlarm.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                        calAlarm.set(Calendar.SECOND,0);

//                        if (calAlarm.before(calNow)){
//                            calAlarm.add(Calendar.DATE,1);
//                        }


                        m_Text = input.getText().toString();
                        Reminder reminder = new Reminder(System.currentTimeMillis(),m_Text,"2.3","3.33",calAlarm.getTimeInMillis(),"reme","2.33");
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                           @Override
                            public void run() {
                                appDatabase.reminderDao().insertReminder(reminder);
                                reminderViewModel.getDataFromDb();
                                Intent intent = new Intent(HomeActivity.this,Notificationreceiver.class);
                                intent.putExtra("title",reminder.getMessage());
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(HomeActivity.this,123,intent,0);
                                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calAlarm.getTimeInMillis(),
                                        AlarmManager.INTERVAL_DAY, pendingIntent);
                                m_Text = "";

                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings){
            Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
package com.emzah.mobilecomputing.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.emzah.mobilecomputing.Database.AppDatabase;
import com.emzah.mobilecomputing.HomeActivity;
import com.emzah.mobilecomputing.R;
import com.emzah.mobilecomputing.model.Reminder;
import com.emzah.mobilecomputing.utils.AppExecutors;
import com.emzah.mobilecomputing.viewmodel.ReminderViewModel;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ReminderViewHolder> {

    private Context context;
    private List<Reminder> reminderList;
    private AppDatabase appDatabase;
    private ReminderViewModel reminderViewModel;

    public Adapter(Context context, List<Reminder> reminderList , ReminderViewModel reminderViewModel) {
        this.context = context;
        this.reminderList = reminderList;
        this.reminderViewModel = reminderViewModel;
        appDatabase = AppDatabase.getInstance(context);

    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reminder_item,parent,false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {

        holder.reminderTv.setText(reminderList.get(position).getMessage());
        holder.reminderTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Update/Delete Reminder");

                final EditText input = new EditText(context);
                builder.setView(input);
               final String text = reminderList.get(position).getMessage();
                input.setText(text);

                builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Reminder reminder = new Reminder(reminderList.get(position).getCreatorId(),input.getText().toString(),"2.3","3.33","2:3","reme","2.33");
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                appDatabase.reminderDao().updateReminder(reminder);
                                reminderViewModel.getDataFromDb();

                            }
                        });
                    }
                });
                builder.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                appDatabase.reminderDao().deleteReminder(reminderList.get(position).getCreatorId());
                                reminderViewModel.getDataFromDb();
                                 dialog.cancel();
                            }
                        });

                    }
                });

                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (reminderList !=null){
            return reminderList.size();
        }
        return 0;
    }

    public void notifyDataSetChangedList(List<Reminder> reminders) {
        reminderList = reminders;
        notifyDataSetChanged();
    }

    public class ReminderViewHolder extends RecyclerView.ViewHolder{

        public TextView reminderTv;
        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            reminderTv = itemView.findViewById(R.id.reminder_tv);
        }
    }
}

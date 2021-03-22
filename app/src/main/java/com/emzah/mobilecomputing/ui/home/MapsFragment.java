package com.emzah.mobilecomputing.ui.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.emzah.mobilecomputing.Database.AppDatabase;
import com.emzah.mobilecomputing.HomeActivity;
import com.emzah.mobilecomputing.LocationMonitoringService;
import com.emzah.mobilecomputing.Notificationreceiver;
import com.emzah.mobilecomputing.R;
import com.emzah.mobilecomputing.model.Reminder;
import com.emzah.mobilecomputing.utils.AppExecutors;
import com.emzah.mobilecomputing.viewmodel.ReminderViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.ALARM_SERVICE;

public class MapsFragment extends Fragment {
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Context mContext;
    private GoogleMap mMap;
    public boolean locationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private FloatingActionButton locationFab;
    private String m_Text = "";
    private AppDatabase appDatabase;
    private ReminderViewModel reminderViewModel;
    public static final String WORKER_TAG = "notification_worker";
    private LatLng lastKnownLocation;
    private SharedPreferences sharedPreferences;


    private boolean mAlreadyStartedService = false;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = mContext.getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        appDatabase = AppDatabase.getInstance(mContext);
        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
        getLocationPermission();
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(mContext.getApplicationContext(),
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            requestPermissions(
                    new String[]{ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                    getDeviceLocation();
                } else {

                }
            }
        }
    }

    private void getDeviceLocation() {
        try {
            if (locationPermissionGranted) {

                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(),
                        new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    if (mMap !=null){
                                        mMap.clear();
                                    }
                                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                    mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                            new LatLng(location.getLatitude(),
                                                    location.getLongitude()), 15));
                                }
                            }
                        }
                );

            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    lastKnownLocation = new LatLng(latLng.latitude, latLng.longitude);
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                    Toast.makeText(mContext, "" + latLng.latitude + "," + latLng.longitude, Toast.LENGTH_SHORT).show();


                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Add Reminder");

                    LayoutInflater inflater = LayoutInflater.from(mContext);
                    View layout = inflater.inflate(R.layout.dialog_layout, null);
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
                            calAlarm.set(Calendar.SECOND, 0);

//                        if (calAlarm.before(calNow)){
//                            calAlarm.add(Calendar.DATE,1);
//                        }

                            LocationMonitoringService.latlng = latLng;
                                //Start location sharing service to app server.........
                                Intent intent = new Intent(mContext, LocationMonitoringService.class);
                                intent.putExtra("lat", latLng.latitude);
                                intent.putExtra("lng", latLng.longitude);
                                mContext.startService(intent);


//
                            m_Text = input.getText().toString();
                            Reminder reminder = new Reminder(System.currentTimeMillis(), m_Text, lastKnownLocation.latitude, lastKnownLocation.longitude, calAlarm.getTimeInMillis(), "reme", "2.33");
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    appDatabase.reminderDao().insertReminder(reminder);
                                    reminderViewModel.getDataFromDb();
//
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

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

           View view=inflater.inflate(R.layout.fragment_maps, container, false);
           locationFab = getActivity().findViewById(R.id.location_get);
           return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        locationFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap == null) {
                    return;
                }
                try {
                    if (locationPermissionGranted) {
                        getDeviceLocation();
                    } else {
                        getLocationPermission();

                    }
                } catch (SecurityException e) {
                    Log.e("Exception: %s", e.getMessage());
                }
            }
        });


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
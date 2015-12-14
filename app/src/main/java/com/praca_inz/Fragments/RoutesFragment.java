package com.praca_inz.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.praca_inz.Activities.MapActivity;
import com.praca_inz.Adapters.RecyclerItemClickListener;
import com.praca_inz.Adapters.RecyclerViewAdapter;
import com.praca_inz.CalculateTrack;
import com.praca_inz.ModelsAndDB.RoutesDB;
import com.praca_inz.ModelsAndDB.RoutesModel;
import com.praca_inz.ModelsAndDB.RoutesPointsDB;
import com.praca_inz.ModelsAndDB.RoutesPointsModel;
import com.praca_inz.R;
import com.praca_inz.UnitConversions;
import com.praca_inz.Utilities;

import java.util.List;

/**
 * Created by KamilH on 2015-10-14.
 */
public class RoutesFragment extends Fragment {
    private View view;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Button startButton;
    private TextView distanceTextView, timeTextView, avgSpeedTextView, maxSpeedTextView;
    private int START = 0, STOP = 1, startStop = 0;
    private long tStart = 0;
    private Handler mHandler;
    private int iterator = 0;
    private Location lastLocation;
    private double distance = 0, time = 0, avgSpeed = 0, maxSpeed = 0, consumption = 0;
    private RecyclerView recyclerView;
    private SharedPreferences carInfoPreferences, petrolPreferences;
    private long routeID;

    public RoutesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_routes, container, false);

        distanceTextView = (TextView) view.findViewById(R.id.timeTextView);
        timeTextView = (TextView) view.findViewById(R.id.distanceTextView);
        avgSpeedTextView = (TextView) view.findViewById(R.id.avgSpeedTextView);
        maxSpeedTextView = (TextView) view.findViewById(R.id.maxSpeedTextView);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent i = new Intent(getActivity(), MapActivity.class);
                        i.putExtra("id", position + 1);
                        startActivity(i);
                    }
                })
        );

        displayCards();

        startButton = (Button) view.findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startStop == START) {
                    if(isGPSenabled()){
                        getTrackID();
                        getLocation();
                        Toast.makeText(getActivity(), "Pomiar rozpocznie się w momencie, gdy GPS ustali pozycję", Toast.LENGTH_SHORT).show();

                        startButton.setText("STOP");
                        startStop = STOP;
                    }
                } else {
                    locationManager.removeUpdates(locationListener);
                    stopRepeatingTask();

                    if(iterator > 0){
                        showCostDialog();
                    }

                    startButton.setText("START");
                    startStop = START;
                }
            }
        });

        mHandler = new Handler();

        carInfoPreferences = getActivity().getSharedPreferences("car_info_preferences", Context.MODE_PRIVATE);
        petrolPreferences = getActivity().getSharedPreferences("petrol_prices_preferences", Context.MODE_PRIVATE);
        this.consumption = carInfoPreferences.getFloat("consumption", 0);

        return view;
    }

    private void getTrackID(){
        RoutesDB rDB = new RoutesDB(getActivity());
        this.routeID = rDB.getRowCount() + 1;
    }

    private void displayRPM(){
        RoutesPointsDB rPDB = new RoutesPointsDB(getActivity());
        List<RoutesPointsModel> routesPointsModel = rPDB.getRoute(2);

        for (RoutesPointsModel rPM : routesPointsModel) {
            String log = rPM.toString();
            Log.d("POINTS: : ", log);
        }
    }

    private void display(List<RoutesModel> routesModels){
        for (RoutesModel fM : routesModels) {
            String log = fM.toString();
            Log.d("Fuelings: : ", log);
        }
    }

    private void getCost(double consum){
        double price = petrolPreferences.getFloat(String.valueOf(carInfoPreferences.getInt("petrol_type", 0)), 0);
        double distanceKM = this.distance * UnitConversions.M_TO_KM;

        //Toast.makeText(getActivity(), "PRICE: "+ String.valueOf(price) + "CONS: " + String.valueOf(consum) + "DIST: " + String.valueOf(distanceKM), Toast.LENGTH_LONG).show();
        if (consum != 0 && distance != 0) {
            updateRoutesDB(price * (distanceKM / 100) * consum);
        } else {
            updateRoutesDB(0);
        }
    }

    private void updateRoutesPointsDB(double lat, double lon, double time, double accuracy, double speed){
        RoutesPointsDB rPDB = new RoutesPointsDB(getActivity());
        rPDB.addPoint(new RoutesPointsModel((int)this.routeID, lat, lon, time, accuracy, speed));
    }

    private void updateRoutesDB(double cost){
        RoutesDB rDB = new RoutesDB(getActivity());
        String date = Utilities.getCurrentDate();
        rDB.addRoute(new RoutesModel(Utilities.roundOff(this.distance * UnitConversions.M_TO_KM), this.time, Utilities.roundOff(cost), Utilities.roundOff(this.avgSpeed),
                Utilities.roundOff(this.maxSpeed), date));

        resetValues();
        displayCards();
    }

    private void resetValues(){
        this.iterator = 0;
        this.distance = 0;
        this.time = 0;
        this.avgSpeed = 0;
        this.maxSpeed = 0;
    }

    private void displayCards(){
        RoutesDB rDB = new RoutesDB(getActivity());
        List<RoutesModel> routesModels = rDB.getAllRoutes();

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), routesModels);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void getLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                double accuracy = location.getAccuracy();
                double speed = location.getSpeed() * UnitConversions.MS_TO_KMH;
                long tEnd = System.currentTimeMillis();
                double elapsedTime = getElapsedTime(tStart, tEnd);

                if(iterator > 0){
                    CalculateTrack calculateTrack = new CalculateTrack(lastLocation, location, elapsedTime, distance);
                    saveData(calculateTrack.getAvgSpeed(), Double.valueOf(speed), calculateTrack.getDistance(), elapsedTime);
                    lastLocation = location;
                }
                else {
                    lastLocation = location;
                    tStart = System.currentTimeMillis();
                    startRepeatingTask();
                }
                iterator++;

                if (speed > 0)
                    updateRoutesPointsDB(location.getLatitude(), location.getLongitude(), elapsedTime, accuracy, speed);
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            public void onProviderEnabled(String provider) {
            }
            public void onProviderDisabled(String provider) {
            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    private void saveData(double avgSpeed, double speed, double distance, double time){
        if(speed >= this.maxSpeed)
            this.maxSpeed = speed;
        this.avgSpeed = avgSpeed;
        this.distance = distance;
        this.time = time;

        fillForms();
    }

    private void fillForms(){
        avgSpeedTextView.setText(String.valueOf(String.format("%.2f", avgSpeed)));
        maxSpeedTextView.setText(String.valueOf(String.format("%.2f", maxSpeed)));
        distanceTextView.setText(String.valueOf(String.format("%.2f", distance * UnitConversions.M_TO_KM)));
    }

    private double getElapsedTime(long tStart, long tEnd){
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;

        return Math.round(elapsedSeconds);
    }

    private boolean isGPSenabled() {
        locationManager = (LocationManager)this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))  {
            Toast.makeText(getActivity(), "Proszę włączyć GPS.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            return false;
        }
        else return true;
    }

    private void showCostDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.consumption_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText consumptionEditText = (EditText) dialogView.findViewById(R.id.consumptionEditText);
        consumptionEditText.setText(String.valueOf(consumption));

        dialogBuilder.setTitle("Proszę podać spalanie");
        //dialogBuilder.setMessage("Enter text below");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                getCost(Double.parseDouble(consumptionEditText.getText().toString()));
            }
        });
        dialogBuilder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                getCost(consumption);
            }
        });
        dialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                getCost(consumption);
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - tStart;
            int seconds = (int) (millis / 1000);

            timeTextView.setText(String.format(Utilities.timeConversion(seconds)));
            int mInterval = 1000;
            mHandler.postDelayed(mStatusChecker, mInterval);
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }
}
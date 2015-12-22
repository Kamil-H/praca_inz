package com.praca_inz.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.praca_inz.Database.RoutesDB;
import com.praca_inz.DateAndTime;
import com.praca_inz.Models.RoutesModel;
import com.praca_inz.Interfaces.ValuesChangeListener;
import com.praca_inz.Interfaces.FinishRecordingListener;
import com.praca_inz.R;
import com.praca_inz.TrackRoute;
import com.praca_inz.UnitConversions;

import java.util.List;

/**
 * Created by KamilH on 2015-10-14.
 */

public class RoutesFragment extends Fragment implements ValuesChangeListener, FinishRecordingListener {
    private View view;
    private Button startButton;
    private TextView distanceTextView, timeTextView, avgSpeedTextView, maxSpeedTextView;
    private long tStart = 0;
    private float consumption = 0;
    private boolean isFirstLocation = true, isStart = true;
    private RecyclerView recyclerView;
    private SharedPreferences carInfoPreferences, petrolPreferences;
    private TrackRoute trackRoute;
    private Handler mHandler;

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

        trackRoute = new TrackRoute(getActivity());
        trackRoute.addValuesChangeListener(this);
        trackRoute.addFinishRecordingListeners(this);

        startButton = (Button) view.findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStart) {
                    if (isGPSenabled()) {
                        trackRoute.startRecording();
                        Toast.makeText(getActivity(), "Pomiar rozpocznie się w momencie, gdy GPS ustali pozycję", Toast.LENGTH_SHORT).show();

                        startButton.setText("STOP");
                        isStart = false;
                    }
                } else {
                    stopRepeatingTask();

                    if (trackRoute.isRecording()) {
                        showCostDialog();
                    }

                    startButton.setText("START");
                    isStart = true;
                }
            }
        });

        mHandler = new Handler();

        carInfoPreferences = getActivity().getSharedPreferences("car_info_preferences", Context.MODE_PRIVATE);
        petrolPreferences = getActivity().getSharedPreferences("petrol_prices_preferences", Context.MODE_PRIVATE);
        this.consumption = carInfoPreferences.getFloat("consumption", 0);

        return view;
    }

    private void getFuelCost(double consum){
        int petrolType = carInfoPreferences.getInt("petrolType", 0);
        double price = petrolPreferences.getFloat(String.valueOf(petrolType), 0);
        if (consum != 0) {
            trackRoute.stopRecording(price * consum);
            Log.w("TAG", String.format("petrolType: %d, price: %f, consumption: %f", petrolType, price, consum));
        } else {
            trackRoute.stopRecording(0);
        }
    }

    private void displayCards(){
        RoutesDB rDB = new RoutesDB(getActivity());
        List<RoutesModel> routesModels = rDB.getAllRoutes();

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), routesModels);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void fillForms(RoutesModel routesModel){
        avgSpeedTextView.setText(String.valueOf(String.format("%.2f", routesModel.getAvgSpeed())));
        maxSpeedTextView.setText(String.valueOf(String.format("%.2f", routesModel.getMaxSpeed())));
        distanceTextView.setText(String.valueOf(String.format("%.2f", routesModel.getDistance() * UnitConversions.M_TO_KM)));
    }

    private boolean isGPSenabled() {
        LocationManager locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
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
                getFuelCost(Double.parseDouble(consumptionEditText.getText().toString()));
            }
        });
        dialogBuilder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                getFuelCost(consumption);
            }
        });
        dialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                getFuelCost(consumption);
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

            timeTextView.setText(String.format(DateAndTime.timeConversion(seconds)));
            int mInterval = 1000;
            mHandler.postDelayed(mStatusChecker, mInterval);
        }
    };

    private void startRepeatingTask() {
        mStatusChecker.run();
    }

    private void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    @Override
    public void valuesChanged(RoutesModel routesModel) {
        if(isFirstLocation){
            tStart = System.currentTimeMillis();
            startRepeatingTask();
        }
        isFirstLocation = false;
        fillForms(routesModel);
    }

    @Override
    public void recordingFinished() {
        displayCards();
    }
}
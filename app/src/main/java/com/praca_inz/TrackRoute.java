package com.praca_inz;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.praca_inz.Interfaces.FinishRecordingListener;
import com.praca_inz.Interfaces.ValuesChangeListener;
import com.praca_inz.Database.RoutesDB;
import com.praca_inz.Models.RoutesModel;
import com.praca_inz.Database.RoutesPointsDB;
import com.praca_inz.Models.RoutesPointsModel;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by KamilH on 2015-12-17.
 */
public class TrackRoute {
    private Context context;
    private Location lastLocation;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private long tStart = 0, routeID;
    private double maxSpeed = 0, avgSpeed = 0, distance = 0, time = 0;
    private boolean isFirstLocation = true, isRecording = false;
    private List<ValuesChangeListener> valuesChangeListeners = new ArrayList<>();
    private List<FinishRecordingListener> finishRecordingListeners = new ArrayList<>();

    public TrackRoute(Context context){
        this.context = context;
        getTrackID();
    }

    public void addValuesChangeListener(ValuesChangeListener listener) {
        valuesChangeListeners.add(listener);
    }

    void notifyValuesChanged(RoutesModel routesModel){
        for(ValuesChangeListener listener : valuesChangeListeners){
            listener.valuesChanged(routesModel);
        }
    }

    public void addFinishRecordingListeners(FinishRecordingListener listener) {
        finishRecordingListeners.add(listener);
    }

    void notifyFinishRecording(){
        for(FinishRecordingListener listener : finishRecordingListeners){
            listener.recordingFinished();
        }
    }

    private void getTrackID(){
        RoutesDB rDB = new RoutesDB(context.getApplicationContext());
        this.routeID = rDB.getRowCount() + 1;
    }

    public void startRecording(){
        getLocation();
    }

    private void getLocation() {
        locationManager = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                isRecording = true;
                long tEnd = System.currentTimeMillis();

                double accuracy = location.getAccuracy();
                double elapsedTime = getElapsedTime(tStart, tEnd);
                double speed = location.getSpeed();

                if(!isFirstLocation){
                    if (speed > 0) {
                        TrackCalculations trackCalculations = new TrackCalculations(lastLocation, location, elapsedTime, distance);
                        RoutesPointsModel routesPointsModel = new RoutesPointsModel((int) routeID, location.getLatitude(), location.getLongitude(), elapsedTime, accuracy, speed);
                        updateRoutesPointsDB(routesPointsModel);
                        saveData(trackCalculations.getAvgSpeed(), speed, trackCalculations.getDistance(), elapsedTime);
                        lastLocation = location;
                    }
                }
                else {
                    tStart = System.currentTimeMillis();
                    lastLocation = location;
                    isFirstLocation = false;
                }
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
        RoutesModel routesModel = new RoutesModel(distance, time, avgSpeed, maxSpeed);
        notifyValuesChanged(routesModel);
    }

    private double getElapsedTime(long tStart, long tEnd){
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;

        return Math.round(elapsedSeconds);
    }

    private void updateRoutesPointsDB(RoutesPointsModel routesPointsModel){
        RoutesPointsDB rPDB = new RoutesPointsDB(context.getApplicationContext());
        rPDB.addPoint(routesPointsModel);
    }

    public void stopRecording(double fuelCost){
        if(isRecording){
            RoutesModel routesModel = new RoutesModel(Utilities.roundOff(this.distance), this.time, getTripCost(fuelCost), Utilities.roundOff(this.avgSpeed),
                    Utilities.roundOff(this.maxSpeed), DateAndTime.getCurrentTime());
            updateRoutesDB(routesModel);
        }
        removeGPSupdates();
        isRecording = false;
        resetValues();
        notifyFinishRecording();
    }

    private double getTripCost(double fuelCost){
        return Utilities.roundOff((UnitConversions.M_TO_KM * distance / 100) * fuelCost);
    }

    private void updateRoutesDB(RoutesModel routesModel){
        RoutesDB rDB = new RoutesDB(context.getApplicationContext());
        rDB.addRoute(routesModel);
    }

    private void removeGPSupdates(){
        locationManager.removeUpdates(locationListener);
    }

    public boolean isRecording(){
        return isRecording;
    }

    private void resetValues(){
        maxSpeed = 0;
        avgSpeed = 0;
        distance = 0;
        time = 0;
    }
}

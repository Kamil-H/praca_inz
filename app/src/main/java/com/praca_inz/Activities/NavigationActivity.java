package com.praca_inz.Activities;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beyondar.android.fragment.BeyondarFragmentSupport;
import com.beyondar.android.world.World;
import com.praca_inz.TrackCalculations;
import com.praca_inz.GeneratePOIs;
import com.praca_inz.Global;
import com.praca_inz.ImagesClass;
import com.praca_inz.Models.MapModels.Leg;
import com.praca_inz.Models.MapModels.Route;
import com.praca_inz.Models.MapModels.Step;
import com.praca_inz.R;
import com.praca_inz.UnitConversions;
import com.praca_inz.Utilities;

import java.util.List;

/**
 * Created by KamilH on 2015-11-04.
 */
public class NavigationActivity extends FragmentActivity {
    private TextView nowTextView, nextTextView, nowDistanceTextView, distanceTextView, timeTextView;
    private ImageView nowImageView, nextImageView;
    private Route route;
    private Leg leg;
    private List<Step> stepList;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location lastLocation;
    private int iterator = 0, manoeuvreIndex = 0;
    private double routeDistance = 0, nextManoeuvreDistance = 0;
    private BeyondarFragmentSupport mBeyondarFragment;
    private World mWorld;
    private boolean isLessThan = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Global global = (Global) getApplicationContext();
        route = global.getRoute();
        leg = route.getLegsList().get(0);
        stepList = leg.getStepList();

        mWorld = GeneratePOIs.generateObjects(this, stepList);
        mBeyondarFragment = (BeyondarFragmentSupport) getSupportFragmentManager().findFragmentById(
                R.id.beyondarFragment);
        mBeyondarFragment.setWorld(mWorld);

        routeDistance = leg.getDistance().getValue();
        nextManoeuvreDistance = stepList.get(0).getDistance().getValue();

        nowDistanceTextView = (TextView) findViewById(R.id.nowDistanceTextView);
        distanceTextView = (TextView) findViewById(R.id.distanceTextView);
        timeTextView = (TextView) findViewById(R.id.timeTextView);
        nowTextView = (TextView) findViewById(R.id.nowTextView);
        nextTextView = (TextView) findViewById(R.id.nextTextView);

        nowImageView = (ImageView) findViewById(R.id.nowImageView);
        nextImageView = (ImageView) findViewById(R.id.nextImageView);

        timeTextView.setText(Utilities.getTripTime(leg.getDuration().getValue()));

        for(int i = 0; i<3; i++){
            Toast.makeText(this, Html.fromHtml(stepList.get(0).getHtmlInstructions()), Toast.LENGTH_LONG).show();
        }

        if(isGPSenabled()){
            getLocation();
        }
        fillTexts();
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    private void getLocation(){
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                mWorld.setLocation(location);
                if (iterator > 0 && location.getSpeed() > 0 ){
                    TrackCalculations trackCalculations = new TrackCalculations(lastLocation, location);
                    calculateDistances(trackCalculations.getDistance());
                    lastLocation = location;
                } else {
                    lastLocation = location;
                }
                iterator++;
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

    private void calculateDistances(double distance){
        routeDistance = routeDistance - distance;
        nextManoeuvreDistance = nextManoeuvreDistance - distance;

        checkDistances();
    }

    private void checkDistances(){
        if(routeDistance <= 1000){
            if(routeDistance <= 0){
                distanceTextView.setText("0m");
            } else {
                // jeśli wartość jest mniejsza niż 1, ale większa od 0
                distanceTextView.setText(String.valueOf(routeDistance) + "m");
            }
        } else {
            // jeśli wartość jest większa od 1 to wyświetlam w KM
            distanceTextView.setText(String.valueOf(Utilities.roundOff(routeDistance * UnitConversions.M_TO_KM)) + "km");
        }

        if(nextManoeuvreDistance <= 100 && !isLessThan){
            isLessThan = true;
            TrackCalculations trackCalculations = new TrackCalculations(lastLocation, stepList.get(manoeuvreIndex).getEndLocation().getLat(),
                    stepList.get(manoeuvreIndex).getEndLocation().getLon());
            double nextManoeuvreDistanceTMP = trackCalculations.getDistance();

            if((nextManoeuvreDistanceTMP - nextManoeuvreDistance) >= 50){
                Toast.makeText(this, "Zjechałeś z trasy!", Toast.LENGTH_LONG).show();
            }
            else {
                nextManoeuvreDistance = nextManoeuvreDistanceTMP;
            }
        }

        if(nextManoeuvreDistance <= 1000){
            if(nextManoeuvreDistance <= 0){
                manoeuvreIndex++;
                if(manoeuvreIndex < stepList.size()){
                    isLessThan = false;
                    nextManoeuvreDistance = stepList.get(manoeuvreIndex).getDistance().getValue();
                    fillTexts();
                } else {
                    finishNavigation();
                }
            } else {
                // jeśli wartość jest mniejsza niż 1000, ale większa od 0
                nowDistanceTextView.setText(String.valueOf(nextManoeuvreDistance) + "m");
            }
        } else {
            // jeśli wartość jest większa od 1000 to wyświetlam w KM
            nowDistanceTextView.setText(String.valueOf(Utilities.roundOff(nextManoeuvreDistance * UnitConversions.M_TO_KM)) + "km");
        }
    }

    private void fillTexts(){
        if(manoeuvreIndex + 1 < stepList.size()){
            Step step = stepList.get(manoeuvreIndex + 1);
            nowTextView.setText(Html.fromHtml(step.getHtmlInstructions()));
            nowImageView.setImageResource(getImageResourceID(ImagesClass.setIcon(step.getManeuver())));
        }

        if(manoeuvreIndex + 2 < stepList.size()){
            Step stepPlusOne = stepList.get(manoeuvreIndex + 2);
            nextTextView.setText(Html.fromHtml(stepPlusOne.getHtmlInstructions()));
            nextImageView.setImageResource(getImageResourceID(ImagesClass.setIcon(stepPlusOne.getManeuver())));
        } else {
            nextTextView.setText("Jesteś u celu!");
        }
    }

    private void finishNavigation(){
        locationManager.removeUpdates(locationListener);
        nowTextView.setText("Jesteś u celu!");
        nextTextView.setText("");
        distanceTextView.setText("--.--km");
        timeTextView.setText("--:--");
        nowDistanceTextView.setText("--.--km");
        nowImageView.setImageResource(R.drawable.ic_action_room);
        nextImageView.setVisibility(View.GONE);
    }

    private int getImageResourceID(String name){
        return getResources().getIdentifier("com.praca_inz:drawable/" + name, null, null);
    }

    private boolean isGPSenabled() {
        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))  {
            Toast.makeText(this, "Proszę włączyć GPS.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            return false;
        }
        else return true;
    }
}

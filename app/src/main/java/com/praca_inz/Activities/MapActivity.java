package com.praca_inz.Activities;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.praca_inz.Database.RoutesDB;
import com.praca_inz.DateAndTime;
import com.praca_inz.Models.RoutesModel;
import com.praca_inz.Database.RoutesPointsDB;
import com.praca_inz.Models.RoutesPointsModel;
import com.praca_inz.R;
import com.praca_inz.TestCalc;
import com.praca_inz.Utilities;

import java.util.List;

public class MapActivity extends AppCompatActivity {
    private SupportMapFragment mSupportMapFragment;
    private List<RoutesPointsModel> routePointsModels;
    private LatLngBounds.Builder bounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int id = 0;
        // odczytanie parametru
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
            Log.v("ID: ", String.valueOf(id));
        }
        getRoutePoints(id);
        getRoute(id);

        mSupportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mSupportMapFragment != null) {
            mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(final GoogleMap googleMap) {
                    if (googleMap != null && routePointsModels.size() > 0) {
                        drawPath(googleMap, routePointsModels);

                        // ustawienie ZOOMu musi być wykonywane po załadowaniu mapy
                        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                            @Override
                            public void onMapLoaded() {
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 50));
                            }
                        });
                    }
                }
            });
        }
        else Log.v("SupportMapFragment", "NULL");
    }

    private void drawPath(GoogleMap map, List<RoutesPointsModel> routePointsModels){
        bounds = new LatLngBounds.Builder();
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        for (int i = 0; i < routePointsModels.size(); i++) {
            LatLng point = new LatLng(routePointsModels.get(i).getLat(), routePointsModels.get(i).getLon());
            bounds.include(point);
            options.add(point);
        }
        Polyline line = map.addPolyline(options);
    }

    private void getRoutePoints(int id){
        RoutesPointsDB rPDB = new RoutesPointsDB(this);
        this.routePointsModels = rPDB.getRoute(id);

        TestCalc testCalc = new TestCalc(routePointsModels);
        Log.w("DISTANCE", String.valueOf(testCalc.getDistance()));
    }

    private void getRoute(int id){
        RoutesDB rDB = new RoutesDB(this);
        RoutesModel routesModel = rDB.getRoute(id);
        getSupportActionBar().setTitle(routesModel.getDate());

        fillForms(routesModel);
    }

    private void fillForms(RoutesModel rM){
        TextView distanceTextView = (TextView) findViewById(R.id.timeTextView);
        TextView timeTextView = (TextView) findViewById(R.id.distanceTextView);
        TextView maxSpeedTextView = (TextView) findViewById(R.id.maxSpeedTextView);
        TextView avgSpeedTextView = (TextView) findViewById(R.id.avgSpeedTextView);
        TextView costTextView = (TextView) findViewById(R.id.costTextView);

        distanceTextView.setText(rM.getDistance() + " km");
        timeTextView.setText(DateAndTime.timeConversion(rM.getTime()));
        avgSpeedTextView.setText(rM.getAvgSpeed() + " km/h");
        maxSpeedTextView.setText(rM.getMaxSpeed() + " km/h");
        costTextView.setText(rM.getCost() + " zł");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private double getElapsedTime(long tStart, long tEnd){
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;

        return elapsedSeconds;
    }
}

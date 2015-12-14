package com.praca_inz.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.beyondar.android.plugin.googlemap.GoogleMapWorldPlugin;
import com.beyondar.android.world.World;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.praca_inz.GeneratePOIs;
import com.praca_inz.Global;
import com.praca_inz.ModelsAndDB.MapModels.Leg;
import com.praca_inz.ModelsAndDB.MapModels.Route;
import com.praca_inz.ModelsAndDB.MapModels.Step;
import com.praca_inz.R;
import com.praca_inz.Utilities;

import java.util.List;
import java.util.ArrayList;

public class PreviewMapActivity extends AppCompatActivity {
    private SupportMapFragment mSupportMapFragment;
    private Route route;
    private Leg leg;
    private List<Step> stepList;
    private final int ALL = 0, STARTSTOP = 1;
    private GoogleMapWorldPlugin mGoogleMapPlugin;
    private World mWorld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Podgląd trasy");

        Global global = (Global) getApplicationContext();
        route = global.getRoute();
        leg = route.getLegsList().get(0);
        stepList = leg.getStepList();

        mSupportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mSupportMapFragment != null) {
            mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(final GoogleMap googleMap) {
                    if (googleMap != null) {
                        drawPoints(googleMap);
                        drawPath(googleMap);

                        // ustawienie ZOOMu musi być wykonywane po załadowaniu mapy
                        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                            @Override
                            public void onMapLoaded() {
                                LatLng northeast = new LatLng(route.getBounds().getNortheast().getLat(), route.getBounds().getNortheast().getLon());
                                LatLng southwest = new LatLng(route.getBounds().getSouthwest().getLat(), route.getBounds().getSouthwest().getLon());
                                LatLngBounds bounds = new LatLngBounds(southwest, northeast);
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                            }
                        });
                    }
                }
            });
        }
        else Log.v("SupportMapFragment", "NULL");

        TextView timeTextView = (TextView) findViewById(R.id.timeTextView);
        TextView distanceTextView = (TextView) findViewById(R.id.distanceTextView);
        timeTextView.setText(Utilities.getTripTime(leg.getDuration().getValue()));
        distanceTextView.setText(leg.getDistance().getText());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity();
            }
        });
    }

    private void goToActivity(){
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
    }

    private void drawPoints(GoogleMap googleMap){
        mWorld = GeneratePOIs.generateObjects(this, stepList);
        mGoogleMapPlugin = new GoogleMapWorldPlugin(this);
        mGoogleMapPlugin.setGoogleMap(googleMap);
        mWorld.addPlugin(mGoogleMapPlugin);
    }

    private void drawPath(GoogleMap googleMap){
        List<LatLng> polyList = decodePoly(route.getOverview_polyline().getPoints());

        PolylineOptions options = new PolylineOptions().width(10).color(Color.BLUE).geodesic(true);
        for(LatLng coords : polyList){
            options.add(coords);
        }
        googleMap.addPolyline(options);
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(p);
        }
        return poly;
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
}

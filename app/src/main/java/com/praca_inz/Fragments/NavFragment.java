package com.praca_inz.Fragments;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.praca_inz.Activities.NavigationActivity;
import com.praca_inz.Activities.PreviewMapActivity;
import com.praca_inz.Adapters.RecyclerItemClickListener;
import com.praca_inz.Adapters.RecyclerViewAdapterDirections;
import com.praca_inz.InternetClasses.Directions;
import com.praca_inz.InternetClasses.Geocoding;
import com.praca_inz.Global;
import com.praca_inz.ModelsAndDB.MapModels.Route;
import com.praca_inz.ModelsAndDB.MapModels.Step;
import com.praca_inz.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KamilH on 2015-10-13.
 */
public class NavFragment extends Fragment {
    private View view;
    private Button button2;
    private EditText editText;
    private Spinner spinner;
    private List<Address> addresses;
    private Address destAddress;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location lastLocation;
    private RecyclerView recyclerView;
    private List<Route> routeList;
    private Route route;
    private boolean isSearching;

    public NavFragment() {    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isSearching){
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        editText.clearFocus();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_nav, container, false);

        button2 = (Button) view.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDirections();
            }
        });

        spinner = (Spinner) view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                editText.setText(spinner.getSelectedItem().toString());
                if (position != 0) {
                    spinner.setAdapter(null);
                    destAddress = addresses.get(position - 1);
                    if (lastLocation != null) {
                        getDirections(lastLocation.getLatitude(), lastLocation.getLongitude(), destAddress.getLatitude(), destAddress.getLongitude());
                    }
                    if(isSearching){
                        Toast.makeText(getActivity(), "Trasy zostaną wyświetlone w momencie, gdy GPS ustali Twoją pozycję.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //optionally do something here
            }
        });
        editText = (EditText) view.findViewById(R.id.editText);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if(isGPSenabled()){
                        getLocation();
                    }
                }
            }
        });

        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    getAddresses();
                    return true;
                }
                return false;
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView4);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        route = routeList.get(position);
                        Global global = (Global) getActivity().getApplicationContext();
                        global.setRoute(route);

                        Intent intent = new Intent(getActivity(), PreviewMapActivity.class);
                        startActivity(intent);
                    }
                })
        );

        return view;
    }

    private void populateSpinner(List<Address> addressList){
        ArrayList<String> descriptions = new ArrayList<String>();
        for(int i = 0; i < addressList.size()+1; i++){
            if(i == 0){
                descriptions.add("");
            }
            else{
                Address address = addressList.get(i - 1);
                String s0 = address.getAddressLine(0);
                String s1 = address.getAddressLine(1);
                String s2 = address.getAddressLine(2);
                if(s2 != null){
                    descriptions.add(s0 + ", " + s1 + ", "+ s2);
                } else {
                    descriptions.add(s0 + ", " + s1);
                }
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, descriptions);
        spinner.setAdapter(adapter);

        spinner.performClick();
    }

    /*51.093994, 16.984675*/
    private void getAddresses(){
        String text = editText.getText().toString();
        new Geocoding(getActivity(), this).execute(text);
    }

    private void getDirections(){
        new Directions(this).execute(51.0938323,16.9826363, 51.0883454,16.9969049);
    }

    public void setAddresses(List<Address> addresses){
        this.addresses = addresses;
        populateSpinner(addresses);
    }

    private void getLocation() {
        isSearching = true;
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                Log.v("TAG ", "onLocationChanged");
                if(destAddress != null){
                    getDirections(location.getLatitude(), location.getLongitude(), destAddress.getLatitude(), destAddress.getLongitude());
                    lastLocation = location;
                    locationManager.removeUpdates(locationListener);
                    isSearching = false;
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

    private void getDirections(double srcLat, double srcLon, double destLat, double destLon){
        new Directions(this).execute(srcLat, srcLon, destLat, destLon);
    }

    public void displayCards(List<Route> routeList){
        this.routeList = routeList;

        RecyclerViewAdapterDirections recyclerViewAdapterDirections = new RecyclerViewAdapterDirections(getActivity(), routeList);
        recyclerView.setAdapter(recyclerViewAdapterDirections);
    }

    private void display(List<Step> steps){
        for (Step step : steps) {
            String log = step.toString();
            Log.d("Step: : ", log);
        }
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
}
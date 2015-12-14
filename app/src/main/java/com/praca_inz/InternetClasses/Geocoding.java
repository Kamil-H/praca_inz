package com.praca_inz.InternetClasses;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.praca_inz.Fragments.NavFragment;

import java.util.Locale;

import java.util.List;

import java.io.IOException;

/**
 * Created by KamilH on 2015-10-29.
 */
public class Geocoding extends AsyncTask<String, Void, List<Address>> {

    private NavFragment navFragment;
    private static final int MAX_RESULTS = 5;
    Context mContext;

    public Geocoding(Context context, NavFragment navFragment) {
        super();
        mContext = context;
        this.navFragment = navFragment;
    }

    @Override
    protected List<Address> doInBackground(String... params) {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

        String locationName = params[0];

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(locationName, MAX_RESULTS);
        } catch (IOException e) {
        e.printStackTrace();
        }

        return addresses;
    }

    @Override
    protected void onPostExecute(List<Address> result) {
        for (Address r : result) {
            String log = r.toString();
            Log.d("Address: ", log);
        }
        navFragment.setAddresses(result);
    }

}

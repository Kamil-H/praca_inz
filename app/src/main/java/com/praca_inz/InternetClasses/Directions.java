package com.praca_inz.InternetClasses;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.praca_inz.Fragments.NavFragment;
import com.praca_inz.ModelsAndDB.MapModels.DirectionModel;
import com.praca_inz.ModelsAndDB.MapModels.Route;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

/**
 * Created by KamilH on 2015-10-17.
 */
public class Directions extends AsyncTask<Double, Void, List<Route>> {
    NavFragment navFragment;

    public Directions(NavFragment navFragment){
        this.navFragment = navFragment;
    }

    @Override
    public List<Route> doInBackground(Double... params) {
        URL url = null;
        BufferedReader in = null;
        try {

            url = new URL("https://maps.googleapis.com/maps/api/directions/json?origin=" + params[0] + "," + params[1] + "&destination=" + params[2] + "," + params[3] +
                    "&alternatives=true&language=pl&key=AIzaSyCO7VA_dor2CbL7t_o9pnG6VphvlKBFCMo");
            in = new BufferedReader(new InputStreamReader(url.openStream()));

            Log.d("URL: ", url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(in != null){
            Gson gson = new GsonBuilder().create();
            DirectionModel directionModel = gson.fromJson(in, DirectionModel.class);

            if(directionModel.getStatus().equals("OK")){
                List<Route> routeList = directionModel.getRoutes();
                return routeList;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Route> result) {
        navFragment.displayCards(result);
    }
}
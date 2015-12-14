package com.praca_inz.InternetClasses;

import android.os.AsyncTask;

import com.praca_inz.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by KamilH on 2015-10-17.
 */
public class Region extends AsyncTask<Double, Void, String> {
    MainActivity ma;
    public Region(MainActivity ma){this.ma = ma;    }

    @Override
    public String doInBackground(Double... params) {
        URL url = null;
        BufferedReader in = null;
        try {

            url = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng="+params[0]+","+params[1]);
            in = new BufferedReader(new InputStreamReader(url.openStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = null;
        String line = null;
        if(in != null){
            // przejście do 32 linii na stronie
            for (int i = 0; i<31; i++){
                if (in != null) {
                    try {
                        line = in.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            // uodębnienie tesktu znajdującego się na pozycji od 42 do 2 od końca
            return line.substring(42, line.length()-2);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        new PetrolPrices(ma).execute(result);
    }
}

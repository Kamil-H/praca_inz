package com.praca_inz.InternetClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.praca_inz.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by KamilH on 2015-10-17.
 */
public class PetrolPrices extends AsyncTask<String, Void, float[]> {
    MainActivity ma;
    public PetrolPrices(MainActivity ma){this.ma = ma;    }

    @Override
    protected float[] doInBackground(String... params) {
        URL url = null;
        BufferedReader in = null;
        try {

            url = new URL("http://www.e-petrol.pl/notowania/rynek-krajowy/ceny-stacje-paliw");
            in = new BufferedReader(new InputStreamReader(url.openStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }

        String line = null;
        float array[] = new float[4];

        if (in != null){
            try {
                while((line = in.readLine())!= null) {
                    // wyszukanie linii, w której znajduje się dane województwo
                    if (line.matches("<td class=\"even\">"+params[0]+"</td>")) {
                        // zapisanie do tablicy cen kolejnych paliw. Podane czeny są w formacie z "," zaś float wymaga ".", więc wykonuję zamianę za pomocą "replace"
                        // funkcja substring ucina tekst i pozostawia tylko to co znajduje się na pozycjach od 17 do 21
                        array[0] = Float.valueOf(in.readLine().substring(17, 21).replace(",", "."));
                        array[1] = Float.valueOf(in.readLine().substring(17, 21).replace(",", "."));
                        array[2] = Float.valueOf(in.readLine().substring(17, 21).replace(",", "."));
                        array[3] = Float.valueOf(in.readLine().substring(17, 21).replace(",", "."));
                        break;      // kończenie pętli while
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return array;
        }
        return array;
    }

    protected void onPostExecute(float[] result) {
        super.onPostExecute(result);
        SharedPreferences sharedPreferences = ma.getSharedPreferences("petrol_prices_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("0", result[0]);
        editor.putFloat("1", result[1]);
        editor.putFloat("2", result[2]);
        editor.putFloat("3", result[3]);
        editor.apply();
    }
}

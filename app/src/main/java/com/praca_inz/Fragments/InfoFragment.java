package com.praca_inz.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.praca_inz.Activities.CarInfoActivity;
import com.praca_inz.R;
import com.praca_inz.Utilities;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by KamilH on 2015-10-13.
 */
public class InfoFragment extends Fragment {
    View view;
    TextView brandAndModelTextView, yearTextView, consumptionTextView, insuranceTextView, serviceTextView, petrolTypeTextView;
    FloatingActionButton fab;

    private String brand, model, insuranceDate, serviceDate;
    private static String[] monthNames = {"styczeń" ,"luty" ,"marzec" ,"kwiecień" ,"maj" ,"czerwiec" ,"lipiec" ,"sierpień" ,"wrzesień" ,"październik" ,"listopad", "grudzień"};
    private float consumption;
    private int year, petrolType;

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_info, container, false);

        readData();

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CarInfoActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

    // OBLICZANIE POZOSTAŁYCH DNI DO ITD.
    private int getDaysToDate(int date[]){
        // wyznaczanie, który to dzień roku (ten dzień wczytany)
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(Calendar.DAY_OF_MONTH, date[0]);
        gc.set(Calendar.MONTH, date[1]-1);
        gc.set(Calendar.YEAR, date[2]);
        int numberofDaysPassed = gc.get(Calendar.DAY_OF_YEAR);

        // dzisiaj, który dzień roku
        Calendar calendar = Calendar.getInstance();
        int dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        int actualYear = Calendar.getInstance().get(Calendar.YEAR);

        int daysToDate;

        if(actualYear < date[2]){
            daysToDate = 365 - dayOfYear + numberofDaysPassed;
        }
        else {
            daysToDate = numberofDaysPassed - dayOfYear;
        }
        return daysToDate;
    }

    private int[] parseDate(String date){
        String dateArr[] = date.split(" ");
        int dateInt[] = new int[3];

        for(int i = 0; i<dateArr.length; i++)
            dateInt[i] = Integer.valueOf(dateArr[i]);

        return dateInt;
    }

    private void readData(){
        SharedPreferences preferences = getActivity().getSharedPreferences("car_info_preferences", Context.MODE_PRIVATE);
        int count = preferences.getAll().size();
        if(count == 7){
            brand = preferences.getString("brand", "");
            model = preferences.getString("model", "");
            year = preferences.getInt("year", 0);
            petrolType = preferences.getInt("petrolType", 0);
            consumption = preferences.getFloat("consumption", 0);
            insuranceDate = preferences.getString("insuranceDate", "");
            serviceDate = preferences.getString("serviceDate", "");

            fillForms();
        }
    }

    private void fillForms(){
        String petrolTypes[] = getResources().getStringArray(R.array.petrol_types);
        brandAndModelTextView = (TextView) view.findViewById(R.id.brandAndModelTextView);
        yearTextView = (TextView) view.findViewById(R.id.yearTextView);
        consumptionTextView = (TextView) view.findViewById(R.id.petrolTypeTextView);
        petrolTypeTextView = (TextView) view.findViewById(R.id.consumptionTextView);
        insuranceTextView = (TextView) view.findViewById(R.id.insuranceTextView);
        serviceTextView = (TextView) view.findViewById(R.id.serviceTextView);

        brandAndModelTextView.setText(brand + " " + model);
        yearTextView.setText(String.valueOf(year));
        petrolTypeTextView.setText(petrolTypes[petrolType]);
        consumptionTextView.setText(String.valueOf(consumption));

        int daysToInsurance = getDaysToDate(parseDate(insuranceDate));
        if(daysToInsurance >= 0)
            insuranceTextView.setText(Utilities.dateNumberToText(insuranceDate) + " " + "(" + daysToInsurance + " dni do końca)");
        else insuranceTextView.setText(Utilities.dateNumberToText(insuranceDate) + " " + "(" + Math.abs(daysToInsurance) + " dni temu minął termin!)");

        int daysToService = getDaysToDate(parseDate(serviceDate));
        if(daysToService >= 0)
            serviceTextView.setText(Utilities.dateNumberToText(serviceDate) + " " + "(" + daysToService + " dni do końca)");
        else serviceTextView.setText(Utilities.dateNumberToText(serviceDate) + " " + "(" + Math.abs(daysToService) + " dni temu minął termin!)");
    }
}
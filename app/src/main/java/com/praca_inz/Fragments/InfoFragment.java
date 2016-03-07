package com.praca_inz.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.praca_inz.Activities.CarInfoActivity;
import com.praca_inz.DateAndTime;
import com.praca_inz.R;

/**
 * Created by KamilH on 2015-10-13.
 */
public class InfoFragment extends Fragment {
    View view;
    TextView brandAndModelTextView, yearTextView, consumptionTextView, insuranceTextView, serviceTextView, petrolTypeTextView;
    FloatingActionButton fab;

    private String brand, model, insuranceDate, serviceDate;
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
        String petrolTypes[] = getResources().getStringArray(R.array.fuel_types);
        brandAndModelTextView = (TextView) view.findViewById(R.id.brandAndModelTextView);
        yearTextView = (TextView) view.findViewById(R.id.yearTextView);
        consumptionTextView = (TextView) view.findViewById(R.id.petrolTypeTextView);
        petrolTypeTextView = (TextView) view.findViewById(R.id.consumptionTextView);
        insuranceTextView = (TextView) view.findViewById(R.id.insuranceTextView);
        serviceTextView = (TextView) view.findViewById(R.id.serviceTextView);

        brandAndModelTextView.setText(String.format("%s %s", brand, model));
        yearTextView.setText(String.valueOf(year));
        petrolTypeTextView.setText(petrolTypes[petrolType]);
        consumptionTextView.setText(String.valueOf(consumption));

        int daysToInsurance = DateAndTime.getDaysBetween(insuranceDate);
        if(daysToInsurance >= 0)
            insuranceTextView.setText(getString(R.string.days_before, insuranceDate, daysToInsurance));
        else
            insuranceTextView.setText(getString(R.string.days_after, insuranceDate, Math.abs(daysToInsurance)));

        int daysToService = DateAndTime.getDaysBetween(serviceDate);
        if(daysToService >= 0)
            serviceTextView.setText(getString(R.string.days_before, serviceDate, daysToService));
        else
            serviceTextView.setText(getString(R.string.days_after, serviceDate, Math.abs(daysToService)));
    }
}
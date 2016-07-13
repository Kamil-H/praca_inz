package com.praca_inz.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.praca_inz.R;
import com.praca_inz.Utilities;

/**
 * Created by KamilH on 2015-10-13.
 */
public class CalcFragment extends Fragment {
    private SharedPreferences carInfoPreferences, petrolPreferences;
    private EditText consumptionEditText, distanceEditText, priceEditText;
    private RadioGroup radioGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calc, container, false);

        carInfoPreferences = getActivity().getSharedPreferences("car_info_preferences", Context.MODE_PRIVATE);
        petrolPreferences = getActivity().getSharedPreferences("petrol_prices_preferences", Context.MODE_PRIVATE);

        consumptionEditText = (EditText) view.findViewById(R.id.consumptionEditText);

        distanceEditText = (EditText) view.findViewById(R.id.distanceEditText);
        priceEditText = (EditText) view.findViewById(R.id.priceEditText);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioSex);

        Button button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index = radioGroup.indexOfChild(getActivity().findViewById(radioGroup.getCheckedRadioButtonId()));
                priceEditText.setText(String.valueOf(petrolPreferences.getFloat(String.valueOf(index), 0)));
            }
        });

        fillForms();

        return view;
    }

    private void fillForms(){
        consumptionEditText.setText(String.valueOf(carInfoPreferences.getFloat("consumption", 0)));
        ((RadioButton)radioGroup.getChildAt(carInfoPreferences.getInt("petrolType", 0))).setChecked(true);

        // nazwy mają taką samą kolejność jak w radioButton i petrol_types
        priceEditText.setText(String.valueOf(petrolPreferences.getFloat(String.valueOf(carInfoPreferences.getInt("petrolType", 0)), 0)));
    }

    public void count() {
        int index = -1;
        float consumption = 0, distance = 0, price = 0;

        // sprawdzenie czy zostało coś wpisane w pole spalania
        if (TextUtils.isEmpty(consumptionEditText.getText().toString())){
            consumptionEditText.setError(getString(R.string.consumption_empty));
        }
        else {
            consumption = Float.valueOf(consumptionEditText.getText().toString());
        }

        // sprawdzenie czy zostało coś wpisane w pole odległości
        if (TextUtils.isEmpty(distanceEditText.getText().toString())){
            distanceEditText.setError(getString(R.string.distance_empty));
        }
        else {
            distance = Float.valueOf(distanceEditText.getText().toString());
        }

        // sprawdzenie czy zostało coś wpisane w pole ceny
        if (TextUtils.isEmpty(priceEditText.getText().toString())){
            priceEditText.setError(getString(R.string.price_empty));
        }
        else {
            price = Float.valueOf(priceEditText.getText().toString());
        }

        // sprawdzenie czy został wybrany rodzaj paliwa
        if (radioGroup.getCheckedRadioButtonId() != -1){
            index = radioGroup.indexOfChild(getActivity().findViewById(radioGroup.getCheckedRadioButtonId()));
        }
        else {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.fuel_type_empty), Toast.LENGTH_SHORT).show();
        }

        // wyliczenie i wyświetlenie kosztów podróży z wcześniejszym spawdzeniem czy zostały wypełnione wszystkie pola
        if (consumption != 0 && distance != 0 && index != -1) {
            float result = price * (distance / 100) * consumption;
            showCostDialog(result);
        }
    }

    private void showCostDialog(float cost) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setMessage(getString(R.string.cost_calculated, Utilities.roundOff(cost), "zł"));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
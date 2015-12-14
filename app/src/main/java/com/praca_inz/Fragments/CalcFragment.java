package com.praca_inz.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.praca_inz.R;
import com.praca_inz.Utilities;

/**
 * Created by KamilH on 2015-10-13.
 */
public class CalcFragment extends Fragment {
    SharedPreferences carInfoPreferences, petrolPreferences;
    TextView resultTextView;
    EditText consumptionEditText, distanceEditText, priceEdtiText;
    RadioGroup radioGroup;
    Button button;
    View view;

    public CalcFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calc, container, false);

        carInfoPreferences = getActivity().getSharedPreferences("car_info_preferences", Context.MODE_PRIVATE);
        petrolPreferences = getActivity().getSharedPreferences("petrol_prices_preferences", Context.MODE_PRIVATE);

        resultTextView = (TextView) view.findViewById(R.id.resultTextView);
        consumptionEditText = (EditText) view.findViewById(R.id.consumptionEditText);
        distanceEditText = (EditText) view.findViewById(R.id.distanceEditText);
        priceEdtiText = (EditText) view.findViewById(R.id.priceEdtiText);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioSex);

        button = (Button) view.findViewById(R.id.button);
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
                priceEdtiText.setText(String.valueOf(petrolPreferences.getFloat(String.valueOf(index), 0)));
            }
        });

        fillForms();

        return view;
    }

    private void fillForms(){
        consumptionEditText.setText(String.valueOf(carInfoPreferences.getFloat("consumption", 0)));
        ((RadioButton)radioGroup.getChildAt(carInfoPreferences.getInt("petrolType", 0))).setChecked(true);

        // nazwy mają taką samą kolejność jak w radioButton i petrol_types
        priceEdtiText.setText(String.valueOf(petrolPreferences.getFloat(String.valueOf(carInfoPreferences.getInt("petrolType", 0)), 0)));
    }

    public void count() {
        int index = -1;
        float consumption = 0, distance = 0, price = 0;

        // sprawdzenie czy zostało coś wpisane w pole spalania
        if (TextUtils.isEmpty(consumptionEditText.getText().toString())){
            consumptionEditText.setError("Proszę podać spalanie!");
        }
        else {
            consumption = Float.valueOf(consumptionEditText.getText().toString());
        }

        // sprawdzenie czy zostało coś wpisane w pole odległości
        if (TextUtils.isEmpty(distanceEditText.getText().toString())){
            distanceEditText.setError("Proszę podać odległość!");
        }
        else {
            distance = Float.valueOf(distanceEditText.getText().toString());
        }

        // sprawdzenie czy zostało coś wpisane w pole ceny
        if (TextUtils.isEmpty(priceEdtiText.getText().toString())){
            priceEdtiText.setError("Proszę podać odległość!");
        }
        else {
            price = Float.valueOf(priceEdtiText.getText().toString());
        }

        // sprawdzenie czy został wybrany rodzaj paliwa
        if (radioGroup.getCheckedRadioButtonId() != -1){
            index = radioGroup.indexOfChild(getActivity().findViewById(radioGroup.getCheckedRadioButtonId()));
        }
        else {
            Toast.makeText(getActivity().getApplicationContext(), "Proszę zaznaczyć jeden z rodzajów paliwa!", Toast.LENGTH_SHORT).show();
        }

        // wyliczenie i wyświetlenie kosztów podróży z wcześniejszym spawdzeniem czy zostały wypełnione wszystkie pola
        if (consumption != 0 && distance != 0 && index != -1) {
            float result = price * (distance / 100) * consumption;
            resultTextView.setText("Koszt Twojej podróży to: " + String.valueOf(Utilities.roundOff(result)) + " zł");
        }
    }
}
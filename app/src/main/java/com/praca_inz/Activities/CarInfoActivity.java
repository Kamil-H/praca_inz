package com.praca_inz.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.praca_inz.DateAndTime;
import com.praca_inz.Fragments.DatePickerFragment;
import com.praca_inz.MainActivity;
import com.praca_inz.R;
import com.praca_inz.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class CarInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText consumptionEditText, modelEditText, serviceEditText, insuranceEditText;
    private Spinner yearSpinner, brandsSpinner, petrolTypesSpinner;
    private static int insuranceDateTAG = 1, serviceDateTAG = 2;
    private String brand, model, insuranceDate, serviceDate;
    private float consumption;
    private int year, petrolType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // nadanie nazwy || włączenie przycisk EXIT || nadanie przyciskowi EXIT wyglądu X
        getSupportActionBar().setTitle("EDYTUJ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);

        brandsSpinner  = (Spinner) findViewById(R.id.brandSpinner);
        modelEditText = (EditText) findViewById(R.id.modelEditText);
        consumptionEditText  = (EditText) findViewById(R.id.consumptionEditText);
        petrolTypesSpinner = (Spinner) findViewById(R.id.petrolTypesSpinner);
        yearSpinner = (Spinner) findViewById(R.id.yearSpinner);
        serviceEditText = (EditText) findViewById(R.id.serviceEditText);
        insuranceEditText = (EditText) findViewById(R.id.insuranceEditText);

        serviceEditText.setOnClickListener(this);
        insuranceEditText.setOnClickListener(this);

        populateSpinner();
        fillForms();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_car_info, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void populateSpinner(){
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= 1985; i--) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);

        yearSpinner.setAdapter(adapter);
    }

    private void fillForms() {
        SharedPreferences preferences =  this.getSharedPreferences("car_info_preferences", Context.MODE_PRIVATE);
        int count = preferences.getAll().size();
        if(count != 0){
            String brandsArray[] = getResources().getStringArray(R.array.car_brands);
            ArrayList<String> brands = new ArrayList<String>(Arrays.asList(brandsArray));
            int thisYear = Calendar.getInstance().get(Calendar.YEAR);

            brandsSpinner.setSelection(brands.indexOf(preferences.getString("brand", "")));
            modelEditText.setText(preferences.getString("model", ""));
            yearSpinner.setSelection(thisYear - preferences.getInt("year", 0));
            consumptionEditText.setText(String.valueOf(preferences.getFloat("consumption", 0)));
            insuranceEditText.setText(preferences.getString("insuranceDate", ""));
            petrolTypesSpinner.setSelection(preferences.getInt("petrolType", 0));
            serviceEditText.setText(preferences.getString("serviceDate", ""));
        }
    }

    public void onComplete(int year, int month, int day, int cur) {
        String dateToDisplay = DateAndTime.getNewDate(day, month, year);

        if (cur == insuranceDateTAG) {
            insuranceEditText.setText(dateToDisplay);
        }
        if (cur == serviceDateTAG) {
            serviceEditText.setText(dateToDisplay);
        }
    }

    private void readForms(){
        model = modelEditText.getText().toString();

        if (TextUtils.isEmpty(consumptionEditText.getText().toString())){
            consumptionEditText.setError("Proszę podać spalanie!");
        }
        else {
            consumption = Float.valueOf(consumptionEditText.getText().toString());
        }

        if (TextUtils.isEmpty(insuranceEditText.getText().toString())){
            insuranceEditText.setError("Proszę podać datę końca ubezpieczenia!");
        }
        else {
            insuranceDate = insuranceEditText.getText().toString();
        }

        if (TextUtils.isEmpty(serviceEditText.getText().toString())){
            serviceEditText.setError("Proszę podać datę końca ważności przeglądu!");
        }
        else {
            serviceDate = serviceEditText.getText().toString();
        }

        brand = brandsSpinner.getSelectedItem().toString();
        petrolType = petrolTypesSpinner.getSelectedItemPosition();
        year = Integer.valueOf(yearSpinner.getSelectedItem().toString());
    }

    private void saveData(){
        readForms();

        if (Utilities.isNotNull(model) && Utilities.isNotNull(String.valueOf(consumption)) && Utilities.isNotNull(insuranceDate) && Utilities.isNotNull(serviceDate)){
            SharedPreferences preferences = this.getSharedPreferences("car_info_preferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("brand", brand);
            editor.putString("model", model);
            editor.putInt("year", year);
            editor.putInt("petrolType", petrolType);
            editor.putFloat("consumption", consumption);
            editor.putString("insuranceDate", insuranceDate);
            editor.putString("serviceDate", serviceDate);
            editor.apply();
            goBack();
        }
        else
            Toast.makeText(getApplicationContext(), "Proszę wypełnić wszystkie pola!", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveData();

                return true;
            // obsługa przycisku EXIT
            case android.R.id.home:

                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goBack(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        DialogFragment newFragment;

        switch (v.getId()) {
            case R.id.insuranceEditText:
                newFragment = new DatePickerFragment() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        onComplete(year, month, day, insuranceDateTAG);
                    }
                };
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;

            case R.id.serviceEditText:
                newFragment = new DatePickerFragment() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        onComplete(year, month, day, serviceDateTAG);
                    }
                };
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
        }
    }
}

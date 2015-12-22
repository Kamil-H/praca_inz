package com.praca_inz.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.praca_inz.Fragments.DatePickerFragment;
import com.praca_inz.Database.FuelingDB;
import com.praca_inz.Models.FuelingModel;
import com.praca_inz.MainActivity;
import com.praca_inz.R;
import com.praca_inz.Utilities;

import java.util.Calendar;
import java.util.List;

public class AddFuelingActivity extends AppCompatActivity {
    EditText dateEditText, costEditText, litresEditText, priceEditText;
    private static String[] monthNames = {"styczeń" ,"luty" ,"marzec" ,"kwiecień" ,"maj" ,"czerwiec" ,"lipiec" ,"sierpień" ,"wrzesień" ,"październik" ,"listopad", "grudzień"};
    private float cost, litres, price;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fueling);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // nadanie nazwy || włączenie przycisk EXIT || nadanie przyciskowi EXIT wyglądu X
        getSupportActionBar().setTitle("EDYTUJ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);

        dateEditText = (EditText) findViewById(R.id.dateEditText);
        costEditText = (EditText) findViewById(R.id.costEditText);
        litresEditText = (EditText) findViewById(R.id.litresEditText);
        priceEditText = (EditText) findViewById(R.id.priceEditText);

        dateEditText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        onComplete(year, month, day);
                    }
                };
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        Calendar c = Calendar.getInstance();
        dateEditText.setText(c.get(Calendar.DAY_OF_MONTH) + " " + monthNames[c.get(Calendar.MONTH)] + " " + c.get(Calendar.YEAR));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_car_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onComplete(int year, int month, int day) {
        String monthName = monthNames[month];
        String dateToDisplay = Integer.toString(day) + " " + monthName + " " + Integer.toString(year);
        dateEditText.setText(dateToDisplay);
    }

    private void readForms(){
        boolean priceBool = TextUtils.isEmpty(priceEditText.getText().toString());
        boolean costBool = TextUtils.isEmpty(costEditText.getText().toString());
        boolean litresBool = TextUtils.isEmpty(litresEditText.getText().toString());

        date = Utilities.dateTextToNumber(dateEditText.getText().toString());

        if (!priceBool && !costBool && !litresBool){
            price = Float.valueOf(priceEditText.getText().toString());
            cost = Float.valueOf(costEditText.getText().toString());
            litres = Float.valueOf(litresEditText.getText().toString());
            saveToDB();
        }
        else if (priceBool || costBool || litresBool){
                if (priceBool && !costBool && !litresBool){
                    cost = Float.valueOf(costEditText.getText().toString());
                    litres = Float.valueOf(litresEditText.getText().toString());
                    price = cost / litres;
                    saveToDB();
                }
                else if (!priceBool && costBool && !litresBool){
                    price = Float.valueOf(priceEditText.getText().toString());
                    litres = Float.valueOf(litresEditText.getText().toString());
                    cost = price * litres;
                    saveToDB();
                }
                else if (!priceBool && !costBool && litresBool){
                    price = Float.valueOf(priceEditText.getText().toString());
                    cost = Float.valueOf(costEditText.getText().toString());
                    litres = cost / price;
                    saveToDB();
                }
                else Toast.makeText(getApplicationContext(), "Proszę wypełnić 2 z 3 pól", Toast.LENGTH_SHORT).show();
            }
        //String val = String.valueOf(price) + " " + String.valueOf(litres) + " " + String.valueOf(cost);
        //Log.v("FUELING", val);
    }

    private void saveToDB(){
        FuelingDB fDB = new FuelingDB(this);
        FuelingModel fuelingModel = new FuelingModel(price, litres, cost, date);
        fDB.addFueling(fuelingModel);

        List<FuelingModel> fuelingModels = fDB.getAllFuelings();

        for (FuelingModel fM : fuelingModels) {
            String log = fM.toString();
            Log.d("Fuelings: : ", log);
        }
        goBack();
    }

    private void goBack(){
        Intent i = new Intent(this, MainActivity.class);
        // przekazanie do głownego activity wartości 3 oznaczającej pozycję (zakładkę, która ma być zaznaczona)
        i.putExtra("position", 3);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                readForms();
                return true;

            // obsługa przycisku EXIT
            case android.R.id.home:

                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

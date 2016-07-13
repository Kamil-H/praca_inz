package com.praca_inz.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.praca_inz.Database.FuelingDB;
import com.praca_inz.DateAndTime;
import com.praca_inz.Fragments.DatePickerFragment;
import com.praca_inz.MainActivity;
import com.praca_inz.Models.FuelingModel;
import com.praca_inz.R;

public class AddFuelingActivity extends AppCompatActivity {
    EditText dateEditText, costEditText, litresEditText, priceEditText;
    private float cost, litres, price;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fueling);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // nadanie nazwy || włączenie przycisk EXIT || nadanie przyciskowi EXIT wyglądu X
        getSupportActionBar().setTitle(getString(R.string.edit_button));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);

        dateEditText = (EditText) findViewById(R.id.dateEditText);
        costEditText = (EditText) findViewById(R.id.costEditText);
        litresEditText = (EditText) findViewById(R.id.litresEditText);
        priceEditText = (EditText) findViewById(R.id.priceEditText);

        dateEditText.setOnClickListener(new View.OnClickListener() {
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

        dateEditText.setText(DateAndTime.getCurrentDate());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_car_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onComplete(int year, int month, int day) {
        String dateToDisplay = DateAndTime.getNewDate(day, month, year);
        dateEditText.setText(dateToDisplay);
    }

    private void readForms(){
        boolean priceBool = TextUtils.isEmpty(priceEditText.getText().toString());
        boolean costBool = TextUtils.isEmpty(costEditText.getText().toString());
        boolean litresBool = TextUtils.isEmpty(litresEditText.getText().toString());

        date = dateEditText.getText().toString();

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
                    saveAndGoBack();
                }
                else if (!priceBool && costBool && !litresBool){
                    price = Float.valueOf(priceEditText.getText().toString());
                    litres = Float.valueOf(litresEditText.getText().toString());
                    cost = price * litres;
                    saveAndGoBack();
                }
                else if (!priceBool && !costBool && litresBool){
                    price = Float.valueOf(priceEditText.getText().toString());
                    cost = Float.valueOf(costEditText.getText().toString());
                    litres = cost / price;
                    saveAndGoBack();
                }
                else Toast.makeText(getApplicationContext(), getString(R.string.fill_2_from_3), Toast.LENGTH_SHORT).show();
            }
    }

    private void saveAndGoBack(){
        saveToDB();
        goBack();
    }

    private void saveToDB(){
        FuelingDB fDB = new FuelingDB(this);
        FuelingModel fuelingModel = new FuelingModel(price, litres, cost, date);
        fDB.addFueling(fuelingModel);
    }

    private void goBack(){
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("position", 3);
        startActivity(i);
        this.finish();
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

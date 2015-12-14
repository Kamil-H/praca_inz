package com.praca_inz.ModelsAndDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.praca_inz.ModelsAndDB.FuelingModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KamilH on 2015-10-18.
 */
public class FuelingDB extends SQLiteOpenHelper {
    private final String TABLE_NAME = "fueling", KEY_ID = "id", PRICE = "price", LITRES = "litres", COST = "cost", DATE = "date";

    public FuelingDB(Context context) {
        super(context, "Fueling", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FUELING_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + PRICE + " REAL,"
                + LITRES + " REAL," + COST + " REAL," + DATE + " INTEGER" + ")";
        db.execSQL(CREATE_FUELING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addFueling(FuelingModel fuelingModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRICE, fuelingModel.getPrice());
        values.put(LITRES, fuelingModel.getLitres());
        values.put(COST, fuelingModel.getCost());
        values.put(DATE, fuelingModel.getDate());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }


    public List<FuelingModel> getAllFuelings() {
        List<FuelingModel> fuelingModels = new ArrayList<FuelingModel>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                FuelingModel fuelingModel = new FuelingModel();
                fuelingModel.setId(Integer.parseInt(cursor.getString(0)));
                fuelingModel.setPrice(Float.parseFloat(cursor.getString(1)));
                fuelingModel.setLitres(Float.parseFloat(cursor.getString(2)));
                fuelingModel.setCost(Float.parseFloat(cursor.getString(3)));
                fuelingModel.setDate((cursor.getString(4)));
                fuelingModels.add(fuelingModel);
            } while (cursor.moveToNext());
        }
        return fuelingModels;
    }

    // Deleting a shop
    public void deleteFueling(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    public void removeAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Fueling", null, null);
    }
}

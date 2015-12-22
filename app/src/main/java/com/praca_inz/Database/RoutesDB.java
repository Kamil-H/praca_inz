package com.praca_inz.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.praca_inz.Models.RoutesModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KamilH on 2015-10-23.
 */
public class RoutesDB extends SQLiteOpenHelper {
    private final String TABLE_NAME = "routes", KEY_ID = "id", DISTANCE = "distance", TIME = "time", COST = "cost", AVGSPEED = "avgspeed", MAXSPEED = "maxspeed", DATE = "date";

    public RoutesDB(Context context) {
        super(context, "Routes", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE "
                + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + DISTANCE + " TEXT,"
                + TIME + " TEXT,"
                + COST + " TEXT,"
                + AVGSPEED + " TEXT,"
                + MAXSPEED + " TEXT,"
                + DATE + " TEXT" +")";

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addRoute(RoutesModel routesModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DISTANCE, routesModel.getDistance());
        values.put(TIME, routesModel.getTime());
        values.put(COST, routesModel.getCost());
        values.put(AVGSPEED, routesModel.getAvgSpeed());
        values.put(MAXSPEED, routesModel.getMaxSpeed());
        values.put(DATE, routesModel.getDate());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<RoutesModel> getAllRoutes() {
        List<RoutesModel> routesModels = new ArrayList<RoutesModel>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                RoutesModel routesModel = new RoutesModel();

                routesModel.setId(cursor.getInt(0));
                routesModel.setDistance(Double.parseDouble(cursor.getString(1)));
                routesModel.setTime(Double.parseDouble(cursor.getString(2)));
                routesModel.setCost(Double.parseDouble(cursor.getString(3)));
                routesModel.setAvgSpeed(Double.parseDouble(cursor.getString(4)));
                routesModel.setMaxSpeed(Double.parseDouble(cursor.getString(5)));
                routesModel.setDate(cursor.getString(6));

                routesModels.add(routesModel);
            } while (cursor.moveToNext());
        }
        return routesModels;
    }

    public RoutesModel getRoute(int ID){
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID + "=" + ID;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        RoutesModel routesModel = new RoutesModel();
        if (cursor.moveToFirst()) {
            routesModel.setId(cursor.getInt(0));
            routesModel.setDistance(Double.parseDouble(cursor.getString(1)));
            routesModel.setTime(Double.parseDouble(cursor.getString(2)));
            routesModel.setCost(Double.parseDouble(cursor.getString(3)));
            routesModel.setAvgSpeed(Double.parseDouble(cursor.getString(4)));
            routesModel.setMaxSpeed(Double.parseDouble(cursor.getString(5)));
            routesModel.setDate(cursor.getString(6));
        }
        return routesModel;
    }

    public void deleteRoute(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void removeAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Routes", null, null);
    }

    public long getRowCount() {
        return DatabaseUtils.queryNumEntries(getReadableDatabase(), TABLE_NAME);
    }
}

package com.praca_inz.ModelsAndDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KamilH on 2015-10-24.
 */
public class RoutesPointsDB extends SQLiteOpenHelper {
    private final String TABLE_NAME = "routes_points", KEY_ID = "id", ROUTE_ID = "route_id", LAT = "lat", LON = "lon", TIME = "time", ACCURACY = "accuracy", SPEED = "speed";

    public RoutesPointsDB(Context context) {
        super(context, "Routes_points", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE "
                + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + ROUTE_ID + " INTEGER,"
                + LAT + " TEXT,"
                + LON + " TEXT,"
                + TIME + " TEXT,"
                + ACCURACY + " TEXT,"
                + SPEED + " TEXT" +")";

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addPoint(RoutesPointsModel routesPointsModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ROUTE_ID, routesPointsModel.getTrackID());
        values.put(LAT, routesPointsModel.getLat());
        values.put(LON, routesPointsModel.getLon());
        values.put(TIME, routesPointsModel.getTime());
        values.put(ACCURACY, routesPointsModel.getAccuracy());
        values.put(SPEED, routesPointsModel.getSpeed());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<RoutesPointsModel> getRoute(long routeID) {
        List<RoutesPointsModel> routesPointsModels = new ArrayList<RoutesPointsModel>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + ROUTE_ID + "=" + routeID;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                RoutesPointsModel routesPointsModel = new RoutesPointsModel();

                routesPointsModel.setId(cursor.getInt(0));
                routesPointsModel.setTrackID(cursor.getInt(1));
                routesPointsModel.setLat(Double.parseDouble(cursor.getString(2)));
                routesPointsModel.setLon(Double.parseDouble(cursor.getString(3)));
                routesPointsModel.setTime(Double.parseDouble(cursor.getString(4)));
                routesPointsModel.setAccuracy(Double.parseDouble(cursor.getString(5)));
                routesPointsModel.setSpeed(Double.parseDouble(cursor.getString(6)));

                routesPointsModels.add(routesPointsModel);
            } while (cursor.moveToNext());
        }
        return routesPointsModels;
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
        db.delete("Routes_points", null, null);
    }
}

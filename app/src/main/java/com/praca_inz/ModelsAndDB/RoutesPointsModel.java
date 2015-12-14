package com.praca_inz.ModelsAndDB;

/**
 * Created by KamilH on 2015-10-24.
 */
public class RoutesPointsModel {
    int id, routeID;
    double lat, lon, time, accuracy, speed;

    public RoutesPointsModel(int routeID, double lat, double lon, double time, double accuracy, double speed) {
        this.routeID = routeID;
        this.lat = lat;
        this.lon = lon;
        this.time = time;
        this.accuracy = accuracy;
        this.speed = speed;
    }

    public RoutesPointsModel() { }

    @Override
    public String toString() {
        return "RoutesPointsModel{" +
                "id=" + id +
                ", routeID=" + routeID +
                ", lat=" + lat +
                ", lon=" + lon +
                ", time=" + time +
                ", accuracy=" + accuracy +
                ", speed=" + speed +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrackID() {
        return routeID;
    }

    public void setTrackID(int routeID) {
        this.routeID = routeID;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

}

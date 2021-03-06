package com.praca_inz;

import android.location.Location;

/**
 * Created by KamilH on 2015-10-22.
 */
public class TrackCalculations {
    private double distance = 0, avgSpeed = 0;

    // konstruktor w którym obliczam dystans od początku pomiaru, czyli cały dystans + dystans pomiędzy ostatnimi dwoma pomiarami
    // obliczam też prędkość, czyli cały dystans / cały czas
    public TrackCalculations(Location lastLocation, Location actualLocation, double time, double allDistance){
        this.distance = allDistance + calculateDistance(lastLocation, actualLocation);
        this.avgSpeed = calculateSpeed(allDistance, time);
    }

    public TrackCalculations(Location lastLocation, Location actualLocation){
        this.distance = Math.round(calculateDistance(lastLocation, actualLocation));
    }

    public TrackCalculations(Location lastLocation, double lat2, double lon2){
        this.distance = Math.round(calculateDistance(lastLocation, lat2, lon2));
    }

    public TrackCalculations(double lat1, double lon1, double lat2, double lon2){
        this.distance = distanceBetween(lat1, lon1, lat2, lon2);
    }

    private double calculateDistance(Location location1, Location location2)
    {
        double lat1 = location1.getLatitude();
        double lon1 = location1.getLongitude();
        double lat2 = location2.getLatitude();
        double lon2 = location2.getLongitude();

        return distanceBetween(lat1, lon1, lat2, lon2);
    }

    private double calculateDistance(Location location1, double lat2, double lon2)
    {
        double lat1 = location1.getLatitude();
        double lon1 = location1.getLongitude();

        return distanceBetween(lat1, lon1, lat2, lon2);
    }

    private double distanceBetween(double lat1, double lon1, double lat2, double lon2)
    {
        double Radius = 6371;
        double dLat = (lat2-lat1)*Math.PI/180;
        double dLon = (lon2-lon1)*Math.PI/180;
        lat1 = lat1*Math.PI/180;
        lat2 = lat2*Math.PI/180;

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = Radius * c * 1000;

        return distance;
    }

    // obliczanie prędkości w m/s i konwersja do km/h
    private double calculateSpeed(double distance, double time){
        double speed = distance / time;
        return speed;
    }

    public double getDistance() {
        return distance;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }
}

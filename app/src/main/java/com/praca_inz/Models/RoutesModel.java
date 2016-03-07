package com.praca_inz.Models;

/**
 * Created by KamilH on 2015-10-23.
 */
public class RoutesModel {
    int id;
    double distance, time, cost, avgSpeed, maxSpeed;
    String date;

    @Override
    public String toString() {
        return "RoutesModel{" +
                "distance=" + distance +
                ", time=" + time +
                ", cost=" + cost +
                ", avgSpeed=" + avgSpeed +
                ", maxSpeed=" + maxSpeed +
                ", date='" + date + '\'' +
                '}';
    }

    public RoutesModel(double distance, double time, double cost, double avgSpeed, double maxSpeed, String date){
        this.distance = distance;
        this.time = time;
        this.cost = cost;
        this.avgSpeed = avgSpeed;
        this.maxSpeed = maxSpeed;
        this.date = date;
    }

    public RoutesModel(double distance, double time, double avgSpeed, double maxSpeed){
        this.distance = distance;
        this.time = time;
        this.avgSpeed = avgSpeed;
        this.maxSpeed = maxSpeed;
    }

    public RoutesModel() {}

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

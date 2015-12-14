package com.praca_inz.ModelsAndDB.MapModels;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by KamilH on 2015-11-02.
 */
public class Leg {
    @SerializedName("distance")
    private Distance distance;
    @SerializedName("duration")
    private Duration duration;
    @SerializedName("end_address")
    private String endAddress;
    @SerializedName("end_location")
    private Coords endLocation;
    @SerializedName("start_address")
    private String startAddress;
    @SerializedName("start_location")
    private Coords startLocation;
    @SerializedName("steps")
    private List<Step> stepList;

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public Coords getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Coords endLocation) {
        this.endLocation = endLocation;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public Coords getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Coords startLocation) {
        this.startLocation = startLocation;
    }

    public List<Step> getStepList() {
        return stepList;
    }

    public void setStepList(List<Step> stepList) {
        this.stepList = stepList;
    }

    @Override
    public String toString() {
        return "Leg{" +
                "distance=" + distance +
                ", duration=" + duration +
                ", endAddress='" + endAddress + '\'' +
                ", endLocation=" + endLocation +
                ", startAddress='" + startAddress + '\'' +
                ", startLocation=" + startLocation +
                ", stepList=" + stepList +
                '}';
    }
}

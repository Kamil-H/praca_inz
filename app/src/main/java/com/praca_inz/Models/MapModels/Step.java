package com.praca_inz.Models.MapModels;

import android.text.Html;

import com.google.gson.annotations.SerializedName;

/**
 * Created by KamilH on 2015-11-02.
 */
public class Step {
    @SerializedName("distance")
    private Distance distance;
    @SerializedName("duration")
    private Duration duration;
    @SerializedName("end_location")
    private Coords endLocation;
    @SerializedName("start_location")
    private Coords startLocation;
    @SerializedName("travel_mode")
    private String travelMode;
    @SerializedName("html_instructions")
    private String htmlInstructions;
    @SerializedName("maneuver")
    private String maneuver;
    private String plainText;


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

    public Coords getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Coords endLocation) {
        this.endLocation = endLocation;
    }

    public Coords getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Coords startLocation) {
        this.startLocation = startLocation;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    public String getManeuver() {
        return maneuver;
    }

    public void setManeuver(String maneuver) {
        this.maneuver = maneuver;
    }

    public String getHtmlInstructions() {
        return htmlInstructions;
    }

    public void setHtmlInstructions(String htmlInstructions) {
        this.htmlInstructions = htmlInstructions;
        this.plainText = Html.fromHtml(htmlInstructions).toString();
    }

    @Override
    public String toString() {
        return "Step{" +
                "distance=" + distance +
                ", duration=" + duration +
                ", endLocation=" + endLocation +
                ", startLocation=" + startLocation +
                ", travelMode='" + travelMode + '\'' +
                ", htmlInstructions='" + htmlInstructions + '\'' +
                ", maneuver='" + maneuver + '\'' +
                '}';
    }
}

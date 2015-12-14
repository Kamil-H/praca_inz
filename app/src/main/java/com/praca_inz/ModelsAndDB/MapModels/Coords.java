package com.praca_inz.ModelsAndDB.MapModels;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by KamilH on 2015-11-04.
 */
public class Coords {
    @SerializedName("lat")
    private double lat;
    @SerializedName("lng")
    private double lon;

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

    @Override
    public String toString() {
        return "Coords{" +
                "lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}

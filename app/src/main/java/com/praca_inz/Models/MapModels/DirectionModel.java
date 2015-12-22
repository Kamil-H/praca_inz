package com.praca_inz.Models.MapModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;
/**
 * Created by KamilH on 2015-11-02.
 */
public class DirectionModel {
    @SerializedName("geocoded_waypoints")
    private GeocodedWaypoints[] geocodedWaypoints;
    @SerializedName("routes")
    private List<Route> routes;
    @SerializedName("status")
    private String status;

    public GeocodedWaypoints[] getGeocodedWaypoints() {
        return geocodedWaypoints;
    }

    public void setGeocodedWaypoints(GeocodedWaypoints[] geocodedWaypoints) {
        this.geocodedWaypoints = geocodedWaypoints;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

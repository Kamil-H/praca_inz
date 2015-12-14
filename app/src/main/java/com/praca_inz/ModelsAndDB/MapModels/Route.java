package com.praca_inz.ModelsAndDB.MapModels;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import com.google.gson.annotations.SerializedName;
/**
 * Created by KamilH on 2015-11-02.
 */
public class Route {
    @SerializedName("summary")
    private String summary;
    @SerializedName("warnings")
    private String[] warnings;
    @SerializedName("legs")
    private List<Leg> legsList;

    @SerializedName("waypoint_order")
    private String[] waypoint_order;
    @SerializedName("overview_polyline")
    private OverviewPolyline overview_polyline;
    @SerializedName("bounds")
    private Bounds bounds;
    @SerializedName("copyrights")
    private String copyrights;

    public String[] getWarnings() {
        return warnings;
    }

    public void setWarnings(String[] warnings) {
        this.warnings = warnings;
    }

    public String[] getWaypoint_order() {
        return waypoint_order;
    }

    public void setWaypoint_order(String[] waypoint_order) {
        this.waypoint_order = waypoint_order;
    }

    public OverviewPolyline getOverview_polyline() {
        return overview_polyline;
    }

    public void setOverview_polyline(OverviewPolyline overview_polyline) {
        this.overview_polyline = overview_polyline;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public String getCopyrights() {
        return copyrights;
    }

    public void setCopyrights(String copyrights) {
        this.copyrights = copyrights;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String[] getWarinings() {
        return warnings;
    }

    public void setWarinings(String[] warinings) {
        this.warnings = warinings;
    }

    public List<Leg> getLegsList() {
        return legsList;
    }

    public void setLegsList(List<Leg> legsList) {
        this.legsList = legsList;
    }

    @Override
    public String toString() {
        return "Route{" +
                "summary='" + summary + '\'' +
                ", warnings=" + Arrays.toString(warnings) +
                ", legsList=" + legsList +
                ", waypoint_order=" + Arrays.toString(waypoint_order) +
                ", overview_polyline='" + overview_polyline + '\'' +
                ", bounds=" + bounds +
                ", copyrights='" + copyrights + '\'' +
                '}';
    }
}

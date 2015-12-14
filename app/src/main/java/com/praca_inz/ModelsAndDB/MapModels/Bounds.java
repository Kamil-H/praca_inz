package com.praca_inz.ModelsAndDB.MapModels;


import java.io.Serializable;

/**
 * Created by KamilH on 2015-11-02.
 */
public class Bounds {
    private Coords northeast;
    private Coords southwest;

    public Coords getNortheast() {
        return northeast;
    }

    public void setNortheast(Coords northeast) {
        this.northeast = northeast;
    }

    public Coords getSouthwest() {
        return southwest;
    }

    public void setSouthwest(Coords southwest) {
        this.southwest = southwest;
    }
}

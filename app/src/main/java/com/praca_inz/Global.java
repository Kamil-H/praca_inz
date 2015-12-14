package com.praca_inz;

import android.app.Application;

import com.praca_inz.ModelsAndDB.MapModels.Route;

/**
 * Created by KamilH on 2015-11-04.
 */
public class Global extends Application {
    private Route route;

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route){
        this.route = route;
    }
}

package com.praca_inz;

import com.praca_inz.Models.RoutesPointsModel;

import java.util.List;

/**
 * Created by KamilH on 2015-12-21.
 */
public class TestCalc {
    double distance = 0;
    public TestCalc(List<RoutesPointsModel> routePointsModels){
        for (int i = 0; i<routePointsModels.size() - 1; i++){
            RoutesPointsModel src = routePointsModels.get(i);
            RoutesPointsModel dst = routePointsModels.get(i+1);
            TrackCalculations trackCalculations = new TrackCalculations(src.getLat(), src.getLon(), dst.getLat(), src.getLon());
            distance = distance + trackCalculations.getDistance();
        }
    }

    public double getDistance(){
        return distance * UnitConversions.M_TO_KM;
    }
}

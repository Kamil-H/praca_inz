package com.praca_inz;

import android.content.Context;
import android.util.Log;

import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;
import com.praca_inz.ModelsAndDB.MapModels.Step;
import java.util.List;

/**
 * Created by KamilH on 2015-11-05.
 */
public class GeneratePOIs {

    public static World sharedWorld;

    public static World generateObjects(Context context, List<Step> stepList) {
        if (sharedWorld != null) {
            return sharedWorld;
        }

        sharedWorld = new World(context);

        // początkowa pozycja użytkwnika
        sharedWorld.setGeoPosition(stepList.get(0).getStartLocation().getLat(), stepList.get(0).getStartLocation().getLon());
        for(int i = 0; i <stepList.size(); i++){
            Step step = stepList.get(i);
            GeoObject go = new GeoObject(i);
            go.setGeoPosition(step.getStartLocation().getLat(), step.getStartLocation().getLon());
            int id = context.getResources().getIdentifier("com.praca_inz:drawable/" + ImagesClass.setImage(step.getManeuver()), null, null);
            go.setImageResource(id);
            go.setName("Maneuver" + i);
            sharedWorld.addBeyondarObject(go);
        }

        GeoObject go = new GeoObject(stepList.size());
        go.setGeoPosition(stepList.get(stepList.size()-1).getEndLocation().getLat(), stepList.get(stepList.size()-1).getEndLocation().getLon());
        go.setImageResource(R.drawable.ic_action_room);
        go.setName("Punkt docelowy");
        sharedWorld.addBeyondarObject(go);
        sharedWorld.setDefaultImage(R.drawable.straight);

        Log.v("generateObjects", "HERE");
        return sharedWorld;
    }
}

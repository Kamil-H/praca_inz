package com.praca_inz;

/**
 * Created by KamilH on 2015-11-05.
 */
public class ImagesClass {

    public static String setIcon(String maneuver){
        if(maneuver == null){
            return "ic_straight";
        } else {
            switch (maneuver){
                case "turn-right":
                    return "ic_turn_right";

                case "turn-sharp-right":
                    return "ic_turn_right";

                case "turn-slightly-right":
                    return "ic_turn_slightly_right";

                case "fork-right":
                    return "ic_turn_slightly_right";

                case "ramp-right":
                    return "ic_turn_slightly_right";
/*                                                  */
                case "turn-left":
                    return "ic_turn_left";

                case "turn-sharp-left":
                    return "ic_turn_left";

                case "turn-slightly-left":
                    return "ic_turn_slightly_left";

                case "fork-left":
                    return "ic_turn_slightly_left";

                case "ramp-left":
                    return "ic_turn_slightly_left";

                case "roundabout-right":
                    return "ic_roundabout_right";

                case "roundabout-left":
                    return "ic_roundabout_left";

                case "uturn-left":
                    return "ic_uturn_left";

                case "straight":
                    return "ic_straight";

                default:
                    return "ic_straight";
            }
        }
    }

    public static String setImage(String maneuver){
        if(maneuver == null){
            return "straight";
        } else {
            switch (maneuver){
                case "turn-right":
                    return "turn_right";

                case "turn-sharp-right":
                    return "turn_right";

                case "turn-slightly-right":
                    return "turn_slightly_right";

                case "fork-right":
                    return "turn_slightly_right";

                case "ramp-right":
                    return "turn_slightly_right";
/*                                                  */
                case "turn-left":
                    return "turn_left";

                case "turn-sharp-left":
                    return "turn_left";

                case "turn-slightly-left":
                    return "turn_slightly_left";

                case "fork-left":
                    return "turn_slightly_left";

                case "ramp-left":
                    return "turn_slightly_left";

                case "roundabout-right":
                    return "roundabout_right";

                case "roundabout-left":
                    return "roundabout_left";

                case "uturn-left":
                    return "uturn_left";

                case "straight":
                    return "straight";

                default:
                    return "straight";
            }
        }
    }
}

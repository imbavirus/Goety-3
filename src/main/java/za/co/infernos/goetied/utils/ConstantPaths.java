package za.co.infernos.goetied.utils;

import za.co.infernos.goetied.Goetied;

public class ConstantPaths {
    public static String readScroll(){
        return "goetied:readScroll";
    }

    public static String structureMob(){
        return "goetied:structure";
    }

    public static String rainArrow(){
        return "goetied:rain_arrow";
    }

    public static String resultItem(){
        return "goetied:resultItem";
    }

    public static String conjuredBee(){
        return "goetied:conjuredBee";
    }

    public static String conjuredBat(){
        return "goetied:conjuredBat";
    }

    public static String gassed(){
        return "goetied:gassed";
    }

    public static String giveAI(){
        return "goetied:giveAI";
    }

    public static String keepEffects(){
        return "goetied:keepEffects";
    }

    public static String boltingDash(){
        return Goetied.location("textures/entity/bolting_dash.png").toString();
    }

}

package com.egirlsnation.swissknife.util;

public class SpawnRadiusManager {

    public boolean isInRadius(double LocX, double LocZ, int radius){
        if (LocX < 0){
            //Negative x
            if (LocZ < 0) {
                return !(LocX > -radius) || !(LocZ > -radius);
            } else {
                return !(LocX > -radius) || !(LocZ < radius);
            }
        } else if (LocZ < 0) {
            //Negative z
            return !(LocX < radius) || !(LocZ > -radius);
        } else if (LocX > -1 && LocZ > -1) {
            //Both positive
            return !(LocX < radius) || !(LocZ < radius);
        } else {
            return true;
        }
    }
}

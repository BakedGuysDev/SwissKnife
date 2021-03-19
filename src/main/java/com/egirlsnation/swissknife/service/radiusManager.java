package com.egirlsnation.swissknife.service;

public class radiusManager {

    //private swissKnife swissknife = swissKnife.getInstance();

    public boolean isInRadius(double LocX, double LocZ, int radius) {

        if (LocX < 0){
            //Negative x
            if (LocZ < 0) {
                if (LocX > -radius && LocZ > -radius) {
                    return false;
                } else {
                    return true;
                }
            } else {
                if (LocX > -radius && LocZ < radius) {
                    return false;
                } else {
                    return true;
                }
            }
        } else if (LocZ < 0) {
            //Negative z
            if (LocX < radius && LocZ > -radius) {
                return false;
            } else{
                return true;
            }
        } else if (LocX > -1 && LocZ > -1) {
            //Both positive
            if (LocX < radius && LocZ < radius) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}

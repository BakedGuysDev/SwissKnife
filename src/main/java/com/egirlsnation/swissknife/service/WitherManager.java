package com.egirlsnation.swissknife.service;

import org.bukkit.Bukkit;

public class WitherManager {

    //private swissKnife swissknife = swissKnife.getInstance();

    public boolean witherSpawningAllowed(double LocX, double LocZ) {
        int radius = 500;

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

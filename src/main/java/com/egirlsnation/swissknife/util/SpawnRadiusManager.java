/*
 * This file is part of the SwissKnife plugin distibution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2021 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the MIT License.
 *
 * You should have received a copy of the MIT
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/MIT>.
 */

package com.egirlsnation.swissknife.util;

public class SpawnRadiusManager {

    public boolean isOutsideRadius(double LocX, double LocZ, int radius){
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

    public boolean isInRadius(double LocX, double LocZ, int radius){
        if (LocX < 0){
            //Negative x
            if (LocZ < 0) {
                if (LocX > -radius && LocZ > -radius) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (LocX > -radius && LocZ < radius) {
                    return true;
                } else {
                    return false;
                }
            }
        } else if (LocZ < 0) {
            //Negative z
            if (LocX < radius && LocZ > -radius) {
                return true;
            } else{
                return false;
            }
        } else if (LocX > -1 && LocZ > -1) {
            //Both positive
            if (LocX < radius && LocZ < radius) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}

/*
 * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
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

import org.bukkit.Bukkit;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ServerUtils {

    public List<Double> getTPS(){
        double[] tps = Bukkit.getServer().getTPS();
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);
        List<Double> roundedTPS = new ArrayList<Double>();
        for(double val : tps){
            if(val >= 20){
                roundedTPS.add((double) 20);
            }else{
                roundedTPS.add(Double.parseDouble(df.format(val)));
            }
        }
        return roundedTPS;
    }
}

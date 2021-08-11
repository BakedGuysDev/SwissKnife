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

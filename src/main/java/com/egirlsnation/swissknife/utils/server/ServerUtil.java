/*
 * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2022 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the MIT License.
 *
 * You should have received a copy of the MIT
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/MIT>.
 */

package com.egirlsnation.swissknife.utils.server;

import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerUtil {

    private static Object craftServer;
    private static Field tps;
    private static String version;

    public static void init(){
        version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        try{
            if(minecraftVersion() >= 17){
                craftServer = Class.forName("net.minecraft.server.MinecraftServer").getMethod("getServer").invoke(null);
            }else{
                craftServer = Class.forName("net.minecraft.server." + version + ".MinecraftServer").getMethod("getSever").invoke(null);
            }
            tps = craftServer.getClass().getField("recentTps");
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static double[] getTps(){
        double[] tpsUnform = tps();
        double[] tps = new double[tpsUnform.length];
        for(int i = 0; i < tpsUnform.length; i++){
            tps[i] = fixTps(tpsUnform[i]);
        }
        return tps;
    }

    private static double fixTps(double tps){
        return Math.min(Math.round(tps * 100.0) / 100.0, 20.0);
    }

    private static double[] tps(){
        if(version == null || craftServer == null || tps == null){
            return new double[] {0,0,0};
        }
        try{
            return ((double[]) tps.get(craftServer));
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }
        return new double[] {0,0,0};
    }

    private static int minecraftVersion(){
        try{
            final Matcher matcher = Pattern.compile("\\(MC: (\\d)\\.(\\d+)\\.?(\\d+?)?\\)").matcher(Bukkit.getVersion());
            if(matcher.find()){
                return Integer.parseInt(matcher.toMatchResult().group(2), 10);
            }else{
                throw new IllegalArgumentException(String.format("No match found in '%s'", Bukkit.getVersion()));
            }
        }catch(final IllegalArgumentException ex){
            throw new RuntimeException("Failed to determine Minecraft version", ex);
        }
    }

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

    public int getTicksFromMinutes(int minutes){
        return minutes * 60 * 20;
    }
}

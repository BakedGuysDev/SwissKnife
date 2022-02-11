/*
 * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2022 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GPL-3.0 License.
 *
 * You should have received a copy of the GPL-3.0
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/GPL-3.0>.
 */

package com.egirlsnation.swissknife.utils.server;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class LocationUtil {

    public static boolean isInSpawnRadius(double x, double z, int radius){
        Point p1 = new Point(0, 0);
        Point p2 = new Point((int) x, (int) z);

        return p2.distance(p1) < radius;
    }

    public static Location getRandomSpawnLocation(World world, int radius, int minY, int maxY){
        int x = generateInt(-radius, radius);
        int z = generateInt(-radius, radius);
        int y = minY;

        Location loc = new Location(world, x, y, z);
        Chunk chunk = loc.getChunk();

        int randomY = generateInt(0, 50);

        for(int i = randomY; i <= maxY; i++){
            if(world.getBlockAt(x, i, z).isEmpty() && world.getBlockAt(x, i+1, z).isEmpty() && !world.getBlockAt(x, i-1, z).isEmpty()){
                loc.setY(i);
                y = i;
            }
        }

        if(y == minY || ((randomY + 1) < 25 && isInSpawnRadius(x, z, 250))){
            loc.setY(world.getHighestBlockYAt(loc));
        }

        return loc;
    }

    private static int generateInt(int min, int max){
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    @Deprecated
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

    public static String getLocationString(Location location){
        return "[ " +
                location.getWorld().getName() + " : " +
                location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() +
                " ]";
    }
}

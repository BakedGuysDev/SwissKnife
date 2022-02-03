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

package com.egirlsnation.swissknife.systems.handlers;

import com.egirlsnation.swissknife.utils.server.LocationUtil;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.concurrent.ThreadLocalRandom;

public class RespawnHandler {

    private final LocationUtil locationUtil = new LocationUtil();

    public Location getRandomLocation(World world){
        int x = generateInt(-1000, 1000);
        int z = generateInt(-1000, 1000);
        int y = 0;

        Location loc = new Location(world, x, y, z);
        Chunk chunk = loc.getChunk();

        int randomY = generateInt(0, 50);

        for(int i = randomY; i <= 256; i++){
            if(world.getBlockAt(x, i, z).isEmpty() && world.getBlockAt(x, i+1, z).isEmpty() && !world.getBlockAt(x, i-1, z).isEmpty()){
                loc.setY(i);
                y = i;
            }
        }
        if(y == 0){
            loc.setY(world.getHighestBlockYAt(loc));
        }else if((randomY + 1) < 25 && locationUtil.isInRadius(x, z, 500)){
            loc.setY(world.getHighestBlockYAt(loc));
        }

        return loc;
    }

    private int generateInt(int min, int max){ return ThreadLocalRandom.current().nextInt(min, max + 1); }
}

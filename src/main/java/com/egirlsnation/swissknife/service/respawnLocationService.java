package com.egirlsnation.swissknife.service;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.concurrent.ThreadLocalRandom;

public class respawnLocationService {

    public  static Location randomLocation(World world){
        int x = generateInt(-500, 500);
        int z = generateInt(-500, 500);
        int y = 0;
        Location loc = new Location(world, x, y, z);
        Chunk chunk = loc.getChunk();

        if(!chunk.isLoaded()){
            chunk.load(true);
        }

        loc.setY(world.getHighestBlockYAt(loc));

        return loc;
    }

    public static int generateInt(int min, int max){
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}

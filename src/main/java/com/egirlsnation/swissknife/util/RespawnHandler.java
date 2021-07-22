package com.egirlsnation.swissknife.util;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.concurrent.ThreadLocalRandom;

public class RespawnHandler {

    private final SpawnRadiusManager spawnRadiusManager = new SpawnRadiusManager();

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
        }else if((randomY + 1) < 25 && spawnRadiusManager.isInRadius(x, z, 500)){
            loc.setY(world.getHighestBlockYAt(loc));
        }

        return loc;
    }

    private final int generateInt(int min, int max){ return ThreadLocalRandom.current().nextInt(min, max + 1); }
}
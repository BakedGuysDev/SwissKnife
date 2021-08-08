package com.egirlsnation.swissknife.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;

import java.util.ArrayList;
import java.util.List;

import static com.egirlsnation.swissknife.SwissKnife.Config.vehicleLimitChunk;

public class EntityUtils {

    public Integer countVehicles(Entity[] entities){
        int count = 0;
        for(Entity entity : entities){
            if(entity instanceof Vehicle){
               count++;
            }
        }
        return count;
    }

    public List<Entity> filterVehicles(Entity[] entities){
        List<Entity> vehicles = new ArrayList<>();
        for(Entity entity : entities){
            if(entity instanceof Vehicle && !entity.getPassengers().stream().anyMatch(e -> e instanceof Player)){
                vehicles.add(entity);
            }
        }
        return vehicles;
    }

    public void removeExcessVehicles(List<Entity> vehicleList){
        if(vehicleList.isEmpty() || vehicleList.size() < vehicleLimitChunk){
            return;
        }

        for(Entity entity : vehicleList){
            if(vehicleList.size() > vehicleLimitChunk){
                entity.remove();
                vehicleList.remove(entity);
            }
        }
    }
}

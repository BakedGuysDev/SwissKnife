package com.egirlsnation.swissknife.listener.entity;

import com.egirlsnation.swissknife.util.SpawnRadiusManager;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import static com.egirlsnation.swissknife.SwissKnife.Config.*;
import static com.egirlsnation.swissknife.SwissKnife.Config.jihadsPower;

public class onProjectileHit implements Listener {

    private final SpawnRadiusManager radiusManager = new SpawnRadiusManager();

    @EventHandler
    private void ProjectileHit(ProjectileHitEvent e) {
        if (e.getEntityType().equals(EntityType.SNOWBALL)){
            if(!jihadsEnabled) return;
            if (!radiusManager.isInRadius(e.getEntity().getLocation().getX(), e.getEntity().getLocation().getZ(), jihadsRadius) && limitJihadRadius) {
                return;
            }
            e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), (float) jihadsPower, true);
            return;
        }
        if(e.getEntityType().equals(EntityType.DRAGON_FIREBALL)){
            if(e.getEntity().getCustomName() == null) return;
            if(!e.getEntity().getCustomName().equals("CusFireBallSwissKnife")) return;
            e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), 2.5f, false);
        }
    }
}
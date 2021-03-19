package com.egirlsnation.swissknife.listeners;

import com.egirlsnation.swissknife.service.radiusManager;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class onSnowballHit implements Listener {
    radiusManager radiusManager = new radiusManager();

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile entity = event.getEntity();
        if (event.getEntityType() == EntityType.SNOWBALL) {
            if(!radiusManager.isInRadius(entity.getLocation().getX(), entity.getLocation().getZ(), 3000)){
                entity.getWorld().createExplosion(entity.getLocation(), 6.0F, true);
            }
        }

    }
}

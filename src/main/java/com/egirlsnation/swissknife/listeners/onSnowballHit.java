package com.egirlsnation.swissknife.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class onSnowballHit implements Listener {
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile entity = event.getEntity();
        if (event.getEntityType() == EntityType.SNOWBALL) {
            entity.getWorld().createExplosion(entity.getLocation(), 8.0F, true);
        }

    }
}

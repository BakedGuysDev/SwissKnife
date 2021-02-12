package com.egirlsnation.swissknife.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleSwimEvent;

import java.util.UUID;

import static com.egirlsnation.swissknife.swissKnife.swimmingList;

public class onSwimToggle implements Listener {

    @EventHandler
    public void onToggleSwim (EntityToggleSwimEvent e){
        if(e.getEntity() instanceof Player){
            UUID playerUUID = ((Player) e.getEntity()).getPlayer().getUniqueId();
            if(swimmingList.get(playerUUID) != null){
                if(((Player) e.getEntity()).isSwimming()){
                    return;
                }else{
                    e.setCancelled(true);
                }

            }
        }
    }
}

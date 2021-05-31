package com.egirlsnation.swissknife.listener.player;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class onPlayerInteractEntity implements Listener {

    @EventHandler
    private void PlayerInteractAtEntity(PlayerInteractEntityEvent e){
        if(!e.getRightClicked().getType().equals(EntityType.ENDER_CRYSTAL)) return;
        if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.NAME_TAG)){
            Bukkit.getLogger().info(e.getRightClicked().getCustomName());
            e.setCancelled(true);
            return;
        }
        if(e.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.NAME_TAG)){
            e.setCancelled(true);
        }
    }
}

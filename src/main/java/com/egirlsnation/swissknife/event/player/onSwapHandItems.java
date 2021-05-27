package com.egirlsnation.swissknife.event.player;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class onSwapHandItems implements Listener {

    @EventHandler
    public void SwapHandItems(PlayerSwapHandItemsEvent e){
        if(e.getOffHandItem() != null){
            if(e.getOffHandItem().getType().equals(Material.TOTEM_OF_UNDYING)){
                if(e.getOffHandItem().getAmount() > 2){
                    e.getOffHandItem().setAmount(2);
                }
            }
        }

        if(e.getMainHandItem() != null){
            if(e.getMainHandItem().getType().equals(Material.TOTEM_OF_UNDYING)){
                if(e.getMainHandItem().getAmount() > 2){
                    e.getMainHandItem().setAmount(2);
                }
            }
        }

    }
}

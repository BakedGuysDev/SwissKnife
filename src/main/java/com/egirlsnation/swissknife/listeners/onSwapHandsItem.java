package com.egirlsnation.swissknife.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class onSwapHandsItem implements Listener {

    @EventHandler
    public void onOffHandSwap(PlayerSwapHandItemsEvent e){
        if(e.getOffHandItem() != null){
            if(e.getOffHandItem().getType() == Material.TOTEM_OF_UNDYING){
                if(e.getOffHandItem().getAmount() > 2){
                    e.getOffHandItem().setAmount(2);
                }
            }
        }
    }
}

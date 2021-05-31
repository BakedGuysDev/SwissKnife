package com.egirlsnation.swissknife.listener.block;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import static com.egirlsnation.swissknife.SwissKnife.Config.illegalBlockList;

public class onBlockPlace implements Listener {

    @EventHandler
    private void BlockPlace(BlockPlaceEvent e){
        illegalBlockList.forEach( block -> {
            if(e.getBlock().getType().equals(Material.getMaterial(block))){
                e.setCancelled(true);
            }
        });
    }
}

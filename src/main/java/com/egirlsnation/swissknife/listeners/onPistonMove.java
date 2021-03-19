package com.egirlsnation.swissknife.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;

public class onPistonMove implements Listener {

    @EventHandler
    public void onPistonMoveEvent(BlockPistonExtendEvent e){
        if(e.getBlocks() != null){
            if(e.getBlocks().contains(Material.END_CRYSTAL)){
                Bukkit.getLogger().info("Crystal pushed.");
            }
        }
    }
}

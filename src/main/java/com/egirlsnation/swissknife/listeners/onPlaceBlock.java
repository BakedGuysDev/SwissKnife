package com.egirlsnation.swissknife.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;

public class onPlaceBlock implements Listener {

    World worldEnd = Bukkit.getServer().getWorld("world_the_end");


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        Block block = e.getBlock();



        if(block.getLocation().getWorld().equals(worldEnd) && block.getType().equals(Material.OBSIDIAN)){
            if(e.getBlockAgainst().getType() != null){
                if(e.getBlockAgainst().getType().equals(Material.BEDROCK)){
                    e.setCancelled(true);
                }
            }

        }
        /*if(block.getType().equals(Material.BEEHIVE) || block.getType().equals(Material.BEE_NEST)){
            e.setCancelled(true);
        }*/
    }
}

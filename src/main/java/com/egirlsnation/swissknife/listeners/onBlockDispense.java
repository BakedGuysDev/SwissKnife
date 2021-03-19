package com.egirlsnation.swissknife.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class onBlockDispense implements Listener {

    @EventHandler
    public void onBlockDispenseEvent (BlockDispenseEvent e){
        ItemStack is = e.getItem();
        ItemStack noPaper = new ItemStack(Material.PAPER);
        ItemMeta noPaperMeta = noPaper.getItemMeta();
        noPaperMeta.setDisplayName("No");
        noPaper.setItemMeta(noPaperMeta);
        List<Material> eggList = new ArrayList<>();
        eggList.add(Material.BLAZE_SPAWN_EGG);
        eggList.add( Material.WITHER_SKELETON_SPAWN_EGG);
        for(Material material:eggList){
            if(is != null){
                if(is.getType().equals(material)){
                    Location loc = e.getBlock().getLocation();
                    World world = loc.getWorld();
                    e.getBlock().setType(Material.AIR);
                    double locX = loc.getX();
                    double locY = loc.getY() + 1;
                    double locZ = loc.getZ();
                    Location noCodSpawn = new Location(world, locX, locY, locZ);
                    Entity noCod = world.spawnEntity(noCodSpawn, EntityType.COD);
                    noCod.setCustomName("No");
                    noCod.setCustomNameVisible(true);
                    e.setCancelled(true);
                }
            }
        }
    }
}

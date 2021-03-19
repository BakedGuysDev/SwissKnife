package com.egirlsnation.swissknife.listeners;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class onPlayerInteract implements Listener {

    @EventHandler
    public void onPlayerInteractEvent (PlayerInteractEvent e){
        ItemStack hand = e.getPlayer().getInventory().getItemInMainHand();
        ItemStack offHand = e.getPlayer().getInventory().getItemInOffHand();
        World world = e.getPlayer().getWorld();


        if(hand.getType().equals(Material.EXPERIENCE_BOTTLE) || offHand.getType().equals(Material.EXPERIENCE_BOTTLE)){
            ArrayList<Entity> entitiesArray = new ArrayList<>();
            Chunk chunk = e.getPlayer().getLocation().getChunk();
            for (Entity entities : chunk.getEntities()) {
                if (entities.getType().equals(EntityType.THROWN_EXP_BOTTLE)) {
                    entitiesArray.add(entities);
                }
            }
            if (entitiesArray.size() > 100) {
                for (Entity entity : chunk.getEntities()) {
                    if (entity.getType().equals(EntityType.THROWN_EXP_BOTTLE)) {
                        entity.remove();
                    }
                }
            }
        }

        /*if(hand.getType().equals(Material.END_CRYSTAL) || offHand.getType().equals(Material.END_CRYSTAL)){
            ArrayList<Entity> entitiesArray = new ArrayList<>();
            Chunk chunk = e.getPlayer().getLocation().getChunk();
            for (Entity entities : chunk.getEntities()) {
                if (entities.getType().equals(EntityType.ENDER_CRYSTAL)) {
                    entitiesArray.add(entities);
                }
            }
            if (entitiesArray.size() > 100) {
                int i = 0;
                for (Entity entity : chunk.getEntities()) {
                    if (entity.getType().equals(EntityType.ENDER_CRYSTAL)) {
                        entity.remove();
                        entitiesArray.remove(i);
                        i++;
                    }
                    if (entitiesArray.size() > 100){
                        break;
                    }
                }
            }
        }*/

        if(hand != null){
            if(hand.getType().toString().matches("[A-Z]*_SPAWN_EGG")){
                if(e.getClickedBlock() != null){
                    double locX = e.getClickedBlock().getX();
                    double locY = e.getClickedBlock().getY() + 1;
                    double locZ = e.getClickedBlock().getZ();
                    Location noCodSpawn = new Location(world, locX, locY, locZ);
                    Entity noCod = world.spawnEntity(noCodSpawn, EntityType.COD);
                    noCod.setCustomName("No");
                    noCod.setCustomNameVisible(true);
                }
                e.getPlayer().getInventory().getItemInMainHand().setAmount(0);
                e.setCancelled(true);
            }
        }else if(offHand != null){
            if(hand.getType().toString().matches("[A-Z]*_SPAWN_EGG")){
                if(e.getClickedBlock() != null){
                    double locX = e.getClickedBlock().getX();
                    double locY = e.getClickedBlock().getY() + 1;
                    double locZ = e.getClickedBlock().getZ();
                    Location noCodSpawn = new Location(world, locX, locY, locZ);
                    Entity noCod = world.spawnEntity(noCodSpawn, EntityType.COD);
                    noCod.setCustomName("No");
                    noCod.setCustomNameVisible(true);
                }
                e.getPlayer().getInventory().getItemInOffHand().setAmount(0);
                e.setCancelled(true);
            }
        }
    }
}

package com.egirlsnation.swissknife.listener.player;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.event.PlayerPlaceCrystalEvent;
import com.egirlsnation.swissknife.util.customItem.CustomItemHandler;
import com.egirlsnation.swissknife.util.IllegalItemHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public class onPlayerInteract implements Listener {

    private final SwissKnife plugin;
    public onPlayerInteract(SwissKnife plugin){ this.plugin = plugin; }

    private final IllegalItemHandler illegalItemHandler = new IllegalItemHandler();
    private final CustomItemHandler customItemHandler = new CustomItemHandler();

    @EventHandler
    private void onPlayerInteractEvent(PlayerInteractEvent e){

        if(e.getClickedBlock() != null && illegalItemHandler.isSpawnEgg(e.getItem())){
            e.getPlayer().sendMessage(ChatColor.RED + "You cannot do this m8");
            e.getItem().setAmount(0);
            e.setCancelled(true);
        }

        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)){
            if(customItemHandler.isDraconiteSword(e.getItem())){
                customItemHandler.handleSwordAbility(e.getPlayer(), e.getHand());
                return;
            }else if(customItemHandler.isDraconiteAxe(e.getItem())){
                customItemHandler.handleAxeAbility(e.getPlayer(), e.getHand(), plugin);
                return;
            }
        }

        if(e.getAction().equals(Action.RIGHT_CLICK_AIR)){
            if(customItemHandler.isDraconiteCrystal(e.getItem())){
                customItemHandler.handleCrystalAbility(e.getPlayer(), e.getHand(), plugin);
            }
        }

        if(!Action.RIGHT_CLICK_BLOCK.equals(e.getAction())) return;
        if(e.getClickedBlock().getType().equals(Material.OBSIDIAN) || e.getClickedBlock().getType().equals(Material.BEDROCK) || e.getClickedBlock().getType().equals(Material.CRYING_OBSIDIAN)){
            if(e.getMaterial().equals(Material.END_CRYSTAL)){
                Bukkit.getScheduler().runTask(plugin, () -> {
                    List<Entity> entities = e.getPlayer().getNearbyEntities(4, 4, 4);

                    for(Entity entity : entities){
                        if(EntityType.ENDER_CRYSTAL.equals(entity.getType())){
                            EnderCrystal crystal = (EnderCrystal) entity;
                            Block belowCrystal = crystal.getLocation().getBlock().getRelative(BlockFace.DOWN);

                            if(e.getClickedBlock().equals(belowCrystal)){
                                Bukkit.getPluginManager().callEvent(new PlayerPlaceCrystalEvent(e.getPlayer(), crystal, e.getItem()));
                                break;
                            }
                        }
                    }
                });
            }
        }
    }
}

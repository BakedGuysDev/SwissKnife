package com.egirlsnation.swissknife.listeners;

import com.egirlsnation.swissknife.swissKnife;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

import static com.egirlsnation.swissknife.service.illegalsPatchService.*;

public class onEntityPickUpItem implements Listener {
    private swissKnife swissknife = swissKnife.getInstance();
    Player player;
    UUID playerUUID;

    @EventHandler
    public void onEntityPickUpItem(EntityPickupItemEvent e){
        if(e.getEntity() instanceof Player){
            player = ((Player) e.getEntity()).getPlayer();
            playerUUID = player.getUniqueId();
            if(swissKnife.itemPickUpUsers.get(playerUUID) != null) {
                if (swissKnife.itemPickUpUsers.get(playerUUID)) {
                    e.setCancelled(true);
                }
            }
            ItemStack  item = e.getItem().getItemStack();
            if(item != null){
                if(echantLevelCheck(item)){
                    e.getItem().remove();
                    player.sendMessage(ChatColor.RED + "No overenchanted items for you m8.");
                    e.setCancelled(true);
                }


                if(illegalItemLoreCheck(item)){
                    e.getItem().remove();
                    player.sendMessage(ChatColor.RED + "Next time .peek first m8.");
                    e.setCancelled(true);
                }
                if(item.getType() == Material.TOTEM_OF_UNDYING){
                    if(item.getAmount() > 2){
                        item.setAmount(2);
                        e.getItem().setItemStack(item);
                    }
                }

                ItemMeta newMeta = ancientWeaponReduce(item);
                if(newMeta != null){
                    item.setItemMeta(newMeta);
                    e.getItem().setItemStack(item);
                }
            }

        }
    }
}

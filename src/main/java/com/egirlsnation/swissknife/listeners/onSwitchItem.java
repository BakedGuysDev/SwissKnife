package com.egirlsnation.swissknife.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedMainHandEvent;
import org.bukkit.inventory.ItemStack;

public class onSwitchItem implements Listener {

    ItemStack i = new ItemStack(Material.NETHERITE_SWORD);

    @EventHandler
    public void onHandSwitchItem(PlayerChangedMainHandEvent e){
        Player player = e.getPlayer();
        if(player.getInventory().getItemInMainHand() != null){
            if(player.getInventory().getItemInMainHand().getItemMeta().getLore().contains("§9§lBig Dick Energy X")){
                Bukkit.getLogger().info("Effect giving weapon detected");
                player.getInventory().getItemInMainHand().setAmount(0);
            }else if(player.getInventory().getItemInOffHand().getItemMeta().getLore().contains("§9§lBig Dick Energy X")){
                player.getInventory().getItemInMainHand().setAmount(0);
            }
        }
    }
}

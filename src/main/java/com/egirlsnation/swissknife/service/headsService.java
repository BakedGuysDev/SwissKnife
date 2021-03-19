package com.egirlsnation.swissknife.service;

import com.egirlsnation.swissknife.swissKnife;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class headsService {

    private static final swissKnife swissknife = swissKnife.getInstance();
    Random r  = new Random();
    int chance;

    public static ItemStack headsManager(Player player){
        if(player.getKiller() != null){
            Player killer = player.getKiller();
            String killerName = killer.getName();

            String displayName = player.getDisplayName();
            String killerDisplayName = killer.getDisplayName();

            ItemStack killWeapon = killer.getInventory().getItemInMainHand();
            if(shouldDrop(killWeapon)){

            }



        }

        return null;
    }

    public static Boolean shouldDrop(ItemStack weapon){
        Boolean ancientOnly = swissknife.getConfig().getBoolean("Heads.ancientWeaponsOnly");
        if(ancientOnly){
            if(weapon != null){
                if(weapon.getItemMeta() != null){
                    if(weapon.getItemMeta().hasLore()){
                        List<String> lore = weapon.getItemMeta().getLore();
                        if(lore.contains("§6Ancient weapon")){
                            return true;
                        }
                    }
                }
            }
        }else{
            return true;
        }
        return false;
    }
}

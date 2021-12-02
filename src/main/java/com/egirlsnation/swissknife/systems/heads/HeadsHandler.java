/*
 * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2021 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the MIT License.
 *
 * You should have received a copy of the MIT
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/MIT>.
 */

package com.egirlsnation.swissknife.systems.heads;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class HeadsHandler {

    private static final Random r = new Random();

    public ItemStack getHead(Player holder,Player killer){
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta= (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
        assert meta != null;
        meta.setOwningPlayer(holder);
        meta.setDisplayName(ChatColor.RED + "Head of " + holder.getName());
        meta.setLore(getLore(holder, killer));
        skull.setItemMeta(meta);

        return skull;
    }

    public List<String> getLore(Player holder, Player killer, String timestamp){
        return Arrays.asList(ChatColor.GOLD + "Skull of " + holder.getDisplayName() + ChatColor.GOLD + " (" + holder.getName() + ")",
                ChatColor.GOLD + "Gotten by " + killer.getDisplayName() + ChatColor.GOLD + " (" + killer.getName() + ")",
                ChatColor.GOLD + "On " + ChatColor.AQUA + timestamp);
    }

    public List<String> getLore(Player holder, Player killer){
        return Arrays.asList(ChatColor.GOLD + "Skull of " + holder.getDisplayName() + ChatColor.GOLD + " (" + holder.getName() + ")",
                ChatColor.GOLD + "Gotten by " + killer.getDisplayName() + ChatColor.GOLD + " (" + killer.getName() + ")",
                ChatColor.GOLD + "On " + ChatColor.AQUA + new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
    }

    public boolean isAncientOrDraconite(@Nullable ItemStack item){
        if(item == null) return false;
        if(item.getItemMeta() == null) return false;
        if(!item.getItemMeta().hasLore()) return false;
        if(item.getItemMeta().getLore() == null) return false;
        if(item.getItemMeta().getLore().contains("§6Ancient weapon")){
            return true;
        }
        if(item.getItemMeta().getLore().contains("§cDraconite Weapon")){
            return true;
        }
        return false;
    }

    public void dropHeadIfLucky(Player player, Player killer, ItemStack weapon) {
        if(weapon == null) return;
        if(player.isOp()){
            killer.sendMessage(ChatColor.RED + "This player can't drop their head.");
            return;
        }
        if(player.hasPermission("egirls.rank.slayer")){
            killer.sendMessage(ChatColor.RED + "This player has the " + ChatColor.DARK_PURPLE + "DragonSlayer " + ChatColor.RED + "rank and can't drop their head.");
            player.sendMessage(ChatColor.DARK_PURPLE + "Your DragonSlayer rank protected you from dropping your head.");
            return;
        }

        if(player.hasPermission("egirls.rank.boomerfag") && (r.nextInt(100) < 10)){
            dropHead(player, killer, true);
            return;
        }

        if(player.hasPermission("egirls.rank.legend") && (r.nextInt(100) < 20)){
            dropHead(player, killer, true);
            return;
        }

        if(player.hasPermission("egirls.rank.oldfag") && (r.nextInt(100) < 30)){
            dropHead(player, killer, true);
            return;
        }

        if(player.hasPermission("egirls.rank.vet") && (r.nextInt(100) + 1 < 50)){
            dropHead(player,killer, true);
            return;
        }

        if(player.hasPermission("egirls.rank.newfag") && (r.nextInt(100) + 1 < 75)){
            dropHead(player,killer, true);
            return;
        }

        if(player.hasPermission("egirls.rank.default") && (r.nextInt(100) + 1 < 90)){
            dropHead(player,killer, true);
            return;
        }

        Bukkit.getLogger().severe("Error occured while dropping head of " + player.getName() +".\nPlayer doesn't have any of the permissions, nor is op.");
    }

    public void dropHeadIfLucky(Player player, Player killer) {
        if(player.isOp()){
            killer.sendMessage(ChatColor.RED + "This player can't drop their head.");
            return;
        }
        if(player.hasPermission("egirls.rank.slayer")){
            killer.sendMessage(ChatColor.RED + "This player has the " + ChatColor.DARK_PURPLE + "DragonSlayer " + ChatColor.RED + "rank and can't drop their head.");
            player.sendMessage(ChatColor.DARK_PURPLE + "Your DragonSlayer rank protected you from dropping your head.");
            return;
        }
        if(player.hasPermission("egirls.rank.boomerfag") && (r.nextInt(100) < 10)){
            dropHead(player, killer, true);
            return;
        }

        if(player.hasPermission("egirls.rank.legend") && (r.nextInt(100) < 20)){
            dropHead(player, killer, true);
            return;
        }

        if(player.hasPermission("egirls.rank.oldfag") && (r.nextInt(100) < 30)){
            dropHead(player, killer, true);
            return;
        }

        if(player.hasPermission("egirls.rank.vet") && (r.nextInt(100) + 1 < 50)){
            dropHead(player,killer, true);
            return;
        }

        if(player.hasPermission("egirls.rank.newfag") && (r.nextInt(100) + 1 < 75)){
            dropHead(player,killer, true);
            return;
        }

        if(player.hasPermission("egirls.rank.default") && (r.nextInt(100) + 1 < 90)){
            dropHead(player,killer, true);
            return;
        }

        Bukkit.getLogger().severe("Error occured while dropping head of " + player.getName() +".\nPlayer doesn't have any of the permissions, nor is op.");
    }

    public void dropHead(Player player, Player killer, boolean shouldBroadcast){

        ItemStack head = getHead(player, killer);
        String broadcast = killer.getDisplayName() + ChatColor.RED +  " established their presence over "
                + player.getDisplayName() + ChatColor.RED + " and got their head.";
        player.getWorld().dropItemNaturally(player.getLocation(), head);
        killer.sendMessage(ChatColor.RED + "Player " + player.getDisplayName() + ChatColor.RED + " dropped their head.");
        killer.playSound(killer.getLocation(), Sound.ENTITY_VILLAGER_HURT, 100, 0);
        if(shouldBroadcast){
            PlayerHeadDropEvent event = new PlayerHeadDropEvent(killer, player, head, getLore(player, killer), broadcast);
            Bukkit.getPluginManager().callEvent(event);
            Bukkit.getServer().broadcastMessage(broadcast);
            return;
        }
        PlayerHeadDropEvent event = new PlayerHeadDropEvent(killer, player, head, getLore(player, killer), null);
        Bukkit.getPluginManager().callEvent(event);
    }
}

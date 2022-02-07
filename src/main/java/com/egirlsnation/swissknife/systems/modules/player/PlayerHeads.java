/*
 * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2022 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GPL-3.0 License.
 *
 * You should have received a copy of the GPL-3.0
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/GPL-3.0>.
 */

package com.egirlsnation.swissknife.systems.modules.player;

import com.egirlsnation.swissknife.events.PlayerHeadDropEvent;
import com.egirlsnation.swissknife.settings.*;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.server.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.SimpleDateFormat;
import java.util.*;

public class PlayerHeads extends Module {
    public PlayerHeads() {
        super(Categories.Player, "player-heads", "Adds playerheads with cool lores");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<String> itemName = sgGeneral.add(new StringSetting.Builder()
            .name("item-name")
            .description("The text that will be displayed as the item name (supports color codes).")
            .defaultValue(ChatColor.RED + "Skull of %player% ")
            .build()
    );

    private final Setting<List<String>> nameBlacklist = sgGeneral.add(new StringListSetting.Builder()
            .name("name-bypass")
            .description("List of names that won't drop head at all")
            .defaultValue("Lerbiq", "killmlana")
            .build()
    );

    private final Setting<Boolean> sendMessages = sgGeneral.add(new BoolSetting.Builder()
            .name("send-messages")
            .description("If the module should send messages")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> playSound = sgGeneral.add(new BoolSetting.Builder()
            .name("play-sound")
            .description("If the module should play sound to the killer when the head drops")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> broadcast = sgGeneral.add(new BoolSetting.Builder()
            .name("broadcast")
            .description("If the module should broadcast when a head drops")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> tellKiller = sgGeneral.add(new BoolSetting.Builder()
            .name("tell-killer")
            .description("If the module should tell the killer he got the head")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> tellVictim = sgGeneral.add(new BoolSetting.Builder()
            .name("tell-victim")
            .description("If the module should tell the victim they dropped their head")
            .defaultValue(true)
            .build()
    );



    private final SettingGroup sgLore = settings.createGroup("lore");


    private final Setting<Boolean> useDisplayNames = sgLore.add(new BoolSetting.Builder()
            .name("use-displaynames")
            .description("Uses display names instead of just names in lore")
            .defaultValue(true)
            .build()
    );

    private final Setting<String> lore = sgLore.add(new StringSetting.Builder()
            .name("lore")
            .description("The text that will be displayed in the lore (supports color codes)")
            .defaultValue(ChatColor.GOLD + "Skull of %player%")
            .build()
    );

    private final Setting<Boolean> putKiller = sgLore.add(new BoolSetting.Builder()
            .name("put-killer")
            .description("Puts who got the head in the lore")
            .defaultValue(true)
            .build()
    );

    private final Setting<String> killerLore = sgLore.add(new StringSetting.Builder()
            .name("killer-lore")
            .description("The text with the killer that will be displayed in the lore (supports color codes)")
            .defaultValue(ChatColor.GOLD + "Gotten by %killer%")
            .build()
    );

    private final Setting<Boolean> putDate = sgLore.add(new BoolSetting.Builder()
            .name("put-date")
            .description("Puts date in the lore")
            .defaultValue(true)
            .build()
    );

    private final Setting<String> dateLore = sgLore.add(new StringSetting.Builder()
            .name("date-lore")
            .description("The text with the date that will be displayed in the lore (supports color codes)")
            .defaultValue(ChatColor.GOLD + "On " + ChatColor.AQUA + "%date%")
            .build()
    );

    private final Setting<String> dateFormat = sgLore.add(new StringSetting.Builder()
            .name("simple-date-format")
            .description("Allows you to set the simple date format that will be used for the date. https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html")
            .defaultValue("dd.MMM.yyyy")
            .build()
    );

    private final SettingGroup sgMessages = settings.createGroup("messages");

    private final Setting<String> msgNoDrop = sgMessages.add(new StringSetting.Builder()
            .name("cant-drop-message")
            .description("Message to send when a player can't drop their head")
            .defaultValue(ChatColor.RED + "This player can't drop their head at the moment.")
            .build()
    );

    private final Setting<String> msgBroadcast = sgMessages.add(new StringSetting.Builder()
            .name("broadcast-msg")
            .description("The message that will be broadcasted to everyone")
            .defaultValue("%killer% " + ChatColor.RED + "established their presence over %player% " + ChatColor.RED + "and got their head.")
            .build()
    );

    private final Setting<String> msgKiller = sgMessages.add(new StringSetting.Builder()
            .name("killer-msg")
            .description("The message that will be sent to the killer when they get a head")
            .defaultValue(ChatColor.RED + "Player %player%" + ChatColor.RED + " dropped their head.")
            .build()
    );

    private final Setting<String> msgVictim = sgMessages.add(new StringSetting.Builder()
            .name("victim-msg")
            .description("The message that will be sent to the victim when they drop a head")
            .defaultValue(ChatColor.RED + "Player %killer%" + ChatColor.RED + " got your head.")
            .build()
    );

    @EventHandler
    private void PlayerDeath(PlayerDeathEvent e){
        if(!isEnabled()) return;

        if(e.getEntity().getKiller() == null) return;
        if(!ItemUtil.isAncientOrDraconite(e.getEntity().getKiller().getInventory().getItemInMainHand())) return;
        Player killer = e.getEntity().getKiller();
        Player victim = e.getEntity();


        if(victim.hasPermission("swissknife.heads.nodrop") || nameBlacklist.get().contains(victim.getName())){
            if(sendMessages.get()) killer.sendMessage(ChatColor.translateAlternateColorCodes('§', msgNoDrop.get()));
            return;
        }

        if((Math.random() * 100) < getChance(victim)){
            List<String> lore = getLore(victim, killer);
            ItemStack head = getHead(victim, lore);

            String broadcast = null;
            if(this.broadcast.get()){
                if(useDisplayNames.get()){
                    broadcast = msgBroadcast.get().replaceAll("%killer%", killer.getDisplayName()).replaceAll("%player%", victim.getDisplayName());
                }else{
                    broadcast = msgBroadcast.get().replaceAll("%killer%", killer.getName()).replaceAll("%player%", victim.getName());
                }
            }

            victim.getWorld().dropItemNaturally(victim.getLocation(), head);
            if(tellKiller.get()){
                if(useDisplayNames.get()){
                    killer.sendMessage(ChatColor.translateAlternateColorCodes('§', msgKiller.get().replaceAll("%player%", victim.getDisplayName())));
                }else{
                    killer.sendMessage(ChatColor.translateAlternateColorCodes('§', msgKiller.get().replaceAll("%player%", victim.getName())));
                }
            }

            if(tellVictim.get()){
                if(useDisplayNames.get()){
                    victim.sendMessage(ChatColor.translateAlternateColorCodes('§', msgVictim.get().replaceAll("%killer%", killer.getDisplayName())));
                }else{
                    victim.sendMessage(ChatColor.translateAlternateColorCodes('§', msgVictim.get().replaceAll("%killer%", killer.getName())));
                }
            }

            if(playSound.get()){
                killer.playSound(killer.getLocation(), Sound.ENTITY_VILLAGER_HURT, 100, 0);
            }

            if(this.broadcast.get()){
                PlayerHeadDropEvent event = new PlayerHeadDropEvent(killer, victim, head, lore, broadcast);
                Bukkit.getPluginManager().callEvent(event);
                if(broadcast != null) Bukkit.getServer().broadcastMessage(broadcast);
                return;
            }

            PlayerHeadDropEvent event = new PlayerHeadDropEvent(killer, victim, head, lore, null);
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    //I know this is a terrible way to do it, but I was tired tired :/
    private int getChance(Player player){
        int chance = 0;
        if(player.hasPermission("swissknife.heads.chance.100")){
            chance = 100;
        }
        if(player.hasPermission("swissknife.heads.chance.90")){
            chance = 90;
        }
        if(player.hasPermission("swissknife.heads.chance.80")){
            chance = 80;
        }
        if(player.hasPermission("swissknife.heads.chance.70")){
            chance = 70;
        }
        if(player.hasPermission("swissknife.heads.chance.60")){
            chance = 60;
        }
        if(player.hasPermission("swissknife.heads.chance.50")){
            chance = 50;
        }
        if(player.hasPermission("swissknife.heads.chance.40")){
            chance = 40;
        }
        if(player.hasPermission("swissknife.heads.chance.30")){
            chance = 30;
        }
        if(player.hasPermission("swissknife.heads.chance.20")){
            chance = 20;
        }
        if(player.hasPermission("swissknife.heads.chance.10")){
            chance = 10;
        }
        if(player.hasPermission("swissknife.heads.chance.05")){
            chance = 5;
        }
        return chance;
    }

    private ItemStack getHead(Player victim, List<String> lore){
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta= (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
        meta.setOwningPlayer(victim);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('§', itemName.get()));

        meta.setLore(lore);
        skull.setItemMeta(meta);

        return skull;
    }

    private List<String> getLore(Player victim, Player killer){
        List<String> lore = new ArrayList<>();
        if(useDisplayNames.get()){
            lore.add(ChatColor.translateAlternateColorCodes('§',
                    this.lore.get().replaceAll("%player%", victim.getDisplayName()) + " (" + victim.getName() + " )" ));

            if(putKiller.get()){
                lore.add(ChatColor.translateAlternateColorCodes('§',
                        killerLore.get().replaceAll("%killer%", killer.getDisplayName()) + "( " + killer.getName() + " )"));
            }
        }else{
            lore.add(ChatColor.translateAlternateColorCodes('§', this.lore.get().replaceAll("%player%", victim.getName())));

            if(putKiller.get()){
                lore.add(ChatColor.translateAlternateColorCodes('§', killerLore.get().replaceAll("%killer%", killer.getName())));
            }
        }

        if(putDate.get()){
            lore.add(ChatColor.translateAlternateColorCodes('§', dateLore.get().replaceAll("%date%", new SimpleDateFormat(dateFormat.get()).format(new Date()))));
        }

        return lore;
    }


}

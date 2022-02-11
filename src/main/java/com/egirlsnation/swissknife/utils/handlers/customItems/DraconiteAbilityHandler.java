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

package com.egirlsnation.swissknife.utils.handlers.customItems;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.utils.entity.player.ExpUtil;
import com.egirlsnation.swissknife.utils.server.ItemUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DraconiteAbilityHandler {
    private final Map<UUID, BukkitTask> pickaxeTaskMap = new HashMap<>();
    private final List<UUID> crystalEnabledList = new ArrayList<>();


    public void handleSwordAbility(Player player, @Nullable EquipmentSlot equipmentSlot, AbilityCooldownHandler cooldownHandler){
        if(equipmentSlot != null){
            if(equipmentSlot.equals(EquipmentSlot.HAND)){
                player.swingMainHand();
            }else if(equipmentSlot.equals(EquipmentSlot.OFF_HAND)){
                player.swingOffHand();
            }
        }

        if(!cooldownHandler.isOnSwordCooldown(player)){
            DragonFireball fireball = player.getWorld().spawn(player.getEyeLocation(), DragonFireball.class);
            fireball.setShooter(player);
            fireball.setCustomName("CusFireBallSwissKnife");
            fireball.setVelocity(player.getLocation().getDirection().multiply(3));
            cooldownHandler.setSwordCooldown(player.getUniqueId(), System.currentTimeMillis());
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, SoundCategory.PLAYERS, 100, 0);
        }else{
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + cooldownHandler.getCooldownMessage(cooldownHandler.getSwordRemainingTime(player))));
        }
    }

    public void handleAxeAbility(Player player, @Nullable EquipmentSlot equipmentSlot, AbilityCooldownHandler cooldownHandler){
        if(equipmentSlot != null){
            if(equipmentSlot.equals(EquipmentSlot.HAND)){
                player.swingMainHand();
            }else if(equipmentSlot.equals(EquipmentSlot.OFF_HAND)){
                player.swingOffHand();
            }
        }

        if(!cooldownHandler.isOnAxeCooldown(player)){
            // Handle ability
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 3));
            player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.PLAYERS, 100, 0);
            cooldownHandler.setAxeCooldown(player.getUniqueId(), System.currentTimeMillis());
            Bukkit.getServer().getScheduler().runTaskLater(SwissKnife.INSTANCE, new Runnable() {
                @Override
                public void run(){
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 50, 1));
                }
            }, 50);
            Bukkit.getServer().getScheduler().runTaskLater(SwissKnife.INSTANCE, new Runnable() {
                @Override
                public void run(){
                    player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, SoundCategory.PLAYERS, 100, 0);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 2));
                }
            }, 100);
        }else{
            // Is still on cooldown
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + cooldownHandler.getCooldownMessage(cooldownHandler.getAxeRemainingTime(player))));
        }
    }

    public void handleCrystalAbility(Player player, @Nullable EquipmentSlot equipmentSlot, AbilityCooldownHandler cooldownHandler){
        if(equipmentSlot != null){
            if(equipmentSlot.equals(EquipmentSlot.HAND)){
                player.swingMainHand();
            }else if(equipmentSlot.equals(EquipmentSlot.OFF_HAND)){
                player.swingOffHand();
            }
        }

        if(!cooldownHandler.isOnCrystalCooldown(player)){
            crystalEnabledList.add(player.getUniqueId());
            player.setGliding(false);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 6));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 150, 6));
            player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_HURT, SoundCategory.PLAYERS, 100, 0);
            cooldownHandler.setCrystalCooldown(player.getUniqueId(), System.currentTimeMillis());
            Bukkit.getServer().getScheduler().runTaskLater(SwissKnife.INSTANCE, new Runnable() {
                @Override
                public void run(){
                    player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_DEATH, SoundCategory.PLAYERS, 100, 0);
                    crystalEnabledList.remove(player.getUniqueId());
                }
            }, 100);
        }else{ // Player is on cooldown. Display cooldown message
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + cooldownHandler.getCooldownMessage(cooldownHandler.getCrystalRemainingTime(player))));
        }
    }

    public void handlePickaxeAbility(Player player, @Nullable EquipmentSlot equipmentSlot, AbilityCooldownHandler cooldownHandler, int experienceToDrain, int hasteLevel){
        if(equipmentSlot != null){
            if(equipmentSlot.equals(EquipmentSlot.HAND)){
                player.swingMainHand();
            }else if(equipmentSlot.equals(EquipmentSlot.OFF_HAND)){
                player.swingOffHand();
            }
        }

        if(pickaxeTaskMap.containsKey(player.getUniqueId())){ //Cancels the ability when it's enabled
            pickaxeTaskMap.get(player.getUniqueId()).cancel();
            pickaxeTaskMap.remove(player.getUniqueId());
            player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, SoundCategory.PLAYERS, 100, 0);
            cooldownHandler.setPickaxeCooldown(player.getUniqueId(), System.currentTimeMillis());
            return;
        }

        if(!cooldownHandler.isOnPickaxeCooldown(player)){

            player.playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.PLAYERS, 100, 0);
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run(){

                    if(ExpUtil.getExp(player) < experienceToDrain){
                        player.setExp(0);
                        player.setLevel(0);
                        player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, SoundCategory.PLAYERS, 100, 0);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 5));
                        cooldownHandler.setPickaxeCooldown(player.getUniqueId(), System.currentTimeMillis());
                        pickaxeTaskMap.remove(player.getUniqueId());
                        this.cancel();
                    }else{
                        if(ItemUtil.isDraconitePickaxe(player.getInventory().getItemInMainHand())){
                            ExpUtil.changeExp(player, -experienceToDrain);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20, hasteLevel));
                        }
                    }
                }
            }.runTaskTimer(SwissKnife.INSTANCE, 0, 20);
            pickaxeTaskMap.put(player.getUniqueId(), task);
        }else{
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + cooldownHandler.getCooldownMessage(cooldownHandler.getPickaxeRemainingTime(player))));
        }
    }

    public boolean hasCrystalAbilityOn(Player player){
        return crystalEnabledList.contains(player.getUniqueId());
    }

    public void removeFromCrystalList(Player player){
        crystalEnabledList.remove(player.getUniqueId());
    }

    public void purgePickaxeTask(Player player){
        if(pickaxeTaskMap.containsKey(player.getUniqueId())){
            pickaxeTaskMap.get(player.getUniqueId()).cancel();
            pickaxeTaskMap.remove(player.getUniqueId());
        }
    }

}

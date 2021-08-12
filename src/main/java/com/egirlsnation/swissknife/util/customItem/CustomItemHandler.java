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

package com.egirlsnation.swissknife.util.customItem;

import com.egirlsnation.swissknife.SwissKnife;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.egirlsnation.swissknife.SwissKnife.Config.xpToDrain;

public class CustomItemHandler {

    private final AbilityCooldownManager abilityCooldownManager = new AbilityCooldownManager();

    private static final Map<UUID, BukkitTask> pickaxeTaskMap = new HashMap<>();

    public ItemStack getDraconiteCrystal() {
        ItemStack item = new ItemStack(Material.END_CRYSTAL);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList("", "§cDraconite Crystal", "", "Legends say that it's one of those crystals from the top of the End towers"));
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(UUID.randomUUID(), "max_health", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(UUID.randomUUID(), "max_health", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.OFF_HAND));
        meta.setDisplayName(ChatColor.RED + "Draconite Crystal");
        item.setItemMeta(meta);
        return item;
    }

    public boolean isDraconiteCrystal(ItemStack item) {
        if (item == null) return false;
        if (!item.getType().equals(Material.END_CRYSTAL)) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        if (!meta.hasLore()) return false;
        if (meta.getLore() == null) return false;

        return meta.getLore().contains("§cDraconite Crystal");
    }

    public ItemStack getPopbobTotem() {
        ItemStack item = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList("", "§cPopbob's Totem", "", "Legends say that Popbob himself shoved this totem up his ass"));
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(UUID.randomUUID(), "max_health", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(UUID.randomUUID(), "max_health", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.OFF_HAND));
        meta.setDisplayName(ChatColor.RED + "Totem of Popbob");
        meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
        item.setItemMeta(meta);

        return item;
    }

    public boolean isPopbobTotem(ItemStack item) {
        if (item == null) return false;
        if (!item.getType().equals(Material.TOTEM_OF_UNDYING)) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        if (!meta.hasLore()) return false;
        if (meta.getLore() == null) return false;

        return meta.getLore().contains("§cPopbob's Totem");
    }

    public ItemStack getDraconiteSword() {
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList("", "§cDraconite Weapon", "", "Legends say that this exact sword was used to kill the first dragon"));
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "attack_damage", 11, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        meta.setDisplayName(ChatColor.RED + "Draconite Sword");
        item.setItemMeta(meta);

        return item;
    }

    public boolean isDraconiteSword(ItemStack item) {
        if (item == null) return false;
        if (!item.getType().equals(Material.NETHERITE_SWORD)) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        if (!meta.hasLore()) return false;
        if (meta.getLore() == null) return false;

        return meta.getLore().contains("§cDraconite Weapon");
    }

    public ItemStack getDraconiteAxe() {
        ItemStack item = new ItemStack(Material.NETHERITE_AXE);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList("", "§cDraconite Weapon", "", "Legends say that this exact axe was used to decapitate the first dragon"));
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "attack_damage", 13, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        meta.setDisplayName(ChatColor.RED + "Draconite Axe");
        item.setItemMeta(meta);

        return item;
    }

    public boolean isDraconiteAxe(ItemStack item) {
        if (item == null) return false;
        if (!item.getType().equals(Material.NETHERITE_AXE)) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        if (!meta.hasLore()) return false;
        if (meta.getLore() == null) return false;

        return meta.getLore().contains("§cDraconite Weapon");
    }

    public ItemStack getDraconitePickaxe(){
        ItemStack item = new ItemStack(Material.NETHERITE_PICKAXE);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList("", "§cDraconite Tool", "", "Legends say that this exact pickaxe was used to mine the first obsidian"));
        meta.setDisplayName(ChatColor.RED + "Draconite Pickaxe");
        item.setItemMeta(meta);

        return item;
    }

    public boolean isDraconitePickaxe(ItemStack item) {
        if (item == null) return false;
        if (!item.getType().equals(Material.NETHERITE_PICKAXE)) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        if (!meta.hasLore()) return false;
        if (meta.getLore() == null) return false;

        return meta.getLore().contains("§cDraconite Tool");
    }

    public void handleSwordAbility(Player player, EquipmentSlot equipmentSlot) {
        if (equipmentSlot.equals(EquipmentSlot.HAND)) {
            player.swingMainHand();
        } else {
            player.swingOffHand();
        }
        long timeLeft = System.currentTimeMillis() - abilityCooldownManager.getSwordCooldown(player.getUniqueId());
        if(TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= AbilityCooldownManager.DEFAULT_SWORD_COOLDOWN){
            DragonFireball fireball = player.getWorld().spawn(player.getEyeLocation(), DragonFireball.class);
            fireball.setShooter(player);
            fireball.setCustomName("CusFireBallSwissKnife");
            fireball.setVelocity(player.getLocation().getDirection().multiply(3));
            abilityCooldownManager.setSwordCooldown(player.getUniqueId(), System.currentTimeMillis());
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, SoundCategory.PLAYERS, 100, 0);
        }else{
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + abilityCooldownManager.getCooldownMessage(abilityCooldownManager.getSwordRemainingTime(player))));
        }
    }

    public void handleAxeAbility(Player player, EquipmentSlot equipmentSlot, SwissKnife plugin) {
        if (equipmentSlot.equals(EquipmentSlot.HAND)) {
            player.swingMainHand();
        } else {
            player.swingOffHand();
        }
        long timeLeft = System.currentTimeMillis() - abilityCooldownManager.getPickaxeCooldown(player.getUniqueId());
        if(TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= AbilityCooldownManager.DEFAULT_PICKAXE_COOLDOWN){
            // Handle ability
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 3));
            player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.PLAYERS, 100, 0);
            abilityCooldownManager.setAxeCooldown(player.getUniqueId(), System.currentTimeMillis());
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 50, 1));
                }
            },50);
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, SoundCategory.PLAYERS, 100, 0);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 2));
                }
            },100);
        }else{
            // Is still on cooldown
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + abilityCooldownManager.getCooldownMessage(abilityCooldownManager.getAxeRemainingTime(player))));
        }
    }

    public void handleCrystalAbility(Player player, EquipmentSlot equipmentSlot, SwissKnife plugin) {
        if (equipmentSlot.equals(EquipmentSlot.HAND)) {
            player.swingMainHand();
        } else {
            player.swingOffHand();
        }
        long timeLeft = System.currentTimeMillis() - abilityCooldownManager.getCrystalCooldown(player.getUniqueId());
        if(TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= AbilityCooldownManager.DEFAULT_CRYSTAL_COOLDOWN){
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 6));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 150, 6));
            player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_HURT, SoundCategory.PLAYERS, 100, 0);
            abilityCooldownManager.setCrystalCooldown(player.getUniqueId(), System.currentTimeMillis());
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_DEATH, SoundCategory.PLAYERS, 100, 0);
                }
            },100);
        }else{
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + abilityCooldownManager.getCooldownMessage(abilityCooldownManager.getCrystalRemainingTime(player))));
        }
    }

    public void handlePickaxeAbility(Player player, EquipmentSlot equipmentSlot, SwissKnife plugin){
        if (equipmentSlot.equals(EquipmentSlot.HAND)) {
            player.swingMainHand();
        } else {
            player.swingOffHand();
        }
        if(pickaxeTaskMap.containsKey(player.getUniqueId())){
            pickaxeTaskMap.get(player.getUniqueId()).cancel();
            // Play sound on ability cancel
            abilityCooldownManager.setPickaxeCooldown(player.getUniqueId(), System.currentTimeMillis());
            return;
        }
        long timeLeft = System.currentTimeMillis() - abilityCooldownManager.getPickaxeCooldown(player.getUniqueId());
        if(TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= AbilityCooldownManager.DEFAULT_CRYSTAL_COOLDOWN){

            //player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_HURT, SoundCategory.PLAYERS, 100, 0);
            //abilityCooldownManager.setPickaxeCooldown(player.getUniqueId(), System.currentTimeMillis());

            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    if(player.getExp() < xpToDrain){
                        player.setExp(0);
                        pickaxeTaskMap.remove(player.getUniqueId());
                        // Play sound
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 3));
                        abilityCooldownManager.setPickaxeCooldown(player.getUniqueId(), System.currentTimeMillis());
                        this.cancel();
                    }else{
                        player.setExp(player.getExp() - xpToDrain);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 101,3));
                    }
                }
            }.runTaskTimer(plugin, 0, 100);
            pickaxeTaskMap.put(player.getUniqueId(), task);
        }else{
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + abilityCooldownManager.getCooldownMessage(abilityCooldownManager.getCrystalRemainingTime(player))));
        }
    }

}

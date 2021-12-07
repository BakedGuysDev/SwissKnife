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

package com.egirlsnation.swissknife.systems.handlers.customItems;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.utils.Config;
import com.egirlsnation.swissknife.utils.player.ExpUtil;
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

import java.util.*;
import java.util.concurrent.TimeUnit;

public class CustomItemHandler {

    private final AbilityCooldownHandler abilityCooldownHandler = new AbilityCooldownHandler();

    private static final Map<UUID, BukkitTask> pickaxeTaskMap = new HashMap<>();

    private static final List<UUID> crystalEnabledList = new ArrayList<>();
    private static final List<UUID> disabledPlayersList = new ArrayList<>();

    public List<UUID> getCrystalEnabledList(){
        return crystalEnabledList;
    }

    public List<UUID> getDisabledPlayersList(){
        return disabledPlayersList;
    }

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
        meta.setUnbreakable(true);
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

    public boolean isDraconiteGem(ItemStack item){
        if(item == null) return false;
        if(!item.hasItemMeta()) return false;
        if(!item.getItemMeta().hasLore()) return false;
        if(item.getItemMeta().getLore().contains(ChatColor.DARK_PURPLE+"Draconite Gem")) return true;
        return false;
    }

    public void handleSwordAbility(Player player, EquipmentSlot equipmentSlot) {
        if (equipmentSlot.equals(EquipmentSlot.HAND)) {
            player.swingMainHand();
        } else {
            player.swingOffHand();
        }
        long timeLeft = System.currentTimeMillis() - abilityCooldownHandler.getSwordCooldown(player.getUniqueId());
        if(TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= AbilityCooldownHandler.DEFAULT_SWORD_COOLDOWN){
            DragonFireball fireball = player.getWorld().spawn(player.getEyeLocation(), DragonFireball.class);
            fireball.setShooter(player);
            fireball.setCustomName("CusFireBallSwissKnife");
            fireball.setVelocity(player.getLocation().getDirection().multiply(3));
            abilityCooldownHandler.setSwordCooldown(player.getUniqueId(), System.currentTimeMillis());
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, SoundCategory.PLAYERS, 100, 0);
        }else{
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + abilityCooldownHandler.getCooldownMessage(abilityCooldownHandler.getSwordRemainingTime(player))));
        }
    }

    public void handleAxeAbility(Player player, EquipmentSlot equipmentSlot, SwissKnife plugin) {
        if (equipmentSlot.equals(EquipmentSlot.HAND)) {
            player.swingMainHand();
        } else {
            player.swingOffHand();
        }
        long timeLeft = System.currentTimeMillis() - abilityCooldownHandler.getPickaxeCooldown(player.getUniqueId());
        if(TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= AbilityCooldownHandler.DEFAULT_PICKAXE_COOLDOWN){
            // Handle ability
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 3));
            player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.PLAYERS, 100, 0);
            abilityCooldownHandler.setAxeCooldown(player.getUniqueId(), System.currentTimeMillis());
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
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + abilityCooldownHandler.getCooldownMessage(abilityCooldownHandler.getAxeRemainingTime(player))));
        }
    }

    public void handleCrystalAbility(Player player, EquipmentSlot equipmentSlot, SwissKnife plugin) {
        if (equipmentSlot.equals(EquipmentSlot.HAND)) {
            player.swingMainHand();
        } else {
            player.swingOffHand();
        }
        long timeLeft = System.currentTimeMillis() - abilityCooldownHandler.getCrystalCooldown(player.getUniqueId());

        // Player isnt on cooldown. Run ability
        if(TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= AbilityCooldownHandler.DEFAULT_CRYSTAL_COOLDOWN){
            crystalEnabledList.add(player.getUniqueId());
            player.setGliding(false);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 6));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 150, 6));
            player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_HURT, SoundCategory.PLAYERS, 100, 0);
            abilityCooldownHandler.setCrystalCooldown(player.getUniqueId(), System.currentTimeMillis());
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_DEATH, SoundCategory.PLAYERS, 100, 0);
                    crystalEnabledList.remove(player.getUniqueId());
                }
            },100);
        }else{ // Player is on cooldown. Display cooldown message
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + abilityCooldownHandler.getCooldownMessage(abilityCooldownHandler.getCrystalRemainingTime(player))));
        }
    }

    public void handlePickaxeAbility(Player player, EquipmentSlot equipmentSlot, SwissKnife plugin){
        if (equipmentSlot.equals(EquipmentSlot.HAND)) {
            player.swingMainHand();
        } else {
            player.swingOffHand();
        }
        if(pickaxeTaskMap.containsKey(player.getUniqueId())){ //Cancels the ability when it's enabled
            pickaxeTaskMap.get(player.getUniqueId()).cancel();
            pickaxeTaskMap.remove(player.getUniqueId());
            player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, SoundCategory.PLAYERS, 100, 0);
            abilityCooldownHandler.setPickaxeCooldown(player.getUniqueId(), System.currentTimeMillis());
            return;
        }
        long timeLeft = System.currentTimeMillis() - abilityCooldownHandler.getPickaxeCooldown(player.getUniqueId());
        if(TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= AbilityCooldownHandler.DEFAULT_PICKAXE_COOLDOWN){

            player.playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.PLAYERS, 100, 0);
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {

                    if(ExpUtil.getExp(player) < Config.instance.xpToDrain){
                        player.setExp(0);
                        player.setLevel(0);
                        pickaxeTaskMap.remove(player.getUniqueId());
                        player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, SoundCategory.PLAYERS, 100, 0);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 5));
                        abilityCooldownHandler.setPickaxeCooldown(player.getUniqueId(), System.currentTimeMillis());
                        pickaxeTaskMap.remove(player.getUniqueId());
                        this.cancel();
                    }else{
                        if(isDraconitePickaxe(player.getInventory().getItemInMainHand())){
                            ExpUtil.changeExp(player, - Config.instance.xpToDrain);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20, Config.instance.hasteLevel));
                        }
                    }
                }
            }.runTaskTimer(plugin, 0, 20);
            pickaxeTaskMap.put(player.getUniqueId(), task);
        }else{
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + abilityCooldownHandler.getCooldownMessage(abilityCooldownHandler.getPickaxeRemainingTime(player))));
        }
    }

}

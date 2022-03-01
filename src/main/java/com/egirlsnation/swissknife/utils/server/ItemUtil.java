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

package com.egirlsnation.swissknife.utils.server;

import com.egirlsnation.swissknife.utils.OldConfig;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ItemUtil {

    private static final List<PotionEffectType> nonPotionList = ImmutableList.<PotionEffectType>builder().add(
            PotionEffectType.ABSORPTION,
            PotionEffectType.BAD_OMEN,
            PotionEffectType.BLINDNESS,
            PotionEffectType.CONDUIT_POWER,
            PotionEffectType.CONFUSION,
            PotionEffectType.DOLPHINS_GRACE,
            PotionEffectType.FAST_DIGGING,
            PotionEffectType.GLOWING,
            PotionEffectType.HEALTH_BOOST,
            PotionEffectType.HERO_OF_THE_VILLAGE,
            PotionEffectType.HUNGER,
            PotionEffectType.LEVITATION,
            PotionEffectType.SATURATION,
            PotionEffectType.SLOW_DIGGING,
            PotionEffectType.UNLUCK,
            PotionEffectType.WITHER
    ).build();

    private static final Map<PotionEffectType, Integer> potionMaxAmplifierMap = ImmutableMap.<PotionEffectType, Integer>builder()
            .put(PotionEffectType.DAMAGE_RESISTANCE, 3)
            .put(PotionEffectType.FIRE_RESISTANCE, 0)
            .put(PotionEffectType.HARM, 1)
            .put(PotionEffectType.HEAL, 1)
            .put(PotionEffectType.INCREASE_DAMAGE, 1)
            .put(PotionEffectType.INVISIBILITY, 0)
            .put(PotionEffectType.JUMP, 1)
            .put(PotionEffectType.LUCK, 0)
            .put(PotionEffectType.NIGHT_VISION, 0)
            .put(PotionEffectType.POISON, 1)
            .put(PotionEffectType.REGENERATION, 1)
            .put(PotionEffectType.SLOW, 5)
            .put(PotionEffectType.SLOW_FALLING, 0)
            .put(PotionEffectType.SPEED, 1)
            .put(PotionEffectType.WATER_BREATHING, 0)
            .put(PotionEffectType.WEAKNESS, 0)
            .build();

    public static boolean isArmorPiece(@Nullable ItemStack item){
        if(item == null) return false;
        final String itemTypeString = item.getType().name();
        return itemTypeString.endsWith("_HELMET")
                || itemTypeString.endsWith("_CHESTPLATE")
                || itemTypeString.endsWith("_LEGGINGS")
                || itemTypeString.endsWith("_BOOTS");
    }

    public static boolean isSpawnEgg(@Nullable ItemStack item){
        if(item == null) return false;
        return item.getType().toString().matches("[A-Z]*?_?[A-Z]*_SPAWN_EGG");
    }

    public static boolean isShulkerBox(@Nullable ItemStack item){
        if(item == null) return false;
        final String itemTypeString = item.getType().name();
        return itemTypeString.endsWith("SHULKER_BOX");
    }

    public boolean hasTooLongName(@Nullable ItemStack item){
        if(item == null) return false;
        if(item.getItemMeta() == null) return false;
        ItemMeta meta = item.getItemMeta();
        if(!meta.hasDisplayName()) return false;
        return meta.getDisplayName().length() > OldConfig.instance.maxItemNameLength;
    }

    public ItemMeta trimName(@NotNull ItemStack item){
        if(item.getItemMeta() == null) return null;
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(meta.getDisplayName().substring(0, Math.min(meta.getDisplayName().length(), OldConfig.instance.maxItemNameLength)));
        return meta;
    }

    public static boolean isAncientOrDraconiteWeapon(@Nullable ItemStack item){
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

    public static int getMaxPotionAmplifier(PotionEffectType effectType){
        return potionMaxAmplifierMap.get(effectType);
    }

    public static boolean isLegitPotionEffect(PotionEffectType effectType){
        return !nonPotionList.contains(effectType);
    }

    public static ItemStack getDraconiteCrystal() {
        ItemStack item = new ItemStack(Material.END_CRYSTAL);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList("", "§cDraconite Crystal", "", "Legends say that it's one of those crystals from the top of the End towers"));
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(UUID.randomUUID(), "max_health", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(UUID.randomUUID(), "max_health", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.OFF_HAND));
        meta.setDisplayName(ChatColor.RED + "Draconite Crystal");
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isDraconiteCrystal(ItemStack item) {
        if (item == null) return false;
        if (!item.getType().equals(Material.END_CRYSTAL)) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        if (!meta.hasLore()) return false;
        if (meta.getLore() == null) return false;

        return meta.getLore().contains("§cDraconite Crystal");
    }

    public static ItemStack getPopbobTotem() {
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

    public static boolean isPopbobTotem(ItemStack item) {
        if (item == null) return false;
        if (!item.getType().equals(Material.TOTEM_OF_UNDYING)) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        if (!meta.hasLore()) return false;
        if (meta.getLore() == null) return false;

        return meta.getLore().contains("§cPopbob's Totem");
    }

    public static ItemStack getDraconiteSword() {
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList("", "§cDraconite Weapon", "", "Legends say that this exact sword was used to kill the first dragon"));
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "attack_damage", 11, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        meta.setDisplayName(ChatColor.RED + "Draconite Sword");
        item.setItemMeta(meta);

        return item;
    }

    public static boolean isDraconiteSword(ItemStack item) {
        if (item == null) return false;
        if (!item.getType().equals(Material.NETHERITE_SWORD)) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        if (!meta.hasLore()) return false;
        if (meta.getLore() == null) return false;

        return meta.getLore().contains("§cDraconite Weapon");
    }

    public static ItemStack getDraconiteAxe() {
        ItemStack item = new ItemStack(Material.NETHERITE_AXE);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList("", "§cDraconite Weapon", "", "Legends say that this exact axe was used to decapitate the first dragon"));
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "attack_damage", 13, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        meta.setDisplayName(ChatColor.RED + "Draconite Axe");
        item.setItemMeta(meta);

        return item;
    }

    public static boolean isDraconiteAxe(ItemStack item) {
        if (item == null) return false;
        if (!item.getType().equals(Material.NETHERITE_AXE)) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        if (!meta.hasLore()) return false;
        if (meta.getLore() == null) return false;

        return meta.getLore().contains("§cDraconite Weapon");
    }

    public static ItemStack getDraconitePickaxe(){
        ItemStack item = new ItemStack(Material.NETHERITE_PICKAXE);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList("", "§cDraconite Tool", "", "Legends say that this exact pickaxe was used to mine the first obsidian"));
        meta.setDisplayName(ChatColor.RED + "Draconite Pickaxe");
        meta.setUnbreakable(true);
        item.setItemMeta(meta);

        return item;
    }

    public static boolean isDraconitePickaxe(ItemStack item) {
        if (item == null) return false;
        if (!item.getType().equals(Material.NETHERITE_PICKAXE)) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        if (!meta.hasLore()) return false;
        if (meta.getLore() == null) return false;

        return meta.getLore().contains("§cDraconite Tool");
    }

    public static boolean isDraconiteGem(ItemStack item){
        if(item == null) return false;
        if(!item.hasItemMeta()) return false;
        if(!item.getItemMeta().hasLore()) return false;
        if(item.getItemMeta().getLore().contains(ChatColor.DARK_PURPLE+"Draconite Gem")) return true;
        return false;
    }

    public static boolean isAncientToolOrWeapon(ItemStack item){
        if(item == null) return false;
        if(item.getItemMeta() == null) return false;
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return false;
        if(!meta.hasLore()) return false;
        if(meta.getLore() == null) return false;


        return (meta.getLore().contains("§6Ancient weapon") || meta.getLore().contains("§6Ancient tool"));
    }

    public static boolean hasEnchants(@javax.annotation.Nullable ItemStack item){
        if(item == null) return false;
        if(item.getItemMeta() == null) return false;
        ItemMeta meta = item.getItemMeta();
        return meta.hasEnchants();
    }

    public static boolean isOverEnchanted(@javax.annotation.Nullable ItemStack item, int maxEnchantLevel){
        if(item == null) return false;
        if(item.getItemMeta() == null) return false;
        ItemMeta meta = item.getItemMeta();
        if(!meta.hasEnchants()) return false;

        Map<Enchantment, Integer> enchantMap = meta.getEnchants();
        for(Map.Entry<Enchantment, Integer> enchant: enchantMap.entrySet()){
            if(enchant.getValue() > maxEnchantLevel) return true;
        }
        return false;
    }

    public static ItemStack getReplacementItem(){
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta meta = paper.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "I fucked ya mom");
        paper.setItemMeta(meta);
        return paper;
    }

    public static boolean isBed(@Nullable Block block){
        if(block == null) return false;
        return block.getType().toString().matches("[A-Z]*?_?[A-Z]*_BED");
    }

    public static boolean isBed(@Nullable ItemStack item){
        if(item == null) return false;
        return item.getType().toString().matches("[A-Z]*?_?[A-Z]*_BED");
    }
}

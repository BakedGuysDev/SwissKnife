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

package com.egirlsnation.swissknife.util;

import com.google.common.collect.Multimap;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.UUID;

import static com.egirlsnation.swissknife.SwissKnife.Config.*;
import static org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE;
import static org.bukkit.attribute.Attribute.GENERIC_ATTACK_SPEED;

public class IllegalItemHandler {


    public boolean isOverEnchanted(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return false;
        if(!meta.hasEnchants()) return false;

        if(item.getType().equals(Material.NETHERITE_PICKAXE) && enable1kPicks){
            Map<Enchantment, Integer> enchantMap = meta.getEnchants();
            for(Map.Entry<Enchantment, Integer> enchant: enchantMap.entrySet()){
                if(enchant.getValue() > 1100) return true;
            }
            return false;
        }

        Map<Enchantment, Integer> enchantMap = meta.getEnchants();
        for(Map.Entry<Enchantment, Integer> enchant: enchantMap.entrySet()){
            if(enchant.getValue() > maxEnchantLevel) return true;
        }
        return false;
    }

    public boolean hasIllegalLore(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return false;
        if(!meta.hasLore()) return false;
        if(meta.getLore() == null) return false;

        return meta.getLore().stream().anyMatch(element -> illegalLoreList.contains(element));
    }

    public ItemMeta reduceAncientWeaponMeta(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;
        if(!meta.hasLore()) return null;
        if(meta.getLore() == null) return null;

        if(!meta.getLore().contains("§6Ancient weapon")) return null;

        Multimap<Attribute, AttributeModifier> modifierMap = meta.getAttributeModifiers();
        // In case something weird happens and the ancient weapon has just lore and no modifiers
        if(modifierMap == null) return null;

        meta.removeAttributeModifier(GENERIC_ATTACK_SPEED);
        meta.removeAttributeModifier(GENERIC_ATTACK_DAMAGE);
        meta.addAttributeModifier(GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), "attack_speed",-1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "attack_damage",7, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        //Return the new meta
        return meta;
    }

    public ItemMeta reduceDraconiteAxeMeta(ItemStack item){
        if(!item.getType().equals(Material.NETHERITE_AXE)) return null;
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;
        if(!meta.hasLore()) return null;
        if(meta.getLore() == null) return null;

        if(!meta.getLore().contains("§cDraconite Weapon")) return null;

        Multimap<Attribute, AttributeModifier> modifierMap = meta.getAttributeModifiers();
        // In case something weird happens and the draconite weapon has just lore and no modifiers
        if(modifierMap == null) return null;
        //Removing the Attack speed attribute from the meta
        meta.removeAttributeModifier(GENERIC_ATTACK_SPEED);
        meta.removeAttributeModifier(GENERIC_ATTACK_DAMAGE);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "attack_damage", 9, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        meta.addAttributeModifier(GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), "attack_speed",-2.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        //Return the new meta
        return meta;
    }

    public ItemMeta reduceDraconiteSwordMeta(ItemStack item){
        if(!item.getType().equals(Material.NETHERITE_SWORD)) return null;
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;
        if(!meta.hasLore()) return null;
        if(meta.getLore() == null) return null;

        if(!meta.getLore().contains("§cDraconite Weapon")) return null;

        Multimap<Attribute, AttributeModifier> modifierMap = meta.getAttributeModifiers();
        // In case something weird happens and the draconite weapon has just lore and no modifiers
        if(modifierMap == null) return null;
        //Removing the Attack speed attribute from the meta

        meta.removeAttributeModifier(GENERIC_ATTACK_SPEED);
        meta.removeAttributeModifier(GENERIC_ATTACK_DAMAGE);
        meta.addAttributeModifier(GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), "attack_speed",-1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "attack_damage", 9, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        //Return the new meta
        return meta;
    }

    public boolean isArmorPiece(ItemStack item){
        if(item == null) return false;
        final String itemTypeString = item.getType().name();
        return itemTypeString.endsWith("_HELMET")
                || itemTypeString.endsWith("_CHESTPLATE")
                || itemTypeString.endsWith("_LEGGINGS")
                || itemTypeString.endsWith("_BOOTS");
    }

    public boolean handleIfSpawnEgg(ItemStack item){
        if(item == null) return false;
        if(item.getType().toString().matches("[A-Z]*?_?[A-Z]*_SPAWN_EGG")) return false;
        item.setAmount(0);
        return true;
    }

    public boolean isSpawnEgg(ItemStack item){
        if(item == null) return false;
        return item.getType().toString().matches("[A-Z]*?_?[A-Z]*_SPAWN_EGG");
    }

    public boolean handleIllegals(Item item, Player player){
        if(item == null) return false;

        ItemStack itemStack = item.getItemStack();
        if(hasIllegalLore(itemStack)){
            item.remove();
            player.sendMessage(ChatColor.RED + "Nope m8.\nhttps://youtu.be/otCpCn0l4Wo?t=15");
            return true;
        }

        if(isOverEnchanted(itemStack)){
            item.remove();
            player.sendMessage(ChatColor.RED + "Nope m8.\nhttps://youtu.be/otCpCn0l4Wo?t=15");
            return true;
        }

        if(itemStack.getType().equals(Material.TOTEM_OF_UNDYING) && itemStack.getAmount() > maxTotemStack){
            itemStack.setAmount(maxTotemStack);
            item.setItemStack(itemStack);
            return true;
        }

        if(isArmorPiece(itemStack) && itemStack.getAmount() > maxArmorStack){
            itemStack.setAmount(maxArmorStack);
            item.setItemStack(itemStack);
            return true;
        }

        if(hasTooLongName(item.getItemStack().getItemMeta())){
            itemStack.setItemMeta(trimName(itemStack.getItemMeta()));
            item.setItemStack(itemStack);
        }

        ItemMeta ancientMeta = reduceAncientWeaponMeta(itemStack);
        if(ancientMeta != null){
            itemStack.setItemMeta(ancientMeta);
            item.setItemStack(itemStack);
            return false;
        }

        ItemMeta draconicAxeMeta = reduceDraconiteAxeMeta(itemStack);
        if(draconicAxeMeta != null){
            itemStack.setItemMeta(draconicAxeMeta);
            item.setItemStack(itemStack);
            return false;
        }

        ItemMeta draconicSwordMeta = reduceDraconiteSwordMeta(itemStack);
        if(draconicSwordMeta != null){
            itemStack.setItemMeta(draconicSwordMeta);
            item.setItemStack(itemStack);
            return false;
        }

        return false;
    }

    public boolean handleIllegals(ItemStack item, Player player){
        if(item == null) return false;

        if(hasIllegalLore(item)){
            item.setAmount(0);
            player.sendMessage(ChatColor.RED + "Nope m8.\nhttps://youtu.be/otCpCn0l4Wo?t=15");
            return true;
        }

        if(isOverEnchanted(item)){
            item.setAmount(0);
            player.sendMessage(ChatColor.RED + "Nope m8.\nhttps://youtu.be/otCpCn0l4Wo?t=15");
            return true;
        }

        if(item.getType().equals(Material.TOTEM_OF_UNDYING) && item.getAmount() > maxTotemStack){
            item.setAmount(maxTotemStack);
            return true;
        }

        if(isArmorPiece(item) && item.getAmount() > maxArmorStack){
            item.setAmount(maxArmorStack);
            return true;
        }

        if(hasTooLongName(item.getItemMeta())){
            item.setItemMeta(trimName(item.getItemMeta()));
        }

        ItemMeta ancientMeta = reduceAncientWeaponMeta(item);
        if(ancientMeta != null){
            item.setItemMeta(ancientMeta);
            return false;
        }

        ItemMeta draconicAxeMeta = reduceDraconiteAxeMeta(item);
        if(draconicAxeMeta != null){
            item.setItemMeta(draconicAxeMeta);
            return false;
        }

        ItemMeta draconicSwordMeta = reduceDraconiteSwordMeta(item);
        if(draconicSwordMeta != null){
            item.setItemMeta(draconicSwordMeta);
            return false;
        }

        return false;
    }

    public boolean handleIllegals(ItemStack item){
        if(item == null) return false;

        if(hasIllegalLore(item)){
            item.setAmount(0);
            return true;
        }

        if(isOverEnchanted(item)){
            item.setAmount(0);
            return true;
        }

        if(item.getType().equals(Material.TOTEM_OF_UNDYING) && item.getAmount() > maxTotemStack){
            item.setAmount(maxTotemStack);
            return true;
        }

        if(isArmorPiece(item) && item.getAmount() > maxArmorStack){
            item.setAmount(maxArmorStack);
            return true;
        }

        if(hasTooLongName(item.getItemMeta())){
            item.setItemMeta(trimName(item.getItemMeta()));
        }

        ItemMeta ancientMeta = reduceAncientWeaponMeta(item);
        if(ancientMeta != null){
            item.setItemMeta(ancientMeta);
            return false;
        }

        ItemMeta draconicAxeMeta = reduceDraconiteAxeMeta(item);
        if(draconicAxeMeta != null){
            item.setItemMeta(draconicAxeMeta);
            return false;
        }

        ItemMeta draconicSwordMeta = reduceDraconiteSwordMeta(item);
        if(draconicSwordMeta != null){
            item.setItemMeta(draconicSwordMeta);
            return false;
        }

        return false;
    }

    public ItemStack getIllegalItemReplacement(){
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta meta = paper.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "I fucked ya mom");
        paper.setItemMeta(meta);
        return paper;
    }

    public boolean hasTooLongName(ItemMeta meta){
        if(meta == null) return false;
        if(!meta.hasDisplayName()) return false;
        return meta.getDisplayName().length() > maxItemNameLength;
    }

    public ItemMeta trimName(ItemMeta meta){
        meta.setDisplayName(meta.getDisplayName().substring(0, Math.min(meta.getDisplayName().length(), maxItemNameLength)));
        return meta;
    }


}

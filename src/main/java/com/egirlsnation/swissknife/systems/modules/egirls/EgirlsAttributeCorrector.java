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

package com.egirlsnation.swissknife.systems.modules.egirls;

import com.egirlsnation.swissknife.settings.BoolSetting;
import com.egirlsnation.swissknife.settings.Setting;
import com.egirlsnation.swissknife.settings.SettingGroup;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.systems.modules.Modules;
import com.egirlsnation.swissknife.utils.server.ItemUtil;
import com.egirlsnation.swissknife.utils.server.LocationUtil;
import com.google.common.collect.Multimap;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.UUID;

import static org.bukkit.attribute.Attribute.*;

public class EgirlsAttributeCorrector extends Module {
    public EgirlsAttributeCorrector(){
        super(Categories.EgirlsNation, "egirls-attribute-corrector", "Attribute corrector for Egirls Nation's custom items");
    }

    //TODO: This is shit. Multimaps suck. Improve.

    private final SettingGroup sgAncient = settings.createGroup("ancient-items");

    private final Setting<Boolean> removeAncient = sgAncient.add(new BoolSetting.Builder()
            .name("remove-ancient-attributes")
            .defaultValue(true)
            .build()
    );

    private final SettingGroup sgDraconite = settings.createGroup("draconite-items");

    public final Setting<Boolean> correctDraconite = sgDraconite.add(new BoolSetting.Builder()
            .name("correct-draconite-attributes")
            .defaultValue(true)
            .build()
    );

    private SettingGroup sgGeneral = settings.getDefaultGroup();

    public final Setting<Boolean> log = sgGeneral.add(new BoolSetting.Builder()
            .name("log")
            .defaultValue(true)
            .build()
    );

    @EventHandler
    private void onInventoryOpen(InventoryOpenEvent e){
        if(!isEnabled()) return;
        if(e.getPlayer().isOp()) return;
        scanAndCorrectMetasInInv(e.getInventory());
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent e){
        if(!isEnabled()) return;
        if(e.getWhoClicked().isOp()) return;
        if(e.getClickedInventory() == null) return;
        scanAndCorrectMetasInInv(e.getClickedInventory());

    }

    private boolean scanAndCorrectMetasInInv(Inventory inv){
        boolean foundAncient = false;
        boolean foundDraconite = false;
        for(ItemStack item : inv.getContents()){
            if(removeAncient.get()){
                if(ItemUtil.isAncientToolOrWeapon(item)){
                    ItemMeta newMeta = getReducedAncientMeta(item.getItemMeta());
                    if(newMeta != null){
                        item.setItemMeta(newMeta);
                        foundAncient = true;
                    }
                    continue;
                }
            }

            if(correctDraconite.get()){
                if(ItemUtil.isDraconiteAxe(item)){
                    ItemMeta newMeta = getReducedDraconiteAxeMeta(item.getItemMeta());
                    if(newMeta != null){
                        item.setItemMeta(newMeta);
                        foundDraconite = true;
                    }
                    continue;
                }

                if(ItemUtil.isDraconiteSword(item)){
                    ItemMeta newMeta = getReducedDraconiteSwordMeta(item.getItemMeta());
                    if(newMeta != null){
                        item.setItemMeta(newMeta);
                        foundDraconite = true;
                    }
                    continue;
                }
                if(ItemUtil.isDraconiteCrystal(item)){
                    ItemMeta newMeta = getReducedDraconiteCrystalMeta(item.getItemMeta());
                    if(newMeta != null){
                        item.setItemMeta(newMeta);
                        foundDraconite = true;
                    }
                    continue;
                }
                if(ItemUtil.isPopbobTotem(item)){
                    ItemMeta newMeta = getReducedDraconiteTotemMeta(item.getItemMeta());
                    if(newMeta != null){
                        item.setItemMeta(newMeta);
                        foundDraconite = true;
                    }
                    continue;
                }
            }
        }
        if(log.get()){
            if(foundAncient){
                if(inv.getLocation() != null){
                    info("Removed ancient attributes from item/s in inventory at " + LocationUtil.getLocationString(inv.getLocation()));
                }else{
                    info("Removed ancient attributes from item/s in inventory.");
                }
            }
            if(foundDraconite){
                if(inv.getLocation() != null){
                    info("Removed draconite attributes from item/s in inventory at " + LocationUtil.getLocationString(inv.getLocation()));
                }else{
                    info("Removed draconite attributes from item/s in inventory.");
                }
            }
        }
        return foundDraconite || foundAncient;
    }

    @EventHandler
    private void onPlayerPickup(EntityPickupItemEvent e){
        if(!isEnabled()) return;
        if(e.getEntity().isOp()) return;
        if(removeAncient.get()){
            if(ItemUtil.isAncientToolOrWeapon(e.getItem().getItemStack())){
                ItemMeta newMeta = getReducedAncientMeta(e.getItem().getItemStack().getItemMeta());
                if(newMeta != null) setNewMeta(e.getItem(), newMeta);
                return;
            }
        }

        if(correctDraconite.get()){
            if(ItemUtil.isDraconiteAxe(e.getItem().getItemStack())){
                ItemMeta newMeta = getReducedDraconiteAxeMeta(e.getItem().getItemStack().getItemMeta());
                if(newMeta != null) setNewMeta(e.getItem(), newMeta);
                return;
            }

            if(ItemUtil.isDraconiteSword(e.getItem().getItemStack())){
                ItemMeta newMeta = getReducedDraconiteSwordMeta(e.getItem().getItemStack().getItemMeta());
                if(newMeta != null) setNewMeta(e.getItem(), newMeta);
            }
        }
    }

    private void setNewMeta(Item entityItem, ItemMeta newMeta){
        if(newMeta != null){
            ItemStack item = entityItem.getItemStack();
            item.setItemMeta(newMeta);
            entityItem.setItemStack(item);
        }
    }

    private ItemMeta getReducedAncientMeta(ItemMeta meta){
        if(meta.getAttributeModifiers() == null) return null;
        if(meta.getAttributeModifiers().isEmpty()) return null;

        for(Attribute attribute : meta.getAttributeModifiers().keys()){
            meta.removeAttributeModifier(attribute);
        }

        return meta;
    }

    private ItemMeta getReducedDraconiteAxeMeta(ItemMeta meta){

        Multimap<Attribute, AttributeModifier> modifierMap = meta.getAttributeModifiers();
        // In case something weird happens and the draconite weapon has just lore and no modifiers
        if(modifierMap == null) return null;

        if(modifierMap.size() == 2){
            Collection<AttributeModifier> attackSpeedModifiers = meta.getAttributeModifiers(GENERIC_ATTACK_SPEED);
            Collection<AttributeModifier> attackDamageModifiers = meta.getAttributeModifiers(GENERIC_ATTACK_DAMAGE);
            if(attackSpeedModifiers == null || attackDamageModifiers == null){
                return getCleanDraconiteAxeMeta(meta, modifierMap);
            }
            if(attackSpeedModifiers.size() > 1 || attackDamageModifiers.size() > 1){
                return getCleanDraconiteAxeMeta(meta, modifierMap);
            }else{
                meta.removeAttributeModifier(GENERIC_ATTACK_SPEED);
                meta.removeAttributeModifier(GENERIC_ATTACK_DAMAGE);
                meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "attack_damage", 9, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
                meta.addAttributeModifier(GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), "attack_speed", -2.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
                return meta;
            }
        }else{
            return getCleanDraconiteAxeMeta(meta, modifierMap);
        }
    }

    private ItemMeta getCleanDraconiteAxeMeta(ItemMeta meta, Multimap<Attribute, AttributeModifier> modifierMap){
        for(Attribute attribute : modifierMap.keys()){
            meta.removeAttributeModifier(attribute);
        }
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "attack_damage", 9, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        meta.addAttributeModifier(GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), "attack_speed", -2.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        return meta;
    }

    private ItemMeta getReducedDraconiteSwordMeta(ItemMeta meta){
        Multimap<Attribute, AttributeModifier> modifierMap = meta.getAttributeModifiers();
        // In case something weird happens and the draconite weapon has just lore and no modifiers
        if(modifierMap == null) return null;

        if(modifierMap.size() == 2){
            Collection<AttributeModifier> attackSpeedModifiers = meta.getAttributeModifiers(GENERIC_ATTACK_SPEED);
            Collection<AttributeModifier> attackDamageModifiers = meta.getAttributeModifiers(GENERIC_ATTACK_DAMAGE);
            if(attackSpeedModifiers == null || attackDamageModifiers == null){
                return getCleanDraconiteSwordMeta(meta, modifierMap);
            }
            if(attackSpeedModifiers.size() > 1 || attackDamageModifiers.size() > 1){
                return getCleanDraconiteSwordMeta(meta, modifierMap);
            }else{
                meta.removeAttributeModifier(GENERIC_ATTACK_SPEED);
                meta.removeAttributeModifier(GENERIC_ATTACK_DAMAGE);
                meta.addAttributeModifier(GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), "attack_speed", -2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
                meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "attack_damage", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
                return meta;
            }
        }else{
            return getCleanDraconiteSwordMeta(meta, modifierMap);
        }
    }

    private ItemMeta getCleanDraconiteSwordMeta(ItemMeta meta, Multimap<Attribute, AttributeModifier> modifierMap){
        for(Attribute attribute : modifierMap.keys()){
            meta.removeAttributeModifier(attribute);
        }
        meta.addAttributeModifier(GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), "attack_speed", -2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "attack_damage", 5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        return meta;
    }

    private ItemMeta getReducedDraconiteTotemMeta(ItemMeta meta){
        Multimap<Attribute, AttributeModifier> modifierMap = meta.getAttributeModifiers();
        if(modifierMap == null) return null;

        if(modifierMap.size() == 2){
            Multimap<Attribute, AttributeModifier> offHandAttributes = meta.getAttributeModifiers(EquipmentSlot.OFF_HAND);
            Multimap<Attribute, AttributeModifier> mainHandAttributes = meta.getAttributeModifiers(EquipmentSlot.HAND);
            if(offHandAttributes == null || mainHandAttributes == null){
                return getCleanDraconiteTotemMeta(meta, modifierMap);
            }
            if(!offHandAttributes.containsKey(GENERIC_MAX_HEALTH) || !mainHandAttributes.containsKey(GENERIC_MAX_HEALTH)){
                return getCleanDraconiteTotemMeta(meta, modifierMap);
            }
            if(offHandAttributes.keys().size() > 1 || mainHandAttributes.keys().size() > 1){
                return getCleanDraconiteTotemMeta(meta, modifierMap);
            }else{
                meta.removeAttributeModifier(GENERIC_MAX_HEALTH);
                meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(UUID.randomUUID(), "max_health", Modules.get().get(DraconiteItems.class).additionalTotemHp.get(), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
                meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(UUID.randomUUID(), "max_health", Modules.get().get(DraconiteItems.class).additionalTotemHp.get(), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.OFF_HAND));
                return meta;
            }
        }else{
            return getCleanDraconiteTotemMeta(meta, modifierMap);
        }
    }

    private ItemMeta getCleanDraconiteTotemMeta(ItemMeta meta, Multimap<Attribute, AttributeModifier> modifierMap){
        for(Attribute attribute : modifierMap.keys()){
            meta.removeAttributeModifier(attribute);
        }
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(UUID.randomUUID(), "max_health", Modules.get().get(DraconiteItems.class).additionalTotemHp.get(), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(UUID.randomUUID(), "max_health", Modules.get().get(DraconiteItems.class).additionalTotemHp.get(), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.OFF_HAND));
        return meta;
    }

    private ItemMeta getReducedDraconiteCrystalMeta(ItemMeta meta){
        Multimap<Attribute, AttributeModifier> modifierMap = meta.getAttributeModifiers();
        if(modifierMap == null) return null;
        if(modifierMap.isEmpty()) return null;
        for(Attribute attribute : modifierMap.keys()){
            meta.removeAttributeModifier(attribute);
        }
        meta.setAttributeModifiers(ItemUtil.getDraconiteCrystal(Modules.get().get(DraconiteItems.class).additionalCrystalHp.get()).getItemMeta().getAttributeModifiers());
        return meta;
    }
}

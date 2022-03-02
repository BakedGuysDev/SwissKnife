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

package com.egirlsnation.swissknife.systems.modules.illegals;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.egirlsnation.swissknife.settings.*;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.server.ItemUtil;
import com.egirlsnation.swissknife.utils.server.LocationUtil;
import com.google.common.collect.Multimap;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class IllegalArmorAttributes extends Module {

    public IllegalArmorAttributes(){
        super(Categories.Illegals, "illegal-armor-attributes", "Resets attributes on armor pieces with illegal " + "attributes.");
    }

    // General settings

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> bypass = sgGeneral.add(new BoolSetting.Builder()
            .name("bypass")
            .description("If the check can be bypassed by permissions.")
            .defaultValue(false)
            .build()
    );

    private final Setting<Boolean> alertPlayers = sgGeneral.add(new BoolSetting.Builder()
            .name("alert-players")
            .description("If the plugin should alert players when it finds illegal armor attributes")
            .defaultValue(false)
            .build()
    );

    private final Setting<String> message = sgGeneral.add(new StringSetting.Builder()
            .name("message")
            .description("The message to send (supports color codes)")
            .defaultValue(ChatColor.RED + "Illegal attributes found. This incident will be reported")
            .build()
    );

    // Armor attribute settings

    private final SettingGroup sgArmorAttributes = settings.createGroup("custom-attributes");

    private final Setting<Boolean> customAttributes = sgArmorAttributes.add(new BoolSetting.Builder()
            .name("enable")
            .description("If enabled uses values bellow instead of vanilla.")
            .defaultValue(false)
            .build()
    );

    private final Setting<Integer> armor = sgArmorAttributes.add(new IntSetting.Builder()
            .name("armor")
            .description("Defines level of armor.")
            .defaultValue(8)
            .build()
    );

    private final Setting<Integer> armorToughness = sgArmorAttributes.add(new IntSetting.Builder()
            .name("armor-toughness")
            .description("Defines toughness level of armor.")
            .defaultValue(8)
            .build()
    );

    private final Setting<Integer> knockbackResistance = sgArmorAttributes.add(new IntSetting.Builder()
            .name("knockback-resistance")
            .description("Defines knockback resistance of armor.")
            .defaultValue(8)
            .build()
    );

    private final Setting<Integer> maxHealth = sgArmorAttributes.add(new IntSetting.Builder()
            .name("max-health")
            .description("Defines max-health added to player with armor.")
            .defaultValue(8)
            .build()
    );

    private final Setting<Boolean> log = sgGeneral.add(new BoolSetting.Builder()
            .name("logging")
            .description("If the plugin should log when player tries to place an illegal block")
            .defaultValue(false)
            .build()
    );


    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    private void onInventoyClick(InventoryClickEvent e){
        if(!isEnabled()) return;
        if(e.getWhoClicked().hasPermission("swissknife.bypass.illegals") && bypass.get()) return;

        // Return if player clicks outside
        if(e.getClickedInventory() == null) return;

        boolean found = false;

        for(ItemStack item : e.getClickedInventory().getContents()){
            if(!ItemUtil.isArmorPiece(item)) continue;
            if(isArmorWithIllegalAttributes(item)){
                removeArmorAttributes(item);
                if(customAttributes.get()){
                    addCustomArmorAttributes(item);
                }
                found = true;
            }
        }
        if(found){
            if(e.getWhoClicked() instanceof Player){
                Player player = (Player) e.getWhoClicked();
                if(alertPlayers.get()) sendMessage(player, message.get());
                if(log.get())
                    info("Illegal armor attributes found in an inventory clicked by " + player.getName() + " at: " + LocationUtil.getLocationString(e.getWhoClicked().getLocation()));
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    private void onPickupItem(EntityPickupItemEvent e){
        if(!isEnabled()) return;
        if(!(e.getEntity() instanceof Player)) return;

        if(e.getEntity().hasPermission("swissknife.bypass.illegals") && bypass.get()) return;

        ItemStack picked = e.getItem().getItemStack();

        // Return if item is not armor piece
        if(!ItemUtil.isArmorPiece(picked)) return;

        if(isArmorWithIllegalAttributes(picked)){
            removeArmorAttributes(picked);
            if(customAttributes.get()){
                addCustomArmorAttributes(picked);
            }
            if(e.getEntity() instanceof Player){
                Player player = (Player) e.getEntity();
                if(alertPlayers.get()) sendMessage(player, message.get());
                if(log.get())
                    info("Illegal armor attributes found in an inventory clicked by " + player.getName() + " at: " + LocationUtil.getLocationString(e.getEntity().getLocation()));
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    private void onArmorChange(PlayerArmorChangeEvent e){
        info("Event fired");
        if(!isEnabled()) return;
        if(e.getPlayer().hasPermission("swissknife.bypass.illegals") && bypass.get()) return;

        ItemStack newItem = e.getNewItem();

        // Return if item is not armor piece
        if(!ItemUtil.isArmorPiece(newItem)) return;

        if(isArmorWithIllegalAttributes(newItem)){
            ItemStack item = newItem.clone();
            removeArmorAttributes(item);
            if(customAttributes.get()){
                addCustomArmorAttributes(item);
            }
            Player player = e.getPlayer();
            switch(e.getSlotType()){
                case FEET:{
                    player.getInventory().setBoots(item);
                    break;
                }
                case LEGS:{
                    player.getInventory().setLeggings(item);
                    break;
                }
                case CHEST:{
                    player.getInventory().setChestplate(item);
                    break;
                }
                case HEAD:{
                    player.getInventory().setHelmet(item);
                    break;
                }
            }
            if(alertPlayers.get()) sendMessage(player, message.get());
            if(log.get())
                info("Illegal armor attributes found in an inventory clicked by " + player.getName() + " at: " + LocationUtil.getLocationString(player.getLocation()));
        }
    }

    private boolean isArmorWithIllegalAttributes(ItemStack item){
        if(item == null) return false;
        if(!item.hasItemMeta()) return false;

        Multimap<Attribute, AttributeModifier> attributeMultiMap = item.getItemMeta().getAttributeModifiers();

        if(attributeMultiMap == null) return false;
        if(attributeMultiMap.isEmpty()) return false;

        AtomicBoolean illegallyAttributed = new AtomicBoolean(false);
        if(customAttributes.get()){
            Collection<AttributeModifier> armorAttribute = attributeMultiMap.get(Attribute.GENERIC_ARMOR);
            Collection<AttributeModifier> armorToughnessAttribute = attributeMultiMap.get(Attribute.GENERIC_ARMOR_TOUGHNESS);
            Collection<AttributeModifier> knockbackResistanceAttribute = attributeMultiMap.get(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
            Collection<AttributeModifier> maxHealthAttribute = attributeMultiMap.get(Attribute.GENERIC_MAX_HEALTH);

            armorAttribute.forEach(attributeModifier -> {
                if(attributeModifier.getAmount() > armor.get()) illegallyAttributed.set(true);
            });
            armorToughnessAttribute.forEach(attributeModifier -> {
                if(attributeModifier.getAmount() > armorToughness.get()) illegallyAttributed.set(true);
            });
            knockbackResistanceAttribute.forEach(attributeModifier -> {
                if(attributeModifier.getAmount() > knockbackResistance.get()) illegallyAttributed.set(true);
            });
            maxHealthAttribute.forEach(attributeModifier -> {
                if(attributeModifier.getAmount() > maxHealth.get()) illegallyAttributed.set(true);
            });

            return illegallyAttributed.get();
        }

        return true;
    }

    private void removeArmorAttributes(ItemStack item){
        ItemMeta itemMeta = item.getItemMeta();

        List<Attribute> attributes = getItemAttributes(item);
        for(Attribute attribute : attributes){
            itemMeta.removeAttributeModifier(attribute);
        }

        item.setItemMeta(itemMeta);
    }

    public List<Attribute> getItemAttributes(ItemStack item){
        if(!item.hasItemMeta()) return null;

        List<Attribute> attributes = new ArrayList<>(1);
        Multimap<Attribute, AttributeModifier> attributeMultiMap = item.getItemMeta().getAttributeModifiers();

        if(attributeMultiMap == null) return null;
        if(attributeMultiMap.isEmpty()) return null;

        attributes.addAll(attributeMultiMap.keys());

        return attributes;
    }

    private void addCustomArmorAttributes(ItemStack item){
        ItemMeta itemMeta = item.getItemMeta();

        AttributeModifier armorAttributeModifier = new AttributeModifier(UUID.randomUUID(), Attribute.GENERIC_ARMOR.name(), armor.get(),
                AttributeModifier.Operation.ADD_NUMBER);
        itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, armorAttributeModifier);

        AttributeModifier armorToughnessModifier = new AttributeModifier(UUID.randomUUID(), Attribute.GENERIC_ARMOR_TOUGHNESS.name(), armorToughness.get(),
                AttributeModifier.Operation.ADD_NUMBER);
        itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, armorToughnessModifier);

        AttributeModifier knockbackModifier = new AttributeModifier(UUID.randomUUID(), Attribute.GENERIC_KNOCKBACK_RESISTANCE.name(),
                knockbackResistance.get(), AttributeModifier.Operation.ADD_NUMBER);
        itemMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, knockbackModifier);

        AttributeModifier maxHealthModifier = new AttributeModifier(UUID.randomUUID(), Attribute.GENERIC_MAX_HEALTH.name(), maxHealth.get(),
                AttributeModifier.Operation.ADD_NUMBER);
        itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, maxHealthModifier);

        item.setItemMeta(itemMeta);
    }
}
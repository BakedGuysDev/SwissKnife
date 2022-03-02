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
import com.google.common.collect.Multimap;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class IllegalArmorAttributes extends Module {

    public IllegalArmorAttributes(){
        super(Categories.Illegals, "illegal-armor-attributes", "Resets attributes on armor pieces with illegal " + "attributes.");
    }

    // General settings

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> bypass = sgGeneral.add(new BoolSetting.Builder().name("bypass")
            .description("If the check can be bypassed by permissions.")
            .defaultValue(false)
            .build());

    private final Setting<String> message = sgGeneral.add(new StringSetting.Builder().name("message")
            .description("Message sent to player who bypasses the patch.")
            .defaultValue("You just did a no-no and got free leather armor!")
            .build());

    // Armor attribute settings

    private final SettingGroup sgArmorAttributes = settings.createGroup("armor-attributes");

    private final Setting<Boolean> customAttributes = sgArmorAttributes.add(new BoolSetting.Builder().name("custom-attributes")
            .description("Allows custom attributes on max-enchanted netherite armor.")
            .defaultValue(false)
            .build());

    private final Setting<Integer> armor = sgArmorAttributes.add(new IntSetting.Builder().name("armor")
            .description("Defines level of armor.")
            .defaultValue(8)
            .build());

    private final Setting<Integer> armorToughness = sgArmorAttributes.add(new IntSetting.Builder().name("armor-toughness")
            .description("Defines toughness level of armor.")
            .defaultValue(8)
            .build());

    private final Setting<Integer> knockbackResistance = sgArmorAttributes.add(new IntSetting.Builder().name("knockback-resistance")
            .description("Defines knockback resistance of armor.")
            .defaultValue(8)
            .build());

    private final Setting<Integer> maxHealth = sgArmorAttributes.add(new IntSetting.Builder().name("max-health")
            .description("Defines max-health added to player with armor.")
            .defaultValue(8)
            .build());

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    private void onInventoyClick(InventoryClickEvent e){
        if(e.getWhoClicked()
                .hasPermission("swissknife.bypass.illegals") && bypass.get()) return;

        // Return if player clicks outside
        if(e.getClickedInventory() == null) return;

        // Run at placing an item
        if(e.getClickedInventory()
                .getType() == InventoryType.PLAYER && e.getClick() == ClickType.SHIFT_LEFT){
            ItemStack shifted = e.getCurrentItem();

            // Return if item is not armor piece
            if(!isArmorPiece(shifted)) return;

            if(isArmorWithIllegalAttributes(shifted)){
                if(customAttributes.get()){
                    removeArmorAttributes(shifted);
                    addCustomArmorAttributes(shifted);
                }else{
                    removeArmorAttributes(shifted);
                }
            }
        }else if(e.getClickedInventory()
                .getType() == InventoryType.PLAYER && (e.getClick() == ClickType.LEFT || e.getClick() == ClickType.RIGHT)){

            ItemStack cursor = e.getWhoClicked()
                    .getItemOnCursor();
            ItemStack placed = e.getCurrentItem();
            boolean slotEmpty = placed.getType() == Material.AIR;
            boolean cursorEmpty = cursor.getType() == Material.AIR;

            if(slotEmpty && !cursorEmpty){
                // Return if item is not armor piece
                if(!isArmorPiece(cursor)) return;

                if(isArmorWithIllegalAttributes(cursor)){
                    if(customAttributes.get()){
                        removeArmorAttributes(cursor);
                        addCustomArmorAttributes(cursor);
                    }else{
                        removeArmorAttributes(cursor);
                    }
                }
            }else if(!slotEmpty && !cursorEmpty){
                // Return if item is not armor piece
                if(!isArmorPiece(placed)) return;

                if(isArmorWithIllegalAttributes(placed)){
                    if(customAttributes.get()){
                        removeArmorAttributes(placed);
                        addCustomArmorAttributes(placed);
                    }else{
                        removeArmorAttributes(placed);
                    }
                }
            }else if(!slotEmpty && !cursorEmpty){
                // Return if item is not armor piece
                if(!isArmorPiece(cursor) && !isArmorPiece(placed)) return;

                if(customAttributes.get()){
                    if(isArmorPiece(placed)){
                        if(isArmorWithIllegalAttributes(placed)){
                            removeArmorAttributes(placed);
                            addCustomArmorAttributes(placed);
                        }
                    }

                    if(isArmorPiece(cursor)){
                        if(isArmorWithIllegalAttributes(cursor)){
                            removeArmorAttributes(cursor);
                            addCustomArmorAttributes(cursor);
                        }
                    }
                }else{
                    if(isArmorPiece(placed)){
                        if(isArmorWithIllegalAttributes(placed)){
                            removeArmorAttributes(placed);
                        }
                    }

                    if(isArmorPiece(cursor)){
                        if(isArmorWithIllegalAttributes(cursor)){
                            removeArmorAttributes(cursor);
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    private void onPickupItem(EntityPickupItemEvent e){
        if(!(e.getEntity() instanceof Player)) return;

        if(e.getEntity()
                .hasPermission("swissknife.bypass.illegals") && bypass.get()) return;

        ItemStack picked = e.getItem()
                .getItemStack();

        // Return if item is not armor piece
        if(!isArmorPiece(picked)) return;

        if(isArmorWithIllegalAttributes(picked)){
            removeArmorAttributes(picked);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    private void onArmorChange(PlayerArmorChangeEvent e){
        if(e.getPlayer()
                .hasPermission("swissknife.bypass.illegals") && bypass.get()) return;

        ItemStack newItem = e.getNewItem();

        // Return if item is not armor piece
        if(!isArmorPiece(newItem)) return;

        if(isArmorWithIllegalAttributes(newItem)){
            ItemStack leatherArmorStack[] = {new ItemStack(Material.LEATHER_BOOTS), new ItemStack(Material.LEATHER_LEGGINGS),
                    new ItemStack(Material.LEATHER_CHESTPLATE), new ItemStack(Material.LEATHER_HELMET)};
            e.getPlayer()
                    .getInventory()
                    .setArmorContents(leatherArmorStack);

            e.getPlayer()
                    .sendMessage(ChatColor.RED + message.get());
        }
    }

    public boolean isArmorPiece(ItemStack item){
        if(item == null) return false;
        final String itemTypeString = item.getType()
                .name();

        return itemTypeString.endsWith("_HELMET") || itemTypeString.endsWith("_CHESTPLATE") || itemTypeString.endsWith("_LEGGINGS") || itemTypeString.endsWith("_BOOTS");
    }

    private boolean isArmorWithIllegalAttributes(ItemStack item){
        if(!item.hasItemMeta()) return false;

        Multimap<Attribute, AttributeModifier> attributeMultiMap = item.getItemMeta()
                .getAttributeModifiers();

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
            info("attempt remove: " + attribute.name());
            info("removedAttr: " + itemMeta.removeAttributeModifier(attribute));
        }

        item.setItemMeta(itemMeta);
    }

    public List<Attribute> getItemAttributes(ItemStack item){
        if(!item.hasItemMeta()) return null;

        List<Attribute> attributes = new ArrayList<>(1);
        Multimap<Attribute, AttributeModifier> attributeMultiMap = item.getItemMeta()
                .getAttributeModifiers();

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
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

import com.egirlsnation.swissknife.settings.BoolSetting;
import com.egirlsnation.swissknife.settings.Setting;
import com.egirlsnation.swissknife.settings.SettingGroup;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.systems.modules.Modules;
import com.egirlsnation.swissknife.systems.modules.egirls.EgirlsAttributeCorrector;
import com.egirlsnation.swissknife.utils.server.ItemUtil;
import com.google.common.collect.Multimap;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class IllegalAttributes extends Module {

    public IllegalAttributes(){
        super(Categories.Illegals, "illegal-attributes", "Removes illegal attributes found on items");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> bypass = sgGeneral.add(new BoolSetting.Builder()
            .name("bypass")
            .description("If the check can be bypassed by permissions")
            .defaultValue(false)
            .build()
    );

    private final Setting<Boolean> removeSlotAttributes = sgGeneral.add(new BoolSetting.Builder()
            .name("remove-slot-attributes")
            .description("Removes slot based attributes")
            .defaultValue(true)
            .build()
    );

    private final SettingGroup sgElytra = settings.createGroup("elytra");

    private final Setting<Boolean> elyKeepUnbreakable = sgElytra.add(new BoolSetting.Builder()
            .name("ignore-unbreakable")
            .description("Makes the plugin ignore the unbreakable tag on an elytra")
            .defaultValue(true)
            .build()
    );



    //TODO: logging, player alerts, testing

    @EventHandler
    private void inventoryClick(InventoryClickEvent e){
        if(!isEnabled()) return;
        if(e.getClickedInventory() == null) return;

        for(ItemStack item : e.getClickedInventory()){
            if(item == null) continue;
            if(item.getItemMeta() != null && item.getItemMeta().hasAttributeModifiers()){
                if(hasIllegalAttributes(item)){
                    ItemUtil.removeAttributes(item);
                }
                if(removeSlotAttributes.get()){
                    removeSlotAttributes(item);
                }
                if(item.getItemMeta().isUnbreakable()){
                    if(elyKeepUnbreakable.get() && item.getType().equals(Material.ELYTRA)){
                        continue;
                    }
                    ItemUtil.removeUnbreakableTag(item);
                }
            }
        }


    }

    @EventHandler
    private void inventoryOpen(InventoryOpenEvent e){
        if(!isEnabled()) return;
        if(e.getPlayer().hasPermission("swissknife.bypass.illegals") && bypass.get()){
            return;
        }
        for(ItemStack item : e.getInventory()){
            if(item == null) continue;
            if(item.getItemMeta() != null && item.getItemMeta().hasAttributeModifiers()){
                if(hasIllegalAttributes(item)){
                    ItemUtil.removeAttributes(item);
                }
                if(removeSlotAttributes.get()){
                    removeSlotAttributes(item);
                }
                if(item.getItemMeta().isUnbreakable()){
                    if(elyKeepUnbreakable.get() && item.getType().equals(Material.ELYTRA)){
                        continue;
                    }
                    ItemUtil.removeUnbreakableTag(item);
                }
            }
        }
    }

    @EventHandler
    private void entityPickupItem(EntityPickupItemEvent e){
        if(!isEnabled()) return;
        if(!(e.getEntity() instanceof Player)) return;

        if(e.getEntity().hasPermission("swissknife.bypass.illegals") && bypass.get()){
            return;
        }

        ItemStack item = e.getItem().getItemStack();

        if(item.hasItemMeta() && item.getItemMeta() != null && item.getItemMeta().hasAttributeModifiers()){
            if(hasIllegalAttributes(item)){
                ItemUtil.removeAttributes(item);
            }
            if(removeSlotAttributes.get()){
                removeSlotAttributes(item);
            }
            if(item.getItemMeta().isUnbreakable()){
                if(elyKeepUnbreakable.get() && !item.getType().equals(Material.ELYTRA)){
                    ItemUtil.removeUnbreakableTag(item);
                }
            }
        }
    }

    public List<Attribute> getSlotAttributes(ItemStack item){
        List<Attribute> attributes = new ArrayList<>(1);
        for(EquipmentSlot slot : EquipmentSlot.values()){
            Multimap<Attribute, AttributeModifier> attributeMultiMap = item.getItemMeta().getAttributeModifiers(slot);
            if(!attributeMultiMap.isEmpty()){
                attributes.addAll(attributeMultiMap.keys());
            }
        }

        return attributes;
    }

    private void removeSlotAttributes(ItemStack item){
        List<Attribute> slotAttributes = getSlotAttributes(item);
        for(Attribute attribute : slotAttributes){
            item.getItemMeta().removeAttributeModifier(attribute);
        }
    }

    private boolean hasIllegalAttributes(ItemStack item){
        ItemStack clone = item.clone();

        if(Modules.get().get(EgirlsAttributeCorrector.class).isEnabled() && ItemUtil.isDraconiteItem(item)) return false;

        if(clone.hasItemMeta() && item.hasItemMeta()){
            return item.getItemMeta().hasAttributeModifiers() && !clone.getItemMeta().hasAttributeModifiers();
        }
        return false;
    }

}

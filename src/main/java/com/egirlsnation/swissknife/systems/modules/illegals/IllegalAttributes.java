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
import com.egirlsnation.swissknife.settings.StringSetting;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.systems.modules.Modules;
import com.egirlsnation.swissknife.systems.modules.egirls.DraconiteItems;
import com.egirlsnation.swissknife.systems.modules.egirls.EgirlsAttributeCorrector;
import com.egirlsnation.swissknife.utils.entity.player.SwissPlayer;
import com.egirlsnation.swissknife.utils.server.ItemUtil;
import com.egirlsnation.swissknife.utils.server.LocationUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

    private final Setting<Boolean> removeItemFlags = sgGeneral.add(new BoolSetting.Builder()
            .name("remove-item-flags")
            .description("Removes item flags")
            .defaultValue(true)
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

    private final Setting<Boolean> log = sgGeneral.add(new BoolSetting.Builder()
            .name("logging")
            .description("If the plugin should log when player tries to place an illegal block")
            .defaultValue(false)
            .build()
    );



    private final SettingGroup sgElytra = settings.createGroup("elytra");

    private final Setting<Boolean> elyKeepUnbreakable = sgElytra.add(new BoolSetting.Builder()
            .name("ignore-unbreakable")
            .description("Makes the plugin ignore the unbreakable tag on an elytra")
            .defaultValue(true)
            .build()
    );



    //TODO: logging, player alerts

    @EventHandler
    private void inventoryClick(InventoryClickEvent e){
        if(!isEnabled()) return;
        if(e.getClickedInventory() == null) return;
        if(e.getWhoClicked().hasPermission("swissknife.bypass.illegals") && bypass.get()){
            return;
        }
        boolean changed = false;

        for(ItemStack item : e.getClickedInventory()){
            if(item == null) continue;
            List<String> changes = new ArrayList<>(0);
            if(removeItemFlags.get()){
                for(ItemFlag flag : ItemFlag.values()){
                    if(item.hasItemFlag(flag)){
                        item.removeItemFlags(flag);
                        changed = true;
                        changes.add("item flags");
                    }
                }
            }
            if(item.getItemMeta() != null){
                if(item.getItemMeta().hasAttributeModifiers()){
                    if(hasIllegalAttributes(item)){
                        ItemUtil.removeAttributes(item);
                        changed = true;
                        changes.add("attributes");
                    }
                    if(removeSlotAttributes.get()){
                        changed = removeSlotAttributes(item);
                        if(changed) changes.add("slot attributes");
                    }
                }
                if(item.getItemMeta().isUnbreakable()){
                    if(elyKeepUnbreakable.get() && item.getType().equals(Material.ELYTRA)){
                        continue;
                    }
                    ItemUtil.removeUnbreakableTag(item);
                    changed = true;
                    changes.add("unbreakable");
                }
            }
            if(log.get() && !changes.isEmpty()){
                String logMsg = "Removed " + StringUtils.join(changes, ", ") + " from " + item.getType();
                if(e.getClickedInventory().getLocation() != null){
                    logMsg += " found in inventory at " + LocationUtil.getLocationString(e.getClickedInventory().getLocation());
                }else{
                    logMsg += " found on player at " + LocationUtil.getLocationString(e.getWhoClicked().getLocation());
                }
                info(logMsg);
            }
        }
        if(e.getWhoClicked() instanceof Player){
            if(changed && alertPlayers.get() && SwissPlayer.getSwissPlayer((Player) e.getWhoClicked()).hasFeatureEnabled(SwissPlayer.SwissFeature.MODULE_ALERTS)){
                sendMessage((Player) e.getWhoClicked(), message.get());
            }
        }
    }

    @EventHandler
    private void inventoryOpen(InventoryOpenEvent e){
        if(!isEnabled()) return;
        if(e.getPlayer().hasPermission("swissknife.bypass.illegals") && bypass.get()){
            return;
        }
        boolean changed = false;

        for(ItemStack item : e.getInventory()){
            if(item == null) continue;
            List<String> changes = new ArrayList<>(0);
            if(removeItemFlags.get()){
                for(ItemFlag flag : ItemFlag.values()){
                    if(item.hasItemFlag(flag)){
                        item.removeItemFlags(flag);
                        changed = true;
                        changes.add("item flags");
                    }
                }
            }
            if(item.getItemMeta() != null){
                if(item.getItemMeta().hasAttributeModifiers()){
                    if(hasIllegalAttributes(item)){
                        ItemUtil.removeAttributes(item);
                        changed = true;
                        changes.add("attributes");
                    }
                    if(removeSlotAttributes.get()){
                        changed = removeSlotAttributes(item);
                        if(changed) changes.add("slot attributes");
                    }
                }
                if(item.getItemMeta().isUnbreakable()){
                    if(elyKeepUnbreakable.get() && item.getType().equals(Material.ELYTRA)){
                        continue;
                    }
                    ItemUtil.removeUnbreakableTag(item);
                    changed = true;
                    changes.add("unbreakable");
                }
            }
            if(log.get() && !changes.isEmpty()){
                String logMsg = "Removed " + StringUtils.join(changes, ", ") + " from " + item.getType();
                if(e.getInventory().getLocation() != null){
                    logMsg += " found in inventory at " + LocationUtil.getLocationString(e.getInventory().getLocation());
                }else{
                    logMsg += " found on player at " + LocationUtil.getLocationString(e.getPlayer().getLocation());
                }
                info(logMsg);
            }
        }
        if(e.getPlayer() instanceof Player){
            if(changed && alertPlayers.get() && SwissPlayer.getSwissPlayer((Player) e.getPlayer()).hasFeatureEnabled(SwissPlayer.SwissFeature.MODULE_ALERTS)){
                sendMessage((Player) e.getPlayer(), message.get());
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
        boolean changed = false;

        ItemStack item = e.getItem().getItemStack();

        List<String> changes = new ArrayList<>(0);
        if(removeItemFlags.get()){
            for(ItemFlag flag : ItemFlag.values()){
                if(item.hasItemFlag(flag)){
                    item.removeItemFlags(flag);
                    changed = true;
                    changes.add("item flags");
                }
            }
        }
        if(item.getItemMeta() != null){
            if(item.getItemMeta().hasAttributeModifiers()){
                if(hasIllegalAttributes(item)){
                    ItemUtil.removeAttributes(item);
                    changed = true;
                    changes.add("attributes");
                }
                if(removeSlotAttributes.get()){
                    changed = removeSlotAttributes(item);
                    if(changed) changes.add("slot attributes");
                }
            }
            if(item.getItemMeta().isUnbreakable()){
                if(!(elyKeepUnbreakable.get() && item.getType().equals(Material.ELYTRA))){
                    ItemUtil.removeUnbreakableTag(item);
                    changed = true;
                    changes.add("unbreakable");
                }
            }
        }

        if(log.get() && !changes.isEmpty()){
            info("Removed " + StringUtils.join(changes, ", ") + " from " + item.getType() + " picked up by " + e.getEntity().getName() + " at " + LocationUtil.getLocationString(e.getEntity().getLocation()));
        }
        if(e.getEntity() instanceof Player){
            if(changed && alertPlayers.get() && SwissPlayer.getSwissPlayer((Player) e.getEntity()).hasFeatureEnabled(SwissPlayer.SwissFeature.MODULE_ALERTS)){
                sendMessage((Player) e.getEntity(), message.get());
            }
        }
    }

    public List<Attribute> getSlotAttributes(ItemStack item){
        List<Attribute> attributes = new ArrayList<>(1);
        Multimap<Attribute, AttributeModifier> attributeMultiMap = ArrayListMultimap.create();
        for(EquipmentSlot slot : EquipmentSlot.values()){
             attributeMultiMap.putAll(item.getItemMeta().getAttributeModifiers(slot));
            if(!attributeMultiMap.isEmpty()){
                attributes.addAll(attributeMultiMap.keys());
            }
        }

        return attributes;
    }

    private boolean removeSlotAttributes(ItemStack item){
        List<Attribute> slotAttributes = getSlotAttributes(item);
        ItemMeta meta = item.getItemMeta();
        if(ItemUtil.isPopbobTotem(item) && Modules.get().isActive(DraconiteItems.class)){
            meta = ItemUtil.getPopbobTotem(Modules.get().get(DraconiteItems.class).additionalTotemHp.get()).getItemMeta();
            meta.setDisplayName(item.getItemMeta().getDisplayName());
            item.setItemMeta(meta);
            return false;
        }
        if(ItemUtil.isDraconiteCrystal(item) && Modules.get().isActive(DraconiteItems.class)){
            meta = ItemUtil.getDraconiteCrystal(Modules.get().get(DraconiteItems.class).additionalCrystalHp.get()).getItemMeta();
            meta.setDisplayName(item.getItemMeta().getDisplayName());
            item.setItemMeta(meta);
            return false;
        }
        boolean removed = false;
        for(Attribute attribute : slotAttributes){
            meta.removeAttributeModifier(attribute);
            removed = true;
        }
        item.setItemMeta(meta);
        return removed;
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

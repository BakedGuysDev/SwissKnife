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

package com.egirlsnation.swissknife.systems.modules.misc;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.settings.*;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.systems.modules.Modules;
import com.egirlsnation.swissknife.systems.modules.illegals.ArmorStackLimiter;
import com.egirlsnation.swissknife.systems.modules.illegals.TotemStackLimiter;
import com.egirlsnation.swissknife.utils.server.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ControlledDupes extends Module {
    public ControlledDupes(){
        super(Categories.Misc, "controlled-dupes", "Adds back some dupes but you can control them this time");
    }

    private final SettingGroup sgCraftingDupe = settings.createGroup("crafting-dupe");

    private final Setting<Boolean> craftingEnabled = sgCraftingDupe.add(new BoolSetting.Builder()
            .name("enabled")
            .description("If the crafting dupe from 1.12 should be enabled")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> craftingStacking = sgCraftingDupe.add(new BoolSetting.Builder()
            .name("stacking")
            .description("If the crafting dupe should stack the items")
            .defaultValue(true)
            .build()
    );

    private final Setting<Integer> craftingMaxStack = sgCraftingDupe.add(new IntSetting.Builder()
            .name("max-stack")
            .description("If stacking, how many items should be in the stack")
            .defaultValue(64)
            .range(2, 64)
            .build()
    );

    private final Setting<List<String>> craftingBlacklist = sgCraftingDupe.add(new StringListSetting.Builder()
            .name("blacklist")
            .description("Item materials that cannot be duped at all (supports regex)")
            .defaultValue(Arrays.asList(".*SHULKER_BOX$", Material.PLAYER_HEAD.toString()))
            .build()
    );

    private final Setting<Boolean> needsResult = sgCraftingDupe.add(new BoolSetting.Builder()
            .name("needs-result")
            .description("If it should dupe only when the crafting has a result")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> removeCrafingItems = sgCraftingDupe.add(new BoolSetting.Builder()
            .name("remove-crafting-items")
            .description("Removes the items in the crafting slots after the person duped")
            .defaultValue(true)
            .build()
    );

    List<UUID> startedCrafting = new ArrayList<>(1);

    private final SettingGroup sgDonkey = settings.createGroup("donkey-dupe");

    private final Setting<Boolean> enableDonkey = sgDonkey.add(new BoolSetting.Builder()
            .name("enabled")
            .description("If the donkey dupe should be enabled")
            .defaultValue(true)
            .build()
    );

    private final Setting<List<String>> enabledMounts = sgDonkey.add(new StringListSetting.Builder()
            .name("enabled-mounts")
            .description("Entity types player is allowed to use for the donkey dupe")
            .defaultValue(Arrays.asList("donkey", "mule", "llama", "trader_llama"))
            .build()
    );

    private final Setting<Boolean> alertPlayer = sgDonkey.add(new BoolSetting.Builder()
            .name("send-msg")
            .description("If the plugin should send message when removing chests from mounts")
            .defaultValue(true)
            .build()
    );

    private final Setting<String> removedChestMsg = sgDonkey.add(new StringSetting.Builder()
            .name("removed-chest-msg")
            .description("The message to send when the plugin prevents player from putting chest on mounts")
            .defaultValue(ChatColor.RED + "You cannot put chest on this entity")
            .build()
    );

    private final SettingGroup sgDebug = settings.createGroup("debug-stick-dupe");

    private final Setting<Boolean> enableDebug = sgDebug.add(new BoolSetting.Builder()
            .name("enabled")
            .description("If the debug stick dupe should be enabled")
            .defaultValue(true)
            .build()
    );

    private final Setting<Integer> debugCooldown = sgDebug.add(new IntSetting.Builder()
            .name("cooldown")
            .description("Cooldown in milliseconds")
            .defaultValue(8500)
            .build()
    );

    private final Map<UUID, Long> debugStickCooldowns = new HashMap<>();

    private final SettingGroup sgItemFrame = settings.createGroup("item-frame-dupe");

    private final Setting<Boolean> enableItemFrame = sgItemFrame.add(new BoolSetting.Builder()
            .name("enabled")
            .description("If the item frame dupe should be enabled")
            .defaultValue(true)
            .build()
    );

    private final Setting<Integer> itemFrameChance = sgItemFrame.add(new IntSetting.Builder()
            .name("dupe-chance")
            .description("Chance of duping in %")
            .defaultValue(20)
            .min(1)
            .max(100)
            .build()
    );

    private final Setting<Integer> iFrameStack = sgItemFrame.add(new IntSetting.Builder()
            .name("stack-chance")
            .description("Chance of stacking when duping in %")
            .defaultValue(5)
            .min(1)
            .max(100)
            .build()
    );

    private final Random random = new Random();

    //Crafting dupe start

    @EventHandler
    private void PrepareCraftItem(PrepareItemCraftEvent e){
        if(!isEnabled()) return;

        if(craftingEnabled.get()){
            for(HumanEntity viewer : e.getViewers()){
                if(!(viewer instanceof Player)) continue;
                startedCrafting.add(viewer.getUniqueId());
            }
        }
    }

    @EventHandler
    private void PickUpEvent(EntityPickupItemEvent e){
        if(!isEnabled()) return;
        if(!craftingEnabled.get()) return;

        if(!(e.getEntity() instanceof Player)) return;
        if(!startedCrafting.contains(e.getEntity().getUniqueId())) return;
        Player player = (Player) e.getEntity();
        for(String material : craftingBlacklist.get()){
            if(e.getItem().getItemStack().getType().toString().matches(material)){
                startedCrafting.remove(player.getUniqueId());
                return;
            }
        }


        if(!startedCrafting.contains(player.getUniqueId())) return;
        Bukkit.getScheduler().runTaskLater(SwissKnife.INSTANCE, () -> {
            if(player.getInventory().getItemInMainHand().isSimilar(e.getItem().getItemStack())){
                if(needsResult.get()){
                    if(!player.getOpenInventory().getTopInventory().getType().equals(InventoryType.CRAFTING)){
                        startedCrafting.remove(player.getUniqueId());
                        return;
                    }
                    ItemStack craftingResult = player.getOpenInventory().getTopInventory().getItem(0);
                    if(craftingResult == null || craftingResult.getType().equals(Material.AIR)){
                        startedCrafting.remove(player.getUniqueId());
                        return;
                    }
                }

                if(craftingStacking.get()){
                    if(Modules.get().isActive(TotemStackLimiter.class) && e.getItem().getItemStack().getType().equals(Material.TOTEM_OF_UNDYING)){
                        player.getInventory().getItemInMainHand().setAmount(Modules.get().get(TotemStackLimiter.class).maxTotemStack.get());
                        if(removeCrafingItems.get()){
                            player.getOpenInventory().getTopInventory().clear();
                        }
                    }else if(Modules.get().isActive(ArmorStackLimiter.class) && ItemUtil.isArmorPiece(e.getItem().getItemStack())){
                        player.getInventory().getItemInMainHand().setAmount(Modules.get().get(ArmorStackLimiter.class).maxArmorStack.get());
                        if(removeCrafingItems.get()){
                            player.getOpenInventory().getTopInventory().clear();
                        }
                    }else{
                        player.getInventory().getItemInMainHand().setAmount(craftingMaxStack.get());
                        if(removeCrafingItems.get()){
                            player.getOpenInventory().getTopInventory().clear();
                        }
                    }
                }else{
                    e.getItem().getWorld().dropItem(e.getItem().getLocation(), e.getItem().getItemStack());
                    if(removeCrafingItems.get()){
                        player.getOpenInventory().getTopInventory().clear();
                    }
                }
                startedCrafting.remove(player.getUniqueId());
            }
        }, 5);
    }

    @EventHandler
    private void PlayerQuit(PlayerQuitEvent e){
        startedCrafting.remove(e.getPlayer().getUniqueId());
        debugStickCooldowns.remove(e.getPlayer().getUniqueId());
    }

    //Crafting dupe end and donkey dupe start

    @EventHandler(ignoreCancelled = true)
    public void entityInteract(PlayerInteractAtEntityEvent e){
        if(!isEnabled()) return;
        if(!enableDonkey.get()) return;
        if(!enabledMounts.get().contains(e.getRightClicked().getType().toString().toLowerCase())) return;

        ItemStack itemOff = e.getPlayer().getInventory().getItemInOffHand();
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        if(itemOff.getType().equals(Material.CHEST)){
            item = itemOff;
        }
        if(!item.getType().equals(Material.CHEST)) return;
        if(!(e.getRightClicked() instanceof ChestedHorse)) return;

        ChestedHorse mount = (ChestedHorse) e.getRightClicked();

        for(ItemStack is : mount.getInventory().getContents()){
            if(is == null) continue;
            if(is.getType() == Material.SADDLE) continue;
            mount.getWorld().dropItemNaturally(mount.getLocation(), is);
            mount.getWorld().dropItemNaturally(mount.getLocation(), is);
        }
        mount.setCarryingChest(false);
        e.setCancelled(true);
        if(alertPlayer.get()){
            sendMessage(e.getPlayer(), removedChestMsg.get(), true);
        }

        Bukkit.getScheduler().runTaskLater(SwissKnife.INSTANCE, () -> {
            mount.setCarryingChest(false);
        }, 2);
    }

    //Donkey dupe end and debug stick dupe start

    @EventHandler
    private void blockBreak(BlockBreakEvent e){
        if(!isEnabled()) return;
        if(!enableDebug.get()) return;
        if(e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;

        if(e.getBlock().getState() instanceof ShulkerBox){
            if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.DEBUG_STICK)){
                Long cooldown = debugStickCooldowns.get(e.getPlayer().getUniqueId());
                if(cooldown != null){
                    if((cooldown + debugCooldown.get()) > System.currentTimeMillis()){
                        e.setCancelled(true);
                        return;
                    }
                }

                debugStickCooldowns.remove(e.getPlayer().getUniqueId());
                for(ItemStack item : e.getBlock().getDrops()){
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), item);
                }
                e.getBlock().breakNaturally();
                debugStickCooldowns.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
            }
        }
    }

    //Item frame dupe start

    @EventHandler
    private void entityDamage(EntityDamageEvent e){
        if(!isEnabled()) return;
        if(!enableItemFrame.get()) return;
        if(!e.getEntity().getType().equals(EntityType.ITEM_FRAME)) return;

        ItemFrame iFrame = (ItemFrame) e.getEntity();
        if((random.nextInt(100) + 1) <= itemFrameChance.get()){
            if((random.nextInt(100) + 1) <= iFrameStack.get()){
                ItemStack isClone = iFrame.getItem();
                isClone.setAmount(64);
                iFrame.getWorld().dropItemNaturally(e.getEntity().getLocation(), isClone);
            }else{
                iFrame.getWorld().dropItemNaturally(e.getEntity().getLocation(), iFrame.getItem());
            }
        }
    }
}

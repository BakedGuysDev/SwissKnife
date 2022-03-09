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

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.events.AddItemEvent;
import com.egirlsnation.swissknife.events.DropExcessEvent;
import com.egirlsnation.swissknife.systems.modules.illegals.IllegalStacks;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;

public final class InventoryUtil {

    private static SwissKnife plugin = SwissKnife.INSTANCE;

    public static void replaceItem(final Inventory inventory, final int slot, final ItemStack stack){
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            ItemStack slotItem = inventory.getItem(slot);

            // Sanity check to make sure the new item is different;
            if((stack != null && slotItem != null && stack.getAmount() != slotItem.getAmount()) || !ItemUtil.isSameItem(stack, slotItem)){
                inventory.setItem(slot, stack);
            }
        });
    }

    public static void addItemsToPlayer(Player player, ItemStack itemToAdd, String extraType){
        addItems(player, itemToAdd, player.getInventory(), 0, 36, null, extraType);
    }

    public static void updateInventory(final Player player){
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, player::updateInventory);
    }

    public static void updateInventoryLater(final Player player, final int ticks){
        Bukkit.getScheduler().runTaskLater(plugin, player::updateInventory, ticks);
    }

    public static void splitStack(Player player, boolean toolCheck){
        ItemStack holding = player.getInventory().getItemInMainHand();

        int amount = holding.getAmount();

        if(amount > 1 && (!toolCheck || ItemUtil.isTool(holding.getType()))){
            ItemStack move = holding.clone();
            move.setAmount(amount - 1);
            addItemsToPlayer(player, move, "");
            holding.setAmount(1);
        }
    }

    // Helper functions go here
    public static void addItems(final Player player, final ItemStack itemToAdd, final Inventory inventory, final int start, final int end, final Inventory fromInventory, final String extraType){
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if(start < end && end <= inventory.getSize()){
                int addAmount = itemToAdd.getAmount();
                int initialAdd = addAmount;

                if(extraType.equals("inventory")){
                    addAmount = addInventoryTTB(player, inventory, itemToAdd, addAmount, start, end, true);

                    if(addAmount > 0){
                        addAmount = addInventoryTTB(player, inventory, itemToAdd, addAmount, start, end, false);
                    }
                }else{
                    boolean hotbarFirst = true;
                    boolean leftToRight = false;
                    boolean topToBottom = false;

                    if(extraType.equals("pickup") || extraType.equals("swap")){
                        leftToRight = true;
                        topToBottom = true;
                    }else{
                        if(fromInventory != null){
                            InventoryType fromType = fromInventory.getType();
                            if(fromType == InventoryType.WORKBENCH || fromType == InventoryType.ANVIL || fromType == InventoryType.FURNACE || fromType == InventoryType.CRAFTING || fromType == InventoryType.MERCHANT){
                                hotbarFirst = false;
                                leftToRight = true;
                                topToBottom = true;
                            }
                        }
                    }

                    //Add to existing stacks
                    if(hotbarFirst){
                        if(leftToRight){
                            addAmount = addHotbarLTR(player, inventory, itemToAdd, addAmount, start, end, true);
                        }else{
                            addAmount = addHotbarRTL(player, inventory, itemToAdd, addAmount, start, end, true);
                        }
                    }

                    if(addAmount > 0){
                        if(topToBottom){
                            addAmount = addInventoryTTB(player, inventory, itemToAdd, addAmount, start, end, true);
                        }else{
                            addAmount = addInventoryBTT(player, inventory, itemToAdd, addAmount, start, end, true);
                        }
                    }

                    if(!hotbarFirst && addAmount > 0){
                        if(leftToRight){
                            addAmount = addHotbarLTR(player, inventory, itemToAdd, addAmount, start, end, true);
                        }else{
                            addAmount = addHotbarRTL(player, inventory, itemToAdd, addAmount, start, end, true);
                        }
                    }

                    // Add to empty slots
                    if(hotbarFirst && addAmount > 0){
                        if(leftToRight){
                            addAmount = addHotbarLTR(player, inventory, itemToAdd, addAmount, start, end, false);
                        }else{
                            addAmount = addHotbarRTL(player, inventory, itemToAdd, addAmount, start, end, false);
                        }
                    }

                    if(addAmount > 0){
                        if(topToBottom){
                            addAmount = addInventoryTTB(player, inventory, itemToAdd, addAmount, start, end, false);
                        }else{
                            addAmount = addInventoryBTT(player, inventory, itemToAdd, addAmount, start, end, false);
                        }
                    }

                    if(!hotbarFirst && addAmount > 0){
                        if(leftToRight){
                            addAmount = addHotbarLTR(player, inventory, itemToAdd, addAmount, start, end, false);
                        }else{
                            addAmount = addHotbarRTL(player, inventory, itemToAdd, addAmount, start, end, false);
                        }
                    }
                }

                ItemStack itemClone = itemToAdd.clone();
                itemClone.setAmount(initialAdd - addAmount);
                AddItemEvent addEvent = new AddItemEvent(player, itemClone, inventory);
                Bukkit.getServer().getPluginManager().callEvent(addEvent);

                if(addAmount > 0){
                    // For some reason it is becoming air at this point in certain situations.
                    if(itemToAdd.getType() != Material.AIR){
                        ItemStack clone = itemToAdd.clone();
                        clone.setAmount(addAmount);
                        player.getWorld().dropItemNaturally(player.getLocation(), clone.clone());

                        DropExcessEvent dropEvent = new DropExcessEvent(player, clone.clone(), inventory);
                        Bukkit.getServer().getPluginManager().callEvent(dropEvent);
                    }
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    private static int addHotbarLTR(Player player, Inventory inventory, ItemStack itemToAdd, int addAmount, int start, int end, boolean partial){
        if(start <= 8){
            if(end > 8){
                end = 8;
            }

            Material type = itemToAdd.getType();
            short durability = itemToAdd.getDurability();

            int i = start;
            while(i <= end && addAmount > 0){
                if(partial){
                    addAmount = addPartialLoopHelper(player, inventory, itemToAdd, type, durability, addAmount, i);
                }else{
                    addAmount = addEmptyLoopHelper(player, inventory, itemToAdd, type, durability, addAmount, i);
                }
                i++;
            }
        }

        return addAmount;
    }

    @SuppressWarnings("deprecation")
    private static int addHotbarRTL(Player player, Inventory inventory, ItemStack itemToAdd, int addAmount, int start, int end, boolean partial){
        if(start <= 8){
            if(end > 8){
                end = 9;
            }
            end--;

            Material type = itemToAdd.getType();
            short durability = itemToAdd.getDurability();

            int i = end;
            while(i >= start && addAmount > 0){
                if(partial){
                    addAmount = addPartialLoopHelper(player, inventory, itemToAdd, type, durability, addAmount, i);
                }else{
                    addAmount = addEmptyLoopHelper(player, inventory, itemToAdd, type, durability, addAmount, i);
                }
                i--;
            }
        }

        return addAmount;
    }

    @SuppressWarnings("deprecation")
    private static int addInventoryTTB(Player player, Inventory inventory, ItemStack itemToAdd, int addAmount, int start, int end, boolean partial){
        boolean validAdd = false;

        if(inventory.getType() == InventoryType.PLAYER){
            if(end > 9){
                if(start < 9){
                    start = 9;
                }
                end--;
                validAdd = true;
            }
        }else{
            if(end > 0){
                if(start < 0){
                    start = 0;
                }
                end--;
                validAdd = true;
            }
        }

        if(validAdd){
            Material type = itemToAdd.getType();
            short durability = itemToAdd.getDurability();

            int i = start;
            while(i <= end && addAmount > 0){
                if(partial){
                    addAmount = addPartialLoopHelper(player, inventory, itemToAdd, type, durability, addAmount, i);
                }else{
                    addAmount = addEmptyLoopHelper(player, inventory, itemToAdd, type, durability, addAmount, i);
                }
                i++;
            }
        }

        return addAmount;
    }

    @SuppressWarnings("deprecation")
    private static int addInventoryBTT(Player player, Inventory inventory, ItemStack itemToAdd, int addAmount, int start, int end, boolean partial){
        boolean validAdd = false;

        if(inventory.getType() == InventoryType.PLAYER){
            if(end > 9){
                if(start < 9){
                    start = 9;
                }
                end--;
                validAdd = true;
            }
        }else{
            if(end > 0){
                if(start < 0){
                    start = 0;
                }
                end--;
                validAdd = true;
            }
        }

        if(validAdd){
            Material type = itemToAdd.getType();
            short durability = itemToAdd.getDurability();

            int i = end;
            while(i >= start && addAmount > 0){
                if(partial){
                    addAmount = addPartialLoopHelper(player, inventory, itemToAdd, type, durability, addAmount, i);
                }else{
                    addAmount = addEmptyLoopHelper(player, inventory, itemToAdd, type, durability, addAmount, i);
                }
                i--;
            }
        }

        return addAmount;
    }

    private static int addPartialLoopHelper(Player player, Inventory inventory, ItemStack itemToAdd, Material type, short durability, int addAmount, int i){
        ItemStack slot = inventory.getItem(i);

        if(slot != null && ItemUtil.isSameItem(slot, itemToAdd)){
            int slotAmount = slot.getAmount();

            int maxAmount = getInventoryMax(player, null, player.getOpenInventory(), inventory, type, durability, i);
            // Handle infinite items
            if(maxAmount == IllegalStacks.ITEM_INFINITE){
                maxAmount = type.getMaxStackSize();
            }

            int canAdd = maxAmount - slotAmount;
            if(canAdd > 0){
                // Add less than a full slot
                if(addAmount <= canAdd){
                    slot.setAmount(slotAmount + addAmount);
                    inventory.setItem(i, slot);
                    addAmount = 0;
                    // Fill the slot and leave the rest
                }else{
                    slot.setAmount(maxAmount);
                    inventory.setItem(i, slot);
                    addAmount -= canAdd;
                }
            }
        }

        return addAmount;
    }

    private static int addEmptyLoopHelper(Player player, Inventory inventory, ItemStack itemToAdd, Material type, short durability, int addAmount, int i){
        ItemStack slot = inventory.getItem(i);

        if(slot == null){
            int maxAmount = getInventoryMax(player, null, player.getOpenInventory(), inventory, type, durability, i);

            // Handle infinite items
            if(maxAmount == IllegalStacks.ITEM_INFINITE){
                maxAmount = type.getMaxStackSize();
            }
            if(addAmount >= maxAmount){
                itemToAdd.setAmount(maxAmount);
                inventory.setItem(i, itemToAdd.clone());
                addAmount -= maxAmount;
            }else if(addAmount > 0){
                itemToAdd.setAmount(addAmount);
                inventory.setItem(i, itemToAdd.clone());
                addAmount = 0;
            }
        }

        return addAmount;
    }

    @SuppressWarnings("deprecation")
    public static int getInventoryMax(Player player, String worldName, InventoryView view, Inventory inventory, Material mat, short dur, int slot){
        InventoryType inventoryType = inventory.getType();

        int maxAmount;
        if(player == null){
            maxAmount = IllegalStacks.getItemMax(mat);
        }else{
            maxAmount = IllegalStacks.getItemMax(mat);
            int maxPlayerAmount = IllegalStacks.getItemMax(mat);

            // Handle player section of inventory separately from the container above it.
            if(slot >= inventory.getSize()){
                maxAmount = maxPlayerAmount;
            }

        }

        String invName = "";
        if(view != null){
            invName = view.getTitle();
        }


        if(inventoryType == InventoryType.CHEST && (invName.equalsIgnoreCase("Horse") || invName.equalsIgnoreCase("Undead horse") || invName.equalsIgnoreCase("Skeleton horse"))){
            if(slot < 2){
                maxAmount = 1;
            }
        }else if(inventoryType == InventoryType.CHEST && (invName.equalsIgnoreCase("Donkey") || invName.equalsIgnoreCase("Mule"))){
            if(slot == 0){
                maxAmount = 1;
            }else if(slot == 1){
                maxAmount = 0;
            }
        }else if(inventoryType == InventoryType.BLAST_FURNACE){
            if(slot >= 0 && slot < 3){
                maxAmount = InventoryType.BLAST_FURNACE.getDefaultSize();
            }
        }else if(inventoryType == InventoryType.SMOKER){
            if(slot >= 0 && slot < 3){
                maxAmount = InventoryType.SMOKER.getDefaultSize();
            }
        }else if(inventoryType == InventoryType.FURNACE){
            if(slot >= 0 && slot < 3){
                maxAmount = InventoryType.FURNACE.getDefaultSize();
            }
        }else if(inventoryType == InventoryType.ENCHANTING){
            if(slot == 0){
                maxAmount = 1;
            }else if(slot == 1){
                if(!(mat == Material.LAPIS_LAZULI)){
                    maxAmount = 0;
                }
            }
        }else if((inventoryType == InventoryType.PLAYER && slot >= 36 && slot < 40) || (inventoryType == InventoryType.CRAFTING && slot >= 5 && slot < 9)){
            maxAmount = 1;
        }else if(inventoryType == InventoryType.MERCHANT){
            if(slot >= 0 && slot < 2){
                maxAmount = mat.getMaxStackSize();
            }
        }else if(inventoryType == InventoryType.BREWING){
            if(slot >= 0 && slot < 3){
                maxAmount = 1;
            }
        }else if(((inventoryType == InventoryType.WORKBENCH && slot >= 1 && slot < 10) || (inventoryType == InventoryType.CRAFTING && slot >= 1 && slot < 5))){
            maxAmount = mat.getMaxStackSize();
        }else if(inventoryType == InventoryType.ANVIL && slot < 2){
            maxAmount = mat.getMaxStackSize();
        }else if(inventoryType == InventoryType.BEACON && slot == 0){
            maxAmount = 1;
        }else if(inventoryType == InventoryType.ENDER_CHEST){
            maxAmount = mat.getMaxStackSize();
        }else if(inventoryType == InventoryType.HOPPER){
            maxAmount = mat.getMaxStackSize();
        }else if(inventoryType == InventoryType.DROPPER){
            maxAmount = mat.getMaxStackSize();
        }else if(inventoryType == InventoryType.DISPENSER){
            maxAmount = mat.getMaxStackSize();
        }else if(inventoryType == InventoryType.SHULKER_BOX){
            maxAmount = mat.getMaxStackSize();
        }else if(inventoryType == InventoryType.LOOM){
            maxAmount = mat.getMaxStackSize();
        }else if(inventoryType == InventoryType.CARTOGRAPHY){
            maxAmount = mat.getMaxStackSize();
        }else if(inventoryType == InventoryType.GRINDSTONE){
            maxAmount = mat.getMaxStackSize();
        }else if(inventoryType == InventoryType.STONECUTTER){
            maxAmount = mat.getMaxStackSize();
        }

        // Handle infinite and default items
        if(maxAmount == IllegalStacks.ITEM_INFINITE || maxAmount == IllegalStacks.ITEM_DEFAULT){
            maxAmount = mat.getMaxStackSize();
        }

        // Prevent Item loss when bukkit doesn't handle the inventory's max stack size
        int inventoryMax = inventory.getMaxStackSize();
        if(inventoryType != InventoryType.PLAYER && maxAmount > inventoryMax && slot < inventory.getSize()){
            maxAmount = inventoryMax;
        }

        return maxAmount;
    }

}

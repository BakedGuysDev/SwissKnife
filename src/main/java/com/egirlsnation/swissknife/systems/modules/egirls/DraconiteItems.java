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

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.events.PlayerPlaceCrystalEvent;
import com.egirlsnation.swissknife.settings.BoolSetting;
import com.egirlsnation.swissknife.settings.IntSetting;
import com.egirlsnation.swissknife.settings.Setting;
import com.egirlsnation.swissknife.settings.SettingGroup;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.entity.player.SwissPlayer;
import com.egirlsnation.swissknife.utils.handlers.customItems.AbilityCooldownHandler;
import com.egirlsnation.swissknife.utils.handlers.customItems.DraconiteAbilityHandler;
import com.egirlsnation.swissknife.utils.server.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;
import java.util.Random;

public class DraconiteItems extends Module {
    private final AbilityCooldownHandler cooldownHandler;
    private final DraconiteAbilityHandler abilityHandler;

    public DraconiteItems(){ //TODO: Fix default attributes
        super(Categories.EgirlsNation, "draconite-items", "Adds various draconite items");

        cooldownHandler = new AbilityCooldownHandler();
        abilityHandler = new DraconiteAbilityHandler();
    }


    private final SettingGroup sgHeads = settings.createGroup("heads");

    public final Setting<Boolean> draconiteItemsBehead = sgHeads.add(new BoolSetting.Builder()
            .name("draconite-items-behead")
            .description("Addon setting for Heads module")
            .defaultValue(true)
            .build()
    );

    public final Setting<Boolean> draconiteCrystalsBehead = sgHeads.add(new BoolSetting.Builder()
            .name("draconite-crystals-behead")
            .description("Addon setting for Heads module")
            .defaultValue(true)
            .build()
    );

    private final SettingGroup sgPickaxe = settings.createGroup("pickaxe");

    public final Setting<Boolean> useGems = sgPickaxe.add(new BoolSetting.Builder()
            .name("use-gems")
            .defaultValue(true)
            .build()
    );

    public final Setting<Boolean> useBedrock = sgPickaxe.add(new BoolSetting.Builder()
            .name("bedrock-handle")
            .defaultValue(true)
            .build()
    );

    public final Setting<Integer> xpToDrain = sgPickaxe.add(new IntSetting.Builder()
            .name("xp-to-drain")
            .defaultValue(5)
            .build()
    );

    public final Setting<Integer> hasteLevel = sgPickaxe.add(new IntSetting.Builder()
            .name("haste-level")
            .defaultValue(4)
            .build()
    );

    private final SettingGroup sgTotem = settings.createGroup("totem");

    public final Setting<Boolean> ignoreTotemAttribute = sgTotem.add(new BoolSetting.Builder()
            .name("ignore-totem-attribute")
            .defaultValue(true)
            .build()
    );

    @EventHandler
    private void playerInteract(PlayerInteractEvent e){
        if(!isEnabled()) return;
        SwissPlayer swissPlayer = SwissPlayer.getSwissPlayer(e.getPlayer());
        if(e.getAction().equals(Action.RIGHT_CLICK_AIR)){
            if(!swissPlayer.hasFeatureEnabled(SwissPlayer.SwissFeature.DRACONITE_ABILITIES)){
                return;
            }
            if(ItemUtil.isDraconiteSword(e.getItem())){
                abilityHandler.handleSwordAbility(e.getPlayer(), e.getHand(), cooldownHandler);
                return;
            }else if(ItemUtil.isDraconiteAxe(e.getItem())){
                abilityHandler.handleAxeAbility(e.getPlayer(), e.getHand(), cooldownHandler);
                return;
            }else if(ItemUtil.isDraconiteCrystal(e.getItem())){
                abilityHandler.handleCrystalAbility(e.getPlayer(), e.getHand(), cooldownHandler);
                return;
            }
        }

        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)){
            if(!swissPlayer.hasFeatureEnabled(SwissPlayer.SwissFeature.DRACONITE_ABILITIES)){
                return;
            }
            if(ItemUtil.isDraconitePickaxe(e.getItem())){
                abilityHandler.handlePickaxeAbility(e.getPlayer(), e.getHand(), cooldownHandler, xpToDrain.get(), hasteLevel.get());
            }
        }

        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if(e.getClickedBlock() == null) return;

        if(e.getClickedBlock().getType().equals(Material.OBSIDIAN) || e.getClickedBlock().getType().equals(Material.BEDROCK) || e.getClickedBlock().getType().equals(Material.CRYING_OBSIDIAN)){
            if(e.getMaterial().equals(Material.END_CRYSTAL)){


                Bukkit.getScheduler().runTask(SwissKnife.INSTANCE, () -> {
                    List<Entity> entities = e.getPlayer().getNearbyEntities(6, 6, 6);

                    for(Entity entity : entities){
                        if(EntityType.ENDER_CRYSTAL.equals(entity.getType())){
                            EnderCrystal crystal = (EnderCrystal) entity;
                            Block belowCrystal = crystal.getLocation().getBlock().getRelative(BlockFace.DOWN);

                            if(e.getClickedBlock().equals(belowCrystal)){
                                Bukkit.getPluginManager().callEvent(new PlayerPlaceCrystalEvent(e.getPlayer(), crystal, e.getItem()));
                                break;
                            }
                        }
                    }
                });
            }
        }
    }

    @EventHandler
    private void PlayerPlaceCrystal(PlayerPlaceCrystalEvent e){
        if(!isEnabled()) return;
        if(ItemUtil.isDraconiteCrystal(e.getCrystalItem())){
            e.getCrystal().setCustomName("Draconite Crystal");
        }
    }

    @EventHandler
    private void PlayerInteractAtEntity(PlayerInteractEntityEvent e){
        if(!isEnabled()) return;
        if(e.getRightClicked().getType().equals(EntityType.ENDER_CRYSTAL)){
            if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.NAME_TAG)){
                e.setCancelled(true);
                return;
            }
            if(e.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.NAME_TAG)){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void enytityToggleGlide(EntityToggleGlideEvent e){
        if(!isEnabled()) return;
        if(!(e.getEntity() instanceof Player)) return;
        if(abilityHandler.hasCrystalAbilityOn((Player) e.getEntity())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void playerQuit(PlayerQuitEvent e){
        cooldownHandler.removeAllCooldowns(e.getPlayer());
        abilityHandler.removeFromCrystalList(e.getPlayer());
        abilityHandler.purgePickaxeTask(e.getPlayer());
    }

    @EventHandler
    private void entityDeath(EntityDeathEvent e){
        if(!isEnabled()) return;
        final Random random = new Random();

        int chance = random.nextInt(100) + 1;
        switch(e.getEntityType()){
            case ENDERMAN:{
                handleEndermanDrops(e.getEntity(), e.getEntity().getKiller(), chance);
            }break;

            case EVOKER:{
                handleEvokerDrops(e.getEntity(), e.getEntity().getKiller(), chance);
            }

            case SHULKER:{
                handleShulkerDrops(e.getEntity(), e.getEntity().getKiller(), chance);
            }
        }
    }

    @EventHandler
    public void CraftItem(CraftItemEvent e) {
        if(!isEnabled()) return;
        if(e.getInventory().getResult() == null) return;
        if (!(e.getView().getPlayer() instanceof Player)) return;
        if (!ItemUtil.isDraconitePickaxe(e.getInventory().getResult())) return;


        if (!useGems.get()) return;
        int gems = 0;

        for (ItemStack item : e.getInventory().getContents()) {
            if (ItemUtil.isDraconiteGem(item)) {
                gems++;
            }
        }
        if (gems != 2){
            e.setCancelled(true);
            e.getInventory().getResult().setAmount(0);
        }
    }

    private void handleEndermanDrops(Entity entity, Player player, int chance){
        if(chance > 3) return;
        entity.getWorld().dropItemNaturally(entity.getLocation(), ItemUtil.getDraconiteCrystal());
    }

    private void handleEvokerDrops(Entity entity, Player player, int chance){
        if(chance >= 3) return;
        entity.getWorld().dropItemNaturally(entity.getLocation(), ItemUtil.getPopbobTotem());
    }

    private void handleShulkerDrops(Entity entity, Player player, int chance){
        if(chance > 5) return;
        if(chance > 3){
            entity.getWorld().dropItemNaturally(entity.getLocation(), ItemUtil.getDraconiteAxe());
        }else{
            entity.getWorld().dropItemNaturally(entity.getLocation(), ItemUtil.getDraconiteSword());
        }
    }

    public void registerRecipes(){
        SwissKnife.swissLogger.info("Registering recipes");
        SwissKnife.swissLogger.info("Registering draconite pickaxe recipe");
        NamespacedKey draconitePickKey = new NamespacedKey(SwissKnife.INSTANCE, "draconite_pickaxe");
        ShapedRecipe draconitePick = new ShapedRecipe(draconitePickKey, ItemUtil.getDraconitePickaxe())
                .shape("GHG", " S ", " S ");
        if(useGems.get()){
            draconitePick.setIngredient('G', Material.PLAYER_HEAD).setIngredient('H', Material.END_CRYSTAL);
        }else{
            draconitePick.setIngredient('G', Material.END_CRYSTAL).setIngredient('H', Material.DRAGON_HEAD);
        }
        if(useBedrock.get()){
            draconitePick.setIngredient('S', Material.BEDROCK);
        }else{
            draconitePick.setIngredient('S', Material.STICK);
        }
        if(Bukkit.getRecipe(draconitePickKey) != null){
            Bukkit.removeRecipe(draconitePickKey);
        }
        Bukkit.addRecipe(draconitePick);
    }
}

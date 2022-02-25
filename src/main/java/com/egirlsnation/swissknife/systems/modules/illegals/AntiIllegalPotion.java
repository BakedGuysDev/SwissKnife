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

import com.egirlsnation.swissknife.settings.*;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.server.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

public class AntiIllegalPotion extends Module {
    public AntiIllegalPotion(){
        super(Categories.Illegals, "anti-illegal-potion", "Fixes/removes illegal potions on interact");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> remove = sgGeneral.add(new BoolSetting.Builder()
            .name("fix")
            .description("If the plugin should remove the potions instead of fixing them")
            .defaultValue(false)
            .build()
    );

    private final Setting<Integer> maxDuration = sgGeneral.add(new IntSetting.Builder()
            .name("max-duration")
            .description("Maximum duration of potions")
            .min(1)
            .defaultValue(10)
            .build()
    );

    private final Setting<Boolean> bypass = sgGeneral.add(new BoolSetting.Builder()
            .name("bypass")
            .description("If the check can be bypassed by permissions")
            .defaultValue(false)
            .build()
    );

    private final Setting<Boolean> alertPlayers = sgGeneral.add(new BoolSetting.Builder()
            .name("alert-players")
            .description("If the plugin should alert player when they try to use an illegal potion")
            .defaultValue(false)
            .build()
    );

    private final Setting<String> message = sgGeneral.add(new StringSetting.Builder()
            .name("message")
            .description("The message to send (supports color codes)")
            .defaultValue(ChatColor.RED + "Illegal item found. This incident will be reported")
            .build()
    );

    private final Setting<Boolean> log = sgGeneral.add(new BoolSetting.Builder()
            .name("logging")
            .description("If the plugin should log when it fixes/removes potion")
            .defaultValue(false)
            .build()
    );

    //TODO: Dispenser??


    @EventHandler
    private void playerInteract(PlayerInteractEvent e){
        if(!isEnabled()) return;
        if(e.getItem() == null) return;
        if(e.getItem().getType().equals(Material.POTION) || e.getItem().getType().equals(Material.SPLASH_POTION) || e.getItem().getType().equals(Material.LINGERING_POTION)){
            if(e.getPlayer().hasPermission("swissknife.bypass.illegals") && bypass.get()){
                return;
            }
            PotionMeta meta = (PotionMeta) e.getItem().getItemMeta();

            if(meta.hasCustomEffects()){
                for(PotionEffect effect : meta.getCustomEffects()){
                    if(!ItemUtil.isLegitPotionEffect(effect.getType())){
                        if(remove.get()){
                            e.setCancelled(true);
                            e.getItem().setAmount(0);
                            if(alertPlayers.get()){
                                sendMessage(e.getPlayer(), ChatColor.translateAlternateColorCodes('§', message.get()));
                            }
                            if(log.get()){
                                info("Player " + e.getPlayer().getName() + " tried to use an illegal potion");
                            }
                            return;
                        }
                        meta.removeCustomEffect(effect.getType());
                        continue;
                    }


                    int duration = effect.getDuration();
                    int amplifier = effect.getAmplifier();
                    boolean changed = false;

                    if(effect.getAmplifier() > ItemUtil.getMaxPotionAmplifier(effect.getType())){
                        if(remove.get()){
                            e.setCancelled(true);
                            e.getItem().setAmount(0);
                            if(alertPlayers.get()){
                                sendMessage(e.getPlayer(), ChatColor.translateAlternateColorCodes('§', message.get()));
                            }
                            if(log.get()){
                                info("Player " + e.getPlayer().getName() + " tried to use an illegal potion");
                            }
                            return;
                        }
                        meta.removeCustomEffect(effect.getType());
                        amplifier = ItemUtil.getMaxPotionAmplifier(effect.getType());
                        changed = true;
                    }

                    if(duration > maxDuration.get() * 60 * 20){
                        if(remove.get()){
                            e.setCancelled(true);
                            e.getItem().setAmount(0);
                            if(alertPlayers.get()){
                                sendMessage(e.getPlayer(), ChatColor.translateAlternateColorCodes('§', message.get()));
                            }
                            if(log.get()){
                                info("Player " + e.getPlayer().getName() + " tried to use an illegal potion");
                            }
                            return;
                        }
                        duration = maxDuration.get() * 60 * 20;
                        changed = true;
                    }

                    if(changed){
                        PotionEffect potionEffect = new PotionEffect(effect.getType(), duration, amplifier);
                        meta.addCustomEffect(potionEffect, true);
                    }
                }
                if(!meta.equals(e.getItem().getItemMeta())){
                    if(alertPlayers.get()){
                        sendMessage(e.getPlayer(), ChatColor.translateAlternateColorCodes('§', message.get()));
                    }
                    if(log.get()){
                        info("Player " + e.getPlayer().getName() + " tried to use an illegal potion");
                    }
                    e.getItem().setItemMeta(meta);
                }
            }

        }
    }

}

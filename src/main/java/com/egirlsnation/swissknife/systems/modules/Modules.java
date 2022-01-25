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

package com.egirlsnation.swissknife.systems.modules;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.systems.System;
import com.egirlsnation.swissknife.systems.Systems;
import com.egirlsnation.swissknife.systems.modules.egirls.Ranks;
import com.egirlsnation.swissknife.systems.modules.entity.*;
import com.egirlsnation.swissknife.systems.modules.illegals.*;
import com.egirlsnation.swissknife.systems.modules.misc.ChatTweaks;
import com.egirlsnation.swissknife.systems.modules.misc.ShulkerStackHandler;
import com.egirlsnation.swissknife.systems.modules.player.CombatCheck;
import com.egirlsnation.swissknife.systems.modules.player.CrystalSpeedLimiter;
import com.egirlsnation.swissknife.systems.modules.player.NetherRoofDisabler;
import com.egirlsnation.swissknife.systems.modules.player.RestrictedCreativeAddon;
import com.egirlsnation.swissknife.systems.modules.world.EndermenGrief;
import com.egirlsnation.swissknife.systems.modules.world.JihadBalls;
import org.bukkit.Bukkit;

import java.util.*;

public class Modules extends System<Module> {

    private static final List<Category> CATEGORIES = new ArrayList<>();

    private final List<Module> modules = new ArrayList<>();
    private final Map<Class<? extends Module>, Module> moduleInstances = new HashMap<>();
    private final Map<Category, List<Module>> groups = new HashMap<>();

    private final List<Module> active = new ArrayList<>();


    public Modules(){
        super("modules");
    }

    public static Modules get(){
        return Systems.get(Modules.class);
    }

    @Override
    public void init(){
        initEntity();
        initIllegals();
        initPlayer();
        initWorld();
        initMisc();
        initEgirls();
    }

    public void sortModules(){
        for(List<Module> modules :groups.values()){
            modules.sort(Comparator.comparing(o -> o.title));
        }
        modules.sort(Comparator.comparing(o -> o.title));
    }

    public static void registerCategory(Category category){
        if(!Categories.REGISTERING) throw new RuntimeException("Modules.registerCategory - Cannot register category outside of onRegisterCategories callback.");

        CATEGORIES.add(category);
    }

    private void add(Module module){
        moduleInstances.put(module.getClass(), module);
        modules.add(module);
        Bukkit.getPluginManager().registerEvents(module, SwissKnife.getPlugin(SwissKnife.class));
    }

    @SuppressWarnings("unchecked")
    public  <T extends Module> T get(Class<T> klass){
        return (T) moduleInstances.get(klass);
    }

    public boolean isActive(Class<? extends Module> klass) {
        Module module = get(klass);
        return module != null && module.isActive();
    }

    void addActive(Module module){
        synchronized (active){
            if(!active.contains(module)){
                active.add(module);
            }
        }
    }

    void removeActive(Module module){
        synchronized (active){
            active.remove(module);
        }
    }

    private void initEntity(){
        add(new DisableEntityPortals());
        add(new DragonSlayerAddon());
        add(new PetTotems());
        add(new VehicleLimiter());
        add(new WitherSpawnLimiter());
        add(new XpBottleLimiter());
    }

    private void initIllegals(){
        add(new AntiSpawnEggs());
        add(new ArmorStackLimiter());
        add(new IllegalBlocks());
        add(new IllegalEnchants());
        add(new IllegalLores());
    }

    private void initPlayer(){
        add(new CombatCheck());
        add(new CrystalSpeedLimiter());
        add(new NetherRoofDisabler());
        add(new RestrictedCreativeAddon());
    }

    private void initWorld(){
        add(new EndermenGrief());
        add(new JihadBalls());
    }

    private void initMisc(){
        add(new ChatTweaks());
        add(new ShulkerStackHandler());
    }

    private void initEgirls(){
        add(new Ranks());
    }
}

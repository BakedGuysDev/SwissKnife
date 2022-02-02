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
import com.egirlsnation.swissknife.settings.Setting;
import com.egirlsnation.swissknife.settings.SettingGroup;
import com.egirlsnation.swissknife.systems.System;
import com.egirlsnation.swissknife.systems.Systems;
import com.egirlsnation.swissknife.systems.modules.database.PlayerStats;
import com.egirlsnation.swissknife.systems.modules.database.Shitlist;
import com.egirlsnation.swissknife.systems.modules.egirls.Ranks;
import com.egirlsnation.swissknife.systems.modules.entity.*;
import com.egirlsnation.swissknife.systems.modules.illegals.*;
import com.egirlsnation.swissknife.systems.modules.misc.ChatTweaks;
import com.egirlsnation.swissknife.systems.modules.misc.ShulkerStackHandler;
import com.egirlsnation.swissknife.systems.modules.misc.SmallFixes;
import com.egirlsnation.swissknife.systems.modules.misc.SpawnCommands;
import com.egirlsnation.swissknife.systems.modules.player.*;
import com.egirlsnation.swissknife.systems.modules.world.EndermenGrief;
import com.egirlsnation.swissknife.systems.modules.world.JihadBalls;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.comments.CommentType;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Modules extends System<Module> {
    private static final List<Category> CATEGORIES = new ArrayList<>();

    private final List<Module> modules = new ArrayList<>();
    private final Map<Class<? extends Module>, Module> moduleInstances = new HashMap<>();
    private final Map<Category, List<Module>> groups = new HashMap<>();

    private final List<Module> enabled = new ArrayList<>();


    public Modules() {
        super("modules");
    }

    public static Modules get() {
        return Systems.get(Modules.class);
    }

    @Override
    public void init() {
        initEntity();
        initIllegals();
        initPlayer();
        initWorld();
        initDatabase();
        initMisc();
        initEgirls();
    }

    public void sortModules() {
        for (List<Module> modules : groups.values()) {
            modules.sort(Comparator.comparing(o -> o.title));
        }
        modules.sort(Comparator.comparing(o -> o.title));
    }

    public static void registerCategory(Category category) {
        if (!Categories.REGISTERING)
            throw new RuntimeException("Modules.registerCategory - Cannot register category outside of onRegisterCategories callback.");

        CATEGORIES.add(category);
    }

    private void add(Module module) {
        if(!CATEGORIES.contains(module.category)){
            throw new RuntimeException("Modules.addModule - Module's category was not registered.");
        }

        AtomicReference<Module> removedModule = new AtomicReference<>();
        if(moduleInstances.values().removeIf(module1 -> {
            if(module1.name.equals(module.name)){
                removedModule.set(module1);

                return true;
            }

            return false;
        })){
            getGroup(removedModule.get().category).remove(removedModule.get());
        }


        moduleInstances.put(module.getClass(), module);
        modules.add(module);
        getGroup(module.category).add(module);
        Bukkit.getPluginManager().registerEvents(module, SwissKnife.getPlugin(SwissKnife.class));
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T get(Class<T> klass) {
        return (T) moduleInstances.get(klass);
    }

    public boolean isActive(Class<? extends Module> klass) {
        Module module = get(klass);
        return module != null && module.isEnabled();
    }

    void addEnabled(Module module) {
        synchronized (enabled) {
            if (!enabled.contains(module)) {
                enabled.add(module);
            }
        }
    }

    void removeEnabled(Module module) {
        synchronized (enabled) {
            enabled.remove(module);
        }
    }

    public List<Module> getGroup(Category category) {
        return groups.computeIfAbsent(category, category1 -> new ArrayList<>());
    }

    private void initEntity() {
        add(new DisableEntityPortals());
        add(new DragonSlayerAddon());
        add(new PetTotems());
        add(new VehicleLimiter());
        add(new WitherSpawnLimiter());
        add(new XpBottleLimiter());
    }

    private void initIllegals() {
        add(new AntiSpawnEggs());
        add(new ArmorStackLimiter());
        add(new HighDamagePrevention());
        add(new IllegalBlocks());
        add(new IllegalEnchants());
        add(new IllegalLores());
        add(new TotemStackLimiter());
    }

    private void initPlayer() {
        add(new AntiOffhandCrash());
        add(new BedrockFloorDisabler());
        add(new CombatCheck());
        add(new CrystalKillTracker());
        add(new CrystalSpeedLimiter());
        add(new NetherRoofDisabler());
        add(new RestrictedCreativeAddon());
    }

    private void initWorld() {
        add(new EndermenGrief());
        add(new JihadBalls());
    }

    private void initMisc() {
        add(new ChatTweaks());
        add(new ShulkerStackHandler());
        add(new SmallFixes());
        add(new SpawnCommands());
    }

    private void initDatabase(){
        add(new PlayerStats());
        add(new Shitlist());
    }

    private void initEgirls() {
        add(new Ranks());
    }

    @Override
    public void writeToConfig() {
        for(Category category : CATEGORIES){
            for(Module module : groups.get(category)){
                ConfigurationSection section = getFile().createSection(module.name);

                getFile().setComment(module.name, module.description, CommentType.SIDE);
                section.set("enabled", module.isEnabled());

                for (SettingGroup sg : module.settings) {
                    section = getFile().createSection(module.name + "." + sg.name);
                    for (Setting<Object> setting : sg) {
                        section.set(setting.name, setting.get());
                        getFile().setComment(module.name + "." + sg.name + "." + setting.name, setting.description, CommentType.SIDE);
                    }
                }
            }

            if(category.equals(Categories.EgirlsNation)){
                getFile().setComment(groups.get(category).get(0).name, "\n" + StringUtils.capitalize(category.name) + " category\n"
                        + "Modules meant for play.egirlsnation.com\nEnabling them is not recommended unless you know exactly what you're doing!\n" , CommentType.BLOCK);
            }else{
                getFile().setComment(groups.get(category).get(0).name, "\n" + StringUtils.capitalize(category.name) + " category" + "\n", CommentType.BLOCK);
            }
        }

        try {
            getFile().save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readFromConfig() {
        for (Module module : modules) {
            ConfigurationSection section = getFile().getConfigurationSection(module.name);
            boolean enabled = section.getBoolean("enabled");
            for (SettingGroup sg : module.settings) {
                section = getFile().getConfigurationSection(module.name + "." + sg.name);
                if (section != null) {
                    for (Setting<Object> setting : sg) {
                        setting.set(section.get(setting.name));
                    }
                }
            }
            if (enabled && !module.isEnabled()) {
                module.toggle();
            }
        }
        try {
            getFile().save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

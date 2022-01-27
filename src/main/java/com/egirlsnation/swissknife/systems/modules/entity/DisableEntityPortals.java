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

package com.egirlsnation.swissknife.systems.modules.entity;

import com.egirlsnation.swissknife.settings.Setting;
import com.egirlsnation.swissknife.settings.SettingGroup;
import com.egirlsnation.swissknife.settings.StringListSetting;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPortalEvent;

import java.util.Arrays;
import java.util.List;

public class DisableEntityPortals extends Module {
    public DisableEntityPortals() {
        super(Categories.Entity, "disable-entity-portals", "Disables portals for certain entities");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<List<String>> entities = sgGeneral.add(new StringListSetting.Builder()
            .name("entities")
            .description("Entities to not allow to teleport with portals")
            .defaultValue(Arrays.asList("bee","end_crystal"))
            .build()
    );

    @EventHandler
    private void onEntityPortalTeleportEvent(EntityPortalEvent e){
        if(!isEnabled()) return;
        if(entities.get().contains(e.getEntityType().name())) {
            e.setCancelled(true);
        }
    }
}

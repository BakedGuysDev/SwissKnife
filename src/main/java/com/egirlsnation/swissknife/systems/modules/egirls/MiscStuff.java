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

import com.egirlsnation.swissknife.settings.BoolSetting;
import com.egirlsnation.swissknife.settings.Setting;
import com.egirlsnation.swissknife.settings.SettingGroup;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class MiscStuff extends Module {
    public MiscStuff(){
        super(Categories.EgirlsNation, "misc-stuff", "Small EgirlsNation stuff");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> tlauncherNotice = sgGeneral.add(new BoolSetting.Builder()
            .name("tlauncher-notice")
            .defaultValue(true)
            .build()
    );

    @EventHandler
    private void commandPreProcessor(PlayerCommandPreprocessEvent e){
        if(!isEnabled()) return;
        if(!tlauncherNotice.get()) return;
        if(e.getMessage().equalsIgnoreCase("/skin")){
            sendMessage(e.getPlayer(), ChatColor.RED + "If you're using tlauncher make sure to disable skins in the account settings, otherwise are not going to see skins of other people set with /skin properly.");
        }
    }
}

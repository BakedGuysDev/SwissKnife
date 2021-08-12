/*
 *
 * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2021 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the MIT License.
 *
 * You should have received a copy of the MIT
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/MIT>.
 *
 */

package com.egirlsnation.swissknife.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class LOGGER {

    private static final java.util.logging.Logger bukkitLogger = Bukkit.getLogger();

    public static void info(String string){
        bukkitLogger.info(ChatColor.AQUA + "[SwissKnife | Info] " + string);
    }

    public static void warning(String string){
        bukkitLogger.warning("[SwissKnife | Warning] " + string);
    }

    public static void error(String string){
        bukkitLogger.severe("[SwissKnife | Error] " + string);
    }

    public static void debug(String string){
        bukkitLogger.info(ChatColor.LIGHT_PURPLE + "[SwissKnife | Debug] " + string);
    }
}

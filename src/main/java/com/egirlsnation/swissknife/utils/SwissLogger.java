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

package com.egirlsnation.swissknife.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.function.Supplier;

public final class SwissLogger {

    private static final java.util.logging.Logger bukkitLogger = Bukkit.getLogger();

    public static void info(String string){
        bukkitLogger.info(ChatColor.AQUA + "[SwissKnife] " + string);
    }

    public static void warning(String string){
        bukkitLogger.warning("[SwissKnife] " + string);
    }

    public static void error(String string){
        bukkitLogger.severe("[SwissKnife] " + string);
    }

    public static void debug(String string){
        bukkitLogger.info(ChatColor.LIGHT_PURPLE + "[SwissKnife | Debug] " + string);
    }

    public static void info(Supplier<String> msgSupplier){
        bukkitLogger.info(ChatColor.AQUA + "[SwissKnife] " + msgSupplier);
    }

    public static void warning(Supplier<String> msgSupplier){
        bukkitLogger.warning("[SwissKnife] " + msgSupplier);
    }

    public static void error(Supplier<String> msgSupplier){
        bukkitLogger.severe("[SwissKnife] " + msgSupplier);
    }

    public static void debug(Supplier<String> msgSupplier){
        bukkitLogger.info(ChatColor.LIGHT_PURPLE + "[SwissKnife | Debug] " + msgSupplier);
    }
}

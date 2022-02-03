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

package com.egirlsnation.swissknife.utils;

import org.bukkit.ChatColor;

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SwissLogger {
    private final Logger logger = Logger.getLogger("SwissKnife");
    private final Logger debugLogger = Logger.getLogger("SwissKnife|Debug");


    private Logger getCustomLogger(String prefix){
        return Logger.getLogger(prefix);
    }

    public void log(Level level, String msg, String prefix){
        getCustomLogger(prefix).log(level, msg);
    }


    public void info(String string){
        logger.log(Level.INFO, ChatColor.AQUA + string);
    }

    public void warning(String string){
        logger.log(Level.WARNING, ChatColor.YELLOW + string);
    }

    public void severe(String string){
        logger.log(Level.SEVERE, ChatColor.RED + string);
    }

    public void debug(String string){
        debugLogger.log(Level.INFO, ChatColor.LIGHT_PURPLE + string);
    }

    public void info(Supplier<String> msgSupplier){
        logger.log(Level.INFO, ChatColor.AQUA + msgSupplier.get());

    }

    public void warning(Supplier<String> msgSupplier){
        logger.log(Level.WARNING, ChatColor.YELLOW + msgSupplier.get());
    }

    public void severe(Supplier<String> msgSupplier){
        logger.log(Level.SEVERE, ChatColor.RED + msgSupplier.get());
    }

    public void debug(Supplier<String> msgSupplier){
        debugLogger.log(Level.INFO, ChatColor.LIGHT_PURPLE + msgSupplier.get());
    }
}

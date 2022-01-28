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

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class SwissLogger extends Logger {
    private String pluginName;

    public SwissLogger(@NotNull Plugin plugin) {
        super(plugin.getClass().getCanonicalName(), (String)null);
        String prefix = plugin.getDescription().getPrefix();
        this.pluginName = prefix != null ? new StringBuilder().append("[").append(prefix).append("] ").toString() : "[" + plugin.getDescription().getName() + "] ";
        this.setParent(plugin.getServer().getLogger());
        this.setLevel(Level.ALL);
    }

    @Override
    public void log(@NotNull LogRecord logRecord) {
        logRecord.setMessage(this.pluginName + logRecord.getMessage());
        super.log(logRecord);
    }

    @Override
    public void log(Level level, String msg){
        if(!isLoggable(level)){
            return;
        }
        LogRecord lr = new LogRecord(level, msg);
        log(lr);
    }

    public void log(Level level, String msg, String prefix){
        if(!isLoggable(level)){
            return;
        }
        LogRecord lr = new LogRecord(level, prefix + msg);
        super.log(lr);
    }

    @Override
    public void log(Level level, Supplier<String> msgSupplier){
        if(!isLoggable(level)){
            return;
        }
        LogRecord lr = new LogRecord(level, msgSupplier.get());
        log(lr);
    }

    public void log(Level level, Supplier<String> msgSupplier, String prefix){
        if(!isLoggable(level)){
            return;
        }
        LogRecord lr = new LogRecord(level, msgSupplier.get() + prefix);
        super.log(lr);
    }

    @Override
    public void info(String string){
        log(Level.INFO, ChatColor.AQUA + string);
    }

    @Override
    public void warning(String string){
        log(Level.WARNING, ChatColor.YELLOW + string);
    }

    @Override
    public void severe(String string){
        log(Level.SEVERE, ChatColor.RED + string);
    }

    public void debug(String string){
        log(Level.INFO, ChatColor.LIGHT_PURPLE + string, "[SwissKnife | Debug]");
    }

    @Override
    public void info(Supplier<String> msgSupplier){
        log(Level.INFO, ChatColor.AQUA + msgSupplier.get());

    }

    @Override
    public void warning(Supplier<String> msgSupplier){
        log(Level.WARNING, ChatColor.YELLOW + msgSupplier.get());
    }

    @Override
    public void severe(Supplier<String> msgSupplier){
        log(Level.SEVERE, ChatColor.RED + msgSupplier.get());
    }

    public void debug(Supplier<String> msgSupplier){
        log(Level.INFO, ChatColor.LIGHT_PURPLE + msgSupplier.get(), "[SwissKnife | Debug]");
    }
}

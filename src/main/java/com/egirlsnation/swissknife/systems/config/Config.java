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

package com.egirlsnation.swissknife.systems.config;

import org.bukkit.ChatColor;

public class Config {
    public static String prefix = ChatColor.GOLD + "[" + ChatColor.LIGHT_PURPLE + "SwissKnife" + ChatColor.GOLD + "]" + ChatColor.RESET;

    //TODO: General config option for all module player alters to use the systemPrefix
    public static String systemPrefix = ChatColor.GOLD + "[" + ChatColor.LIGHT_PURPLE + "SwissKnife | %system%" + ChatColor.GOLD + "]" + ChatColor.RESET;

    public static boolean useDatabase = false;

    public static String databaseHost = "172.18.0.1";

    public static String databasePort = "3306";

    public static String databaseName = "name";

    public static String databaseUsername = "username";

    public static String databasePassword = "password";

    //TODO: Config loading
}

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

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringUtil {

    public static String formatPing(int ping) {
        if (ping <= 50) {
            return "" + ChatColor.GREEN + ping;
        } else if (ping <= 100) {
            return "" + ChatColor.DARK_GREEN + ping;
        } else if (ping <= 200) {
            return "" + ChatColor.YELLOW + ping;
        } else if (ping <= 300) {
            return "" + ChatColor.GOLD + ping;
        } else if (ping <= 500) {
            return "" + ChatColor.RED + ping;
        } else {
            return "" + ChatColor.DARK_RED + ping;
        }
    }

    public static String getFormattedCoords(Location loc){
        int x = (int) loc.getX();
        int y = (int) loc.getY();
        int z = (int) loc.getZ();

        return "X: " + x + " Y: " + y + " Z: " + z;
    }

    public static String nameToTitle(String name){
        return Arrays.stream(name.split("-")).map(StringUtils::capitalize).collect(Collectors.joining(" "));
    }

}

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

package com.egirlsnation.swissknife.util;

import org.bukkit.ChatColor;

public class StringUtils {

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


}

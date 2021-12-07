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

package com.egirlsnation.swissknife.utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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

    public String getFormattedCoords(Location loc){
        int x = (int) loc.getX();
        int y = (int) loc.getY();
        int z = (int) loc.getZ();

        return "X: " + x + " Y: " + y + " Z: " + z;
    }

    public String getCoordsPlaceholderFormatted(Player player){

        String res = ChatColor.translateAlternateColorCodes('&', Config.instance.coordsReplace.replaceAll("%player_world%", player.getWorld().getName())
                .replaceAll("%player_x%", (int) player.getLocation().getX() + "")
                .replaceAll("%player_y%", (int) player.getLocation().getY() + "")
                .replaceAll("%player_z%", (int) player.getLocation().getZ() + "")
        );

        return res;
    }


}

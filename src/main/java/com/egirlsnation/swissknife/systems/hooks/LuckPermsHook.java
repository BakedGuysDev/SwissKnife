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

package com.egirlsnation.swissknife.systems.hooks;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class LuckPermsHook extends Hook{

    public LuckPermsHook(){
        super("luckperms-hook", "LuckPerms");
    }

    private LuckPerms luckPerms = null;

    @Override
    protected void initHook(){
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if(provider != null){
            luckPerms = provider.getProvider();
        }else{
            this.toggleActive();
        }
    }

    @Override
    protected void removeHook(){
        luckPerms = null;
    }

    public boolean isElderFag(Player player){
        if(!isActive()) return false;
        if(luckPerms == null) return false;
        if(!Bukkit.getOnlinePlayers().contains(player)) return false;

        User user = luckPerms.getPlayerAdapter(Player.class).getUser(player);
        return user.getPrimaryGroup().equals("legend");
    }
}

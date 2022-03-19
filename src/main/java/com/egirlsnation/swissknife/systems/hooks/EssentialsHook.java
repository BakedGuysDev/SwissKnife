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

import com.earth2me.essentials.Essentials;
import net.ess3.api.MaxMoneyException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.math.BigDecimal;

public class EssentialsHook extends Hook{


    public EssentialsHook(){
        super("essentials-hook", "Essentials");
    }

    private Essentials essentials = null;

    @Override
    protected void initHook(){
        RegisteredServiceProvider<Essentials> provider = Bukkit.getServicesManager().getRegistration(Essentials.class);
        if(provider != null){
            essentials = provider.getProvider();
        }else{
            this.toggleActive();
        }
    }

    @Override
    protected void removeHook(){
        essentials = null;
    }

    public boolean addTpaTokens(Player player, double amount){
        if(!isActive()) return false;
        if(essentials == null) return false;
        try{
            essentials.getUser(player).giveMoney(BigDecimal.valueOf(amount));
            return true;
        }catch(MaxMoneyException e){
            return false;
        }
    }
}

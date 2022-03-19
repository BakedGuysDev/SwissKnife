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

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

public class ProtocolLibHook extends Hook{

    public ProtocolLibHook(){
        super("protocollib-hook", "ProtocolLib");
    }

    public ProtocolManager protocolManager = null;

    @Override
    protected void initHook(){
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    protected void removeHook(){
        protocolManager = null;
    }
}

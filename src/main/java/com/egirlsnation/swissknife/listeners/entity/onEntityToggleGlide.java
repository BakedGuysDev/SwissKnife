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

package com.egirlsnation.swissknife.listeners.entity;

import com.egirlsnation.swissknife.utils.handlers.CombatCheckHandler;
import com.egirlsnation.swissknife.utils.handlers.customItems.DraconiteAbilityHandler;
import org.bukkit.event.Listener;

public class onEntityToggleGlide implements Listener {

    private final DraconiteAbilityHandler draconiteAbilityHandler = new DraconiteAbilityHandler();
    private final CombatCheckHandler combatCheckHandler = new CombatCheckHandler();

}

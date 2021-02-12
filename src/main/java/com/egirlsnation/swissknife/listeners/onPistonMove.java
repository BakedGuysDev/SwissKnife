package com.egirlsnation.swissknife.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;

import java.util.List;

public class onPistonMove implements Listener {

    World worldEnd = Bukkit.getServer().getWorld("world_the_end");

    @EventHandler
    public void onPistonMove(BlockPistonExtendEvent e){

    }
}

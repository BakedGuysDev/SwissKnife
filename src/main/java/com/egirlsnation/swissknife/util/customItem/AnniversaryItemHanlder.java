package com.egirlsnation.swissknife.util.customItem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class AnniversaryItemHanlder {

    public ItemStack getTpaToken(){
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList("", "Real TPA Token 4567", ""));
        meta.setDisplayName(ChatColor.RED + "Real TPA Token (Pick up to claim)");
        item.setItemMeta(meta);
        return item;
    }

    public boolean isTpaToken(ItemStack item){
        if (item == null) return false;
        if (!item.getType().equals(Material.NETHER_STAR)) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        if (!meta.hasLore()) return false;
        if (meta.getLore() == null) return false;

        return meta.getLore().contains("Real TPA Token 4567");
    }

    public ItemStack getFlesh(){
        ItemStack item = new ItemStack(Material.ROTTEN_FLESH);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList("", "One year anniversary item", "", "Thank you for playing!"));
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getBone(){
        ItemStack item = new ItemStack(Material.BONE);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList("", "One year anniversary item", "", "Thank you for playing!"));
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getString(){
        ItemStack item = new ItemStack(Material.STRING);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList("", "One year anniversary item", "", "Thank you for playing!"));
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getGunpowder(){
        ItemStack item = new ItemStack(Material.GUNPOWDER);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList("", "One year anniversary item", "", "Thank you for playing!"));
        item.setItemMeta(meta);
        return item;
    }
}

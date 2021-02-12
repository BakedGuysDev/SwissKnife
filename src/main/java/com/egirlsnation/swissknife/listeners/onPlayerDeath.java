package com.egirlsnation.swissknife.listeners;

import com.egirlsnation.swissknife.swissKnife;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public class onPlayerDeath implements Listener {

    private swissKnife swissknife = swissKnife.getInstance();
    Random r  = new Random();
    int chance;

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent e){
        Player player = e.getEntity();
        if(e.getEntity().getKiller() instanceof Player){
            String playerName = player.getName();

            if (player == null) {
                Bukkit.getLogger().info("Something fucked up. Maybe check logs.");
            } else {
                if(swissknife.getConfig().getBoolean("Heads.ancientWeaponsOnly")){
                    if(player.getKiller().getInventory().getItemInMainHand() != null && player.getKiller().getInventory().getItemInMainHand().hasItemMeta()){
                        if(player.getKiller().getInventory().getItemInMainHand().getItemMeta().getLore().contains("§6Ancient weapon")){
                            if (player.isOp()) {
                                e.getEntity().getKiller().sendMessage(ChatColor.RED + "This player can't drop a head.");
                            } else {
                                if (player.hasPermission("egirls.rank.slayer") && player.hasPermission("egirls.rank.default")) {
                                    player.sendMessage(ChatColor.RED + "Your " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "DragonSlayer" + ChatColor.RED + " protected you from dropping your skull.");

                                }else{
                                    if (player.hasPermission("egirls.rank.default") && !player.hasPermission("egirls.rank.vet") && !player.hasPermission("egirls.rank.oldfag")) {

                                        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
                                        SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
                                        meta.setOwningPlayer(player);
                                        meta.setDisplayName(ChatColor.RED + "Head of " + playerName);
                                        meta.setLore(Arrays.asList(ChatColor.GOLD + "Skull of " + e.getEntity().getDisplayName() + ChatColor.GOLD + " (" + playerName + ")", ChatColor.GOLD + "Gotten by " + e.getEntity().getKiller().getDisplayName() + ChatColor.GOLD + " (" + e.getEntity().getKiller().getName() + ")"));

                                        skull.setItemMeta(meta);

                                        Bukkit.getWorld(player.getWorld().getName()).dropItemNaturally(player.getLocation(), skull);

                                    }
                                    if (player.hasPermission("egirls.rank.vet") && !player.hasPermission("egirls.rank.oldfag")) {
                                        chance = r.nextInt(100);

                                        if (chance <= 50) {
                                            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
                                            SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
                                            meta.setOwningPlayer(player);
                                            meta.setDisplayName(ChatColor.RED + "Head of " + playerName);
                                            meta.setLore(Arrays.asList(ChatColor.GOLD + "Skull of " + e.getEntity().getDisplayName() + ChatColor.GOLD + " (" + playerName + ")", ChatColor.GOLD + "Gotten by " + e.getEntity().getKiller().getDisplayName() + ChatColor.GOLD + " (" + e.getEntity().getKiller().getName() + ")"));

                                            skull.setItemMeta(meta);

                                            Bukkit.getWorld(player.getWorld().getName()).dropItemNaturally(player.getLocation(), skull);
                                            e.getEntity().getKiller().sendMessage(ChatColor.RED + "Player " + player.getDisplayName() + ChatColor.RED + " dropped their head.");
                                            e.getEntity().getKiller().playSound(e.getEntity().getKiller().getLocation(), Sound.ENTITY_VILLAGER_HURT, 1, 0);
                                            Bukkit.getServer().broadcastMessage(e.getEntity().getKiller().getDisplayName() + ChatColor.RED +  " established their presence over " + player.getDisplayName() + ChatColor.RED + " and got their head.");
                                        }

                                    }
                                    if (player.hasPermission("egirls.rank.oldfag") && player.hasPermission("egirls.rank.vet")) {
                                        chance = r.nextInt(100);

                                        if (chance <= 25) {
                                            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
                                            SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
                                            meta.setOwningPlayer(player);
                                            meta.setDisplayName(ChatColor.RED + "Head of " + playerName);
                                            meta.setLore(Arrays.asList(ChatColor.GOLD + "Skull of " + e.getEntity().getDisplayName() + ChatColor.GOLD + " (" + playerName + ")", ChatColor.GOLD + "Gotten by " + e.getEntity().getKiller().getDisplayName() + ChatColor.GOLD + " (" + e.getEntity().getKiller().getName() + ")"));

                                            skull.setItemMeta(meta);

                                            Bukkit.getWorld(player.getWorld().getName()).dropItemNaturally(player.getLocation(), skull);
                                            e.getEntity().getKiller().sendMessage(ChatColor.RED + "Player " + player.getDisplayName() + ChatColor.RED + " dropped their head.");
                                            e.getEntity().getKiller().playSound(e.getEntity().getKiller().getLocation(), Sound.ENTITY_VILLAGER_HURT, 1, 0);
                                            Bukkit.getServer().broadcastMessage(e.getEntity().getKiller().getDisplayName() + ChatColor.RED +  " established their presence over " + player.getDisplayName() + ChatColor.RED + " and got their head.");
                                        }
                                    }
                                }
                            }

                        }
                        if(player.getKiller().getInventory().getItemInMainHand().getItemMeta().getLore() != null){

                        }else if(player.getKiller().getInventory().getItemInMainHand().getItemMeta().getLore() == null){
                            return;
                        }else{
                            Bukkit.getLogger().severe("Something went massively wrong. Check the logs for more info.");
                        }
                    }
                }else if(!swissknife.getConfig().getBoolean("Heads.ancientWeaponsOnly")){
                    if (player.isOp()) {

                    } else{
                        if (player.hasPermission("egirls.rank.slayer") && player.hasPermission("egirls.rank.default")) {
                            player.sendMessage(ChatColor.RED + "Your " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "DragonSlayer" + ChatColor.RED + " protected you from dropping your skull.");

                        }else{
                            if (player.hasPermission("egirls.rank.default") && !player.hasPermission("egirls.rank.vet") && !player.hasPermission("egirls.rank.oldfag")) {

                                ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
                                SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
                                meta.setOwningPlayer(player);
                                meta.setDisplayName(ChatColor.RED + "Head of " + playerName);
                                meta.setLore(Arrays.asList(ChatColor.GOLD + "Skull of " + e.getEntity().getDisplayName() + ChatColor.GOLD + " (" + playerName + ")", ChatColor.GOLD + "Gotten by " + e.getEntity().getKiller().getDisplayName() + ChatColor.GOLD + " (" + e.getEntity().getKiller().getName() + ")"));

                                skull.setItemMeta(meta);

                                Bukkit.getWorld(player.getWorld().getName()).dropItemNaturally(player.getLocation(), skull);

                            }
                            if (player.hasPermission("egirls.rank.vet") && !player.hasPermission("egirls.rank.oldfag")) {
                                chance = r.nextInt(100);

                                if (chance <= 50) {
                                    ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
                                    SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
                                    meta.setOwningPlayer(player);
                                    meta.setDisplayName(ChatColor.RED + "Head of " + playerName);
                                    meta.setLore(Arrays.asList(ChatColor.GOLD + "Skull of " + e.getEntity().getDisplayName() + ChatColor.GOLD + " (" + playerName + ")", ChatColor.GOLD + "Gotten by " + e.getEntity().getKiller().getDisplayName() + ChatColor.GOLD + " (" + e.getEntity().getKiller().getName() + ")"));

                                    skull.setItemMeta(meta);

                                    Bukkit.getWorld(player.getWorld().getName()).dropItemNaturally(player.getLocation(), skull);
                                    e.getEntity().getKiller().sendMessage(ChatColor.RED + "Player " + player.getDisplayName() + ChatColor.RED + " dropped their head.");
                                    e.getEntity().getKiller().playSound(e.getEntity().getKiller().getLocation(), Sound.ENTITY_VILLAGER_HURT, 1, 0);
                                    Bukkit.getServer().broadcastMessage(e.getEntity().getKiller().getDisplayName() + ChatColor.RED +  " established their presence over " + player.getDisplayName() + ChatColor.RED + " and got their head.");
                                }

                            }
                            if (player.hasPermission("egirls.rank.oldfag") && player.hasPermission("egirls.rank.vet")) {
                                chance = r.nextInt(100);

                                if (chance <= 25) {
                                    ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
                                    SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
                                    meta.setOwningPlayer(player);
                                    meta.setDisplayName(ChatColor.RED + "Head of " + playerName);
                                    meta.setLore(Arrays.asList(ChatColor.GOLD + "Skull of " + e.getEntity().getDisplayName() + ChatColor.GOLD + " (" + playerName + ")", ChatColor.GOLD + "Gotten by " + e.getEntity().getKiller().getDisplayName() + ChatColor.GOLD + " (" + e.getEntity().getKiller().getName() + ")"));

                                    skull.setItemMeta(meta);

                                    Bukkit.getWorld(player.getWorld().getName()).dropItemNaturally(player.getLocation(), skull);
                                    e.getEntity().getKiller().sendMessage(ChatColor.RED + "Player " + player.getDisplayName() + ChatColor.RED + " dropped their head.");
                                    e.getEntity().getKiller().playSound(e.getEntity().getKiller().getLocation(), Sound.ENTITY_VILLAGER_HURT, 1, 0);
                                    Bukkit.getServer().broadcastMessage(e.getEntity().getKiller().getDisplayName() + ChatColor.RED +  " established their presence over " + player.getDisplayName() + ChatColor.RED + " and got their head.");
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}

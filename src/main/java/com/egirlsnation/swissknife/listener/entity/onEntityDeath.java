package com.egirlsnation.swissknife.listener.entity;

import com.egirlsnation.swissknife.util.customItem.AnniversaryItemHanlder;
import com.egirlsnation.swissknife.util.customItem.CustomItemHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Random;

import static com.egirlsnation.swissknife.SwissKnife.Config.anniversaryItems;

public class onEntityDeath implements Listener {

    private final CustomItemHandler customItemHandler = new CustomItemHandler();
    private final AnniversaryItemHanlder anniversaryItemHanlder = new AnniversaryItemHanlder();

    @EventHandler
    private void EntityDeath(EntityDeathEvent e){

        final Random random = new Random();

        int chance = random.nextInt(100) + 1;
        switch(e.getEntityType()){
            case ENDERMAN:{
                handleEndermanDrops(e.getEntity(), e.getEntity().getKiller(), chance);
            }break;

            case ZOMBIE:{
                if(anniversaryItems){
                    handleZombieDrops(e.getEntity(), e.getEntity().getKiller(), chance);
                }
            }break;

            case SKELETON:{
                if(anniversaryItems){
                    handleSkeletonDrops(e.getEntity(), e.getEntity().getKiller(), chance);
                }
            }break;

            case CREEPER:{
                if(anniversaryItems){
                    handleCreeperDrops(e.getEntity(), e.getEntity().getKiller(), chance);
                }
            }break;

            case WITHER:{
                if(anniversaryItems){
                    handleWitherDrops(e.getEntity(), e.getEntity().getKiller(), chance);
                }
            }break;

            case SPIDER:{
                if(anniversaryItems){
                    handleSpiderDrops(e.getEntity(), e.getEntity().getKiller(), chance);
                }
            }break;
            case EVOKER:{
                handleEvokerDrops(e.getEntity(), e.getEntity().getKiller(), chance);
            }
            case SHULKER:{
                handleShulkerDrops(e.getEntity(), e.getEntity().getKiller(), chance);
            }
        }
    }

    private void handleEndermanDrops(Entity entity, Player player, int chance){
        if(chance > 3) return;
        entity.getWorld().dropItemNaturally(entity.getLocation(), customItemHandler.getDraconiteCrystal());
        if(player == null) return;
        broadcastFoundMessage(player, "a " + ChatColor.RED + "Draconite Crystal");


    }

    private void handleZombieDrops(Entity entity, Player player, int chance){
        if(chance > 30) return;
        entity.getWorld().dropItemNaturally(entity.getLocation(), anniversaryItemHanlder.getFlesh());
        if(chance < 5){
            addTpaToken(player);
        }

    }

    private void handleSkeletonDrops(Entity entity, Player player, int chance){
        if(chance > 30) return;
        entity.getWorld().dropItemNaturally(entity.getLocation(), anniversaryItemHanlder.getBone());
        if(chance < 5){
            addTpaToken(player);
        }
    }

    private void handleCreeperDrops(Entity entity, Player player, int chance){
        if(chance > 30) return;
        entity.getWorld().dropItemNaturally(entity.getLocation(), anniversaryItemHanlder.getGunpowder());
        if(chance < 5){
            addTpaToken(player);
        }
    }

    private void handleWitherDrops(Entity entity, Player player, int chance){
        if(chance > 20) return;
        addTpaToken(player);

    }

    private void handleSpiderDrops(Entity entity, Player player, int chance){
        if(chance > 30) return;
        entity.getWorld().dropItemNaturally(entity.getLocation(), anniversaryItemHanlder.getString());
        if(chance < 5){
            addTpaToken(player);
        }
    }

    private void handleEvokerDrops(Entity entity, Player player, int chance){
        if(chance >= 3) return;
        entity.getWorld().dropItemNaturally(entity.getLocation(), customItemHandler.getPopbobTotem());
        if(player == null) return;
        broadcastFoundMessage(player, "the " + ChatColor.RED + "Totem of Popbob");

    }

    private void handleShulkerDrops(Entity entity, Player player, int chance){
        if(chance > 5) return;
        if(chance > 3){
            entity.getWorld().dropItemNaturally(entity.getLocation(), customItemHandler.getDraconiteAxe());
            if(player == null) return;
            broadcastFoundMessage(player, "a " + ChatColor.RED + "Draconite Axe");
        }else{
            entity.getWorld().dropItemNaturally(entity.getLocation(), customItemHandler.getDraconiteSword());
            if(player == null) return;
            broadcastFoundMessage(player, "a " + ChatColor.RED + "Draconite Sword");
        }
    }

    private void broadcastFoundMessage(Player player, String itemName){
         Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "[" + ChatColor.AQUA + "EVENT" + ChatColor.GREEN + "] " + player.getDisplayName() + ChatColor.GREEN + " has found " + itemName);
    }

    private void addTpaToken(Player player){
        if(player == null) return;
        final ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        String command = "eco give " + player.getName() + " 1";
        Bukkit.dispatchCommand(console, command);
    }
}

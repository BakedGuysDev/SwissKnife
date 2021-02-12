package com.egirlsnation.swissknife.listeners;

import com.google.common.base.CharMatcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;

import java.awt.print.Book;
import java.util.List;

public class onBookClose implements Listener {

    @EventHandler
    public void onBookEdit (PlayerEditBookEvent e){
        Player player = e.getPlayer();
        Bukkit.getLogger().info("Book event fired.");
        if(e.getPreviousBookMeta() != null){
            BookMeta previousBookMeta = e.getPreviousBookMeta();
            Bukkit.getLogger().info("Previous book meta not null");
            List<String> previousText = previousBookMeta.getPages();
            for(String string : previousText){
                Bukkit.getLogger().info("Looping through book");
                if(!CharMatcher.ascii().matchesAllOf(string)){
                    Bukkit.getLogger().info("Book contains non-ascii chars.");
                    BookMeta sykeMeta = previousBookMeta;
                    sykeMeta.setPages("M8... Use only ASCII character.");
                    sykeMeta.setAuthor("E");
                    sykeMeta.setTitle("No");
                    e.setNewBookMeta(sykeMeta);
                    e.setSigning(true);
                }
            }
        }

        if(e.getNewBookMeta() != null){
            BookMeta newBookMeta = e.getNewBookMeta();
            List<String> previousText = newBookMeta.getPages();
            for(String string : previousText){
                if(!CharMatcher.ascii().matchesAllOf(string)){
                    BookMeta sykeMeta = newBookMeta;
                    sykeMeta.setPages("M8... Use only ASCII character.");
                    sykeMeta.setAuthor("E");
                    sykeMeta.setTitle("No");
                    e.setNewBookMeta(sykeMeta);
                    e.setSigning(true);
                    player.sendMessage(ChatColor.RED + "Your book contained non-ascii characters and was removed.");
                    Bukkit.getLogger().warning("User " + player.getName() + " tried to sing a book with non-ascii characters at coords: "
                            + player.getLocation());
                }
            }
        }
    }
}

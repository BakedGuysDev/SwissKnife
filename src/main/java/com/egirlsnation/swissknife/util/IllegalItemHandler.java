package com.egirlsnation.swissknife.util;

import com.google.common.collect.Multimap;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

import static com.egirlsnation.swissknife.SwissKnife.Config.*;

public class IllegalItemHandler {


    public boolean isOverEnchanted(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return false;
        if(!meta.hasEnchants()) return false;

        Map<Enchantment, Integer> enchantMap = meta.getEnchants();
        for(Map.Entry<Enchantment, Integer> enchant: enchantMap.entrySet()){
            if(enchant.getValue() > maxEnchantLevel) return true;
        }
        return false;
    }

    public boolean hasIllegalLore(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return false;
        if(!meta.hasLore()) return false;
        if(meta.getLore() == null) return false;

        return meta.getLore().stream().anyMatch(element -> illegalLoreList.contains(element));
    }

    public ItemMeta reduceAncientWeaponMeta(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;
        if(!meta.hasLore()) return null;
        if(meta.getLore() == null) return null;

        if(!meta.getLore().contains("§6Ancient weapon")) return null;

        Multimap<Attribute, AttributeModifier> modifierMap = meta.getAttributeModifiers();
        // In case something weird happens and the ancient weapon has just lore and no modifiers
        if(modifierMap == null) return null;
        //Removing the Attack speed attribute from the meta
        if(modifierMap.containsKey(Attribute.GENERIC_ATTACK_SPEED)){
            meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED);
        }
        //Return the new meta
        return meta;
    }

    public boolean handleIfSpawnEgg(ItemStack item){
        if(item == null) return false;
        if(item.getType().toString().matches("[A-Z]*?_?[A-Z]*_SPAWN_EGG")) return false;
        item.setAmount(0);
        return true;
    }

    public boolean handleIllegals(Item item, Player player){
        if(item == null) return false;

        ItemStack itemStack = item.getItemStack();
        if(hasIllegalLore(itemStack)){
            item.remove();
            player.sendMessage(ChatColor.RED + "Nope m8.\nhttps://youtu.be/otCpCn0l4Wo?t=15");
            return true;
        }

        if(isOverEnchanted(itemStack)){
            item.remove();
            player.sendMessage(ChatColor.RED + "Nope m8.\nhttps://youtu.be/otCpCn0l4Wo?t=15");
            return true;
        }

        if(itemStack.getType().equals(Material.TOTEM_OF_UNDYING) && itemStack.getAmount() != maxTotemStack){
            itemStack.setAmount(1);
            item.setItemStack(itemStack);
            return false;
        }

        ItemMeta ancientMeta = reduceAncientWeaponMeta(itemStack);
        if(ancientMeta != null){
            itemStack.setItemMeta(ancientMeta);
            item.setItemStack(itemStack);
            return false;
        }
        return false;
    }

    public boolean handleIllegals(ItemStack item, Player player){
        if(item == null) return false;

        if(hasIllegalLore(item)){
            item.setAmount(0);
            player.sendMessage(ChatColor.RED + "Nope m8.\nhttps://youtu.be/otCpCn0l4Wo?t=15");
            return true;
        }

        if(isOverEnchanted(item)){
            item.setAmount(0);
            player.sendMessage(ChatColor.RED + "Nope m8.\nhttps://youtu.be/otCpCn0l4Wo?t=15");
            return true;
        }

        if(item.getType().equals(Material.TOTEM_OF_UNDYING) && item.getAmount() != maxTotemStack){
            item.setAmount(1);
            return false;
        }

        ItemMeta ancientMeta = reduceAncientWeaponMeta(item);
        if(ancientMeta != null){
            item.setItemMeta(ancientMeta);
            return false;
        }

        return false;
    }

    public boolean handleIllegals(ItemStack item){
        if(item == null) return false;

        if(hasIllegalLore(item)){
            item.setAmount(0);
            return true;
        }

        if(isOverEnchanted(item)){
            item.setAmount(0);
            return true;
        }

        if(item.getType().equals(Material.TOTEM_OF_UNDYING) && item.getAmount() != maxTotemStack){
            item.setAmount(1);
            return false;
        }

        ItemMeta ancientMeta = reduceAncientWeaponMeta(item);
        if(ancientMeta != null){
            item.setItemMeta(ancientMeta);
            return false;
        }

        return false;
    }

    public ItemStack getIllegalItemReplacement(){
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta meta = paper.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "I fucked ya mom");
        paper.setItemMeta(meta);
        return paper;
    }


}

package com.egirlsnation.swissknife.service;

import com.google.common.collect.Multimap;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.UUID;

import static com.egirlsnation.swissknife.swissKnife.allDisabled;

public class illegalsPatchService {

    public static boolean echantLevelCheck (ItemStack item){
        if(allDisabled){
            return false;
        }else{
            ItemMeta meta = item.getItemMeta();
            if(meta != null){
                if(!meta.hasEnchants()){
                    return false;
                }else{
                    Map<Enchantment, Integer> enchantMap = meta.getEnchants();
                    for(Map.Entry<Enchantment, Integer> enchant:enchantMap.entrySet()){
                        if(enchant.getValue() > 1000){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean illegalItemLoreCheck (ItemStack item){
        if(allDisabled){
            return false;
        }else{
            ItemMeta meta = item.getItemMeta();
            if(meta != null){
                if(!meta.hasLore()){
                    return false;
                }else{
                    if(meta.getLore() != null){
                        if(meta.getLore().contains("§9§lBig Dick Energy X")){
                            return true;
                        }else if(meta.getLore().contains("§cCurse of Simping")){
                            return true;
                        }else{
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static ItemMeta ancientWeaponReduce (ItemStack item){
        ItemMeta meta = item.getItemMeta();
        if(meta != null){
            if(!meta.hasLore()){
                return null;
            }else{
                if(meta.getLore() != null){
                    if(meta.getLore().contains("§6Ancient weapon")){
                        Multimap<Attribute, AttributeModifier> modifiers = meta.getAttributeModifiers();
                        if(modifiers != null){
                            if(modifiers.containsKey(Attribute.GENERIC_ATTACK_SPEED)){

                                meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED);
                                AttributeModifier newAttackSpeed = new AttributeModifier(UUID.randomUUID(), "attack_speed", 0.2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
                                meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, newAttackSpeed);




                                /*Multimap<Attribute, AttributeModifier> newModifiers = MultimapBuilder.hashKeys().arrayListValues().build(modifiers);
                                newModifiers.removeAll(Attribute.GENERIC_ATTACK_SPEED);
                                newModifiers.put(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(uuid, "attack_speed", 1.8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
                                meta.setAttributeModifiers(newModifiers);*/
                                return meta;

                            }


                        }
                    }else{
                        return null;
                    }
                }
            }
        }
        return null;
    }
}

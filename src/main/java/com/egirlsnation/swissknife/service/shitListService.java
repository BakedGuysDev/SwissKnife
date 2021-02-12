package com.egirlsnation.swissknife.service;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

import static com.egirlsnation.swissknife.swissKnife.shitlist;

public class shitListService {

    public static String isShitListed (Player player){
        if(shitlist.containsKey(player.getUniqueId().toString())){
            String violations = shitlist.get(player.getUniqueId().toString());
            return violations;
        }else{
            return "none";
        }
    }

    public static String addToShitList(Player player, String string){
        if(shitlist.containsKey(player.getUniqueId().toString())){
            String violations = shitlist.get(player.getUniqueId().toString());
            switch (string) {
                case "both":
                    switch (violations) {
                        case "both":
                            return "This user is already shitlisted for both.";
                        case "tpa":
                            shitlist.replace(player.getUniqueId().toString(), "both");
                            return "This user was shitlisted for tpa. Now it also for coords.";
                        case "coords":
                            shitlist.replace(player.getUniqueId().toString(), "both");
                            return "This user was shitlisted for coords. Now is also for tpa.";
                        default:
                            return "Sumtin went wrong.";
                    }
                case "tpa":
                    switch (violations) {
                        case "both":
                            return "This user is already shitlisted for both.";
                        case "tpa":
                            return "This user was already shitlisted for tpa.";
                        case "coords":
                            shitlist.replace(player.getUniqueId().toString(), "both");
                            return "This user was shitlisted for coords. Now is also for tpa.";
                        default:
                            return "Sumtin went wrong.";
                    }
                case "coords":
                    switch (violations) {
                        case "both":
                            return "This user is already shitlisted for both.";
                        case "tpa":
                            shitlist.replace(player.getUniqueId().toString(), "both");
                            return "This user was already shitlisted for tpa. Now is also for coords.";
                        case "coords":
                            return "This user is already shitlisted for coords.";
                        default:
                            return "Sumtin went wrong.";
                    }
                default:
                    return "Something went wrong.";
            }
        }else{
            switch (string) {
                case "both":
                    shitlist.put(player.getUniqueId().toString(), "both");
                    return "Shitlisted user for both.";
                case "tpa":
                    shitlist.put(player.getUniqueId().toString(), "tpa");
                    return "Shitlisted user for tpa.";
                case "coords":
                    shitlist.put(player.getUniqueId().toString(), "coords");
                    return "Shitlisted user for coords.";
                default:
                    return "Something went wrong.";
            }
        }
    }

    public static String removeFromShitList(Player player, String string){
        switch (string) {
            case "both":
                if (shitlist.containsKey(player.getUniqueId().toString())) {
                    shitlist.remove(player.getUniqueId().toString());
                    return "Both violations have been removed.";
                } else {
                    return "This user isn't on the shit-list.";
                }
            case "tpa":
                if (shitlist.containsKey(player.getUniqueId().toString())) {
                    String violations = shitlist.get(player.getUniqueId().toString());
                    switch (violations) {
                        case "both":
                            shitlist.replace(player.getUniqueId().toString(), "coords");
                            return "The tpa violation has been removed. Player still is on the shitlist with coords";
                        case "tpa":
                            shitlist.remove(player.getUniqueId().toString());
                            return "The tpa violation has been removed.";
                        case "coords":
                            return "This user is on the shitlist, but only with coords.";
                    }
                } else {
                    return "This user isn't on the shitlist.";
                }

                break;
            case "coords":
                if (shitlist.containsKey(player.getUniqueId().toString())) {
                    String violations = shitlist.get(player.getUniqueId().toString());
                    switch (violations) {
                        case "both":
                            shitlist.replace(player.getUniqueId().toString(), "tpa");
                            return "The coords violation has been removed. Player still is on the shitlist with tpa";
                        case "coords":
                            shitlist.remove(player.getUniqueId().toString());
                            return "The tpa violation has been removed.";
                        case "tpa":
                            return "This user is on the shitlist, but only with coords.";
                    }
                } else {
                    return "This user isn't on the shitlist.";
                }
                break;
        }
        return "Something went wrong.";
    }
}

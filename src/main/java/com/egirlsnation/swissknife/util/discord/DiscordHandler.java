package com.egirlsnation.swissknife.util.discord;

import com.egirlsnation.swissknife.util.player.RankUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.egirlsnation.swissknife.SwissKnife.Config.*;

public class DiscordHandler {

    private static int tpsArrSize;

    private static long lastAlertTime;

    private static String webhookURL = "";

    private final RankUtil rankUtil = new RankUtil();

    public boolean shouldPostAlert(List<Double> tps){
        if(tps.get(0) <= tpsNotifyAlways){
            return true;
        }
        if(tpsArrSize == 3){
            if(longerTPSavg && tps.get(3) <= tpsAvgThreshold){
                return true;
            }else if(!longerTPSavg && tps.get(2) <= tpsAvgThreshold){
                return true;
            }
        }else{
            if(longerTPSavg && tps.get(4) <= tpsAvgThreshold){
                return true;
            }else if(!longerTPSavg && tps.get(3) <= tpsAvgThreshold){
                return true;
            }
        }
        return false;
    }

    public void setTpsArrSize(int size){
        tpsArrSize = size;
    }

    public void setWebhookURL(String URL){
        webhookURL = URL;
    }

    public void postDiscordTPSNotif(@NotNull List<Double> tps, List<String> playerDisplayNames, List<String> lowPtNames) throws IOException {
        DiscordWebhook webhook = new DiscordWebhook(webhookURL);
        if(webhookName.isEmpty()){
            webhook.setUsername("TPS Alert");
        }else{
            webhook.setUsername(webhookName);
        }
        if(!webhookAvatarURL.isBlank()){
            webhook.setAvatarUrl(webhookAvatarURL);
        }
        DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject()
                .setTitle(":warning: Low TPS")
                .setDescription("Please keep in mind that this feature is still in testing.");
        if(tpsArrSize == 3){
            embed.addField("TPS (1m, 5m, 15m)", tps.stream().map(Object::toString).collect(Collectors.joining(", ")), false );
        }else{
            embed.addField("TPS (5s, 1m, 5m, 15m", tps.stream().map(Object::toString).collect(Collectors.joining(", ")), false);
        }
        if(playerDisplayNames != null){
            embed.addField("Players", playerDisplayNames.stream().map(Objects::toString).collect(Collectors.joining(", ")), false );
        }
        if(lowPtNames != null){
            embed.addField("Low Playtime Players", lowPtNames.stream().map(Objects::toString).collect(Collectors.joining(", ")), false );
        }
        webhook.execute();
    }
}

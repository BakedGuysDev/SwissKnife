package com.egirlsnation.swissknife.util.player;

import com.egirlsnation.swissknife.sql.MySQL;
import com.egirlsnation.swissknife.sql.SqlQuery;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PingUtil {


    public int getPing(Player p) {
        String v = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        if (!p.getClass().getName().equals("org.bukkit.craftbukkit." + v + ".entity.CraftPlayer")) { //compatibility with some plugins
            p = Bukkit.getPlayer(p.getUniqueId()); //cast to org.bukkit.entity.Player
        }
        try {
            Class<?> CraftPlayerClass = Class.forName("org.bukkit.craftbukkit." + v + ".entity.CraftPlayer");
            Object CraftPlayer = CraftPlayerClass.cast(p);
            Method getHandle = CraftPlayer.getClass().getMethod("getHandle");
            Object EntityPlayer = getHandle.invoke(CraftPlayer);
            Field ping = EntityPlayer.getClass().getDeclaredField("ping");
            return ping.getInt(EntityPlayer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Map<PlayerInfo, Integer> getAllPings(){
        Map<PlayerInfo, Integer> pingMap = new HashMap<>();
        for (Player p : Bukkit.getOnlinePlayers()){
            pingMap.put(new PlayerInfo(p.getUniqueId(), p.getName()), getPing(p));
        }
        return pingMap;
    }

    public void uploadPingMap(Map<PlayerInfo, Integer> pingMap, MySQL SQL, SqlQuery sqlQuery){

        if(pingMap.isEmpty()) return;

        if(!SQL.isConnected()){
            //Bukkit.getLogger().warning("MySQL database isn't connected. Pings won't be saved into the database!");
            return;
        }

        for(Map.Entry<PlayerInfo, Integer> entry : pingMap.entrySet()){
            sqlQuery.addPingRecord(entry.getKey(), entry.getValue());
        }
    }

}

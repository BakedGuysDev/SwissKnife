package com.egirlsnation.swissknife.sql;

import com.egirlsnation.swissknife.swissKnife;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static com.egirlsnation.swissknife.swissKnife.shitlist;

public class sqlQueryService {

    private final swissKnife plugin;
    public sqlQueryService(swissKnife plugin){
        this.plugin = plugin;
    }

    public void createTable(){
        PreparedStatement ps;
        try{
            ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS playerStats "
                    + "(Name VARCHAR(32),UUID CHAR(36),playTime INT(11),kills INT(11),deaths INT(11),mobKills INT(11),shitlisted BIT,firstPlayed VARCHAR(100),PRIMARY KEY (Name))");
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createPlayer(Player player){
        try{
            UUID uuid = player.getUniqueId();
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM playerStats WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            if(!exists(uuid)){

                Date date = new Date(player.getFirstPlayed());
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                String firstPlayed = sdf.format(date);

                PreparedStatement ps2 = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO playerStats"
                        + " (Name,UUID,playTime,kills,deaths,mobKills,shitlisted,firstPlayed) VALUES (?,?,?,?,?,?,?,?)");
                ps2.setString(1, player.getName());
                ps2.setString(2, uuid.toString());
                ps2.setInt(3, 0);
                ps2.setInt(4, 0);
                ps2.setInt(5, 0);
                ps2.setInt(6, 0);
                ps2.setBoolean(7, false);
                ps2.setString(8, firstPlayed);

                ps2.executeUpdate();

                return;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean exists(UUID uuid){
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM playerStats WHERE UUID=?");
            ps.setString(1, uuid.toString());

            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                return true;
            }
            return false;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public void updateValues(Player player){
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE playerStats SET playTime=?,kills=?,deaths=?,mobKills=?,shitlisted=? WHERE UUID=?");
            ps.setInt(1, player.getStatistic(Statistic.PLAY_ONE_MINUTE));
            ps.setInt(2, player.getStatistic(Statistic.PLAYER_KILLS));
            ps.setInt(3, player.getStatistic(Statistic.DEATHS));
            ps.setInt(4, player.getStatistic(Statistic.MOB_KILLS));
            ps.setBoolean(5, shitlist.containsKey(player.getUniqueId().toString()));

            ps.setString(6, player.getUniqueId().toString());
            if (!exists(player.getUniqueId())) {
                createPlayer(player);
            }
            ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}

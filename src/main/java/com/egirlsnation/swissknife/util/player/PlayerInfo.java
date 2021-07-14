package com.egirlsnation.swissknife.util.player;

import java.util.UUID;

public class PlayerInfo {
    private final UUID uuid;
    private final String name;

    public PlayerInfo(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }


    public UUID getUuid() {
        return uuid;
    }

    public String getName(){
        return name;
    }
}

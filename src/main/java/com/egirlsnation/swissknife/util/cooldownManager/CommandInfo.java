package com.egirlsnation.swissknife.util.cooldownManager;

public class CommandInfo {
    private final CommandType command;
    private final long cooldown;

    public CommandInfo(long cd, CommandType cmd) {
        cooldown = cd;
        command = cmd;
    }


    public CommandType getCommand() {
        return command;
    }

    public long getCooldown(){
        return cooldown;
    }
}

package com.gmail.justdontdiebusiness.command;

import com.gmail.justdontdiebusiness.Factions;
import com.gmail.justdontdiebusiness.faction.player.FPlayer;
import org.bukkit.entity.Player;

public interface ICommand
{
    String getName();

    String getPermission();

    Factions getPlugin();

    void execute(Player player, FPlayer fPlayer, String[] args);
}

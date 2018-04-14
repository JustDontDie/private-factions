package com.gmail.justdontdiebusiness.command.commands;

import com.gmail.justdontdiebusiness.Factions;
import com.gmail.justdontdiebusiness.command.commands.Command;
import com.gmail.justdontdiebusiness.faction.player.FPlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CreateFactionCommand extends Command
{
    private char[] symbols;

    public CreateFactionCommand(Factions plugin)
    {
        super("create", "factions.all", plugin);

        this.symbols = new char[] {'!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '+', '=', '{', '[', '}', ']', '|', '\\', ':', ';', '"', '\'', '<', ',', '>', '.', '?', '/', '`', '~'};
    }

    @Override
    public void execute(Player player, FPlayer fPlayer, String[] args)
    {
        if (args.length == 2)
        {
            if (fPlayer.isInFaction())
            {
                player.sendMessage(ChatColor.RED + "You're already in a faction!");
                return;
            }

            String name = args[1];

            if (name.length() > 16)
            {
                player.sendMessage(ChatColor.RED + "Name is too long!");
                return;
            }

            if (StringUtils.containsAny(name, this.symbols))
            {
                player.sendMessage(ChatColor.RED + "Name cannot contain symbols!");
                return;
            }

            if (getPlugin().getFactionManager().getFactionByName(name) != null)
            {
                player.sendMessage(ChatColor.RED + "There is already a faction with that name!");
                return;
            }

            getPlugin().getFactionManager().createFaction(fPlayer, name);
            Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + " has created the faction '" + name + "'!");
            player.sendMessage(ChatColor.GREEN + "Created faction under the name of '" + name + "'!");
        }
        else
        {
            player.sendMessage(ChatColor.RED + "Invalid args! /faction create <name>");
        }
    }
}

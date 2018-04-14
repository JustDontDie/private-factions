package com.gmail.justdontdiebusiness.command.commands;

import com.gmail.justdontdiebusiness.Factions;
import com.gmail.justdontdiebusiness.command.commands.Command;
import com.gmail.justdontdiebusiness.faction.Faction;
import com.gmail.justdontdiebusiness.faction.player.FPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class JoinFactionCommand extends Command
{
    public JoinFactionCommand(Factions plugin)
    {
        super("join", "factions.all", plugin);
    }

    @Override
    public void execute(Player player, FPlayer fPlayer, String[] args)
    {
        if (args.length > 2)
        {
            if (fPlayer.getFaction() != null)
            {
                player.sendMessage(ChatColor.RED + "You're already in a faction!");
                return;
            }

            Faction faction = getPlugin().getFactionManager().getFactionByName(args[1]);

            if (faction == null)
            {
                player.sendMessage(ChatColor.RED + "That faction does not exist!");
                return;
            }

            if (faction.getStatus() == Faction.Status.PRIVATE)
            {
                if (!faction.getInvites().containsKey(player.getUniqueId()))
                {
                    player.sendMessage(ChatColor.RED + "You have not been invited to this faction!");
                    return;
                }
            }

            faction.join(fPlayer);

            for (FPlayer members : faction.getAllMembers())
            {
                if (members == null || members.getPlayer() == null) continue;

                members.getPlayer().sendMessage(ChatColor.GREEN + player.getName() + " has joined the faction!");
            }

            player.sendMessage("You have joined " + faction.getName() + "!");
        }
        else
        {
            player.sendMessage(ChatColor.RED + "Invalid args! /faction join <faction name>");
        }
    }
}

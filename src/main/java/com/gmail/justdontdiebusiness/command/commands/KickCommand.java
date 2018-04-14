package com.gmail.justdontdiebusiness.command.commands;

import com.gmail.justdontdiebusiness.Factions;
import com.gmail.justdontdiebusiness.faction.Faction;
import com.gmail.justdontdiebusiness.faction.player.FPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KickCommand extends Command
{
    public KickCommand(Factions plugin)
    {
        super("kick", "factions.all", plugin);
    }

    @Override
    public void execute(Player player, FPlayer fPlayer, String[] args)
    {
        if (args.length > 2)
        {
            if (fPlayer.getRole().ordinal() < Faction.Role.OFFICER.ordinal())
            {
                player.sendMessage(ChatColor.RED + "You must be Officer+ in order to kick members!");
                return;
            }

            FPlayer fOther = fPlayer.getFaction().getMember(args[1]);

            if (fOther == null)
            {
                player.sendMessage(ChatColor.RED + args[1] + " is not in the faction!");
                return;
            }

            if (fOther.getRole().ordinal() > fPlayer.getRole().ordinal())
            {
                player.sendMessage(ChatColor.RED + "You cannot kick members higher than you!");
                return;
            }

            fPlayer.getFaction().leaveOrKick(fOther);

            for (FPlayer members : fPlayer.getFaction().getAllMembers())
            {
                if (members == null || members.getPlayer() == null) continue;

                members.getPlayer().sendMessage(ChatColor.GREEN + fOther.getName() + " has been kicked from the faction!");
            }

            player.sendMessage("Successfully kicked " + fOther.getName() + " from the faction!");
        }
        else
        {
            player.sendMessage(ChatColor.RED + "Invalid args! /faction kick <name>");
        }
    }
}

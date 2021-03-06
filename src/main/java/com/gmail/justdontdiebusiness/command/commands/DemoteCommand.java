package com.gmail.justdontdiebusiness.command.commands;

import com.gmail.justdontdiebusiness.Factions;
import com.gmail.justdontdiebusiness.faction.Faction;
import com.gmail.justdontdiebusiness.faction.player.FPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DemoteCommand extends Command
{
    public DemoteCommand(Factions plugin)
    {
        super("demote", "factions.all", plugin);
    }

    @Override
    public void execute(Player player, FPlayer fPlayer, String[] args)
    {
        if (args.length == 2)
        {
            if (fPlayer.getFaction() == null)
            {
                player.sendMessage(ChatColor.RED + "Can't demote players if you're not in a faction :P");
                return;
            }

            if (fPlayer.getRole() != Faction.Role.OWNER)
            {
                player.sendMessage(ChatColor.RED + "You must be the owner of the faction in order to demote players!");
                return;
            }

            FPlayer target = getPlugin().getPlayerManager().getPlayer(args[1]);

            if (target == null)
            {
                player.sendMessage(ChatColor.RED + "Cannot find " + args[1] + "! Maybe they're offline or have never joined?");
                return;
            }

            if (target.getFaction() == null)
            {
                player.sendMessage(ChatColor.RED + target.getName() + "is not in a faction!");
                return;
            }

            if (!target.getFaction().getName().equals(fPlayer.getFaction().getName()))
            {
                player.sendMessage(ChatColor.RED + target.getName() + "is not in the faction!");
                return;
            }

            if (fPlayer.getFaction().demote(target))
            {
                for (FPlayer members : fPlayer.getFaction().getAllMembers())
                {
                    if (members.getPlayer() == null) continue;

                    members.getPlayer().sendMessage(ChatColor.GREEN + fPlayer.getName() + " has demote " + target.getName() + " to " + target.getRole() + ".");
                }

                target.getPlayer().sendMessage(ChatColor.GREEN + "You have been demoted to " + target.getRole() + ".");
                player.sendMessage(ChatColor.GREEN + "You have demoted " + target.getName() + " to " + target.getRole() + ".");
            }
            else
            {
                player.sendMessage(ChatColor.RED + "You cannot demote " + target.getName() + " any lower!");
            }
        }
        else
        {
            player.sendMessage(ChatColor.RED + "Invalid args! /faction demote <name>");
        }
    }
}

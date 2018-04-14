package com.gmail.justdontdiebusiness.command.commands;

import com.gmail.justdontdiebusiness.Factions;
import com.gmail.justdontdiebusiness.command.commands.Command;
import com.gmail.justdontdiebusiness.faction.player.FPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class InviteCommand extends Command
{
    public InviteCommand(Factions plugin)
    {
        super("invite", "factions.all", plugin);
    }

    @Override
    public void execute(Player player, FPlayer fPlayer, String[] args)
    {
        if (args.length > 2)
        {
            if (fPlayer.getFaction() == null)
            {
                player.sendMessage(ChatColor.RED + "You need to be in a faction!");
                return;
            }

            Player other = Bukkit.getPlayer(args[1]);

            if (other == null)
            {
                player.sendMessage(ChatColor.RED + "Cannot find " + args[1] + "! Maybe they're offline or have never joined?");
                return;
            }

            FPlayer fOther = getPlugin().getPlayerManager().getPlayer(other.getUniqueId());

            if (fOther.getFaction() != null)
            {
                player.sendMessage(ChatColor.RED + other.getName() + " is already in a faction!");
                return;
            }

            if (fPlayer.getRole().ordinal() < 1)
            {
                for (FPlayer members : fPlayer.getFaction().getAllMembers())
                {
                    if (members == null || members.getPlayer() == null) continue;

                    members.getPlayer().sendMessage(ChatColor.GREEN + player.getName() + " has suggested for " + other.getName() + " to be added to the faction.");
                }
            }
            else
            {
                fPlayer.getFaction().invite(fOther, TimeUnit.MINUTES.toMillis(1) * System.currentTimeMillis());

                for (FPlayer members : fPlayer.getFaction().getAllMembers())
                {
                    if (members == null || members.getPlayer() == null) continue;

                    members.getPlayer().sendMessage(ChatColor.GREEN + player.getName() + " has invited " + other.getName() + " to the faction.");
                }

                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        if (fPlayer.getFaction().getInvites().get(other.getUniqueId()) < System.currentTimeMillis())
                        {
                            fPlayer.getFaction().getInvites().remove(other.getUniqueId());

                            for (FPlayer members : fPlayer.getFaction().getAllMembers())
                            {
                                if (members == null || members.getPlayer() == null) continue;

                                members.getPlayer().sendMessage(ChatColor.GREEN + other.getName() + " has taken to long to accept the invitation.");
                            }
                        }
                    }
                }.runTaskTimer(getPlugin(), 0, 1);
            }
        }
        else
        {
            player.sendMessage(ChatColor.RED + "Invalid args! /faction invite <name>");
        }
    }
}

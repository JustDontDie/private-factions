package com.gmail.justdontdiebusiness.command.commands;

import com.gmail.justdontdiebusiness.Factions;
import com.gmail.justdontdiebusiness.command.commands.Command;
import com.gmail.justdontdiebusiness.faction.Faction;
import com.gmail.justdontdiebusiness.faction.player.FPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class DisbandFactionCommand extends Command
{
    public DisbandFactionCommand(Factions plugin)
    {
        super("disband", "factions.all", plugin);
    }

    @Override
    public void execute(Player player, FPlayer fPlayer, String[] args)
    {
        if (!fPlayer.isInFaction())
        {
            player.sendMessage(ChatColor.RED + "You must be in a faction in order to use this command!");
            return;
        }

        if (fPlayer.getRole() != Faction.Role.OWNER)
        {
            player.sendMessage(ChatColor.RED + "You must be owner in order to disband the faction!");
            return;
        }

        for (FPlayer members : fPlayer.getFaction().getMembers())
        {
            if (members == null || members.getPlayer() == null) continue;

            members.getPlayer().sendMessage(ChatColor.RED + player.getName() + " has disbanded the faction you were in!");
        }

        String name = fPlayer.getFaction().getName();

        getPlugin().getFactionManager().disbandFaction(fPlayer, fPlayer.getFaction());
        Bukkit.broadcastMessage(ChatColor.GREEN + "The faction, " + name + ", has been disbanded!");
        player.sendMessage(ChatColor.GREEN + "The faction has been disbanded!");
    }
}

package com.gmail.justdontdiebusiness.command.commands;

import com.gmail.justdontdiebusiness.Factions;
import com.gmail.justdontdiebusiness.command.commands.Command;
import com.gmail.justdontdiebusiness.faction.player.FPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class UnClaimCommand extends Command
{
    public UnClaimCommand(Factions plugin)
    {
        super("unclaim", "factions.all", plugin);
    }

    @Override
    public void execute(Player player, FPlayer fPlayer, String[] args)
    {
        if (fPlayer.getFaction() == null)
        {
            player.sendMessage(ChatColor.RED + "You must be in a faction to use this command!");
            return;
        }

        getPlugin().getClaimManager().unclaim(getPlugin().getClaimManager().getClaim(player.getLocation().getChunk()));

        for (FPlayer members : fPlayer.getFaction().getAllMembers())
        {
            if (members == null || members.getPlayer() == null) continue;

            members.getPlayer().sendMessage(ChatColor.GREEN + player.getName() + " has unclaimed some land!");
        }

        player.sendMessage(ChatColor.GREEN + "You have unclaimed some land!");
    }
}

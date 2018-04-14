package com.gmail.justdontdiebusiness.command.commands;

import com.gmail.justdontdiebusiness.Factions;
import com.gmail.justdontdiebusiness.command.commands.Command;
import com.gmail.justdontdiebusiness.faction.player.FPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ClaimCommand extends Command
{
    public ClaimCommand(Factions plugin)
    {
        super("claim", "factions.all", plugin);
    }

    @Override
    public void execute(Player player, FPlayer fPlayer, String[] args)
    {
        if (fPlayer.getFaction() == null)
        {
            player.sendMessage(ChatColor.RED + "You must be in a faction to use this command!");
            return;
        }

        getPlugin().getClaimManager().claim(fPlayer.getFaction(), player.getLocation().getChunk());

        for (FPlayer members : fPlayer.getFaction().getAllMembers())
        {
            if (members == null || members.getPlayer() == null) continue;

            members.getPlayer().sendMessage(ChatColor.GREEN + player.getName() + " has claimed some land!");
        }

        player.sendMessage(ChatColor.GREEN + "You have claimed some land!");
    }
}

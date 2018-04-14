package com.gmail.justdontdiebusiness.command.commands;

import com.gmail.justdontdiebusiness.Factions;
import com.gmail.justdontdiebusiness.faction.Faction;
import com.gmail.justdontdiebusiness.faction.claim.Claim;
import com.gmail.justdontdiebusiness.faction.player.FPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/*
Unfished command. Not sure exactly how I wanted to go about
making it, and not really good when it comes to stuf like this :P
 */
public class MapCommand extends Command
{
    public MapCommand(Factions plugin)
    {
        super("map", "factions.all", plugin);
    }

    @Override
    public void execute(Player player, FPlayer fPlayer, String[] args)
    {
        if (args.length >= 2)
        {
            player.sendMessage(ChatColor.RED + "Invalid args! /faction map");
        }
        else
        {
            Claim onlyClaim = getPlugin().getClaimManager().getClaims().get(0);

            System.out.println("Claim 0: " + (onlyClaim == null ? "null" : onlyClaim.getX() + ":" + onlyClaim.getZ()));

            StringBuilder map = new StringBuilder();

            for (int height = 0; height < 5; height++)
            {
                for (int width = 0; width < 5; width++)
                {
                    int x = height + player.getLocation().getChunk().getX();
                    int z = width + player.getLocation().getChunk().getZ();

                    Claim claim = getPlugin().getClaimManager().getClaim(x, z);

                    System.out.println(x + ":" + z);

                    if (height == 2 && width == 2)
                        map.append(ChatColor.GREEN).append("O");
                    else if (claim == null)
                        map.append(ChatColor.WHITE).append("#");
                    else if (fPlayer.getFaction().isRelation(claim.getFaction(),Faction.Relation.NONE))
                        map.append(ChatColor.GRAY).append("#");
                    else if (fPlayer.getFaction().getName().equals(claim.getFaction().getName()) || fPlayer.getFaction().isRelation(claim.getFaction(), Faction.Relation.ALLY))
                        map.append(ChatColor.GREEN).append("#");
                    else if (fPlayer.getFaction().isRelation(claim.getFaction(), Faction.Relation.TRUCE))
                        map.append(ChatColor.AQUA).append("#");
                    else
                        map.append(ChatColor.RED).append('#');

                    if (width == 4) map.append("\n");
                }
            }

            player.sendMessage(map.toString());
        }
    }
}

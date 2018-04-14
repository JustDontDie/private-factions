package com.gmail.justdontdiebusiness.listeners;

import com.gmail.justdontdiebusiness.Factions;
import com.gmail.justdontdiebusiness.faction.Faction;
import com.gmail.justdontdiebusiness.faction.claim.Claim;
import com.gmail.justdontdiebusiness.faction.player.FPlayer;
import com.gmail.justdontdiebusiness.utils.PacketUtils;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The class deals with the claims on the server
 */
public class ClaimListener implements Listener
{
    private Map<UUID, Claim> claimChecker;
    private Factions plugin;

    public ClaimListener(Factions plugin)
    {
        this.plugin = plugin;
        this.claimChecker = new HashMap<>();
    }

    //Alert the player whenever they change claim, if necessary.
    @EventHandler
    public void onMove(PlayerMoveEvent e)
    {
        //Get the player
        FPlayer player = plugin.getPlayerManager().getPlayer(e.getPlayer().getUniqueId());

        //Get the clam the player is currently in. Null if the wilderness
        Claim claim = plugin.getClaimManager().getClaim(e.getTo().getChunk());

        //Get the previous claim they were in. Null if the wilderness.
        Claim previousClaim = claimChecker.get(e.getPlayer().getUniqueId());

        //If they joined, assuming that is the case, then put them in our map and send them a message.
        //Else, if they haven't changed factions/from the wilderness, then forget about it. Else, change their previous claim and send them a message
        if (!claimChecker.containsKey(e.getPlayer().getUniqueId()))
        {
            //Add them to our map since they have mostly likely just joined the server
            claimChecker.put(e.getPlayer().getUniqueId(), claim);
        }
        else
        {
            //If they haven't changed chunks, then do not do anything.
            if (previousClaim == claim) return;

            //Change their current claim
            claimChecker.put(e.getPlayer().getUniqueId(), claim);
        }

        //Send them 3 PacketPlayOutTitle packets (title, subtitle, and time) & a client message telling them that they've changed claims
        if (claim != null)
        {
            if (player.getFaction() != null)
            {
                if (player.getFaction().getName().equals(claim.getFaction().getName()) || player.getFaction().isRelation(claim.getFaction(), Faction.Relation.ALLY))
                {
                    PacketUtils.sendTitle(e.getPlayer(), ChatColor.DARK_GREEN + "Changed Claims", ChatColor.GREEN + "You're now in " + claim.getFaction().getName(), 10, 40, 10);
                    e.getPlayer().sendMessage(ChatColor.GREEN + "You're now in " + ChatColor.DARK_GREEN + claim.getFaction().getName());
                } else if (player.getFaction().isRelation(claim.getFaction(), Faction.Relation.TRUCE))
                {
                    PacketUtils.sendTitle(e.getPlayer(), ChatColor.DARK_AQUA + "Changed Claims", ChatColor.AQUA + "You're now in " + claim.getFaction().getName(), 10, 40, 10);
                    e.getPlayer().sendMessage(ChatColor.AQUA + "You're now in " + ChatColor.DARK_AQUA + claim.getFaction().getName());
                } else
                {
                    PacketUtils.sendTitle(e.getPlayer(), ChatColor.DARK_RED + "Changed Claims", ChatColor.RED + "You're now in " + claim.getFaction().getName(), 10, 40, 10);
                    e.getPlayer().sendMessage(ChatColor.RED + "You're now in " + ChatColor.DARK_RED + claim.getFaction().getName());
                }
            }
            else
            {
                PacketUtils.sendTitle(e.getPlayer(), ChatColor.DARK_RED + "Changed Claims", ChatColor.RED + "You're now in " + claim.getFaction().getName(), 10, 40, 10);
                e.getPlayer().sendMessage(ChatColor.RED + "You're now in " + ChatColor.DARK_RED + claim.getFaction().getName());
            }
        }
        else
        {
            PacketUtils.sendTitle(e.getPlayer(), ChatColor.DARK_GRAY + "Changed Claims", ChatColor.GRAY + "You're now in the wilderness", 10, 40, 10);
            e.getPlayer().sendMessage(ChatColor.GRAY + "You're now in " + ChatColor.DARK_GRAY + "the wilderness");
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e)
    {

    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e)
    {

    }
}

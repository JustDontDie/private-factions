package com.gmail.justdontdiebusiness.listeners;

import com.gmail.justdontdiebusiness.Factions;
import com.gmail.justdontdiebusiness.faction.Faction;
import com.gmail.justdontdiebusiness.faction.player.FPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener
{
    private Factions plugin;

    public ChatListener(Factions plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e)
    {
        Player player = e.getPlayer();

        for (Player other : e.getRecipients())
        {
            FPlayer player1 = plugin.getPlayerManager().getPlayer(player.getUniqueId());

            if (player1.getFaction() == null)
            {
                other.sendMessage(ChatColor.GRAY + "[" + player1.getName() + ChatColor.GRAY + "]: " + ChatColor.WHITE + e.getMessage());
                continue;
            }

            FPlayer player2 = plugin.getPlayerManager().getPlayer(other.getUniqueId());

            if (player2.getFaction() != null)
            {
                if (player1.getFaction().isRelation(player2.getFaction(), Faction.Relation.ENEMY))
                {
                    other.sendMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + player1.getFaction().getName() +
                            ChatColor.DARK_RED + "] [" + getStars(player1) + "] " + ChatColor.GRAY + "[" + player1.getName()
                            + ChatColor.GRAY + "]: " + ChatColor.WHITE + e.getMessage());
                } else if (player1.getFaction().isRelation(player2.getFaction(), Faction.Relation.TRUCE))
                {
                    other.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + player1.getFaction().getName() +
                            ChatColor.DARK_AQUA + "] [" + getStars(player1) + "] " + ChatColor.GRAY + "[" + player1.getName()
                            + ChatColor.GRAY + "]: " + ChatColor.WHITE + e.getMessage());
                } else if (player1.getFaction().isRelation(player2.getFaction(), Faction.Relation.ALLY))
                {
                    other.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + player1.getFaction().getName() +
                            ChatColor.DARK_GREEN + "] [" + getStars(player1) + "] " + ChatColor.GRAY + "[" + player1.getName()
                            + ChatColor.GRAY + "]: " + ChatColor.WHITE + e.getMessage());
                }
                else
                {
                    other.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + player1.getFaction().getName() +
                            ChatColor.DARK_GRAY + "] [" + getStars(player1) + "] " + ChatColor.GRAY + "[" + player1.getName()
                            + ChatColor.GRAY + "]: " + ChatColor.WHITE + e.getMessage());
                }
            }
            else
            {
                other.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + player1.getFaction().getName() +
                        ChatColor.DARK_GRAY + "] [" + getStars(player1) + "] " + ChatColor.GRAY + "[" + player1.getName()
                        + ChatColor.GRAY + "]: " + ChatColor.WHITE + e.getMessage());
            }
        }

        e.setCancelled(true);
    }

    private String getStars(FPlayer player)
    {
        StringBuilder builder = new StringBuilder();

        switch (player.getRole())
        {
            case OFFICER:
                builder.append("*");
                break;
            case OWNER:
                builder.append("**");
                break;
            default:
                break;
        }

        return builder.toString();
    }
}

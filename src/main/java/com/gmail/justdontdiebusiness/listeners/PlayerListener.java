package com.gmail.justdontdiebusiness.listeners;

import com.gmail.justdontdiebusiness.Factions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener
{
    private Factions plugin;

    public PlayerListener(Factions plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        plugin.getPlayerManager().createPlayer(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e)
    {
        plugin.getPlayerManager().getPlayer(e.getPlayer().getUniqueId()).setPlayer(null);
    }
}

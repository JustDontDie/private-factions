package com.gmail.justdontdiebusiness;

import com.gmail.justdontdiebusiness.command.CommandManager;
import com.gmail.justdontdiebusiness.faction.FactionManager;
import com.gmail.justdontdiebusiness.faction.claim.ClaimManager;
import com.gmail.justdontdiebusiness.faction.player.PlayerManager;
import com.gmail.justdontdiebusiness.listeners.ChatListener;
import com.gmail.justdontdiebusiness.listeners.ClaimListener;
import com.gmail.justdontdiebusiness.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Factions extends JavaPlugin
{
    private File factionDir;
    private File playerDir;

    private FactionManager factionManager;
    private ClaimManager claimManager;
    private PlayerManager playerManager;

    @Override
    public void onEnable()
    {
        if (!getDataFolder().exists())
        {
            getDataFolder().mkdir();
        }

        this.factionDir = new File(getDataFolder() + File.separator + "factions" + File.separator);
        this.playerDir = new File(getDataFolder() + File.separator + "players" + File.separator);

        if (!factionDir.exists())
        {
            factionDir.mkdir();
        }

        if(!playerDir.exists())
        {
            playerDir.mkdir();
        }

        new CommandManager(this); //No need to create a variable for it since no other "manager" will ever use it.
        this.playerManager = new PlayerManager(this);

        this.claimManager = new ClaimManager();
        this.factionManager = new FactionManager(this);

        getServer().getPluginManager().registerEvents(new ClaimListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);

        for (Player player : Bukkit.getOnlinePlayers())
        {
            getPlayerManager().getPlayer(player.getUniqueId()).setPlayer(player);
        }
    }

    @Override
    public void onDisable()
    {
        getFactionManager().clearFactions();
        getPlayerManager().clearPlayers();
        getClaimManager().clearClaims();
    }

    public File getFactionDir()
    {
        return factionDir;
    }

    public File getPlayerDir()
    {
        return playerDir;
    }

    public FactionManager getFactionManager()
    {
        return factionManager;
    }

    public ClaimManager getClaimManager()
    {
        return claimManager;
    }

    public PlayerManager getPlayerManager()
    {
        return playerManager;
    }
}

package com.gmail.justdontdiebusiness.faction.player;

import com.gmail.justdontdiebusiness.Factions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerManager
{
    private Factions plugin;

    private Set<FPlayer> players;

    public PlayerManager(Factions plugin)
    {
        this.plugin = plugin;
        this.players = new HashSet<>();

        int amount = 0;

        //Load players from files
        for (File file : plugin.getPlayerDir().listFiles(fileName -> fileName.isFile() && fileName.getName().endsWith(".yml")))
        {
            createPlayer(file, YamlConfiguration.loadConfiguration(file));
            amount++;
        }

        System.out.println("[Player Manager] Loaded (" + amount + ") players");
    }

    private Set<FPlayer> getPlayers()
    {
        return players;
    }

    public FPlayer getPlayer(UUID uuid)
    {
        for (FPlayer player : getPlayers())
        {
            if (player.getUUID().equals(uuid))
            {
                return player;
            }
        }

        return null;
    }

    public FPlayer getPlayer(String name)
    {
        for (FPlayer player : getPlayers())
        {
            if (player.getName().equalsIgnoreCase(name))
            {
                return player;
            }
        }

        return null;
    }

    public void createPlayer(File file, YamlConfiguration config)
    {
        getPlayers().add(new FPlayer(file, config));
    }

    public void createPlayer(Player player)
    {
        if (!getPlayers().contains(getPlayer(player.getUniqueId())))
            getPlayers().add(new FPlayer(player, null, null, new File(plugin.getPlayerDir() + File.separator + player.getUniqueId().toString() + ".yml")));
        else
            getPlayer(player.getUniqueId()).setPlayer(player);
    }

    /**
     * Checks if player exists by seeing if they've logged on or they have a player file.
     * @param uuid Player's UUID
     * @return True if they exist; otherwise, false.
     */
    public boolean playerExists(UUID uuid)
    {
        return getPlayer(uuid) != null || new File(plugin.getPlayerDir() + File.separator + uuid.toString() + ".yml").exists();
    }

    public void clearPlayers()
    {
        System.out.println("[Player Manager] Clearing " + getPlayers().size() + " players");
        getPlayers().clear();
    }
}

package com.gmail.justdontdiebusiness.faction.player;

import com.gmail.justdontdiebusiness.faction.Faction;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FPlayer
{
    private Player player;
    private String name;
    private UUID uuid;

    private Faction faction;
    private Faction.Role role;

    private File playerFile;
    private YamlConfiguration config;

    public FPlayer(File file, YamlConfiguration configuration)
    {
        this.player = null;

        this.name = configuration.getString("name");
        this.uuid = UUID.fromString(file.getName().replace(".yml", ""));

        try
        {
            this.role = Faction.Role.valueOf(configuration.getString("faction.role"));
        } catch (IllegalArgumentException e)
        {
            this.role = null;
        }

        this.playerFile = file;
        this.config = configuration;
    }

    public FPlayer(Player player, Faction faction, Faction.Role role, File playerFile)
    {
        this.player = player;

        this.name = this.player.getName();
        this.uuid = this.player.getUniqueId();

        this.faction = faction;
        this.role = role;

        this.playerFile = playerFile;

        if (!playerFile.exists())
        {
            try
            {
                playerFile.createNewFile();
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            this.config = YamlConfiguration.loadConfiguration(playerFile);

            getConfig().createSection("name");
            getConfig().createSection("faction");
            getConfig().createSection("faction.name");
            getConfig().createSection("faction.joinedDate");
            getConfig().createSection("faction.role");

            getConfig().set("name", player.getName());

            if (faction != null)
            {
                getConfig().set("faction.name", faction.getName());
            }

            if (role != null)
            {
                getConfig().set("faction.role", role.name());
            }

            try
            {
                getConfig().save(playerFile);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public Player getPlayer()
    {
        return player;
    }

    public String getName()
    {
        return name;
    }

    public UUID getUUID()
    {
        return uuid;
    }

    public Faction getFaction()
    {
        return faction;
    }

    public boolean isInFaction()
    {
        return getFaction() != null;
    }

    public void setFaction(Faction faction)
    {
        this.faction = faction;
    }

    public Faction.Role getRole()
    {
        return role;
    }

    public void setRole(Faction.Role role)
    {
        this.role = role;
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }

    public File getPlayerFile()
    {
        return playerFile;
    }

    public YamlConfiguration getConfig()
    {
        return config;
    }
}

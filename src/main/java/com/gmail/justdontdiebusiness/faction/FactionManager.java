package com.gmail.justdontdiebusiness.faction;

import com.gmail.justdontdiebusiness.Factions;
import com.gmail.justdontdiebusiness.faction.player.FPlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FactionManager
{
    private List<Faction> factions;
    private Factions plugin;

    public FactionManager(Factions plugin)
    {
        this.factions = new ArrayList<>();
        this.plugin = plugin;

        int amount = 0;

        //Load factions from file
        for (File file : plugin.getFactionDir().listFiles(pathname -> pathname.isFile() && pathname.getName().endsWith(".yml")))
        {
            getFactions().add(new Faction(plugin, file, YamlConfiguration.loadConfiguration(file)));
            amount++;
        }

        System.out.println("[Faction Manager] Loaded (" + amount + ") factions");
    }

    public List<Faction> getFactions()
    {
        return factions;
    }

    public Faction getFactionByName(String name)
    {
        for (Faction faction : getFactions())
        {
            if (faction.getName().equalsIgnoreCase(name))
            {
                return faction;
            }
        }

        return null;
    }

    public FPlayer getMemberInFaction(UUID uuid)
    {
        for (Faction faction : getFactions())
        {
            for (FPlayer player : faction.getAllMembers())
            {
                if (player.getUUID().equals(uuid))
                {
                    return player;
                }
            }
        }

        return null;
    }

    public void createFaction(FPlayer owner, String name)
    {
        Faction faction = new Faction(name, owner, new File(this.plugin.getFactionDir() + File.separator + name + ".yml"));
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(faction.getFactionFile());

        configuration.createSection("name");
        configuration.createSection("description");
        configuration.createSection("status");
        configuration.createSection("recruits");
        configuration.createSection("members");
        configuration.createSection("officers");
        configuration.createSection("owner");
        configuration.createSection("claims");
        configuration.createSection("allies");
        configuration.createSection("truces");
        configuration.createSection("enemies");

        configuration.set("name", name);
        configuration.set("status", Faction.Status.PRIVATE.name());
        configuration.set("description", "null");
        configuration.set("owner", owner.getName());

        try
        {
            configuration.save(faction.getFactionFile());
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        getFactions().add(faction);
    }

    public void disbandFaction(FPlayer player, Faction faction)
    {
        //Set all member's faction & role to nothing :P
        for (FPlayer members : player.getFaction().getAllMembers())
        {
            members.setFaction(null);
            members.setRole(null);
        }

        //Remove all members
        faction.getMembers().clear();
        faction.setOwner(null);

        faction.getFactionFile().delete(); //Delete file
        getFactions().remove(faction); //Remove faction from list
    }

    public void clearFactions()
    {
        System.out.println("[Faction Manager] Clearing " + getFactions().size() + " factions");
        getFactions().clear();
    }
}

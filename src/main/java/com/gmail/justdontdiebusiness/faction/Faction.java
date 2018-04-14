package com.gmail.justdontdiebusiness.faction;

import com.gmail.justdontdiebusiness.Factions;
import com.gmail.justdontdiebusiness.faction.claim.Claim;
import com.gmail.justdontdiebusiness.faction.player.FPlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Faction
{
    public enum Role
    {
        RECRUIT, MEMBER, OFFICER, OWNER
    }

    public enum Relation
    {
        NONE, ALLY, TRUCE, ENEMY
    }

    public enum Status
    {
        OPEN, PRIVATE
    }

    private String name;
    private String description;
    private Status status;

    private List<FPlayer> members;
    private FPlayer owner;

    private Map<UUID, Long> invites;

    private List<Claim> claims;

    private List<String> allies;
    private List<String> truces;
    private List<String> enemies;

    private File factionFile;
    private YamlConfiguration config;

    public Faction(Factions plugin, File factionFile, YamlConfiguration config)
    {
        this.factionFile = factionFile;
        this.config = config;

        this.name = config.getString("name");
        this.status = Status.valueOf(config.getString("status"));
        this.description = config.getString("description");
        this.invites = new HashMap<>();

        this.members = new ArrayList<>();

        for (String recruits : config.getStringList("recruits"))
        {
            getMembers().add(plugin.getPlayerManager().getPlayer(recruits));
            plugin.getPlayerManager().getPlayer(recruits).setRole(Role.RECRUIT);
            plugin.getPlayerManager().getPlayer(recruits).setFaction(this);
        }

        for (String members : config.getStringList("members"))
        {
            getMembers().add(plugin.getPlayerManager().getPlayer(members));
            plugin.getPlayerManager().getPlayer(members).setRole(Role.MEMBER);
            plugin.getPlayerManager().getPlayer(members).setFaction(this);
        }

        for (String officers : config.getStringList("officers"))
        {
            getMembers().add(plugin.getPlayerManager().getPlayer(officers));
            plugin.getPlayerManager().getPlayer(officers).setRole(Role.OFFICER);
            plugin.getPlayerManager().getPlayer(officers).setFaction(this);
        }

        this.owner = plugin.getPlayerManager().getPlayer(config.getString("owner"));
        this.owner.setFaction(this);
        this.owner.setRole(Role.OWNER);

        this.claims = new ArrayList<>();

        for (String s : config.getStringList("claims"))
        {
            getClaims().add(new Claim(this, config.getInt(s.split("-")[0]), config.getInt( s.split("-")[1])));
            plugin.getClaimManager().getClaims().add(new Claim(this, config.getInt(s.split("-")[0]), config.getInt( s.split("-")[1])));
        }

        this.allies = new ArrayList<>();
        this.truces = new ArrayList<>();
        this.enemies = new ArrayList<>();

        for (String allies : config.getStringList("allies"))
        {
            getAllies().add(allies);
        }

        for (String truces : config.getStringList("truces"))
        {
            getTruces().add(truces);
        }

        for (String enemies : config.getStringList("enemies"))
        {
            getEnemies().add(enemies);
        }
    }

    public Faction(String name, FPlayer owner, File factionFile)
    {
        this.name = name;
        this.status = Status.PRIVATE;
        this.invites = new HashMap<>();

        this.members = new ArrayList<>();
        this.owner = owner;
        owner.setFaction(this);
        owner.setRole(Role.OWNER);

        this.claims = new ArrayList<>();

        this.allies = new ArrayList<>();
        this.truces = new ArrayList<>();
        this.enemies = new ArrayList<>();

        this.factionFile = factionFile;
        this.config = YamlConfiguration.loadConfiguration(factionFile);

        //If for some reason there is already a fie under the faction name, then delete it
        if (getFactionFile().exists())
        {
            getFactionFile().delete();
        }

        //Create a new file for the faction
        try
        {
            getFactionFile().createNewFile();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public String getName()
    {
        return name;
    }

    public void setNewName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setNewDescription(String description)
    {
        this.description = description;
    }

    public Status getStatus()
    {
        return status;
    }

    public void changeStatus(Status status)
    {
        this.status = status;
    }

    public Map<UUID, Long> getInvites()
    {
        return invites;
    }

    public void invite(FPlayer player, long time)
    {
        getInvites().put(player.getUUID(), time);
    }

    public void join(FPlayer player)
    {
        getInvites().remove(player.getUUID());
        getMembers().add(player);

        player.setFaction(this);
        player.setRole(Role.RECRUIT);

        List<String> names = getConfig().getStringList("recruits");
        names.add(player.getName());

        getConfig().set("recruits", names);
    }

    public boolean promote(FPlayer player)
    {
        switch (player.getRole())
        {
            case RECRUIT:
                player.setRole(Role.MEMBER);
                return true;
            case MEMBER:
                player.setRole(Role.OFFICER);
                return true;
        }

        return false;
    }

    public boolean demote(FPlayer player)
    {
        switch (player.getRole())
        {
            case OFFICER:
                player.setRole(Role.MEMBER);
                return true;
            case MEMBER:
                player.setRole(Role.RECRUIT);
                return true;
        }

        return false;
    }

    public void leaveOrKick(FPlayer player)
    {
        getMembers().remove(player);
        player.setFaction(null);

        List<String> names = getConfig().getStringList(player.getRole().name().toLowerCase());
        names.remove(player.getName());

        getConfig().set(player.getRole().name().toLowerCase(), names);

        player.setRole(null);
    }

    public List<FPlayer> getMembers()
    {
        return this.members;
    }

    public List<FPlayer> getAllMembers()
    {
        List<FPlayer> members = new ArrayList<>();

        members.addAll(getMembers());

        members.add(getOwner());

        return members;
    }

    public FPlayer getMember(String name)
    {
        for (FPlayer player : getAllMembers())
        {
            if (player.getName().equalsIgnoreCase(name))
            {
                return player;
            }
        }

        return null;
    }

    public FPlayer getOwner()
    {
        return owner;
    }

    public void setOwner(FPlayer owner)
    {
        this.owner = owner;
    }

    public List<Claim> getClaims()
    {
        return claims;
    }

    public List<String> getAllies()
    {
        return allies;
    }

    public List<String> getTruces()
    {
        return truces;
    }

    public List<String> getEnemies()
    {
        return enemies;
    }

    public boolean isRelation(Faction faction, Relation relation)
    {
        switch (relation)
        {
            case ALLY:
                if (getAllies().contains(faction.getName())) return true;
                break;
            case TRUCE:
                if (getTruces().contains(faction.getName())) return true;
                break;
            case ENEMY:
                if (getEnemies().contains(faction.getName())) return true;
            case NONE:
                if (!getAllies().contains(faction.getName()) && getTruces().contains(faction.getName())
                   && getEnemies().contains(faction.getName())) return true;
                break;
        }

        return false;
    }

    public File getFactionFile()
    {
        return factionFile;
    }

    public YamlConfiguration getConfig()
    {
        return config;
    }
}

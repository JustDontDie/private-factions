package com.gmail.justdontdiebusiness.faction.claim;

import com.gmail.justdontdiebusiness.faction.Faction;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;

public class Claim
{
    private Faction faction;
    private int x;
    private int z;

    public Claim(Faction faction, int x, int z)
    {
        this.faction = faction;
        this.x = x;
        this.z = z;
    }

    public Faction getFaction()
    {
        return faction;
    }

    public int getX()
    {
        return x;
    }

    public int getZ()
    {
        return z;
    }

    public Chunk getChunk()
    {
        return Bukkit.getWorld("world").getChunkAt(getX(), getZ());
    }
}

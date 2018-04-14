package com.gmail.justdontdiebusiness.faction.claim;

import com.gmail.justdontdiebusiness.Factions;
import com.gmail.justdontdiebusiness.faction.Faction;
import org.bukkit.Chunk;

import java.util.ArrayList;
import java.util.List;

/**
 * This class manages all the claims on the server
 */
public class ClaimManager
{
    private List<Claim> claims;

    public ClaimManager()
    {
        this.claims = new ArrayList<>();
    }

    public List<Claim> getClaims()
    {
        return claims;
    }

    public Claim getClaim(int x, int z)
    {
        for (Claim claim : getClaims())
        {
            if (claim.getX() == x && claim.getZ() == z)
            {
                return claim;
            }
        }

        return null;
    }

    public Claim getClaim(Chunk chunk)
    {
        for (Claim claim : getClaims())
        {
            if (claim.getX() == chunk.getX() && claim.getZ() == chunk.getZ())
            {
                return claim;
            }
        }

        return null;
    }

    public void claim(Faction faction, Chunk chunk)
    {
        getClaims().add(new Claim(faction, chunk.getX(), chunk.getZ()));
        faction.getClaims().add(new Claim(faction, chunk.getX(), chunk.getZ()));

        List<String> claims = faction.getConfig().getStringList("claims");
        claims.add(chunk.getX() + "-" + chunk.getZ());
        faction.getConfig().set("claims", claims);
    }

    public void unclaim(Claim claim)
    {
        getClaims().remove(claim);
        claim.getFaction().getClaims().remove(claim);
    }

    public void clearClaims()
    {
        System.out.println("[Claim Manager] Cleared " + getClaims().size() + " claims");
        getClaims().clear();
    }
}

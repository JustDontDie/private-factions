package com.gmail.justdontdiebusiness.command.commands;

import com.gmail.justdontdiebusiness.Factions;
import com.gmail.justdontdiebusiness.command.commands.Command;
import com.gmail.justdontdiebusiness.faction.Faction;
import com.gmail.justdontdiebusiness.faction.player.FPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InfoCommand extends Command
{
    public InfoCommand(Factions plugin)
    {
        super("info", "factions.all", plugin);
    }

    @Override
    public void execute(Player player, FPlayer fPlayer, String[] args)
    {
        if (args.length == 2)
        {
            Faction faction = getPlugin().getFactionManager().getFactionByName(args[1]);

            if (faction == null)
            {
                player.sendMessage(ChatColor.RED + "That faction does not exist!");
                return;
            }

            String title = ChatColor.DARK_GREEN + "===== " + ChatColor.GREEN + "About " + faction.getName() + ChatColor.DARK_GREEN + " =====";

            player.sendMessage(title);

            String onlinePlayers = get(faction, 0);

            player.sendMessage("Created: TODO");
            player.sendMessage("Players (" + getOnlineMembers(faction) + "/" + (occurrencesInString(onlinePlayers, ',') + 1) + "): " + onlinePlayers);
            player.sendMessage("Allies: " + get(faction, 1));
            player.sendMessage("Truces: " + get(faction, 2));
            player.sendMessage("Enemies: " + get(faction, 3));

            player.sendMessage(ChatColor.DARK_GREEN + replaceCharsInStringWith(title, '='));
        }
        else
        {
            player.sendMessage(ChatColor.RED + "Invalid args! /faction info <faction name>");
        }
    }

    private String get(Faction faction, int i)
    {
        StringBuilder builder = new StringBuilder();

        switch (i)
        {
            case 0:
                List<FPlayer> members = faction.getAllMembers();
                List<String> names = new ArrayList<>();

                for (FPlayer player : members)
                {
                    names.add(player.getName());
                }

                Collections.sort(names);

                for (int j = 0; j < names.size(); j++)
                {
                    String name = names.get(j);
                    FPlayer player = getPlugin().getPlayerManager().getPlayer(name);

                    if (j == names.size() - 1)
                    {
                        if (player.getPlayer() != null)
                            builder.append(ChatColor.WHITE).append(player.getRole() == Faction.Role.OWNER ? "[**]" : player.getRole() == Faction.Role.OFFICER ? "[*]" : "").append(ChatColor.GREEN).append(name);
                        else
                            builder.append(ChatColor.WHITE).append(player.getRole() == Faction.Role.OWNER ? "[**]" : player.getRole() == Faction.Role.OFFICER ? "[*]" : "").append(ChatColor.GRAY).append(name);
                    }
                    else
                    {
                        if (player.getPlayer() != null)
                            builder.append(ChatColor.WHITE).append(player.getRole() == Faction.Role.OWNER ? "[**]" : player.getRole() == Faction.Role.OFFICER ? "[*]" : "").append(ChatColor.GREEN).append(name).append(", ");
                        else
                            builder.append(ChatColor.WHITE).append(player.getRole() == Faction.Role.OWNER ? "[**]" : player.getRole() == Faction.Role.OFFICER ? "[*]" : "").append(ChatColor.GRAY).append(name).append(", ");
                    }
                }
            case 1:
                for (int j = 0; j < faction.getAllies().size(); j++)
                {
                    String ally = faction.getAllies().get(j);

                    if (j == faction.getAllies().size() - 1)
                        builder.append(ally);
                    else
                        builder.append(ally).append(", ");
                }

                break;
            case 2:
                for (int j = 0; j < faction.getTruces().size(); j++)
                {
                    String truce = faction.getTruces().get(j);

                    if (j == faction.getTruces().size() - 1)
                        builder.append(truce);
                    else
                        builder.append(truce).append(", ");
                }

                break;
            case 3:
                for (int j = 0; j < faction.getEnemies().size(); j++)
                {
                    String enemy = faction.getEnemies().get(i);

                    if (j == faction.getEnemies().size() - 1)
                        builder.append(enemy);
                    else
                        builder.append(enemy).append(", ");
                }

                break;
        }

        return builder.toString();
    }

    private int getOnlineMembers(Faction faction)
    {
        int i = 0;

        for (FPlayer player : faction.getAllMembers())
        {
            if (player.getPlayer() != null) i++;
        }

        return i;
    }

    private int occurrencesInString(String s, char c)
    {
        int i = 0;

        for (char character : s.toCharArray())
        {
            if (character == c) i++;
        }

        return i;
    }

    private String replaceCharsInStringWith(String s, char c)
    {
        StringBuilder builder = new StringBuilder();
        for (char chars : s.toCharArray())
        {
            builder.append(c);
        }

        return builder.toString().substring(0, s.length());
    }
}

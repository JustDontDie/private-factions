package com.gmail.justdontdiebusiness.command;

import com.gmail.justdontdiebusiness.Factions;
import com.gmail.justdontdiebusiness.command.commands.*;
import com.gmail.justdontdiebusiness.faction.player.FPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class CommandManager implements CommandExecutor
{
    private List<ICommand> commands;
    private Map<UUID, Map<String, Long>> cooldown;

    private Factions plugin;

    public CommandManager(Factions plugin)
    {
        this.commands = new ArrayList<>();
        this.cooldown = new HashMap<>();
        this.plugin = plugin;

        addCommand(new CreateFactionCommand(plugin));
        addCommand(new DisbandFactionCommand(plugin));
        addCommand(new InfoCommand(plugin));

        addCommand(new InviteCommand(plugin));
        addCommand(new JoinFactionCommand(plugin));

        addCommand(new ClaimCommand(plugin));
        addCommand(new UnClaimCommand(plugin));
        addCommand(new MapCommand(plugin));

        addCommand(new PromoteCommand(plugin));
        addCommand(new DemoteCommand(plugin));

        plugin.getCommand("faction").setExecutor(this);
    }

    private List<ICommand> getCommands()
    {
        return this.commands;
    }

    public Factions getPlugin()
    {
        return plugin;
    }

    private ICommand getCommand(String command)
    {
        for (ICommand commands : getCommands())
        {
            if (commands.getName().equalsIgnoreCase(command))
            {
                return commands;
            }
        }

        return null;
    }

    public void addCommand(ICommand icommand)
    {
        getCommands().add(icommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (cmd.getName().equalsIgnoreCase("faction"))
        {
            if (sender instanceof Player)
            {
                Player player = (Player) sender;

                if (args.length >= 1)
                {
                    ICommand command = getCommand(args[0]);

                    if (command != null)
                    {
                        //Cooldown system - Wait 5 seconds before using command again
                        if (!cooldown.containsKey(player.getUniqueId()))
                        {
                            cooldown.put(player.getUniqueId(), new HashMap<>());
                            cooldown.get(player.getUniqueId()).put(command.getName(),TimeUnit.SECONDS.toMillis(5) + System.currentTimeMillis());
                        }
                        else
                        {
                            if (cooldown.get(player.getUniqueId()).get(command.getName()) != null)
                            {
                                if (cooldown.get(player.getUniqueId()).get(command.getName()) < System.currentTimeMillis())
                                {
                                    cooldown.put(player.getUniqueId(), new HashMap<>());
                                    cooldown.get(player.getUniqueId()).put(command.getName(), TimeUnit.SECONDS.toMillis(5) + System.currentTimeMillis());
                                }
                                else
                                {
                                    player.sendMessage(ChatColor.RED + "You must wait " + TimeUnit.MILLISECONDS.toSeconds(cooldown.get(player.getUniqueId()).get(command.getName()) - System.currentTimeMillis()) + " seconds before using this command again!");
                                    return true;
                                }
                            }
                            else
                            {
                                cooldown.put(player.getUniqueId(), new HashMap<>());
                                cooldown.get(player.getUniqueId()).put(command.getName(),TimeUnit.SECONDS.toMillis(5) + System.currentTimeMillis());
                            }
                        }
                        //End of cooldown system

                        if (!command.getPermission().equals("factions.all") && !player.hasPermission(command.getPermission()))
                        {
                            player.sendMessage(ChatColor.RED + "No permission to run this command!");
                            return true;
                        }

                        FPlayer fPlayer = getPlugin().getPlayerManager().getPlayer(player.getUniqueId());
                        command.execute(player, fPlayer, args);
                        return true;
                    }
                    else
                    {
                        player.sendMessage(ChatColor.RED + "Command does not exist!");
                        return true;
                    }
                }
                else
                {
                    //TODO: Replace with all faction commands = hard :( :P
                    player.sendMessage(ChatColor.RED + "Invalid args!");
                }
                return true;
            }
            else
            {
                sender.sendMessage("Only players can use this command!");
                return true;
            }
        }

        return true;
    }
}

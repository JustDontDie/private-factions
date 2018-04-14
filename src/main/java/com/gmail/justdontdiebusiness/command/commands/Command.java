package com.gmail.justdontdiebusiness.command.commands;

import com.gmail.justdontdiebusiness.Factions;
import com.gmail.justdontdiebusiness.command.ICommand;

public abstract class Command implements ICommand
{
    private String name;
    private String permission;

    private Factions plugin;

    public Command(String name, String permission, Factions plugin)
    {
        this.name = name;
        this.permission = permission;
        this.plugin = plugin;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public String getPermission()
    {
        return this.permission;
    }

    @Override
    public Factions getPlugin()
    {
        return this.plugin;
    }
}

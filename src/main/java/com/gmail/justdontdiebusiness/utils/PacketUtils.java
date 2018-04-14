package com.gmail.justdontdiebusiness.utils;

import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;

public class PacketUtils
{
    public static void sendTitle(Player player, String title, String subTitle, int fadeIn, int stay, int fadeOut)
    {
        if (title != null)
        {
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, CraftChatMessage.fromString(title)[0]));
        }

        if (subTitle != null)
        {
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, CraftChatMessage.fromString(subTitle)[0]));
        }

        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutTitle(fadeIn, stay, fadeOut));
    }
}

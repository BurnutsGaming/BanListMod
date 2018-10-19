package com.github.bartimaeusnek.banhammer;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListBansEntry;

import java.util.UUID;

public final class EventHandler {

    @SubscribeEvent
    public void onlogin(PlayerLoggedInEvent event) {
        UUID id = event.player.getUniqueID();
        for (String banlistentry : BanHammer.bans)
            if (id.toString().equals(banlistentry)) {
                if (FMLCommonHandler.instance().getSide().isServer()) {
                    event.player.setDead();
                    String playername = null;
                    for (String activename : MinecraftServer.getServer().getAllUsernames()) {
                        UUID id2 = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(activename).getId();
                        if (id2.toString().equals(banlistentry))
                            playername = activename;
                    }

                    final GameProfile gameprofile = new GameProfile(event.player.getUniqueID(), playername);
                    final UserListBansEntry userlistbansentry = new UserListBansEntry(gameprofile, null, "BanHammerModpackBan", null, BanLoader.banreason);
                    MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().addEntry(userlistbansentry);
                    EntityPlayerMP entityplayermp = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(playername);

                    if (entityplayermp != null) {
                        entityplayermp.playerNetServerHandler.kickPlayerFromServer("You have been banned from all of this Modpack's Servers.");
                    }
                }
            }
    }
}

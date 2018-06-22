package com.github.bartimaeusnek.banhammer;

import java.util.Date;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.server.management.UserListEntry;

public final class EventHandler {
	
	@SubscribeEvent
    public void onlogin(PlayerLoggedInEvent event)
    {
    	UUID id  = event.player.getUniqueID();
            for (String name: BanHammer.bans)
                if ( id.toString().equals(name) ){
                     if (FMLCommonHandler.instance().getEffectiveSide().isServer()){
                    	event.player.setDead();
                    	String playername = null;
                    	for (String activename : MinecraftServer.getServer().getAllUsernames()) {
                    		UUID id2 = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(activename).getId();
                    	if (id2.toString().equals(name))
                    		playername=activename;
                    	}
                    	 
                        final GameProfile gameprofile = new GameProfile(event.player.getUniqueID(), playername);
                        final UserListBansEntry userlistbansentry = new UserListBansEntry(gameprofile, (Date)null, "BanHammerModpackBan", (Date)null, "BanHammerModpackBan");
                        MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().addEntry((UserListEntry)userlistbansentry);
                        EntityPlayerMP entityplayermp = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(playername);

                        if (entityplayermp != null){
                            entityplayermp.playerNetServerHandler.kickPlayerFromServer("You have been banned from all of this Modpack's Servers.");
                        }
                    }
                } 
    }
}

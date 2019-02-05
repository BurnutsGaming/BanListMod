package com.github.bartimaeusnek.banhammer;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.UUID;

public final class EventHandler {

    @SubscribeEvent
    public void onlogin(PlayerEvent.PlayerLoggedInEvent event) {
        UUID id = event.player.getUniqueID();
        for (String banlistentry : BanHammer.bans)
            if (id.toString().equals(banlistentry)) {
                if (FMLCommonHandler.instance().getSide().isServer()) {
                    event.player.setDead();
                    String playername = null;
                    for (String activename : event.player.getServer().getPlayerList().getOnlinePlayerNames()) {
                        UUID id2 = event.player.getServer().getPlayerProfileCache().getGameProfileForUsername(activename).getId();
                        if (id2.toString().equals(banlistentry))
                            playername = activename;
                    }

                    final GameProfile gameprofile = new GameProfile(event.player.getUniqueID(), playername);
                    final UserListBansEntry userlistbansentry = new UserListBansEntry(gameprofile, null, "BanHammerModpackBan", null, BanLoader.banreason);
                    event.player.getServer().getPlayerList().getBannedPlayers().addEntry(userlistbansentry);
                    EntityPlayerMP entityplayermp = event.player.getServer().getPlayerList().getPlayerByUsername(playername);

                    if (entityplayermp != null) {
                        entityplayermp.connection.disconnect(new TextComponentTranslation("banHammerJ.banned"));
                    }
                }
            }
    }
}

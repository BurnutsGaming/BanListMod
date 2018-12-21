package com.github.bartimaeusnek.banhammer;

import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.server.CommandBanPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListBansEntry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public class BanHammerCommand extends CommandBanPlayer {

    public String getCommandName()
    {
        return "banhammer";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length >= 1 && args[0].length() > 0)
        {
            MinecraftServer minecraftserver = MinecraftServer.getServer();
            GameProfile gameprofile = minecraftserver.getPlayerProfileCache().getGameProfileForUsername(args[0]);

            if (gameprofile == null)
            {
                throw new CommandException("commands.ban.failed", new Object[] {args[0]});
            }
            else
            {
                String s = null;

                if (args.length >= 2)
                {
                    s = getChatComponentFromNthArg(sender, args, 1).getUnformattedText();
                }

                UserListBansEntry userlistbansentry = new UserListBansEntry(gameprofile, (Date)null, sender.getCommandSenderName(), (Date)null, s);
                minecraftserver.getConfigurationManager().getBannedPlayers().addEntry(userlistbansentry);
                EntityPlayerMP entityplayermp = minecraftserver.getConfigurationManager().getPlayerByUsername(args[0]);

                if (entityplayermp != null)
                {
                    final UUID bannedUUID = entityplayermp.getUniqueID();
                    if (BanHammer.useFiles) {
                        final File bandir = new File(BanHammer.configdir, "Bans");
                        if (!bandir.exists()) {
                            bandir.mkdirs();
                        }
                        final File nuBan = new File(bandir.toString() + "/" + bannedUUID.toString());
                        try {
                            nuBan.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        final File bantxt = new File(BanHammer.configdir.toPath()+"/Bans.BanHammer");
                        if (!bantxt.exists())
                            bantxt.createNewFile();
                        final FileWriter fw = new FileWriter(bantxt,true);
                        fw.write(bannedUUID.toString()+"\n");
                        fw.flush();
                        fw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    entityplayermp.playerNetServerHandler.kickPlayerFromServer("You have been banned from all of this Modpack's Servers.");
                }

                notifyOperators(sender, this, "commands.ban.success", new Object[] {args[0]});
            }
        }
        else
        {
            throw new WrongUsageException("commands.ban.usage", new Object[0]);
        }
    }

}

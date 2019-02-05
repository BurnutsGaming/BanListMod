package com.github.bartimaeusnek.banhammer;


import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.HashSet;

@Mod(modid = BanHammer.MODID, name = BanHammer.MODNAME, version = BanHammer.VERSION, acceptableRemoteVersions = "*")
public final class BanHammer {
    public final static String MODID = "banhammerj";
    public final static String MODNAME = "BanHammerJ";
    public final static String VERSION = "0.0.2";
    public final static Logger LOGGER = LogManager.getLogger(MODID);
    public static HashSet<String> bans = new HashSet<>();
    public static File configdir;
    public static boolean useFiles = false;

    @EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        if (FMLCommonHandler.instance().getSide().isClient())
            return;
        LOGGER.info("Starting BanHammer");
        configdir = event.getModConfigurationDirectory();
        bans = BanLoader.loadBans(event);
        FMLCommonHandler.instance().bus().register(new com.github.bartimaeusnek.banhammer.EventHandler());
    }

    @EventHandler
    public static void ServerStarting(FMLServerStartingEvent event) {
        if (FMLCommonHandler.instance().getSide().isClient())
            return;
        event.registerServerCommand(new BanHammerCommand());
    }

}

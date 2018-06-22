package com.github.bartimaeusnek.banhammer;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = BanHammer.MODID, version = BanHammer.VERSION)
public final class BanHammer {
    public final static String MODID  = "BanHammer";
    public final static String VERSION = "0.0.1J";
    public final static Logger LOGGER = LogManager.getLogger(MODID);
    public static ArrayList<String> bans = new ArrayList<String>();

    @EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
    	bans = BanLoader.loadBans(event);
        FMLCommonHandler.instance().bus().register(new com.github.bartimaeusnek.banhammer.EventHandler());
    }
    
}

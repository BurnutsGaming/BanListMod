package com.github.bartimaeusnek.banhammer;

import java.io.File;
import java.util.ArrayList;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

public final class BanLoader{
	public static final String[] HARDCODED = {
			"UUID1",
			"UUID2"
			};
	public static String[] Configbans= {
			"UUID1",
			"UUID2"
	};
	
	public static String banreason = "BanHammerModpackBan";
	
    public static ArrayList<String> loadBans(FMLPreInitializationEvent event) {
    	Configuration banconf = new Configuration(event.getSuggestedConfigurationFile());
    	File bandir = new File(event.getModConfigurationDirectory(), "Bans");
    	
    	ArrayList<String> ret = new ArrayList<String>();
    	
    	Configbans = banconf.getStringList("UUIDs", "Bans", Configbans, "");
    	banreason = banconf.getString("Ban Reason", "Bans", banreason, "");
    	
    	if (banconf.hasChanged())
        	banconf.save();
    	
        if (!bandir.exists()) {
            bandir.mkdirs();
        }
        
        for (File file: bandir.listFiles()){
        	String name = file.getName();
        	if (name.contains(".dat"))
        		name = name.substring(0,name.length()-".dat".length());
            ret.add(name);
        }
        
        for (String s: HARDCODED)
        	ret.add(s);
        
        for (String s: Configbans)
        	ret.add(s);
        
        
        return ret;
    }

    
}
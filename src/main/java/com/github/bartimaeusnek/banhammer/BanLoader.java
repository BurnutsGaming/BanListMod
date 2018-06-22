package com.github.bartimaeusnek.banhammer;

import java.io.File;
import java.util.ArrayList;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public final class BanLoader{
	public static final String[] HARDCODED = {
			"UUID1",
			"UUID2"
			};

    public static ArrayList<String> loadBans(FMLPreInitializationEvent event) {
    	File bandir = new File(event.getModConfigurationDirectory(), "Bans");
    	ArrayList<String> ret = new ArrayList<String>();
    	
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
        return ret;
    }

    
}
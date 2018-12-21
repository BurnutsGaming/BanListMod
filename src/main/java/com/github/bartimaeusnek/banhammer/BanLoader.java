package com.github.bartimaeusnek.banhammer;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;

import static com.github.bartimaeusnek.banhammer.BanHammer.LOGGER;

public final class BanLoader {
    public static final String[] HARDCODED = {
    };

    public static String[] Configbans = {
    };

    public static String banreason = "BanHammerModpackBan";

    public static HashSet<String> loadBans(FMLPreInitializationEvent event) {

        LOGGER.info("Loading Config");

        final Configuration banconf = new Configuration(event.getSuggestedConfigurationFile());
        final File URLS = new File(event.getSuggestedConfigurationFile().getPath().substring(0,event.getSuggestedConfigurationFile().getPath().length()-4) + "URLs.cfg");
        final File bandir = new File(event.getModConfigurationDirectory(), "Bans");
        final File bantxt = new File(BanHammer.configdir.toPath()+"/Bans.BanHammer");
        final HashSet<String> ret = new HashSet<String>();
        final HashSet<String> intxt = new HashSet<String>();

        try {
            if (URLS.exists()) {
                LOGGER.info("Loading Banlist URLs");
                final FileReader fr = new FileReader(URLS);
                final BufferedReader in = new BufferedReader(fr);
                final HashSet<String> HSURLS = new HashSet<>();
                String str;
                while ((str = in.readLine()) != null) {
                    if (!str.substring(0,1).equals("#"))
                        HSURLS.add(str);
                }
                in.close();
                fr.close();
                for (String u : HSURLS)
                    ret.addAll(fetchFromURL(new URL(u)));
            } else {
                final FileWriter fw = new FileWriter(URLS);
                final String[] info = {"#Use this file to determine URLs conatining Banned UUID", "#Lines beginning with # will be ignored", "#Only text files are allowed, in UTF-8.", "#Example: https://www.github.com/some-github-repo/some.txt"};
                for (String s : info) {
                    fw.write(s);
                    fw.write("\n");
                }
                fw.flush();
                fw.close();
            }


            if (bantxt.exists()){
                LOGGER.info("Loading Bans.txt");
                final FileReader fr = new FileReader(bantxt);
                final BufferedReader in = new BufferedReader(fr);
                String str;
                while ((str = in.readLine()) != null) {
                    ret.add(str);
                    intxt.add(str);
                }
                in.close();
                fr.close();
            }else{
                bantxt.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        BanHammer.useFiles = banconf.getBoolean("Use Ban Files","System",false,"Will generate a UUID file for each banned player");
        Configbans = banconf.getStringList("UUIDs", "Bans", Configbans, "");
        banreason = banconf.getString("Ban Reason", "Bans", banreason, "");

        if (banconf.hasChanged())
            banconf.save();

        if (!bandir.exists()) {
            bandir.mkdirs();
        }

        LOGGER.info("Loading Bans from Files");
        for (File file : bandir.listFiles()) {
            String name = file.getName();
            if (getFileExtension(file).length()>0)
                name = name.substring(0, name.length() - getFileExtension(file).length());
            ret.add(name);
        }

        LOGGER.info("Loading Hardcoded Bans");
        ret.addAll(Arrays.asList(HARDCODED));

        LOGGER.info("Loading Bans from Config");
        ret.addAll(Arrays.asList(Configbans));

        //removal of not propperly formatted UUIDs / not UUIDs
        final HashSet<String> torem = new HashSet<String>();
        for (String e : ret){
            if (!(e.length() == 36 || e.length() == 32))
                torem.add(e);
        }
        ret.removeAll(torem);

        LOGGER.info("BanHammer has loaded " +ret.size()+ " Bans!");

        if(intxt.size()<ret.size()) {
            LOGGER.info("BanHammer has found "+ (ret.size()-intxt.size()) +" new Bans, that will be added to the local Storage!");
            try {
                final FileWriter fw = new FileWriter(bantxt, true);
                for (String e : ret) {
                    fw.write(e+"\n");
                }
                fw.flush();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ret;
    }

    private static String getFileExtension(File file) {
        if (file == null || file.getName() == null)
            return "";
        final String fileName = file.getName();
        int lastdot = fileName.lastIndexOf(".");
        if (lastdot == -1)
            return "";
        return fileName.substring(lastdot);
    }

    public static HashSet<String> fetchFromURL(URL url) {
        HashSet<String> ret = new HashSet<>();
        try {
            final HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.addRequestProperty("User-Agent", "Mozilla/4.76");
            final InputStreamReader isr = new InputStreamReader(http.getInputStream());
            final BufferedReader bufferedReader = new BufferedReader(isr);
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                if (!str.substring(0,1).equals("#"))
                    ret.add(new String(str.getBytes(), "UTF-8"));
            }
            bufferedReader.close();
            isr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

}
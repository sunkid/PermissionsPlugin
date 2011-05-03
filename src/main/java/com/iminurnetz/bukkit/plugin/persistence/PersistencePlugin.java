package com.iminurnetz.bukkit.plugin.persistence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import com.iminurnetz.bukkit.plugin.BukkitPlugin;
import com.iminurnetz.bukkit.plugin.persistence.model.adapters.PlayerDataPersistAdapter;
import com.iminurnetz.bukkit.plugin.persistence.model.ProfileData;
import com.iminurnetz.bukkit.plugin.persistence.model.WorldData;

public class PersistencePlugin extends BukkitPlugin {

    @Override
    public void enablePlugin() throws Exception {
         installDDL();
         PluginManager pm = getServer().getPluginManager();
         
         PersistencePlayerListener playerListener = new PersistencePlayerListener(this);
         pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Monitor, this);
         pm.registerEvent(Event.Type.PLAYER_TELEPORT, playerListener, Priority.Monitor, this);
         
         PlayerDataPersistAdapter.setServer(getDatabase());
         
         ensureWorldExists(getServer().getWorlds());
    }
    
    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        URL url = getClass().getResource(getClass().getSimpleName() + ".class");
        if (url.getProtocol().equals("jar")) {
            JarEntry entry = null;;
            try {
                JarURLConnection con = (JarURLConnection) url.openConnection();
                JarFile archive = con.getJarFile();
                Enumeration<JarEntry> entries = archive.entries();
                while (entries.hasMoreElements()) {
                    entry = entries.nextElement();
                    if (entry.getName().contains("/model/") && entry.getName().endsWith(".class")) {
                        classes.add(Class.forName(entry.getName().replaceAll("/", ".").substring(0, entry.getName().length()-6)));
                    }
                }
            } catch (IOException e) {
                log(Level.SEVERE, "Cannot open my own JAR", e);
            } catch (ClassNotFoundException e) {
                log(Level.SEVERE, "No class found for " + entry.getName(), e);
            }
        }
        log("found " + classes.size() + " entity beans!");
        return classes;
    }
    
    @Override
    public void installDDL() {
        SpiEbeanServer serv = (SpiEbeanServer) getDatabase();
        DdlGenerator gen = serv.getDdlGenerator();

        gen.runScript(true, gen.generateCreateDdl());
        
        URL updateScript = getClass().getResource("/update.sql");
        if (updateScript != null) {
            BufferedReader in;
            StringBuffer s = new StringBuffer();
            String line;
            try {
                in = new BufferedReader(new InputStreamReader(updateScript.openStream()));
                while ((line = in.readLine()) != null) {
                    s.append(line);
                }
                in.close();
            } catch (IOException e) {
                log(Level.SEVERE, "Could not load update.sql from jar", e);
            }
            
            gen.runScript(true, s.toString());
        }
    }
    
    protected void ensureWorldExists(List<World> worlds) {
        for (World world : worlds) {
            ensureWorldExists(world.getName());
        }
    }
    
    protected void ensureWorldExists(String name) {
        EbeanServer server = getDatabase();
        synchronized(server) {
            List<WorldData> worlds = server.createQuery(WorldData.class).where().eq("name", name).findList();
            if (worlds == null || worlds.size() == 0) {
                WorldData world = new WorldData();
                world.setName(name);
                server.save(world);
            }
        }
    }
}

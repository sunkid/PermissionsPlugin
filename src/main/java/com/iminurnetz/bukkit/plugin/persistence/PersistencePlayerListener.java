package com.iminurnetz.bukkit.plugin.persistence;

import java.util.Date;
import java.util.List;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.avaje.ebean.EbeanServer;
import com.iminurnetz.bukkit.plugin.persistence.model.PlayerData;
import com.iminurnetz.bukkit.plugin.persistence.model.WorldData;

public class PersistencePlayerListener extends PlayerListener {
    
    private final PersistencePlugin plugin;
    public PersistencePlayerListener(PersistencePlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        EbeanServer server = plugin.getDatabase();
        List<PlayerData> players = server.createQuery(PlayerData.class).where().eq("name", event.getPlayer().getName()).findList();
        PlayerData p = null;
        if (players == null || players.size() == 0) {
            p = new PlayerData();
            p.setName(event.getPlayer().getName());
        } else {
            p = players.get(0);
            plugin.log(p.getName() + " was last seen " + p.getLastLogin());
        }
        
        p.setLastLogin(new Date());
        server.save(p);
    }

    @Override
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        plugin.ensureWorldExists(event.getPlayer().getWorld().getName());
    }
}

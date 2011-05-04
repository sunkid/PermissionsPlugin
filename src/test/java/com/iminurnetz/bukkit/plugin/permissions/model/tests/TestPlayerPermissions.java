package com.iminurnetz.bukkit.plugin.permissions.model.tests;

import com.avaje.ebean.Ebean;
import com.iminurnetz.bukkit.plugin.permissions.model.PlayerData;
import com.iminurnetz.bukkit.plugin.permissions.model.WorldData;

public class TestPlayerPermissions extends TestUtil {
    
    PlayerData player;
    WorldData world;
    
    public void setUp() {
        player = Ebean.find(PlayerData.class).where().ieq("name", "sunkid").findUnique();
        if (player == null) {
            player = new PlayerData();
            player.setName("sunkid");
            Ebean.save(player);
        }
        
        world = Ebean.find(WorldData.class).where().ieq("name", "world").findUnique();
        if (world == null) {
            world = new WorldData();
            world.setName("world");
            Ebean.save(world);
        }
        
    }
    
    public TestPlayerPermissions() {
        super();
    }
    
    public void testPlayerPermission() {
        player.setPermission(world.getName(), "can.do", new Boolean(true));
        Ebean.save(player);
        
        PlayerData p = Ebean.find(PlayerData.class).where().ieq("name", "sunkid").findUnique();
        assertNotNull(p);
        assertTrue((Boolean) p.getPermission(world.getName(), "can.do"));
    }
}

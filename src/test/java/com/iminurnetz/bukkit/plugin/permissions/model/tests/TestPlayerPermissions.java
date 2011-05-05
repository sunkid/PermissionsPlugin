package com.iminurnetz.bukkit.plugin.permissions.model.tests;

import com.avaje.ebean.Ebean;
import com.iminurnetz.bukkit.plugin.permissions.model.PlayerData;

public class TestPlayerPermissions extends TestUtil {
    
    public TestPlayerPermissions() {
        super();
    }
    
    public void testPlayerPermission() {
        player.setPermission(world.getName(), "can.do", true);
        Ebean.save(player);
        PlayerData p = Ebean.find(PlayerData.class).where().ieq("name", "sunkid").findUnique();
        assertTrue((Boolean) p.getPermission(world.getName(), "can.do"));
        
        player.setPermission(world.getName(), "can.do", false);
        Ebean.save(player);
        p = Ebean.find(PlayerData.class).where().ieq("name", "sunkid").findUnique();
        assertFalse((Boolean) p.getPermission(world.getName(), "can.do"));
    }
}

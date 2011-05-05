package com.iminurnetz.bukkit.plugin.permissions.model.tests;

import com.avaje.ebean.Ebean;
import com.iminurnetz.bukkit.plugin.permissions.model.GroupData;
import com.iminurnetz.bukkit.plugin.permissions.model.PlayerData;

public class TestGroupPermissions extends TestUtil {

    public TestGroupPermissions() {
        super();
    }
    
    public void testGroupOverride() {
        player.setPermission(world.getName(), "can.do", true);
        GroupData group = new GroupData("test2");
        player.getGroups(world).add(group);        
        group.setPermission(world.getName(), "cannot.do", false);
        group.setPermission(world.getName(), "can.do", false);
        Ebean.save(player);
     
        PlayerData p = Ebean.find(PlayerData.class).where().ieq("name", "sunkid").findUnique();
        assertTrue((Boolean) p.getPermission(world.getName(), "can.do"));
        assertFalse((Boolean) p.getPermission(world.getName(), "cannot.do"));
    }

}

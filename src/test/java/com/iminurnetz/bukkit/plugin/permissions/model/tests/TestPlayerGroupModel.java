package com.iminurnetz.bukkit.plugin.permissions.model.tests;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import com.iminurnetz.bukkit.plugin.permissions.model.GroupData;
import com.iminurnetz.bukkit.plugin.permissions.model.PlayerData;
import com.iminurnetz.bukkit.plugin.permissions.model.WorldData;
import com.iminurnetz.bukkit.plugin.permissions.model.adapters.GroupDataPersistAdapter;
import com.iminurnetz.bukkit.plugin.permissions.model.adapters.PlayerDataPersistAdapter;

public class TestPlayerGroupModel extends TestCase {
    
    public TestPlayerGroupModel() {       
        EbeanServer server = EbeanServerFactory.create("mysql");
        PlayerDataPersistAdapter.setServer(server);
        GroupDataPersistAdapter.setServer(server);
        DdlGenerator gen = ((SpiEbeanServer)server).getDdlGenerator();

        gen.runScript(true, gen.generateDropDdl());
        gen.runScript(true, gen.generateCreateDdl());
    }

    public void testPlayerModel() {
        PlayerData player = new PlayerData();
        player.setName("sunkid");
        Ebean.save(player);
        
        PlayerData p = Ebean.find(PlayerData.class, player.getPlayerId());
        assertEquals(player.getName(), p.getName());
        
        Ebean.delete(player);
        p = Ebean.find(PlayerData.class, player.getPlayerId());
        assertNull(p);
    }
    
    public void testPlayerGroupLink() {
        PlayerData player = new PlayerData();
        player.setName("sunkid");
        Ebean.save(player);
        
        WorldData world = new WorldData();
        world.setName("world");
        Ebean.save(world);
        
        List<GroupData> groups = new ArrayList<GroupData>();
        for (int n = 1; n <= 10; n++) {
            GroupData group = new GroupData();
            group.setName("group " + n);
            group.setGroupId(n);
            groups.add(group);
        }
        
        player.getGroups(world).addAll(groups);
        Ebean.save(player);

        PlayerData p = Ebean.find(PlayerData.class, player.getPlayerId());
        int n = 1;
        
        assertEquals(10, p.getGroups(world).size());
        
        for (GroupData group : p.getGroups(world)) {
            assertEquals("group " + n++, group.getName());
        }
        
        GroupData group = new GroupData();
        group.setName("outlier");
        Ebean.save(group);
        player.getGroups(world).add(2, group);
        Ebean.save(player);
        
        p = Ebean.find(PlayerData.class, player.getPlayerId());
        assertEquals("outlier", p.getGroups(world).get(2).getName());
        
    }
}

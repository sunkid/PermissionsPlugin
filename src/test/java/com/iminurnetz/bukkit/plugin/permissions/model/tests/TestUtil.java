package com.iminurnetz.bukkit.plugin.permissions.model.tests;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import com.iminurnetz.bukkit.plugin.permissions.model.GroupData;
import com.iminurnetz.bukkit.plugin.permissions.model.GroupProfileLink;
import com.iminurnetz.bukkit.plugin.permissions.model.PlayerData;
import com.iminurnetz.bukkit.plugin.permissions.model.PlayerGroupLink;
import com.iminurnetz.bukkit.plugin.permissions.model.PlayerProfileLink;
import com.iminurnetz.bukkit.plugin.permissions.model.ProfileData;
import com.iminurnetz.bukkit.plugin.permissions.model.WorldData;
import com.iminurnetz.bukkit.plugin.permissions.model.adapters.GroupDataPersistAdapter;
import com.iminurnetz.bukkit.plugin.permissions.model.adapters.PlayerDataPersistAdapter;

import junit.framework.TestCase;

public class TestUtil extends TestCase {
    public TestUtil() {
        ServerConfig config = new ServerConfig();
        config.setName("mysql");
        config.loadFromProperties();
        config.addClass(PlayerData.class);
        config.addClass(GroupData.class);
        config.addClass(ProfileData.class);
        config.addClass(WorldData.class);
        config.addClass(PlayerGroupLink.class);
        config.addClass(PlayerProfileLink.class);
        config.addClass(GroupProfileLink.class);
        config.addClass(GroupDataPersistAdapter.class);
        config.addClass(PlayerDataPersistAdapter.class);
        config.setDefaultServer(true);
        
        EbeanServer server = EbeanServerFactory.create(config);
        
        PlayerDataPersistAdapter.setServer(server);
        GroupDataPersistAdapter.setServer(server);
        DdlGenerator gen = ((SpiEbeanServer)server).getDdlGenerator();

        gen.runScript(true, gen.generateDropDdl());
        gen.runScript(true, gen.generateCreateDdl());
    }
    
    public void test() {
        assertTrue(true);
    }
}

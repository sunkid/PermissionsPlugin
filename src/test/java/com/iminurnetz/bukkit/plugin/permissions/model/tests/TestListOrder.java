package com.iminurnetz.bukkit.plugin.permissions.model.tests;

import junit.framework.TestCase;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import com.iminurnetz.bukkit.plugin.permissions.model.playground.LinkData;
import com.iminurnetz.bukkit.plugin.permissions.model.playground.LinkDataId;
import com.iminurnetz.bukkit.plugin.permissions.model.playground.ListData;
import com.iminurnetz.bukkit.plugin.permissions.model.playground.StringData;

public class TestListOrder extends TestCase {

    public void testOrder() {
        ServerConfig config = new ServerConfig();
        config.setName("mysql");
        config.loadFromProperties();
        config.addClass(ListData.class);
        config.addClass(LinkData.class);
        config.addClass(LinkDataId.class);
        config.addClass(StringData.class);
        config.setDefaultServer(true);
        
        EbeanServer server = EbeanServerFactory.create(config);
        
        DdlGenerator gen = ((SpiEbeanServer)server).getDdlGenerator();

        gen.runScript(true, gen.generateDropDdl());
        gen.runScript(true, gen.generateCreateDdl());

        ListData e = new ListData(1);
        e.setName("test");

        e.addWord(new StringData(1, "first"), 0);
        e.addWord(new StringData(2, "second"), 1);      
        
         // will insert
        Ebean.save(e);

        e.addWord(new StringData(3, "between"), 1);
        
        // this will update
        Ebean.update(e);

        // find the inserted entity by its id
        ListData e2 = Ebean.find(ListData.class, e.getListId());
        
        for (LinkData s : e2.getWords()) {
            System.out.println(s.getWord().getWord());
        }
 
        // Ebean.delete(e);
        // can use delete by id when you don't have the bean
        // Ebean.delete(ESimple.class, e.getId());

    }
}
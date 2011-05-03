package com.iminurnetz.bukkit.plugin.permissions.model.adapters;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Set;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Transaction;
import com.avaje.ebean.event.BeanPersistAdapter;
import com.avaje.ebean.event.BeanPersistRequest;
import com.iminurnetz.bukkit.plugin.permissions.model.GroupData;
import com.iminurnetz.bukkit.plugin.permissions.model.PlayerData;
import com.iminurnetz.bukkit.plugin.permissions.model.PlayerGroupLink;
import com.iminurnetz.bukkit.plugin.permissions.model.WorldData;

/**
 * This adapter maintains the link tables between players and groups. 
 */
public class PlayerDataPersistAdapter extends BeanPersistAdapter {
    
    private static EbeanServer server;

    @Override
    public void postLoad(Object bean, Set<String> includedProperties) {
        PlayerData player = (PlayerData) bean;
        Map<WorldData, List<GroupData>> groupMap = new HashMap<WorldData, List<GroupData>>();
        int worldId = -1;
        WorldData world;
        List<GroupData> groupList = null;
        for (PlayerGroupLink l : getPlayerGroupLinks(player)) {
            if (worldId != l.getWorldId()) {
                worldId = l.getWorldId();
                world = server.getReference(WorldData.class, worldId);
                groupList = new ArrayList<GroupData>();
                groupMap.put(world, groupList);
            }
            
            groupList.add(server.getReference(GroupData.class, l.getGroupId()));
        }
        
        Timestamp lastUpdate = player.getLastUpdate();
        player.setGroups(groupMap);
        player.setLastUpdate(lastUpdate);        
    }
    
    private List<PlayerGroupLink> getPlayerGroupLinks(PlayerData player) {
        return server.createQuery(PlayerGroupLink.class)
                       .where()
                       .eq("playerId", player.getPlayerId())
                       .orderBy("worldId asc, rankOrder asc")
                       .findList();
    }

    @Override
    public boolean isRegisterFor(Class<?> cls) {
        return cls.isAssignableFrom(PlayerData.class);
    }
    
    @Override
    public void postInsert(BeanPersistRequest<?> request) {
        persistPlayerGroupLink(request);
    }
    
    @Override
    public void postUpdate(BeanPersistRequest<?> request) {
        persistPlayerGroupLink(request);
    }

    @Override
    public void postDelete(BeanPersistRequest<?> request) {
        
        Transaction transaction = server.beginTransaction();
        
        PlayerData p = (PlayerData) request.getBean();
        List<PlayerGroupLink> links = getPlayerGroupLinks(p);
        server.delete(links.iterator(), transaction);
        
        server.commitTransaction();
    }
    
    private void persistPlayerGroupLink(BeanPersistRequest<?> request) {        
        PlayerData p = (PlayerData) request.getBean();
        Map<WorldData, List> groups = p.getGroups();
        int rankOrder = 0;
        PlayerGroupLink theLink = null;
        for (WorldData world : groups.keySet()) {
            for (GroupData group : (List<GroupData>) groups.get(world)) {
                theLink = server.createQuery(PlayerGroupLink.class)
                                    .where()
                                    .eq("playerId", p.getPlayerId())
                                    .eq("worldId", world.getWorldId())
                                    .eq("groupId", group.getGroupId())
                                    .findUnique();
                if (theLink == null) {
                    theLink = new PlayerGroupLink(p.getPlayerId(), group.getGroupId(), world.getWorldId());
                }
                theLink.setRankOrder(rankOrder++);
                server.save(theLink);
                server.save(group);
            }
        }
    }

    public static void setServer(EbeanServer server) {
        PlayerDataPersistAdapter.server = server;
    }
}

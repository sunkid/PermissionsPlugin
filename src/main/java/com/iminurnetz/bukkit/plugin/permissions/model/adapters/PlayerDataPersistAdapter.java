package com.iminurnetz.bukkit.plugin.permissions.model.adapters;

import java.util.Map;
import java.util.List;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.event.BeanPersistAdapter;
import com.avaje.ebean.event.BeanPersistRequest;
import com.iminurnetz.bukkit.plugin.permissions.model.GroupData;
import com.iminurnetz.bukkit.plugin.permissions.model.PlayerData;
import com.iminurnetz.bukkit.plugin.permissions.model.PlayerGroupLink;
import com.iminurnetz.bukkit.plugin.permissions.model.WorldData;

public class PlayerDataPersistAdapter extends BeanPersistAdapter {
    
    private static EbeanServer server;

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
        PlayerData p = (PlayerData) request.getBean();
        List<PlayerGroupLink> links = server.createQuery(PlayerGroupLink.class).where().eq("player_id", p.getPlayerId()).findList();
        server.delete(links);
    }
    
    private void persistPlayerGroupLink(BeanPersistRequest<?> request) {
        PlayerData p = (PlayerData) request.getBean();
        Map<WorldData, List> groups = p.getGroups();
        int rankOrder = 0;
        PlayerGroupLink theLink = null;
        for (WorldData world : groups.keySet()) {
            for (GroupData group : (List<GroupData>) groups.get(world)) {
                List<PlayerGroupLink> links = server.createQuery(PlayerGroupLink.class)
                                                        .where()
                                                        .eq("player_id", p.getPlayerId())
                                                        .eq("world_id", world.getWorldId())
                                                        .eq("group_id", group.getGroupId())
                                                        .findList();
                if (links != null && links.size() != 0) {
                    theLink = links.get(0);
                } else {
                    theLink = new PlayerGroupLink(p.getPlayerId(), group.getGroupId(), world.getWorldId());
                }
                theLink.setRankOrder(rankOrder++);
                server.save(theLink);
            }
        }
    }

    public static void setServer(EbeanServer server) {
        PlayerDataPersistAdapter.server = server;
    }
}

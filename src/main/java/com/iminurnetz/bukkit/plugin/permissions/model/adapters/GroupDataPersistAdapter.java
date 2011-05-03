package com.iminurnetz.bukkit.plugin.permissions.model.adapters;

import java.util.List;
import java.util.Map;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Transaction;
import com.avaje.ebean.event.BeanPersistAdapter;
import com.avaje.ebean.event.BeanPersistRequest;
import com.iminurnetz.bukkit.plugin.permissions.model.GroupData;
import com.iminurnetz.bukkit.plugin.permissions.model.GroupProfileLink;
import com.iminurnetz.bukkit.plugin.permissions.model.PlayerGroupLink;
import com.iminurnetz.bukkit.plugin.permissions.model.ProfileData;
import com.iminurnetz.bukkit.plugin.permissions.model.WorldData;

/**
 * This adapter maintains the link tables between players and groups and between groups and profiles. 
 */
public class GroupDataPersistAdapter extends BeanPersistAdapter {

    private static EbeanServer server;

    @Override
    public boolean isRegisterFor(Class<?> cls) {
        return cls.isAssignableFrom(GroupData.class);
    }

    @Override
    public void postInsert(BeanPersistRequest<?> request) {
        persistGroupProfileLink(request);
    }

    @Override
    public void postUpdate(BeanPersistRequest<?> request) {
        persistGroupProfileLink(request);
    }

    @Override
    public void postDelete(BeanPersistRequest<?> request) {
        
        Transaction transaction = server.beginTransaction();
        
        GroupData g = (GroupData) request.getBean();
        List<PlayerGroupLink> pgl = server.createQuery(PlayerGroupLink.class).where().eq("group_id", g.getGroupId()).findList();
        server.delete(pgl, transaction);
        List<GroupProfileLink> ppl = server.createQuery(GroupProfileLink.class).where().eq("group_id", g.getGroupId()).findList();
        server.delete(ppl, transaction);
        
        server.commitTransaction();
    }

    private void persistGroupProfileLink(BeanPersistRequest<?> request) {
        
        Transaction transaction = server.beginTransaction();
        
        GroupData g = (GroupData) request.getBean();
        Map<WorldData, List> profiles = g.getProfiles();
        int rankOrder = 0;
        GroupProfileLink theLink = null;
        for (WorldData world : profiles.keySet()) {
            for (ProfileData profile : (List<ProfileData>) profiles.get(world)) {
                List<GroupProfileLink> links = server.createQuery(GroupProfileLink.class)
                                                        .where()
                                                        .eq("group_id", g.getGroupId())
                                                        .eq("world_id", world.getWorldId())
                                                        .eq("profile_id", profile.getProfileId())
                                                        .findList();
                if (links != null && links.size() != 0) {
                    theLink = links.get(0);
                } else {
                    theLink = new GroupProfileLink(g.getGroupId(), profile.getProfileId(), world.getWorldId()); 
                }
                
                theLink.setRankOrder(rankOrder++);
                server.save(theLink, transaction);
            }
        }
        
        server.commitTransaction();
    }

    public static void setServer(EbeanServer server) {
        GroupDataPersistAdapter.server = server;
    }
}

package com.iminurnetz.bukkit.plugin.permissions.model.adapters;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Transaction;
import com.avaje.ebean.event.BeanPersistAdapter;
import com.avaje.ebean.event.BeanPersistRequest;
import com.iminurnetz.bukkit.plugin.permissions.model.GroupData;
import com.iminurnetz.bukkit.plugin.permissions.model.GroupProfileLink;
import com.iminurnetz.bukkit.plugin.permissions.model.PlayerData;
import com.iminurnetz.bukkit.plugin.permissions.model.PlayerGroupLink;
import com.iminurnetz.bukkit.plugin.permissions.model.ProfileData;
import com.iminurnetz.bukkit.plugin.permissions.model.WorldData;

/**
 * This adapter maintains the link tables between players and groups and between groups and profiles. 
 */
public class GroupDataPersistAdapter extends BeanPersistAdapter {

    private static EbeanServer server;

    @Override
    public void postLoad(Object bean, Set<String> includedProperties) {
        GroupData group = (GroupData) bean;
        int worldId = -1;
        WorldData world = null;
        
        Set<PlayerData> players = null;
        Map<WorldData, Set> playerMap = new HashMap<WorldData, Set>();
        group.setPlayers(playerMap);
        for (PlayerGroupLink l : getPlayerGroupLinks(group)) {
            if (worldId != l.getWorldId()) {
                worldId = l.getWorldId();
                world = server.getReference(WorldData.class, worldId);
                players = new HashSet<PlayerData>();
                playerMap.put(world, players);
            }
            
            players.add(server.getReference(PlayerData.class, l.getPlayerId()));
        }
        
        List<ProfileData> profiles = null;
        Map<WorldData, List> profileMap = new HashMap<WorldData, List>();
        Map<WorldData, ProfileData> overrideMap = new HashMap<WorldData, ProfileData>();
        worldId = -1;
        ProfileData profile = null;
        for (GroupProfileLink l : getGroupProfileLinks(group)) {
            if (worldId != l.getWorldId()) {
                worldId = l.getWorldId();
                world = server.getReference(WorldData.class, worldId);
                profiles = new ArrayList<ProfileData>();
                profileMap.put(world, profiles);
            }
            
            profile = server.getReference(ProfileData.class, l.getProfileId());
            if (l.getRankOrder() == -1) {
                overrideMap.put(world, profile);
            } else {
                profiles.add(profile);
            }
        }
        
        int version = group.getVersion();
        group.setGroupProfiles(overrideMap);
        group.setProfiles(profileMap);
        group.setVersion(version);
    }
    
    private List<GroupProfileLink> getGroupProfileLinks(GroupData group) {
        return server.createQuery(GroupProfileLink.class)
                        .where()
                        .eq("groupId", group.getGroupId())
                        .orderBy("worldId asc, rankOrder asc")                       
                        .findList();
    }

    private List<PlayerGroupLink> getPlayerGroupLinks(GroupData group) {
        return server.createQuery(PlayerGroupLink.class)
                       .where()
                       .eq("groupId", group.getGroupId())
                       .orderBy("worldId asc")
                       .findList();
    }
   
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
        server.delete(pgl.iterator(), transaction);
        
        Set<PlayerData> players = new HashSet<PlayerData>();
        for (WorldData world : g.getPlayers().keySet()) {
            for (PlayerData p : g.getPlayers(world)) {
                p.getGroups(world).remove(g);
            }
        }
        
        server.delete(getGroupProfileLinks(g).iterator(), transaction);
        
        server.commitTransaction();
    }

    private void persistGroupProfileLink(BeanPersistRequest<?> request) {
        GroupData g = (GroupData) request.getBean();
        Map<WorldData, List> profiles = g.getProfiles();
        int rankOrder = 0;
        for (WorldData world : profiles.keySet()) {
            for (ProfileData profile : (List<ProfileData>) profiles.get(world)) {
                updateLink(g, profile, world, rankOrder++);
            }
        }
        
        for (WorldData world : g.getGroupProfiles().keySet()) {
            updateLink(g, g.getGroupProfiles().get(world), world, -1);
        }
    }

    private void updateLink(GroupData group, ProfileData profile, WorldData world, int rankOrder) {
        server.save(profile);
        GroupProfileLink theLink = server.createQuery(GroupProfileLink.class)
                                            .where()
                                            .eq("groupId", group.getGroupId())
                                            .eq("worldId", world.getWorldId())
                                            .eq("profileId", profile.getProfileId())
                                            .findUnique();
        if (theLink == null) {
            theLink = new GroupProfileLink(group.getGroupId(), profile.getProfileId(), world.getWorldId()); 
        }
        
        theLink.setRankOrder(rankOrder++);
        server.save(theLink);
    }

    public static void setServer(EbeanServer server) {
        GroupDataPersistAdapter.server = server;
    }
}

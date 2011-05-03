package com.iminurnetz.bukkit.plugin.permissions.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.iminurnetz.bukkit.plugin.permissions.util.ListChangeInterceptor;

/**
 * This entity represents a group with a set of world-specific players and a list of world-specific profiles. 
 */
@Entity
@Table(name = "group_data")
public class GroupData  implements ListChangeInterceptor {
    @Id
    private int groupId;
    @Column(unique=true, nullable=false)
    private String name;
    
    @Version
    private Timestamp lastUpdate;

    public GroupData() {
        players = new HashMap<WorldData, Set>();
        profiles = new HashMap<WorldData, List>();
    }
    
    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    /**
     * The world-specific set of players that belong to this group. In an ideal world,
     * these would be maintained by a many-to-many relationship. Alas, there is no good support for
     * the order of lists in ebean.
     */
    @Transient
    private Map<WorldData, Set> players;
    
    /**
     * The world-specific list of profiles that are associated with this group. In an ideal world,
     * these would be maintained by a many-to-many relationship. Alas, there is no good support for
     * the order of lists in ebean.
     */
    @Transient
    private Map<WorldData, List> profiles;
    
    public Map<WorldData, Set> getPlayers() {
        return players;
    }

    public void setPlayers(Map<WorldData, Set> players) {
        this.players = players;
    }
    
    public Set<PlayerData> getPlayers(WorldData world) {
        if (!players.containsKey(world)) {
            players.put(world, new HashSet<PlayerData>());
        }
        return players.get(world);
    }

    public void addPlayers(WorldData world, List<PlayerData> thePlayers) {
        if (!players.containsKey(world)) {
            players.put(world, new HashSet<PlayerData>());
        }
        players.get(world).addAll(thePlayers);
    }
    
    public void addPlayer(WorldData world, PlayerData player) {
        addPlayers(world, Arrays.asList(player));
    }

    public void setProfiles(Map<WorldData, List> profiles) {
        this.profiles = profiles;
        callPostIntercept();
    }

    public Map<WorldData, List> getProfiles() {
        return profiles;
    }
    
    public List<ProfileData> getProfiles(WorldData world) {
        if (!profiles.containsKey(world)) {
            List<ProfileData> l = new ArrayList<ProfileData>();
            profiles.put(world, l);
            callPostIntercept();
        }
        return profiles.get(world);
    }
    
    public void setProfiles(WorldData world, List<ProfileData> profiles) {
        this.profiles.put(world, profiles);
        callPostIntercept();
    }

    public void callPostIntercept() {
        setLastUpdate(new Timestamp(new Date().getTime()));
    }
}

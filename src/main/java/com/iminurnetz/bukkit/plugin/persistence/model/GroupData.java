package com.iminurnetz.bukkit.plugin.persistence.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "group")
public class GroupData {
    @Id
    private int groupId;
    private String name;

    @Transient
    private Map<WorldData, Set<PlayerData>> players;
    
    @Transient
    private Map<WorldData, List<ProfileData>> profiles;
    
    public GroupData() {
        players = new HashMap<WorldData, Set<PlayerData>>();
        profiles = new HashMap<WorldData, List<ProfileData>>();
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

    public Map<WorldData, Set<PlayerData>> getPlayers() {
        return players;
    }

    public void setPlayers(Map<WorldData, Set<PlayerData>> players) {
        this.players = players;
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

    public void setProfiles(Map<WorldData, List<ProfileData>> profiles) {
        this.profiles = profiles;
    }

    public Map<WorldData, List<ProfileData>> getProfiles() {
        return profiles;
    }
    
    public List<ProfileData> getProfiles(WorldData world) {
        if (!profiles.containsKey(world)) {
            List<ProfileData> l = new ArrayList<ProfileData>();
            profiles.put(world, l);
        }
        return profiles.get(world);
    }
    
    public void setProfiles(WorldData world, List<ProfileData> profiles) {
        this.profiles.put(world, profiles);
    }

    public Set<PlayerData> getPlayers(WorldData world) {
        if (!players.containsKey(world)) {
            players.put(world, new HashSet<PlayerData>());
        }
        return players.get(world);
    }
}

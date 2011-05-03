package com.iminurnetz.bukkit.plugin.persistence.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "player")
public class PlayerData {
    @Id
    private int playerId;
    private String name;

    private Date lastLogin;
    
    @Transient
    private Map<WorldData, List<GroupData>> groups;
    
    public PlayerData() {
        groups = new HashMap<WorldData, List<GroupData>>();
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setGroups(HashMap<WorldData, List<GroupData>> groupMap) {
        groups = new HashMap<WorldData, List<GroupData>>();
        for (WorldData world : groupMap.keySet()) {
            setGroups(world, groupMap.get(world));
        }
    }

    public Map<WorldData, List<GroupData>> getGroups() {
        return groups;
    }

    public void setGroups(WorldData world, List<GroupData> groups) {
        this.groups.put(world, groups);
        for (GroupData group : groups) {
            group.addPlayer(world, this);
        }
    }
    
    public List<GroupData> getGroups(WorldData world) {
        if (!groups.containsKey(world)) {
            groups.put(world, new ArrayList<GroupData>());
        }
        return groups.get(world);
    }
}

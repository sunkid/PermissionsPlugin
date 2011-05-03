package com.iminurnetz.bukkit.plugin.permissions.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.iminurnetz.bukkit.plugin.permissions.util.ListChangeMonitor;
import com.iminurnetz.bukkit.plugin.permissions.util.MonitoredList;

/**
 * This entity represents a player with a list of world-specific groups. 
 */
@Entity
@Table(name = "player_data")
public class PlayerData implements ListChangeMonitor {
    @Id
    private int playerId;
    @Column(unique=true, nullable=false)
    private String name;

    private Date lastLogin;
    
    @Version
    private Timestamp lastUpdate;
    
    
    public PlayerData() {
        groups = new HashMap<WorldData, List>();
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

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    /**
     * The world-specific list of groups that this player belongs to. In an ideal world,
     * these would be maintained by a many-to-many relationship. Alas, there is no good support for
     * the order of lists in ebean.
     */
    @Transient
    private Map<WorldData, List> groups;

    public void setGroups(Map<WorldData, List<GroupData>> groupMap) {
        groups = new HashMap<WorldData, List>();
        for (WorldData world : groupMap.keySet()) {
            setGroups(world, groupMap.get(world));
        }
        onMonitoredListChange();
    }

    public Map<WorldData, List> getGroups() {
        return groups;
    }

    public void setGroups(WorldData world, List<GroupData> groups) {
        this.groups.put(world, groups);
        for (GroupData group : groups) {
            group.addPlayer(world, this);
        }
        onMonitoredListChange();
    }
    
    public List<GroupData> getGroups(WorldData world) {
        if (!groups.containsKey(world)) {
            groups.put(world, new MonitoredList<GroupData>(new ArrayList<GroupData>(), this));
            onMonitoredListChange();
        }
        return groups.get(world);
    }

    public void onMonitoredListChange() {
        setLastUpdate(new Timestamp(new Date().getTime()));
    }

}

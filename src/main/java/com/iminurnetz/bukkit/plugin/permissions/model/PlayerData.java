package com.iminurnetz.bukkit.plugin.permissions.model;

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

import com.iminurnetz.bukkit.plugin.permissions.Profile;
import com.iminurnetz.bukkit.plugin.permissions.util.MonitoredList;

/**
 * This entity represents a player with a list of world-specific groups. 
 */
@Entity
@Table(name = "player_data")
public class PlayerData extends AbstractPermissionHandler {
    @Id
    private int playerId;
    @Column(unique=true, nullable=false)
    private String name;
    
    @Transient
    private Map<WorldData, ProfileData> playerProfile;

    private int rank;
    
    private int version;

    public PlayerData() {
        groups = new HashMap<WorldData, List>();
        playerProfile = new HashMap<WorldData, ProfileData>();
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

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

    public void setPlayerProfile(Map<WorldData, ProfileData> playerProfile) {
        this.playerProfile = playerProfile;
        postUpdate();
    }

    public Map<WorldData, ProfileData> getPlayerProfile() {
        return playerProfile;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
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
        postUpdate();
    }

    public Map<WorldData, List> getGroups() {
        return groups;
    }

    public void setGroups(WorldData world, List<GroupData> groups) {
        this.groups.put(world, groups);
        for (GroupData group : groups) {
            group.addPlayer(world, this);
        }
        postUpdate();
    }
    
    public List<GroupData> getGroups(WorldData world) {
        if (!groups.containsKey(world)) {
            groups.put(world, new MonitoredList<GroupData>(new ArrayList<GroupData>(), this));
            postUpdate();
        }
        return groups.get(world);
    }

    public void postUpdate() {
        setVersion(version++);
    }

    @Override
    public <T> T getPermission(String world, String permission) {
        WorldData worldData = getWorldData(world);
        Profile profile = getProfileData(worldData).getProfile();
        if (profile != null && profile.getData().containsKey(permission)) {
            return (T) profile.getData().get(permission);
        }
        
        for (GroupData group : getGroups(worldData)) {
            if (group.getPermission(worldData.getName(), permission) != null) {
                return (T) group.getPermission(worldData.getName(), permission);
            }
        }
        
        return null;
    }

    protected ProfileData getProfileData(WorldData worldData) {
        if (!playerProfile.containsKey(worldData)) {
            ProfileData override = new ProfileData(getName() + "_" + worldData.getName() + "_override");
            playerProfile.put(worldData, override);
        }
        return playerProfile.get(worldData);
    }
}

package com.iminurnetz.bukkit.plugin.permissions.model;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.iminurnetz.bukkit.plugin.permissions.Profile;
import com.iminurnetz.bukkit.plugin.permissions.util.MonitoredList;

/**
 * This entity represents a group with a set of world-specific players and a list of world-specific profiles. 
 */
@Entity
@Table(name = "group_data")
public class GroupData extends AbstractPermissionHandler {
    @Id
    private int groupId;
    @Column(unique=true, nullable=false)
    private String name;
    
    private int rank;
    
    private int version;

    public GroupData() {
        players = new HashMap<WorldData, Set>();
        profiles = new HashMap<WorldData, List>();
        groupProfiles = new HashMap<WorldData, ProfileData>();
    }
    
    public GroupData(String name) {
        this();
        this.name = name;
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

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
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
    
    @Transient
    private Map<WorldData, ProfileData> groupProfiles;
    
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
        postUpdate();
    }

    public Map<WorldData, List> getProfiles() {
        return profiles;
    }
    
    public List<ProfileData> getProfiles(WorldData world) {
        if (!profiles.containsKey(world)) {
            profiles.put(world, new MonitoredList<ProfileData>(new ArrayList<ProfileData>(), this));
            postUpdate();
        }
        return profiles.get(world);
    }
    
    public void setProfiles(WorldData world, List<ProfileData> profiles) {
        this.profiles.put(world, profiles);
        postUpdate();
    }

    public void setGroupProfiles(Map<WorldData, ProfileData> groupProfile) {
        this.groupProfiles = groupProfile;
        postUpdate();
    }

    public Map<WorldData, ProfileData> getGroupProfiles() {
        return groupProfiles;
    }

    public <T> T getPermission(String world, String permission) {
        WorldData worldData = getWorldData(world);
        Profile profile = getProfileData(worldData).getProfile();
        if (profile != null && profile.getData().containsKey(permission)) {
            return (T) profile.getData().get(permission);
        }

        for (ProfileData profileData : getProfiles(worldData)) {
            profile = profileData.getProfile();
            if (profile != null && profile.getData().containsKey(permission)) {
                return (T) profile.getData().get(permission);
            }
        }
        
        return null;
    }

    public ProfileData getProfileData(WorldData worldData) {
        if (!groupProfiles.containsKey(worldData)) {
            ProfileData override = new ProfileData(getName() + "_" + worldData.getName() + "_override");
            groupProfiles.put(worldData, override);
        }
        return groupProfiles.get(worldData);
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

}

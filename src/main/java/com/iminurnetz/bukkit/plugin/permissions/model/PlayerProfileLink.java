package com.iminurnetz.bukkit.plugin.permissions.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * This entity represents the link table between players and profiles. This table is maintained by the
 * {@link PlayerDataPersistAdapter} adapter.
 */
@Entity
@Table(name = "player_profile_link")
public class PlayerProfileLink {
    private int playerId;
    private int worldId;
    private int profileId;
    
    public PlayerProfileLink() {
    }
    
    public PlayerProfileLink(int playerId, int profileId, int worldId) {
        this.playerId = playerId;
        this.profileId = profileId;
        this.worldId = worldId;
    }
    
    public int getPlayerId() {
        return playerId;
    }
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
    public int getWorldId() {
        return worldId;
    }
    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }
    public int getProfileId() {
        return profileId;
    }
    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }
}

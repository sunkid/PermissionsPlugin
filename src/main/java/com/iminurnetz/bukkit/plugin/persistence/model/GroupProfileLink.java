package com.iminurnetz.bukkit.plugin.persistence.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "group_profile_link")
public class GroupProfileLink {
    private int groupId;
    private int profileId;
    private int worldId;
    private int rankOrder;
    
    public GroupProfileLink() {
    }
    
    public GroupProfileLink(int groupId, int profileId, int worldId) {
        this.groupId = groupId;
        this.profileId = profileId;
        this.worldId = worldId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public int getWorldId() {
        return worldId;
    }

    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }

    public int getRankOrder() {
        return rankOrder;
    }

    public void setRankOrder(int rankOrder) {
        this.rankOrder = rankOrder;
    }
}

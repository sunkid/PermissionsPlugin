package com.iminurnetz.bukkit.plugin.persistence.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "player_group_link")
public class PlayerGroupLink {
    private int playerId;
    private int groupId;
    private int worldId;
    private int rankOrder;

    public PlayerGroupLink(int playerId, int groupId, int worldId) {
        this.playerId = playerId;
        this.groupId = groupId;
        this.worldId = worldId;
    }

    public PlayerGroupLink() {
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getWorldId() {
        return worldId;
    }

    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }

    public void setRankOrder(int rankOrder) {
        this.rankOrder = rankOrder;
    }

    public int getRankOrder() {
        return rankOrder;
    }
}

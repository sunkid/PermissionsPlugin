package com.iminurnetz.bukkit.plugin.permissions.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A simple representation of a world in the database. 
 */
@Entity
@Table(name = "world_data")
public class WorldData {
    @Id
    private int worldId;
    @Column(unique=true, nullable=false)
    private String name;

    public int getWorldId() {
        return worldId;
    }

    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

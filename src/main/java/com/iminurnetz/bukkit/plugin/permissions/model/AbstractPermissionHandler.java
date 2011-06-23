package com.iminurnetz.bukkit.plugin.permissions.model;

import java.io.Serializable;

import org.bukkit.World;
import org.bukkit.permission.Permissions;
import org.bukkit.permission.Rank;

import com.avaje.ebean.Ebean;
import com.iminurnetz.bukkit.plugin.permissions.util.ListChangeMonitor;

import javax.persistence.Entity;

@Entity
public abstract class AbstractPermissionHandler implements ListChangeMonitor, Permissions, Rank  {

    public <T> T getPermission(World world, String permission) {
        return (T) getPermission(world.getName(), permission);
    }
    
    public boolean isPermissionSet(World world, String permission) {
        return isPermissionSet(world.getName(), permission);
    }
    
    public boolean isPermissionSet(String world, String permission) {
        return getPermission(world, permission) != null;
    }

    public void setPermission(World world, String permission, Serializable value) {
        setPermission(world.getName(), permission, value);
    }
    
    public void setPermission(String world, String permission, Serializable value) {
        WorldData worldData = getWorldData(world);
        getProfileData(worldData).put(permission, value);
        postUpdate();
    }

    public void unsetPermission(World world, String permission) {
        unsetPermission(world.getName(), permission);
    } 
    
    public void unsetPermission(String world, String permission) {
        WorldData worldData = getWorldData(world);
        getProfileData(worldData).remove(permission);
        postUpdate();
    }
    
    protected WorldData getWorldData(String world) {
        WorldData w = Ebean.find(WorldData.class).where().eq("name", world).findUnique();
        if (w == null) {
            w = new WorldData(world);
            Ebean.save(w);
        }
        
        return w;
    }

    public void postUpdate() {
        int version = getVersion();
        setVersion(++version);
    }

    public abstract <T> T getPermission(String world, String permission);
    public abstract int getVersion();
    public abstract void setVersion(int version);
    protected abstract ProfileData getProfileData(WorldData worldData);
}

package com.iminurnetz.bukkit.plugin.permissions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A very simple representation of a profile.
 */
public class Profile implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private final Map<String, Serializable> data;

    public Profile() {
        data = new HashMap<String, Serializable>();
    }

    public Map<String, Serializable> getData() {
        return data;
    }
}

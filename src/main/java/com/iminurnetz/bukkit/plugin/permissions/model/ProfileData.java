package com.iminurnetz.bukkit.plugin.permissions.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.iminurnetz.bukkit.plugin.permissions.Profile;


/**
 * This entity persists profile data as a serialized java object to the database. 
 */
@Entity
@Table(name = "profile_data")
public class ProfileData {
    @Id
    private int profileId;
    @Column(unique=true, nullable=false)
    private String name;

    @Lob
    @Basic(fetch=FetchType.EAGER)
    private byte[] data;
    
    @Transient
    private Profile profile;

    public ProfileData() {
        profile = new Profile();
    }
    
    public ProfileData(String name) {
        this();
        this.name = name;
    }
    
    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    /**
     * Retrieve a copy of the stored profile.
     * @return a de-serialized copy of the stored profile.
     */
    public Profile getProfile() {
        System.err.println("retrieving profile");
        convertProfileFromData();
        return profile;
    }

    /**
     * Set the profile data for storage in the DB. The provided profile is serialized.
     * @param profile the Profile to store.
     */
    public void setProfile(Profile profile) {
        System.err.println("setting profile");
        this.profile = profile;
        convertDataFromProfile();
    }
    
    private void convertProfileFromData() {
        System.err.println("converting profile from data " + (getData() == null ? 0 : data.length));
        if (getData() == null) {
            profile = new Profile();
        } else {
            ObjectInputStream ois;
            try {
                ois = new ObjectInputStream(new ByteArrayInputStream(getData()));
                Object o = ois.readObject();
                ois.close();
                profile = (Profile) o;
            } catch (Exception e) {
                e.printStackTrace(System.err);
                profile = null;
            }
        }
    }
    
    private void convertDataFromProfile() {
        System.err.println("converting data from profile " + (data == null ? 0 : data.length));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(profile);
            oos.close();
            data = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace(System.err);
            data = null;
        }
    }

    public void put(String permission, Serializable value) {
        profile.getData().put(permission, value);
        convertDataFromProfile();
    }

    public void remove(String permission) {
        profile.getData().remove(permission);
        convertDataFromProfile();
    }

    public <T> T get(String permission) {
        convertProfileFromData();
        return (T) profile.getData().get(permission);
    }
}

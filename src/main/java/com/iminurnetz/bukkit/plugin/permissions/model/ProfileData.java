package com.iminurnetz.bukkit.plugin.permissions.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Column;

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

    public Profile getProfile() {
        if (getData() == null) {
            return new Profile();
        }
        
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(getData()));
            Object o = ois.readObject();
            ois.close();
            return (Profile) o;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setProfile(Profile profile) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(profile);
            oos.close();
            setData(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            setData(null);
        }
    }
}

package com.iminurnetz.bukkit.plugin.permissions.model.tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.avaje.ebean.Ebean;
import com.iminurnetz.bukkit.plugin.permissions.Profile;
import com.iminurnetz.bukkit.plugin.permissions.model.ProfileData;

public class TestProfileData extends TestUtil {
    public TestProfileData() {
        super();
    }
    
    public void testProfileData() {
        ProfileData pd = new ProfileData("test");
        Profile profile;

        pd.put("cannot.do", false);
        pd.put("can.do", true);
        Ebean.save(pd);
        
        profile = pd.getProfile();
        byte[] data = null;
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
        
        System.err.println("length is " + data.length);
        
        ProfileData pd2 = Ebean.find(ProfileData.class, pd.getProfileId());
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(data));
            Object o = ois.readObject();
            ois.close();
            profile = (Profile) o;
        } catch (Exception e) {
            e.printStackTrace();
            profile = null;
        }

        data = pd2.getData();
        System.err.println("length is " + data.length);

        assertTrue((Boolean) profile.getData().get("can.do"));
        assertFalse((Boolean) profile.getData().get("cannot.do"));        
    }
}

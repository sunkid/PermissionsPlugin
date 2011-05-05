package com.iminurnetz.bukkit.plugin.permissions.model.tests;

import com.avaje.ebean.Ebean;
import com.iminurnetz.bukkit.plugin.permissions.model.ProfileData;

public class TestProfileData extends TestUtil {
    public TestProfileData() {
        super();
    }
    
    public void testProfileData() {
        ProfileData pd = new ProfileData("test");
        pd.put("cannot.do", false);
        pd.put("can.do", true);
        Ebean.save(pd);
                
        ProfileData pd2 = Ebean.find(ProfileData.class, pd.getProfileId());
        assertTrue((Boolean) pd2.get("can.do"));
        assertFalse((Boolean) pd2.get("cannot.do"));        
    }
}

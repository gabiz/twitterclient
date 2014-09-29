package com.gabiq.twitterpro.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Serializable {
    private static final long serialVersionUID = -2759720950518584160L;
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    
    
    public static User fromJSON(JSONObject json) {
        User user = new User();
        
        try {
            user.name = json.getString("name");
            user.uid = json.getLong("id");
            user.screenName = json.getString("screen_name");
            user.profileImageUrl = json.getString("profile_image_url");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return user;
    }

    
    public String getName() {
        return name;
    }


    public long getUid() {
        return uid;
    }


    public String getScreenName() {
        return screenName;
    }


    public String getProfileImageUrl() {
        return profileImageUrl;
    }
 
    
}

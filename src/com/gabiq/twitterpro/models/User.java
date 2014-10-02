package com.gabiq.twitterpro.models;

import java.io.Serializable;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Users")
public class User extends Model implements Serializable {
    private static final long serialVersionUID = -2759720950518584160L;

    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid;

    @Column(name = "name")
    private String name;

    @Column(name = "screenname")
    private String screenName;

    @Column(name = "profileImageUrl")
    private String profileImageUrl;
    
    public User(){
       super();
    }
    
    public List<Tweet> items() {
        return getMany(Tweet.class, "User");
    }

    
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
        return "@"+screenName;
    }


    public String getProfileImageUrl() {
        return profileImageUrl;
    }
 
    public static User byId(long uid) {
        return new Select().from(User.class).where("uid = ?", uid).executeSingle();
    }

}

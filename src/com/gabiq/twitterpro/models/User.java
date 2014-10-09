package com.gabiq.twitterpro.models;

import java.io.Serializable;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

// JSON example for user

//"user":  {
//    "id": 14312122,
//    "id_str": "14312122",
//    "name": "Gabi Zuniga",
//    "screen_name": "gabiz",
//    "location": "",
//    "description": "",
//    "url": "http://t.co/4kaqeFiot4",
//    "entities":  {
//      "url":  {
//        "urls":  [
//           {
//            "url": "http://t.co/4kaqeFiot4",
//            "expanded_url": "http://about.me/gabiz",
//            "display_url": "about.me/gabiz",
//            "indices":  [
//              0,
//              22
//            ]
//          }
//        ]
//      },
//      "description":  {
//        "urls":  []
//      }
//    },
//    "protected": false,
//    "followers_count": 13,
//    "friends_count": 6,
//    "listed_count": 1,
//    "created_at": "Sat Apr 05 19:59:53 +0000 2008",
//    "favourites_count": 0,
//    "utc_offset": -25200,
//    "time_zone": "Pacific Time (US & Canada)",
//    "geo_enabled": false,
//    "verified": false,
//    "statuses_count": 28,
//    "lang": "en",
//    "contributors_enabled": false,
//    "is_translator": false,
//    "is_translation_enabled": false,
//    "profile_background_color": "FFFFFF",
//    "profile_background_image_url": "http://pbs.twimg.com/profile_background_images/516106218146758656/cKHJfwM6.png",
//    "profile_background_image_url_https": "https://pbs.twimg.com/profile_background_images/516106218146758656/cKHJfwM6.png",
//    "profile_background_tile": false,
//    "profile_image_url": "http://pbs.twimg.com/profile_images/52503798/gabi2_normal.JPG",
//    "profile_image_url_https": "https://pbs.twimg.com/profile_images/52503798/gabi2_normal.JPG",
//    "profile_banner_url": "https://pbs.twimg.com/profile_banners/14312122/1411884444",
//    "profile_link_color": "000080",
//    "profile_sidebar_border_color": "FFFFFF",
//    "profile_sidebar_fill_color": "D86E3F",
//    "profile_text_color": "000000",
//    "profile_use_background_image": true,
//    "default_profile": false,
//    "default_profile_image": false,
//    "following": false,
//    "follow_request_sent": false,
//    "notifications": false
//  },

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
    
    @Column(name = "following")
    private boolean following;

    @Column(name = "followersCount")
    private int followersCount;

    @Column(name = "friendsCount")
    private int friendsCount;

    @Column(name = "listed_count")
    private int listed_count;

    @Column(name = "statusesCount")
    private int statusesCount;
    
    @Column(name = "bannerUrl")
    private String bannerUrl;
    
    @Column(name= "description")
    private String description;
    
    
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
            if (json.has("profile_banner_url")) {
                user.bannerUrl = json.getString("profile_banner_url");
            } else {
                user.bannerUrl = "";
            }
            user.following = json.getBoolean("following");
            user.followersCount = json.getInt("followers_count");
            user.friendsCount = json.getInt("friends_count");
            user.statusesCount = json.getInt("statuses_count");
            user.listed_count = json.getInt("listed_count");
            user.description = json.getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return user;
    }

    
    public String getDescription() {
        return description;
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
 
    public String getBannerUrl() {
        return bannerUrl;
    }
 
    
    
    public static User byId(long uid) {
        return new Select().from(User.class).where("uid = ?", uid).executeSingle();
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public boolean isFollowing() {
        return following;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public int getListed_count() {
        return listed_count;
    }

    public int getStatusesCount() {
        return statusesCount;
    }

}

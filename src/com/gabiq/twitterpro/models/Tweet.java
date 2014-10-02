package com.gabiq.twitterpro.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Tweets")
public class Tweet extends Model implements Serializable {

    private static final long serialVersionUID = 4249504467131192979L;
    
    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid;

    @Column(name = "body")
    private String body;
    
    @Column(name = "createdAt")
    private String createdAt;

    @Column(name = "User", onUpdate = ForeignKeyAction.CASCADE, onDelete = ForeignKeyAction.CASCADE)
    private User user;

    @Column(name = "url")
    private String url;
    
    public Tweet(){
       super();
    }

    
    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            User newUser = User.fromJSON(jsonObject.getJSONObject("user"));
            User savedUser = User.byId(newUser.getUid());
            if (savedUser != null) {
                tweet.user = savedUser;
            } else {
                tweet.user = newUser;
            }
            
            tweet.user.save();
            
            JSONObject entities = jsonObject.getJSONObject("entities");
            if (entities != null && entities.has("media")) {
                Log.d("INFO", "###################### has media");
                JSONArray media = entities.getJSONArray("media");
                if (media.length() > 0) {
                    tweet.url = ((JSONObject) media.get(0)).getString("media_url");
                    Log.d("INFO", tweet.url);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = Tweet.fromJSON(tweetJson);
            if (tweet != null) {
                tweet.save();
                tweets.add(tweet);
            }
        }

        return tweets;
    }
    
    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }
    
    public Spanned getFullName() {
        return Html.fromHtml("<b>" + user.getName() +
                "</b><font color=\"#b0b0b0\"> " + user.getScreenName() + "</font>");
    }
    
    public User getUser() {
        return user;
    }
    
    public String getUrl() {
        return url;
    }
    public String getRelativeTime() {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);
     
        String relativeDate = "";
        try {
            long dateMillis = sf.parse(createdAt).getTime();
            long interval = (System.currentTimeMillis() / 1000 - dateMillis/1000);

            String timeStamp = "";
            if (interval < 0) {
                timeStamp = "now";
            } else if (interval > 60*60*24*30) {
                timeStamp = interval / (60*60*24*30) + "M";
            } else if (interval > 60*60*24*7) {
                timeStamp = interval / (60*60*24*7) + "w";
            } else if (interval > 60*60*24) {
                timeStamp = interval / (60*60*24) + "d";
            } else if (interval > 60*60) {
                timeStamp = interval / (60*60) + "h";
            } else if (interval > 60) {
                timeStamp = interval / 60 + "m";
            } else {
                timeStamp = interval + "s";
            }
            relativeDate = timeStamp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
     
        return relativeDate;
        
        
    }

    
    public String getFormatedTime() {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        try {
            Date date = sf.parse(createdAt);
            return new SimpleDateFormat("hh:mm aaa á dd MMM yy").format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
        
    }
    
    public static Tweet byId(long uid) {
        return new Select().from(Tweet.class).where("uid = ?", uid).executeSingle();
    }

    public static List<Tweet> recentItems() {
        return new Select().from(Tweet.class).orderBy("uid DESC").limit("25").execute();
    }

}

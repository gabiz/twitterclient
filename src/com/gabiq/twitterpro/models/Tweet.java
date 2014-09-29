package com.gabiq.twitterpro.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.Html;
import android.text.Spanned;

public class Tweet implements Serializable {

    private static final long serialVersionUID = 4249504467131192979L;
    private String body;
    private long uid;
    private String createdAt;
    private User user;
    
    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
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
                "</b><font color=\"#b0b0b0\"> @" + user.getScreenName() + "</font>");
    }
    
    public User getUser() {
        return user;
    }
    
    
    public static String formatRelativeTime(long interval) {
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
        return timeStamp;
    }

    
 // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo() {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);
     
        String relativeDate = "";
        try {
            long dateMillis = sf.parse(createdAt).getTime();
            relativeDate = formatRelativeTime(System.currentTimeMillis() / 1000 - dateMillis/1000);
//            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
//                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
     
        return relativeDate;
    }
    
}

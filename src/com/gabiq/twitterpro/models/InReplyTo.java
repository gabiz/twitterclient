package com.gabiq.twitterpro.models;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class InReplyTo {

//    "in_reply_to_status_id": 517530850242486300,
//    "in_reply_to_status_id_str": "517530850242486273",
//    "in_reply_to_user_id": 14312122,
//    "in_reply_to_user_id_str": "14312122",
//    "in_reply_to_screen_name": "gabiz",

    
    private String statusId;
    private String userId;
    private String screenName;
    
    public static InReplyTo fromJSON(JSONObject jsonObject) {
        InReplyTo inReplyTo = new InReplyTo();
        
        try {
            inReplyTo.statusId = jsonObject.getString("in_reply_to_status_id_str");
            inReplyTo.userId = jsonObject.getString("in_reply_to_user_id_str");
            inReplyTo.screenName = jsonObject.getString("in_reply_to_screen_name");
            
        } catch (JSONException e) {
            Log.e("REPLY", "parsing reply_to " + e.toString());
            return null;
        }
        return inReplyTo;
    }

}

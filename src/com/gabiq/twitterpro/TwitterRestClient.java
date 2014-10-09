package com.gabiq.twitterpro;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class TwitterRestClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
    public static final String REST_URL_SCHEME = "https";
    public static final String REST_URL_DOMAIN = "api.twitter.com";
    public static final String REST_URL_VERSION = "1.1";
    public static final String REST_URL_STATUSES = "statuses";
    
    public static final String REST_URL = "https://api.twitter.com/1.1/";
    public static final String REST_CONSUMER_KEY = "pWIXrNTpZWgMpiraXGm3e9Ncc";
    public static final String REST_CONSUMER_SECRET = "m5uvMowg6VVhzWekd940qjP9zQzt76IBhGFC5DmO4jJc8MZ1qE";
    public static final String REST_CALLBACK_URL = "oauth://twitterpro";

    private static String HOME_TIMELINE_URL = "home_timeline.json";
    private static String MENTIONS_TIMELINE_URL = "mentions_timeline.json";
    private static String USER_TIMELINE_URL = "user_timeline.json";

    private static String STATUS_UPDATE_URL = "statuses/update.json";
    private static String USER_INFO_URL = "/account/verify_credentials.json";
    private static String REPLY_URL = "statuses/update.json";

    public TwitterRestClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY,
                REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    public void getTimeline(String apiUrl, long uid, long maxId,
            long sinceId, AsyncHttpResponseHandler handler) {
        
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(REST_URL_SCHEME).authority(REST_URL_DOMAIN)
                .appendPath(REST_URL_VERSION).appendPath(REST_URL_STATUSES).appendPath(apiUrl);
        
        if (uid != 0) {
            builder.appendQueryParameter("user_id", String.valueOf(uid));
        }
        if (maxId != 0) {
            builder.appendQueryParameter("max_id", String.valueOf(maxId));
        }
        if (sinceId != 0) {
            builder.appendQueryParameter("since_id", String.valueOf(sinceId));
        }

        String finalUrl = builder.build().toString();
        
        Log.d("INFO", "#### url = "+finalUrl);
        client.get(finalUrl, null, handler);
    }

    public void getHomeTimeline(long maxId, long sinceId,
            AsyncHttpResponseHandler handler) {
        getTimeline(HOME_TIMELINE_URL, 0, maxId, sinceId, handler);
    }

    public void getMentionsTimeline(long maxId, long sinceId,
            AsyncHttpResponseHandler handler) {
        getTimeline(MENTIONS_TIMELINE_URL, 0, maxId, sinceId, handler);
    }

    public void getUserTimeline(long uid, long maxId, long sinceId,
            AsyncHttpResponseHandler handler) {
        getTimeline(USER_TIMELINE_URL, uid, maxId, sinceId, handler);
    }

    public void postUpdateStatus(String text, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(STATUS_UPDATE_URL);
        String encodedText;
        try {
            encodedText = URLEncoder.encode(text, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return;
        }
        apiUrl = apiUrl + "?status=" + encodedText;

        client.post(apiUrl, null, handler);
    }

    public void getUserInfo(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(USER_INFO_URL);
        client.get(apiUrl, null, handler);
    }

    public void postReplyStatus(String text, String status_id,
            AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(REPLY_URL);
        RequestParams params = new RequestParams();
        params.put("status", text);
        params.put("in_reply_to_status_id", status_id);
        client.post(apiUrl, params, handler);
    }

}
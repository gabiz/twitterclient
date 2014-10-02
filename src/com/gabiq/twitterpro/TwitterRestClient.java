package com.gabiq.twitterpro;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class TwitterRestClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
    public static final String REST_URL = "https://api.twitter.com/1.1/";
    public static final String REST_CONSUMER_KEY = "pWIXrNTpZWgMpiraXGm3e9Ncc";
    public static final String REST_CONSUMER_SECRET = "m5uvMowg6VVhzWekd940qjP9zQzt76IBhGFC5DmO4jJc8MZ1qE";
    public static final String REST_CALLBACK_URL = "oauth://twitterpro";

    private static String HOME_TIMELINE_URL = "statuses/home_timeline.json";
    private static String STATUS_UPDATE_URL = "statuses/update.json";
    private static String USER_INFO_URL = "/account/verify_credentials.json";
    private static String REPLY_URL = "statuses/update.json";

    public TwitterRestClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY,
                REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    public void getHomeTimeline(long maxId, long sinceId,
            AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(HOME_TIMELINE_URL);
        if (maxId != 0) {
            apiUrl = apiUrl + "?max_id=" + String.valueOf(maxId);
        } else if (sinceId != 0) {
            apiUrl = apiUrl + "?since_id=" + String.valueOf(sinceId);
        }

        client.get(apiUrl, null, handler);
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
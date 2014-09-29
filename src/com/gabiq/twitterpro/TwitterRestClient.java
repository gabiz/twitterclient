package com.gabiq.twitterpro;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterRestClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "pWIXrNTpZWgMpiraXGm3e9Ncc";       // Change this
	public static final String REST_CONSUMER_SECRET = "m5uvMowg6VVhzWekd940qjP9zQzt76IBhGFC5DmO4jJc8MZ1qE"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://twitterpro"; // Change this (here and in manifest)

    private static String HOME_TIMELINE_URL = "statuses/home_timeline.json";
    private static String STATUS_UPDATE_URL = "statuses/update.json";

	public TwitterRestClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
//	public void getInterestingnessList(AsyncHttpResponseHandler handler) {
//		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
//		// Can specify query string params directly or through RequestParams.
//		RequestParams params = new RequestParams();
//		params.put("format", "json");
//		client.get(apiUrl, params, handler);
//	}

	
	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */

    public void getHomeTimeline(long maxId, long sinceId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(HOME_TIMELINE_URL);
        if (maxId != 0) {
            apiUrl = apiUrl + "?max_id=" + String.valueOf(maxId);
        }
        Log.d("INFO", "******************* "+apiUrl);

        client.get(apiUrl, null, handler);
    }

    public void postUpdateStatus(String text, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(STATUS_UPDATE_URL);
        String encodedText;
        try {
            encodedText = URLEncoder.encode(text, "utf-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("ERROR", "post update status encoding error " + e.toString());
            // call handler error
            return;
        }
        apiUrl = apiUrl + "?status=" + encodedText;
        
        client.post(apiUrl, null, handler);
    }
}
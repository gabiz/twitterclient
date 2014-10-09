package com.gabiq.twitterpro.fragments;

import android.app.Activity;
import android.os.Bundle;

import com.gabiq.twitterpro.TwitterProApp;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class MentionsFragment extends TweetsListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    protected void getTweets(long maxId, long sinceId,
            AsyncHttpResponseHandler handler) {
        TwitterProApp.getRestClient().getMentionsTimeline(maxId, sinceId, handler);
    }

}

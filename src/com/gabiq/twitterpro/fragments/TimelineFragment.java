package com.gabiq.twitterpro.fragments;

import android.app.Activity;
import android.os.Bundle;

import com.gabiq.twitterpro.models.Tweet.Feed;

public class TimelineFragment extends TweetsListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
    }

    @Override
    protected Feed getType() {
        return Feed.TIMELINE;
    }
    
}

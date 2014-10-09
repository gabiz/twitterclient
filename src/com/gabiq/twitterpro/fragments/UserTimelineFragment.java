package com.gabiq.twitterpro.fragments;

import com.gabiq.twitterpro.TwitterProApp;
import com.gabiq.twitterpro.models.Tweet.Feed;
import com.gabiq.twitterpro.models.User;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class UserTimelineFragment extends TweetsListFragment {
    private User user;
    
    @Override
    protected void getTweets(long maxId, long sinceId,
            AsyncHttpResponseHandler handler) {
        TwitterProApp.getRestClient().getUserTimeline(user != null ? user.getUid() : 0, maxId, sinceId, handler);
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    @Override
    protected Feed getType() {
        return Feed.PROFILE;
    }
   
}

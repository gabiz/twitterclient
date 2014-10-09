package com.gabiq.twitterpro.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.gabiq.twitterpro.R;
import com.gabiq.twitterpro.TwitterProApp;
import com.gabiq.twitterpro.activities.DetailActivity;
import com.gabiq.twitterpro.adapters.EndlessScrollListener;
import com.gabiq.twitterpro.adapters.TimelineAdapter;
import com.gabiq.twitterpro.models.Tweet;
import com.gabiq.twitterpro.models.Tweet.Feed;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TweetsListFragment extends SherlockFragment {

    private ArrayList<Tweet> tweets;
    private TimelineAdapter aTimeline;
    private PullToRefreshListView lvTimeline;
    private ProgressBar pbTweetsLoading;

    private static boolean initialized = false;
    private static boolean refreshing = false;
    private static long maxId = 0;
    private static long sinceId = 0;

    private final int REQUEST_CODE = 20;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<Tweet>();
        aTimeline = new TimelineAdapter(getActivity(), tweets);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets_list, container,
                false);

        setupListView(view);
        fetchFromDB();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialized = true;
        fetchHomeTimeline();
    }

    private void setupListView(View view) {
        pbTweetsLoading = (ProgressBar) view.findViewById(R.id.pbTweetsLoading);
        lvTimeline = (PullToRefreshListView) view.findViewById(R.id.lvTweets);
        lvTimeline.setAdapter(aTimeline);
        lvTimeline.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (initialized) {
                    if (isNetworkAvailable()) {
                        loadTweets(maxId, 0);
                    } else {
                        Toast.makeText(getActivity(), "@strings/network_error",
                                Toast.LENGTH_SHORT);
                    }
                }
            }
        });

        lvTimeline.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (initialized) {
                    refreshing = true;
                    fetchNewTweets();
                }
            }
        });

        lvTimeline.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Intent i = new Intent(getActivity(), DetailActivity.class);
                Tweet tweet = tweets.get(position);
                i.putExtra("tweet", tweet);
                startActivityForResult(i, REQUEST_CODE);
                return false;
            }
        });

    }

    protected Feed getType() {
        return Feed.TIMELINE;
    }

    private void fetchFromDB() {
        if (getType() != Feed.PROFILE) {
            // not handling yet different user profiles
            return;
        }
        
        List<Tweet> newTweets = Tweet.recentItems(getType());
        if (newTweets.size() > 0) {
            Tweet lastTweet = newTweets.get(newTweets.size() - 1);
            maxId = lastTweet.getUid();

            if (sinceId == 0) {
                Tweet firstTweet = newTweets.get(0);
                sinceId = firstTweet.getUid();
            }
        }
        aTimeline.addAll(newTweets);
    }

    // Defaults to home timeline intended to be overriden by child class
    protected void getTweets(long maxId, long sinceId,
            AsyncHttpResponseHandler handler) {
        TwitterProApp.getRestClient().getHomeTimeline(maxId, sinceId, handler);
    }

    protected void loadTweets(final long maxId, final long sinceId) {
        Log.d("INFO",
                "******************* load tweets maxId="
                        + String.valueOf(maxId) + " sinceId="
                        + String.valueOf(sinceId));

        if (!refreshing) {
            pbTweetsLoading.setVisibility(ProgressBar.VISIBLE);
        }
        getTweets(maxId, sinceId, new JsonHttpResponseHandler() {

            @Override
            protected void handleFailureMessage(Throwable e, String message) {
                Log.e("ERROR", "Error loading tweets " + e.toString() + " : "
                        + message);

                pbTweetsLoading.setVisibility(ProgressBar.INVISIBLE);
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
                if (refreshing) {
                    lvTimeline.onRefreshComplete();
                    refreshing = false;
                }
            }

            @Override
            public void onFailure(Throwable e, JSONArray arg1) {
                Log.e("ERROR", "******************* Error loading tweets");

                pbTweetsLoading.setVisibility(ProgressBar.INVISIBLE);
                Toast.makeText(getActivity(), "Error loading tweets",
                        Toast.LENGTH_LONG);
                if (refreshing) {
                    lvTimeline.onRefreshComplete();
                    refreshing = false;
                }
            }

            @Override
            public void onSuccess(int arg0, JSONArray json) {
                Log.d("INFO", "******************* loaded tweets");

                ArrayList<Tweet> newTweets = Tweet.fromJSONArray(getType(),
                        json);

                if (sinceId != 0) {
                    // this is not more
                    for (int i = newTweets.size() - 1; i >= 0; i--) {
                        aTimeline.insert(newTweets.get(i), 0);
                    }
                } else {
                    aTimeline.addAll(newTweets);
                }
                // update maxId
                if (newTweets != null && !newTweets.isEmpty()) {
                    Tweet lastTweet = newTweets.get(newTweets.size() - 1);
                    TweetsListFragment.maxId = lastTweet.getUid();

                    if (sinceId != 0 || TweetsListFragment.sinceId == 0) {
                        Tweet firstTweet = newTweets.get(0);
                        TweetsListFragment.sinceId = firstTweet.getUid();
                    }
                }

                pbTweetsLoading.setVisibility(ProgressBar.INVISIBLE);
                if (refreshing) {
                    lvTimeline.onRefreshComplete();
                    refreshing = false;
                }

            }
        });
    }

    private void fetchHomeTimeline() {
        maxId = 0;
        sinceId = 0;
        aTimeline.clear();

        if (isNetworkAvailable()) {
            loadTweets(0, 0);
        } else {
            Toast.makeText(getActivity(), "@strings/network_error",
                    Toast.LENGTH_SHORT);
        }
    }

    private void fetchNewTweets() {
        loadTweets(0, sinceId);
    }

    public void insertTweet(Tweet tweet) {
        if (tweet != null) {
            aTimeline.insert(tweet, 0);
            sinceId = tweet.getUid();
        }
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity()
                .getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null
                && activeNetworkInfo.isConnectedOrConnecting();
    }

}

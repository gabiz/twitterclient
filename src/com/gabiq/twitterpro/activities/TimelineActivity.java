package com.gabiq.twitterpro.activities;

import java.util.ArrayList;

import org.json.JSONArray;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

import com.gabiq.twitterpro.R;
import com.gabiq.twitterpro.TwitterProApp;
import com.gabiq.twitterpro.adapters.EndlessScrollListener;
import com.gabiq.twitterpro.adapters.TimelineAdapter;
import com.gabiq.twitterpro.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TimelineActivity extends FragmentActivity {
    private ArrayList<Tweet> tweets;
    private TimelineAdapter aTimeline;
    private PullToRefreshListView lvTimeline;

    private static boolean refreshing = false;
    private static long maxId = 0;
    private static long sinceId = 0;

    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        tweets = new ArrayList<Tweet>();
        lvTimeline = (PullToRefreshListView) findViewById(R.id.lvTimeline);
        aTimeline = new TimelineAdapter(this, tweets);
        lvTimeline.setAdapter(aTimeline);
        lvTimeline.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadTweets(maxId, 0);
            }
        });

        lvTimeline.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshing = true;
                fetchNewTweets();
            }
        });

        lvTimeline.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Intent i = new Intent(TimelineActivity.this,
                        DetailActivity.class);
                Tweet tweet = tweets.get(position);
                i.putExtra("tweet", tweet);
                startActivity(i);
                return false;
            }
//            @Override
//            public void onItemLongClick(AdapterView<?> parent, View view,
//                    int position, long id) {
//                Log.d("INFO", "*********************** clicked");
//                Intent i = new Intent(TimelineActivity.this,
//                        DetailActivity.class);
//                Tweet tweet = tweets.get(position);
//                i.putExtra("tweet", tweet);
//                startActivity(i);
//            }
        });


        fetchHomeTimeline();
    }

    protected void loadTweets(final long maxId, final long sinceId) {
        Log.d("INFO",
                "******************* load tweets maxId="
                        + String.valueOf(maxId) + " sinceId="
                        + String.valueOf(sinceId));

        TwitterProApp.getRestClient().getHomeTimeline(maxId, sinceId,
                new JsonHttpResponseHandler() {

                    @Override
                    protected void handleFailureMessage(Throwable e,
                            String message) {
                        Log.e("ERROR", "Error loading tweets " + e.toString()
                                + " : " + message);

                        Toast.makeText(TimelineActivity.this, message,
                                Toast.LENGTH_LONG);
                        if (refreshing) {
                            lvTimeline.onRefreshComplete();
                            refreshing = false;
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, JSONArray arg1) {
                        // Toast.makeText(this,
                        // "Error retrieving twitter timeline",
                        // Toast.LENGTH_SHORT);
                        Log.e("ERROR",
                                "******************* Error loading tweets");
                        Toast.makeText(TimelineActivity.this,
                                "Error loading tweets", Toast.LENGTH_LONG);
                        if (refreshing) {
                            lvTimeline.onRefreshComplete();
                            refreshing = false;
                        }
                    }

                    @Override
                    public void onSuccess(int arg0, JSONArray json) {
                        Log.d("INFO", "******************* loaded tweets");
                        ArrayList<Tweet> newTweets = Tweet.fromJSONArray(json);

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
                            TimelineActivity.maxId = lastTweet.getUid();

                            if (sinceId != 0 || TimelineActivity.sinceId == 0) {
                                Tweet firstTweet = newTweets.get(0);
                                TimelineActivity.sinceId = firstTweet.getUid();
                            }
                        }

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
        loadTweets(0, 0);
    }

    private void fetchNewTweets() {
        loadTweets(0, sinceId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }

    public void onComposeAction(MenuItem mi) {

        Log.d("INFO", "********************** compose");
        // FragmentManager fm = getSupportFragmentManager();
        // ComposeFragment composeFragment =
        // ComposeFragment.newInstance("Some Title");
        // composeFragment.show(fm, "fragment_compose");

        Intent i = new Intent(this, ComposeActivity.class);
        startActivityForResult(i, REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Tweet tweet = (Tweet) data.getExtras().get("tweet");
            if (tweet != null) {
                aTimeline.insert(tweet, 0);
                sinceId = tweet.getUid();
            }
        }
    }
}

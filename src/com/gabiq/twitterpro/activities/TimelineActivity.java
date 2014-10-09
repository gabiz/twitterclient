package com.gabiq.twitterpro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.gabiq.twitterpro.ActionBarListener;
import com.gabiq.twitterpro.R;
import com.gabiq.twitterpro.fragments.ComposeFragment;
import com.gabiq.twitterpro.fragments.MentionsFragment;
import com.gabiq.twitterpro.fragments.TimelineFragment;
import com.gabiq.twitterpro.models.Tweet;

public class TimelineActivity extends SherlockFragmentActivity implements
        ComposeFragment.OnTweetPostListener {
    private SearchView searchView;
    private static String TIMELINE_TAB_TAG = "timeline";
    private static String  MENTIONS_TAB_TAG = "mentions";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        setupTabs();
    }

    private void setupTabs() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        Tab tabTimeline = actionBar
                .newTab()
                .setText("Home")
                // .setIcon(R.drawable.ic_launcher)
                .setTabListener(
                        new ActionBarListener<TimelineFragment>(
                                R.id.flContainer, this, TIMELINE_TAB_TAG,
                                TimelineFragment.class));

        actionBar.addTab(tabTimeline);
        actionBar.selectTab(tabTimeline);

        Tab tabMentions = actionBar
                .newTab()
                .setText("@ Mentions")
                // .setIcon(R.drawable.ic_launcher)
                .setTabListener(
                        new ActionBarListener<MentionsFragment>(
                                R.id.flContainer, this, MENTIONS_TAB_TAG,
                                MentionsFragment.class));

        actionBar.addTab(tabMentions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.timeline, menu);
        
//        MenuItem searchItem = menu.findItem(R.id.svSearch);
//        searchView = (SearchView) searchItem.getActionView();
//        searchView.setOnQueryTextListener(new OnQueryTextListener() {
//           @Override
//           public boolean onQueryTextSubmit(String query) {
//                // perform query here
//                return true;
//           }
//
//           @Override
//           public boolean onQueryTextChange(String newText) {
//               return false;
//           }
//       });

        return super.onCreateOptionsMenu(menu);
    }

    // @Override
    // public boolean onCreateOptionsMenu(Menu menu) {
    // // Inflate the menu; this adds items to the action bar if it is present.
    // getMenuInflater().inflate(R.menu.timeline, menu);
    // return true;
    // }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        // Handle item selection
        switch (item.getItemId()) {
//        case R.id.svSearch:
//            return true;
        case R.id.miCompose:
            FragmentManager fm = getSupportFragmentManager();
            ComposeFragment composeFragment = ComposeFragment.newInstance();
            composeFragment.show(fm, "fragment_compose");
            return true;
        case R.id.miProfile:
            Intent i = new Intent(this, ProfileActivity.class);
            startActivity(i);
            return true;

        }
        return false;
    }

    @Override
    public void onTweetPost(Tweet tweet) {
        if (getSupportActionBar().getSelectedNavigationIndex() == 0) {
            TimelineFragment timelineFragment = (TimelineFragment) getSupportFragmentManager().findFragmentByTag(TIMELINE_TAB_TAG);
            timelineFragment.insertTweet(tweet);
        }
        
    }

    // public void onComposeAction(MenuItem mi) {
    // FragmentManager fm = getSupportFragmentManager();
    // ComposeFragment composeFragment = ComposeFragment
    // .newInstance();
    // composeFragment.show(fm, "fragment_compose");
    //
    // // Activity version
    // // Intent i = new Intent(this, ComposeActivity.class);
    // // startActivityForResult(i, REQUEST_CODE);
    //
    // }

}

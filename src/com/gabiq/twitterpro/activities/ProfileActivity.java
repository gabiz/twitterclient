package com.gabiq.twitterpro.activities;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.gabiq.twitterpro.R;
import com.gabiq.twitterpro.TwitterProApp;
import com.gabiq.twitterpro.R.id;
import com.gabiq.twitterpro.R.layout;
import com.gabiq.twitterpro.fragments.UserTimelineFragment;
import com.gabiq.twitterpro.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends SherlockFragmentActivity {
    private UserTimelineFragment fragmentUserTimeline;
    private User user;

    private ImageView ivProfileImage;
    private TextView tvProfileName;
    private TextView tvProfileScreenName;
    private TextView tvProfileDescription;

    private TextView tvStatusTweet;
    private TextView tvStatusFollowers;
    private TextView tvStatusFollowing;
    private ImageView ivProfileBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setupViews();

        user = (User) getIntent().getSerializableExtra("user");
        fragmentUserTimeline.setUser(user);

        if (user != null) {
            updateHeaderForUser(user);
        } else {
            getUserInfo();
        }

    }

    private void setupViews() {
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvProfileName = (TextView) findViewById(R.id.tvProfileName);
        tvProfileDescription = (TextView) findViewById(R.id.tvProfileDescription);
        tvProfileScreenName = (TextView) findViewById(R.id.tvProfileScreenName);
        tvStatusTweet = (TextView) findViewById(R.id.tvStatusTweet);
        tvStatusFollowers = (TextView) findViewById(R.id.tvStatusFollowers);
        tvStatusFollowing = (TextView) findViewById(R.id.tvStatusFollowing);
        ivProfileBanner = (ImageView) findViewById(R.id.ivProfileBanner);
        fragmentUserTimeline = (UserTimelineFragment) getSupportFragmentManager()
                .findFragmentById(R.id.flUserTimeline);
    }

    private void updateHeaderForUser(User user) {
        if (user != null) {
            tvProfileName.setText(user.getName());
            tvProfileScreenName.setText(user.getScreenName());
            tvProfileDescription.setText(user.getDescription());
            tvStatusTweet.setText(formatCount(user.getStatusesCount()));
            tvStatusFollowers.setText(formatCount(user.getFollowersCount()));
            tvStatusFollowing.setText(formatCount(user.getFriendsCount()));
            ImageLoader.getInstance().displayImage(user.getBannerUrl(),
                    ivProfileBanner);
            ImageLoader.getInstance().displayImage(user.getProfileImageUrl(),
                    ivProfileImage);
        }
    }

    private void getUserInfo() {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);
        long uid = pref.getLong("uid", 0);
        if (uid != 0) {
            // search on DB
            User user = User.byId(uid);
            updateHeaderForUser(user);
        } else {
            // read from network
            TwitterProApp.getRestClient().getUserInfo(
                    new JsonHttpResponseHandler() {

                        @Override
                        protected void handleFailureMessage(Throwable e,
                                String message) {
                        }

                        @Override
                        public void onFailure(Throwable e, JSONArray arg1) {
                        }

                        @Override
                        public void onSuccess(JSONObject json) {
                            User user = User.fromJSON(json);
                            user.save();

                            // save user uid
                            SharedPreferences pref = PreferenceManager
                                    .getDefaultSharedPreferences(ProfileActivity.this);
                            Editor edit = pref.edit();
                            edit.putLong("uid", user.getUid());
                            edit.commit();

                            updateHeaderForUser(user);
                        }
                    });
        }

    }

    private String formatCount(long count) {
        if (count >= 1000000) {
            return String.valueOf(count/1000000) + "M";
        } else if (count >= 1100) {
            return String.valueOf(count/1000) + "." + String.valueOf((count % 1000) / 100) + "K";
        } else if (count > 1000) {
            return String.valueOf(count/1000) + "K";
        } else {
            return String.valueOf(count);
        }
    }
}

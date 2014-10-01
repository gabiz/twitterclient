package com.gabiq.twitterpro.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gabiq.twitterpro.R;
import com.gabiq.twitterpro.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DetailActivity extends Activity {
    private ImageView ivTweetImage;
    private TextView tvTweetName;
    private TextView tvTweetUserName;
    private TextView tvTweetBody;
    private TextView tvTweetTimestamp;
    private ImageView ivTweetEntityImage;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        
        Tweet tweet = (Tweet) getIntent().getExtras().get("tweet");
        
        ivTweetImage = (ImageView) findViewById(R.id.ivTweetImage);
        tvTweetName = (TextView) findViewById(R.id.tvTweetName);
        tvTweetUserName = (TextView) findViewById(R.id.tvTweetUserName);
        tvTweetBody = (TextView) findViewById(R.id.tvTweetBody);
        tvTweetTimestamp = (TextView) findViewById(R.id.tvTweetTimestamp);
        ivTweetEntityImage = (ImageView) findViewById(R.id.ivTweetEntityImage);
        
        tvTweetName.setText(tweet.getUser().getName());
        tvTweetUserName.setText("@"+tweet.getUser().getScreenName());
        tvTweetBody.setText(tweet.getBody());
        tvTweetTimestamp.setText(tweet.getFormatedTime());
        ImageLoader.getInstance().displayImage(tweet.getUser().getProfileImageUrl(), ivTweetImage);
        String url = tweet.getUrl();
        if (url != null && url != "") {
            ImageLoader.getInstance().displayImage(url, ivTweetEntityImage);
            ivTweetEntityImage.setVisibility(View.VISIBLE);
        } else {
            ivTweetEntityImage.setVisibility(View.GONE);
        }
    }
}

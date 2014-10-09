package com.gabiq.twitterpro.activities;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gabiq.twitterpro.R;
import com.gabiq.twitterpro.TwitterProApp;
import com.gabiq.twitterpro.models.Tweet;
import com.gabiq.twitterpro.models.Tweet.Feed;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DetailActivity extends Activity {
    private ImageView ivTweetImage;
    private TextView tvTweetName;
    private TextView tvTweetUserName;
    private TextView tvTweetBody;
    private TextView tvTweetTimestamp;
    private ImageView ivTweetEntityImage;
    
    private EditText etTweetReply;
    private Button bnTweetReply;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        
        final Tweet tweet = (Tweet) getIntent().getExtras().get("tweet");
        
        ivTweetImage = (ImageView) findViewById(R.id.ivTweetImage);
        tvTweetName = (TextView) findViewById(R.id.tvTweetName);
        tvTweetUserName = (TextView) findViewById(R.id.tvTweetUserName);
        tvTweetBody = (TextView) findViewById(R.id.tvTweetBody);
        tvTweetTimestamp = (TextView) findViewById(R.id.tvTweetTimestamp);
        ivTweetEntityImage = (ImageView) findViewById(R.id.ivTweetEntityImage);
        etTweetReply = (EditText) findViewById(R.id.etTweetReply);
        bnTweetReply = (Button) findViewById(R.id.bnTweetReply);
        
        bnTweetReply.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                postReply(tweet);
            }
        });
        
        tvTweetName.setText(tweet.getUser().getName());
        tvTweetUserName.setText(tweet.getUser().getScreenName());
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
    
    private void postReply(Tweet tweet) {
        String replyMessage = tweet.getUser().getScreenName() + " " + etTweetReply.getText().toString();
        
        TwitterProApp.getRestClient().postReplyStatus(replyMessage, Long.toString(tweet.getUid()),
                new JsonHttpResponseHandler() {

                    @Override
                    protected void handleFailureMessage(Throwable e,
                            String message) {
                        Toast.makeText(DetailActivity.this, "Error Replying to Tweet: "+ e.toString() , Toast.LENGTH_LONG);
                        finish();
                    }

                    @Override
                    public void onFailure(Throwable e, JSONArray arg1) {
                        Toast.makeText(DetailActivity.this, "Error Replying to Tweet: "+ e.toString() , Toast.LENGTH_LONG);
                        finish();
                    }

                    @Override
                    public void onSuccess(JSONObject json) {
                        Intent data = new Intent();
                        Tweet tweet = Tweet.fromJSON(Feed.TIMELINE, json);
                        tweet.save();
                        if (tweet != null) {
                            data.putExtra("tweet", tweet);
                        }
                        setResult(RESULT_OK, data);

                        finish();
                   }
                });
        
    }
}

package com.gabiq.twitterpro;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.gabiq.twitterpro.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ComposeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.compose, menu);
        return true;
    }

    public void onPostAction(MenuItem mi) {
        Log.d("INFO", "*********** on post action");
        EditText etComposeBody = (EditText) findViewById(R.id.etComposeBody);
        String tweetBody = etComposeBody.getText().toString();

        TwitterProApp.getRestClient().postUpdateStatus(tweetBody,
                new JsonHttpResponseHandler() {

                    @Override
                    protected void handleFailureMessage(Throwable e,
                            String message) {
                        Log.e("ERROR", "Error loading tweets " + e.toString()
                                + " : " + message);
                        
                        finish();
                    }

                    @Override
                    public void onFailure(Throwable arg0, JSONObject json) {
                        Log.e("ERROR",
                                "******************* Error loading tweets");

                        finish();
                    }

                    @Override
                    public void onSuccess(int arg0, JSONObject json) {
                        Log.d("INFO", "******************* success posting tweet "+ json.toString());

                        Intent data = new Intent();
                        Tweet tweet = Tweet.fromJSON(json);
                        if (tweet != null) {
                            data.putExtra("tweet", tweet);
                        }
                        setResult(RESULT_OK, data);

                        finish();
                    }
                });

    }

}

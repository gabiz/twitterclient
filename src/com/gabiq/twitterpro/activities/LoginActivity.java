package com.gabiq.twitterpro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.codepath.oauth.OAuthLoginActivity;
import com.gabiq.twitterpro.R;
import com.gabiq.twitterpro.TwitterRestClient;

public class LoginActivity extends OAuthLoginActivity<TwitterRestClient> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getActionBar().hide();
	}

	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
	@Override
	public void onLoginSuccess() {
		 Intent i = new Intent(this, TimelineActivity.class);
		 startActivity(i);
	}

	// OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
	    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT);
		e.printStackTrace();
	}

	// Click handler method for the button used to start OAuth flow
	// Uses the client to initiate OAuth authorization
	// This should be tied to a button used to login
	public void loginToRest(View view) {
	    Log.d("DEBUG", "****** login called");
		getClient().connect();
	}

}

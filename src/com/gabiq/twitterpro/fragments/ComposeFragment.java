package com.gabiq.twitterpro.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gabiq.twitterpro.R;
import com.gabiq.twitterpro.TwitterProApp;
import com.gabiq.twitterpro.models.Tweet;
import com.gabiq.twitterpro.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeFragment extends DialogFragment {
    private TextView tvTweetCount;
    private EditText etFragComposeBody;
    private ImageView ivProfileImage;
    private TextView tvProfileName;
    private TextView tvProfileUserName;
    private ProgressBar pbTweetPostLoading;

    private OnTweetPostListener listener;

    // Define the events that the fragment will use to communicate
    public interface OnTweetPostListener {
        public void onTweetPost(Tweet tweet);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnTweetPostListener) {
            listener = (OnTweetPostListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement ComposeFragment.OnTweetPostListener");
        }
    }

    public ComposeFragment() {
    }

    public static ComposeFragment newInstance() {
        ComposeFragment frag = new ComposeFragment();
        frag.setStyle(R.style.CustomDialog, 0);
        Bundle args = new Bundle();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compose, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        tvProfileName = (TextView) view.findViewById(R.id.tvComposeName);
        tvProfileUserName = (TextView) view
                .findViewById(R.id.tvComposeUserName);
        ivProfileImage = (ImageView) view.findViewById(R.id.ivComposeImage);

        pbTweetPostLoading = (ProgressBar) getView().findViewById(
                R.id.pbTweetPostLoading);

        pbTweetPostLoading.setVisibility(ProgressBar.VISIBLE);
        TwitterProApp.getRestClient().getUserInfo(
                new JsonHttpResponseHandler() {

                    @Override
                    protected void handleFailureMessage(Throwable e,
                            String message) {
                        pbTweetPostLoading.setVisibility(ProgressBar.INVISIBLE);
                        ivProfileImage.setVisibility(View.GONE);
                        tvProfileName.setText("");
                        tvProfileUserName.setText("");
                    }

                    @Override
                    public void onFailure(Throwable e, JSONArray arg1) {
                        pbTweetPostLoading.setVisibility(ProgressBar.INVISIBLE);
                        ivProfileImage.setVisibility(View.GONE);
                        tvProfileName.setText("");
                        tvProfileUserName.setText("");
                    }

                    @Override
                    public void onSuccess(JSONObject json) {
                        pbTweetPostLoading.setVisibility(ProgressBar.INVISIBLE);
                        User user = User.fromJSON(json);
                        tvProfileName.setText(user.getName());
                        tvProfileUserName.setText(user.getScreenName());
                        ImageLoader.getInstance().displayImage(
                                user.getProfileImageUrl(), ivProfileImage);
                        ivProfileImage.setVisibility(View.VISIBLE);
                    }
                });

        tvTweetCount = (TextView) view.findViewById(R.id.tvTweetCount);
        etFragComposeBody = (EditText) view
                .findViewById(R.id.etFragComposeBody);
        etFragComposeBody.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                tvTweetCount.setText(String.valueOf(140 - s.length()));
            }

            public void afterTextChanged(Editable s) {
            }
        });

        Button bnPostTweet = (Button) view.findViewById(R.id.bnPostTweet);

        bnPostTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postTweet();
            }
        });

        return view;
    }

    // private void dismiss() {
    // getActivity().getSupportFragmentManager().popBackStack();
    // }

    private void postTweet() {
        String tweetBody = etFragComposeBody.getText().toString();

        pbTweetPostLoading.setVisibility(ProgressBar.VISIBLE);
        TwitterProApp.getRestClient().postUpdateStatus(tweetBody,
                new JsonHttpResponseHandler() {

                    @Override
                    protected void handleFailureMessage(Throwable e,
                            String message) {
                        pbTweetPostLoading.setVisibility(ProgressBar.INVISIBLE);
                        dismiss();
                        Toast.makeText(getActivity(), "Error Posting Tweet: "
                                + e.toString(), Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onFailure(Throwable e, JSONObject json) {
                        pbTweetPostLoading.setVisibility(ProgressBar.INVISIBLE);
                        dismiss();
                        Toast.makeText(getActivity(), "Error Posting Tweet: "
                                + e.toString(), Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onSuccess(int arg0, JSONObject json) {
                        Log.d("INFO",
                                "******************* success posting tweet "
                                        + json.toString());

                        pbTweetPostLoading.setVisibility(ProgressBar.INVISIBLE);
                        Tweet tweet = Tweet.fromJSON(json);
                        tweet.save();

                        listener.onTweetPost(tweet);
                        dismiss();
                    }
                });
    }

}
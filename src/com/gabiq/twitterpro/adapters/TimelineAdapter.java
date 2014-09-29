package com.gabiq.twitterpro.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gabiq.twitterpro.R;
import com.gabiq.twitterpro.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TimelineAdapter extends ArrayAdapter<Tweet> {

    public TimelineAdapter(Context context, List<Tweet> tweets) {
        super(context, R.layout.item_tweet, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);

            viewHolder.tvTweetUserName = (TextView) convertView.findViewById(R.id.tvTweetUserName);
            viewHolder.tvTweetBody = (TextView) convertView.findViewById(R.id.tvTweetBody);
            viewHolder.tvTweetTimestamp = (TextView) convertView.findViewById(R.id.tvTweetTimestamp);
            viewHolder.ivTweetImage = (ImageView) convertView.findViewById(R.id.ivTweetImage);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        
        viewHolder.tvTweetUserName.setText(tweet.getFullName());
        viewHolder.tvTweetBody.setText(tweet.getBody());
        viewHolder.tvTweetTimestamp.setText(tweet.getRelativeTimeAgo());
        ImageLoader.getInstance().displayImage(tweet.getUser().getProfileImageUrl(), viewHolder.ivTweetImage);

        return convertView;
    }

    private static class ViewHolder {
        TextView tvTweetUserName;
        TextView tvTweetBody;
        TextView tvTweetTimestamp;
        ImageView ivTweetImage;
    }
    
}

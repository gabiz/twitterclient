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

            viewHolder.tvItemUserName = (TextView) convertView.findViewById(R.id.tvItemUserName);
            viewHolder.tvItemBody = (TextView) convertView.findViewById(R.id.tvItemBody);
            viewHolder.tvItemTimestamp = (TextView) convertView.findViewById(R.id.tvItemTimestamp);
            viewHolder.ivItemImage = (ImageView) convertView.findViewById(R.id.ivItemImage);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        
        viewHolder.tvItemUserName.setText(tweet.getFullName());
        viewHolder.tvItemBody.setText(tweet.getBody());
        viewHolder.tvItemTimestamp.setText(tweet.getRelativeTime());
        ImageLoader.getInstance().displayImage(tweet.getUser().getProfileImageUrl(), viewHolder.ivItemImage);

        return convertView;
    }

    private static class ViewHolder {
        TextView tvItemUserName;
        TextView tvItemBody;
        TextView tvItemTimestamp;
        ImageView ivItemImage;
    }
    
}

package com.gabiq.twitterpro.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gabiq.twitterpro.R;
import com.gabiq.twitterpro.activities.ProfileActivity;
import com.gabiq.twitterpro.models.Tweet;
import com.gabiq.twitterpro.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TimelineAdapter extends ArrayAdapter<Tweet> {
    private Context context;
    
    public TimelineAdapter(Context context, List<Tweet> tweets) {
        super(context, R.layout.item_tweet, tweets);
        
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);

            viewHolder.tvItemUserName = (TextView) convertView.findViewById(R.id.tvItemUserName);
            viewHolder.tvItemBody = (TextView) convertView.findViewById(R.id.tvItemBody);
            viewHolder.tvItemTimestamp = (TextView) convertView.findViewById(R.id.tvItemTimestamp);
            viewHolder.ivItemImage = (ImageView) convertView.findViewById(R.id.ivItemImage);
            viewHolder.ivItemEntityImage = (ImageView) convertView.findViewById(R.id.ivItemEntityImage);

            viewHolder.ivItemImage.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent  i =  new Intent(context, ProfileActivity.class);
                    // avoid recursing profiles
                    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    i.putExtra("user", viewHolder.user);
                    context.startActivity(i);
                }

              });
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        viewHolder.user = tweet.getUser();
        viewHolder.tvItemUserName.setText(tweet.getFullName());
        viewHolder.tvItemBody.setText(tweet.getBody());
        viewHolder.tvItemTimestamp.setText(tweet.getRelativeTime());
        ImageLoader.getInstance().displayImage(tweet.getUser().getProfileImageUrl(), viewHolder.ivItemImage);

        String url = tweet.getUrl();
        if (url != null && url != "") {
            ImageLoader.getInstance().displayImage(url, viewHolder.ivItemEntityImage);
            viewHolder.ivItemEntityImage.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivItemEntityImage.setVisibility(View.GONE);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView tvItemUserName;
        TextView tvItemBody;
        TextView tvItemTimestamp;
        ImageView ivItemImage;
        ImageView ivItemEntityImage;
        User user;
    }
    
}

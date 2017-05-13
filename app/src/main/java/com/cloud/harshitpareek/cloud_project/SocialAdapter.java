package com.cloud.harshitpareek.cloud_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by harshitpareek on 5/11/17.
 */

public class SocialAdapter extends BaseAdapter
{
    private final Context context;
    private List<Social> social_list;

    public SocialAdapter(Context context, List<Social> list)
    {
        this.context = context;
        this.social_list = list;
    }
    @Override
    public int getCount()
    {
        return social_list.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final Social item = social_list.get(position);

        if(convertView == null)
        {
            // inflate the layout
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.social_widget_layout, null);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.social_tab_image);

        TextView nameView = (TextView) convertView.findViewById(R.id.social_tab_name);

        TextView ratingView = (TextView) convertView.findViewById(R.id.social_tab_rating);

        TextView phoneView = (TextView) convertView.findViewById(R.id.social_tab_phone);

        TextView adderessView = (TextView) convertView.findViewById(R.id.social_tab_address);

        Glide.with(context)
                .load(item.getImg_url())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        nameView.setText(item.getName());
        ratingView.setText(String.valueOf(item.getRating()));
        nameView.setText(item.getName());
        phoneView.setText(item.getPhone());
        adderessView.setText(item.getAddress());

        return convertView;
    }
}

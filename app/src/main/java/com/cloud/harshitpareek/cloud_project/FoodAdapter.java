package com.cloud.harshitpareek.cloud_project;

import android.content.Context;
import android.test.suitebuilder.TestMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * Created by harshitpareek on 5/11/17.
 */

public class FoodAdapter extends BaseAdapter
{
    private final Context context;
    private List<Food> food_list;

    public FoodAdapter(Context context, List<Food> list)
    {
        this.context = context;
        this.food_list = list;
    }
    @Override
    public int getCount()
    {
        return food_list.size();
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
        final Food item = food_list.get(position);

        if(convertView == null)
        {
            // inflate the layout
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.list_widget_layout, null);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.list_tab_image);

        TextView nameView = (TextView) convertView.findViewById(R.id.list_tab_name);

        TextView ratingView = (TextView) convertView.findViewById(R.id.list_tab_rating);

        TextView phoneView = (TextView) convertView.findViewById(R.id.list_tab_phone);

        TextView adderessView = (TextView) convertView.findViewById(R.id.list_tab_address);

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

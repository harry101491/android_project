package com.cloud.harshitpareek.cloud_project;

import android.content.Context;
import android.widget.BaseAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;

/**
 * Created by harshitpareek on 5/9/17.
 */

public class GridAdapter extends BaseAdapter
{
    private final Context cuContext;
    private Tab[] tabs;

    public GridAdapter(Context con, Tab[] tabs)
    {
        this.cuContext = con;
        this.tabs = tabs;
    }

    @Override
    public int getCount()
    {
        return tabs.length;
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
        final Tab tab = tabs[position];

        if(convertView == null)
        {
            // inflate the layout
            final LayoutInflater layoutInflater = LayoutInflater.from(cuContext);
            convertView = layoutInflater.inflate(R.layout.tab_layout, null);
        }
        // getting the views from the layout
        ImageView image_view = (ImageView)convertView.findViewById(R.id.grid_item_image);
        TextView text_view = (TextView)convertView.findViewById(R.id.grid_item_label);

        image_view.setImageResource(tab.getImageResource());
        text_view.setText(cuContext.getString(tab.getName()));
        return convertView;
    }
}

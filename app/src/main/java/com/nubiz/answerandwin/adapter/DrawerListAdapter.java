package com.nubiz.answerandwin.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nubiz.answerandwin.R;
import com.nubiz.answerandwin.dao.DrawerItem;

import java.util.ArrayList;

public class DrawerListAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<DrawerItem> navDrawerItems;

    public DrawerListAdapter(Context context, int resource, ArrayList<DrawerItem> navDrawerItems) {
        super(context, resource, navDrawerItems);
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_drawer_list, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.name);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

        txtTitle.setText(navDrawerItems.get(position).getName());

        if (navDrawerItems.get(position).isMenuType())
            imgIcon.setImageResource(Integer.parseInt(navDrawerItems.get(position).getIcon()));


        if (navDrawerItems.get(position).getCounterVisibility()) {
//            txtCount.setText(navDrawerItems.get(position).getCount());
        } else {
            txtCount.setVisibility(View.GONE);
        }

        return convertView;
    }

}

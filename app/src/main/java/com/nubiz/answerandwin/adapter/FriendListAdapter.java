package com.nubiz.answerandwin.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nubiz.answerandwin.R;
import com.nubiz.answerandwin.dao.UserInfo;

import java.util.List;

/**
 * Created by admin on 03-Oct-17.
 */

public class FriendListAdapter extends ArrayAdapter<UserInfo> {

    public FriendListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<UserInfo> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        viewHolder holder = new viewHolder();
        UserInfo userInfo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_friend, parent, false);
            holder.txt_request_status = (TextView) convertView.findViewById(R.id.txt_request_status);
            holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }

        holder.txt_name.setText(userInfo.getUserName());


            if (userInfo.isRequestPending()) {
                holder.txt_request_status.setText("ACCEPTED");
                holder.txt_request_status.setBackgroundResource(R.drawable.corner_btn_green);
            } else {
                holder.txt_request_status.setText("PENDING");
                holder.txt_request_status.setBackgroundResource(R.drawable.corner_btn_grey);
            }

        return convertView;
    }

    public static class viewHolder {
        TextView txt_request_status;
        TextView txt_name;
    }
}

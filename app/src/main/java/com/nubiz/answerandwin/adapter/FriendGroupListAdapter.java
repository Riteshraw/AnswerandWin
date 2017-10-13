package com.nubiz.answerandwin.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.nubiz.answerandwin.R;
import com.nubiz.answerandwin.dao.UserInfo;

import java.util.ArrayList;

/**
 * Created by admin on 03-Oct-17.
 */

public class FriendGroupListAdapter extends ArrayAdapter<UserInfo> {
    private onCheckBoxCheckedListener listener;
    private ArrayList<UserInfo> objects;

    public FriendGroupListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<UserInfo> objects) {
        super(context, resource, objects);
        this.objects = objects;
    }

    public void setListener(onCheckBoxCheckedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        UserInfo friendItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_group_friend, parent, false);
            holder.txt_friend_name = (TextView) convertView.findViewById(R.id.txt_friend_name);
            holder.txt_request_status = (TextView) convertView.findViewById(R.id.txt_request_status);
            holder.checkbox_friend = (CheckBox) convertView.findViewById(R.id.checkbox_friend);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_friend_name.setText(friendItem.getUserName());
        holder.txt_request_status.setVisibility(View.GONE);
        if (friendItem.getRequestStatus().equals("1"))
            holder.txt_request_status.setVisibility(View.VISIBLE);

        holder.checkbox_friend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                objects.get(position).setAddedToGroup(isChecked);
                if (listener != null) {
                    listener.onCheckboxChecked(objects);
                }
            }
        });
        holder.checkbox_friend.setChecked(objects.get(position).isAddedToGroup());

        return convertView;
    }

    public static class ViewHolder {
        private TextView txt_friend_name;
        private TextView txt_request_status;
        private CheckBox checkbox_friend;
    }

    public interface onCheckBoxCheckedListener {
        void onCheckboxChecked(ArrayList<UserInfo> objects);
    }
}

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
import com.nubiz.answerandwin.dao.Group;

import java.util.List;

/**
 * Created by admin on 03-Oct-17.
 */

public class GroupListAdapter extends ArrayAdapter<Group> {

    public GroupListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Group> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        viewHolder holder = new viewHolder();
        Group userInfo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_group, parent, false);
            holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
            holder.total_gp_members = (TextView) convertView.findViewById(R.id.total_gp_members);
            holder.txt_pending_invites = (TextView) convertView.findViewById(R.id.txt_pending_invites);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }

        holder.txt_name.setText(userInfo.getGroupName());
        holder.total_gp_members.setText("Total Members : " + userInfo.getTotalGroupMembers());
        holder.txt_pending_invites.setText("Pending invites : " + userInfo.getPendingRequest());

        return convertView;
    }

    public static class viewHolder {
        TextView txt_name;
        TextView total_gp_members;
        TextView txt_pending_invites;
    }
}

package com.nubiz.answerandwin.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nubiz.answerandwin.R;
import com.nubiz.answerandwin.dao.UserInfo;

import java.util.ArrayList;

/**
 * Created by admin on 03-Oct-17.
 */

public class FriendListRecycleAdapter extends RecyclerView.Adapter<FriendListRecycleAdapter.viewHolder> {

    private final ArrayList<UserInfo> userList;

    public FriendListRecycleAdapter(ArrayList<UserInfo> userList) {
        this.userList = userList;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        UserInfo userInfo = userList.get(position);

        holder.txt_name.setText(userInfo.getUserName());
        holder.txt_number.setText(userInfo.getUserMobile());

        if (userInfo.getRequestStatus().equals("2")) {
            holder.txt_request_status.setText("ACCEPTED");
            holder.txt_request_status.setBackgroundResource(R.drawable.corner_btn_green);
        } else if (userInfo.getRequestStatus().equals("1")) {
            holder.txt_request_status.setText("PENDING");
            holder.txt_request_status.setBackgroundResource(R.drawable.corner_btn_grey);
        } else {
            holder.txt_request_status.setText("REJECTED");
            holder.txt_request_status.setBackgroundResource(R.drawable.corner_btn_red);
        }


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView txt_request_status;
        TextView txt_name;
        TextView txt_number;

        public viewHolder(View itemView) {
            super(itemView);
            this.txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            this.txt_number = (TextView) itemView.findViewById(R.id.txt_number);
            this.txt_request_status = (TextView) itemView.findViewById(R.id.txt_request_status);
        }
    }
}

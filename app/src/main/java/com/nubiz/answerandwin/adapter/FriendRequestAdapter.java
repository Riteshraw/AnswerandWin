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
import com.nubiz.answerandwin.service.CompleteListener;
import com.nubiz.answerandwin.service.PostAsync;
import com.nubiz.answerandwin.util.Constants;
import com.nubiz.answerandwin.util.Messages;
import com.nubiz.answerandwin.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by admin on 07-Oct-17.
 */

public class FriendRequestAdapter extends ArrayAdapter<UserInfo> {
    Context context;

    public FriendRequestAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<UserInfo> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        viewHolder holder = new viewHolder();
        final UserInfo userInfo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_friend_request, parent, false);
            holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
            holder.txt_number = (TextView) convertView.findViewById(R.id.txt_number);
            holder.txt_request_reject = (TextView) convertView.findViewById(R.id.txt_request_reject);
            holder.txt_request_accept = (TextView) convertView.findViewById(R.id.txt_request_accept);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }

        holder.txt_name.setText(userInfo.getUserName());
        holder.txt_number.setText(userInfo.getUserMobile());

        holder.txt_request_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postResponse(userInfo, "3");
            }
        });

        holder.txt_request_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postResponse(userInfo, "2");
            }
        });

        return convertView;
    }

    private void postResponse(UserInfo u, String b) {
        if (!Utils.isConnectedToInternet(context))
            Utils.showConnectivityError(context);
        else {
            JSONObject tempJson = new JSONObject();
            try {
                tempJson.put("Friend_Guid", u.getUserId());
//                tempJson.put("UserMobile", SharedPref.getInstance(context).getString(Constants.USER_MOBILE));
//                tempJson.put("FriendMobile", u.getUserMobile());
                tempJson.put("Status", b); // 1- pending, 2-accept, 3 reject

            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            new PostAsync(context, Messages.LOGIN_PROGRESS_DIALOG, tempJson, new CompleteListener() {

                @Override
                public void onTaskCompleted(String result) {
                    try {
                        JSONObject jsonResponse = new JSONObject(result);
                        int responseCode = jsonResponse.optInt(Constants.STATUS_CODE);

                        if (responseCode == 200) {
                            if (jsonResponse.optString(Constants.RESPONSE).contains("Successfully Updated")) {
                                Utils.showToast(getContext(), "Request Sent Successfully");
//                                context.startActivity(new Intent(context, InvitesListActivity.class)
//                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            } else
                                Utils.showToast(context, "Some error has occured");

                        } else Utils.handleResponse(context, responseCode, jsonResponse);

                    } catch (JSONException e) {
                        if (Constants.SHOW_CUSTOM_MSG)
                            Utils.showToast(context, Constants.CUSTOM_MSG);
                        else
                            Utils.showToast(context, "app: " + result);
                        e.printStackTrace();
                    }
                }
            }).execute("http://answerandwin.nubiz.co.in/api/Group/UpdateInvitationStatus");
        }
    }

    public static class viewHolder {
        TextView txt_request_reject, txt_request_accept;
        TextView txt_name;
        TextView txt_number;
    }
}

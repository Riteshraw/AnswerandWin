package com.nubiz.answerandwin.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nubiz.answerandwin.R;
import com.nubiz.answerandwin.dao.UserInfo;

import java.util.List;

/**
 * Created by admin on 06-Oct-17.
 */

public class GroupNotificationAdapter extends ArrayAdapter<UserInfo> {
    Context context;

    public GroupNotificationAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<UserInfo> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        viewHolder holder = new viewHolder();
        UserInfo userInfo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_group_notification, parent, false);
            holder.txt_question = (TextView) convertView.findViewById(R.id.txt_question);
            holder.txt_answer = (TextView) convertView.findViewById(R.id.txt_answer);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }

        holder.txt_question.setText(userInfo.getUserId());
        Spanned text = Html.fromHtml("Your answer: <font color = green>" + userInfo.getUserName().toUpperCase() + "</font>");
        holder.txt_answer.setText(text);

        return convertView;
    }

    public static class viewHolder {
        TextView txt_question;
        TextView txt_answer;
    }
}

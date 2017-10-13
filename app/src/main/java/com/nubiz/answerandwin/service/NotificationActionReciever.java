package com.nubiz.answerandwin.service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nubiz.answerandwin.util.Constants;
import com.nubiz.answerandwin.util.SharedPref;
import com.nubiz.answerandwin.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 05-Oct-17.
 */

public class NotificationActionReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        int notificationId = intent.getBundleExtra("Extra").getInt("notifyID");
        String responseId = intent.getBundleExtra("Extra").getString("responseId");

        postToServer(context, notificationId, responseId);

        if (notificationId != -1) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notificationId);
        }
    }

    private void postToServer(final Context context, int Q_id, final String answer) {
        if (!Utils.isConnectedToInternet(context))
            Utils.showConnectivityError(context);
        else {
            JSONObject tempJson = new JSONObject();
            try {
                tempJson.put("Mobile", SharedPref.getInstance(context).getString(Constants.USER_MOBILE));
                tempJson.put("QID", Q_id);
                tempJson.put("QOID", answer);

            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            new PostAsyncNoDialog(context, tempJson, new CompleteListener() {

                @Override
                public void onTaskCompleted(String result) {
                    try {
                        JSONObject jsonResponse = new JSONObject(result);
                        int responseCode = jsonResponse.optInt(Constants.STATUS_CODE);
                        if (responseCode == 200) {
                            Utils.showToast(context, "" + jsonResponse.optString(Constants.RESPONSE));
                        } else Utils.handleResponse(context, responseCode, jsonResponse);
                    } catch (JSONException e) {
                        if (Constants.SHOW_CUSTOM_MSG)
                            Utils.showToast(context, Constants.CUSTOM_MSG);
                        else
                            Utils.showToast(context, "app: " + result);
                        e.printStackTrace();
                    }
                }
            }).execute("http://answerandwin.nubiz.co.in/api/Group/AddAnsweredQuestion");
        }
    }
}

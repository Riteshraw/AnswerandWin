package com.nubiz.answerandwin.service;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.nubiz.answerandwin.util.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class GetAsync extends AsyncTask<String, Void, String> {

    private ProgressDialog dialog;
    private Context mContext;
    private String mTitle;
    private CompleteListener mListner;

    public GetAsync(Context context, String title, CompleteListener listner) {
        mContext = context;
        mTitle = title;
        mListner = listner;
    }

    protected void onPreExecute() {
        if (!(mTitle.equals(""))) {
            dialog = new ProgressDialog(mContext, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.setMessage(mTitle);
            dialog.show();
        }
    }

    protected String doInBackground(String... urls) {
        int statusCode;
        JSONObject responseJson = new JSONObject();
        try {
            Log.i("#url", urls[0]);

            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(urls[0]);

            httpget.setHeader("Accept", "application/json");
            httpget.setHeader("Content-type", "application/json");
            HttpResponse content = httpclient.execute(httpget);
            statusCode = content.getStatusLine().getStatusCode();

            String response = EntityUtils.toString(content.getEntity());
            responseJson.put(Constants.STATUS_CODE, statusCode);
            responseJson.put(Constants.RESPONSE, response);

        } catch (Exception e) {
            try {
                responseJson.put(Constants.STATUS_CODE, Constants.EXCEPTION_CODE);
                responseJson.put(Constants.RESPONSE, new JSONObject().put(Constants.MESSAGE, e.getMessage()));
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        Log.i("#url", responseJson.toString());
        return responseJson.toString();
    }

    protected void onPostExecute(String result) {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();

        mListner.onTaskCompleted(result);
    }
}
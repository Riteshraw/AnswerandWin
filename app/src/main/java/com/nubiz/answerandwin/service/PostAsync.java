package com.nubiz.answerandwin.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.nubiz.answerandwin.util.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class PostAsync extends AsyncTask<String, Void, String> {

    private ProgressDialog dialog;
    private Context mContext;
    private String mTitle;
    private CompleteListener mListner;
    private JSONObject mtempJson;

    public PostAsync(Context context, String title, JSONObject tempJson, CompleteListener listner) {
        mContext = context;
        mtempJson = tempJson;
        mTitle = title;
        mListner = listner;
    }

    protected void onPreExecute() {
        if (!(mTitle.equals(""))) {
            dialog = new ProgressDialog(mContext);
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
            Log.i("#url", mtempJson.toString());

            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpost = new HttpPost(urls[0]);
            StringEntity se = new StringEntity(mtempJson.toString());

            httpost.setEntity(se);
            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");

            HttpResponse content = httpclient.execute(httpost);
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

        if (result != null) {
            mListner.onTaskCompleted(result);
        }
    }

}

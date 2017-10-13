package com.nubiz.answerandwin.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nubiz.answerandwin.activity.SplashScreen;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public static boolean isConnectedToInternet(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else
                return false;
        } else
            return false;
    }

    public static String getDeviceId(Context context) {
        TelephonyManager mngr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String myAndroidDeviceId = "";
        if (mngr.getDeviceId() != null && !mngr.getDeviceId().equals("000000000000000")) {
            myAndroidDeviceId = mngr.getDeviceId();
        } else {
            myAndroidDeviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return myAndroidDeviceId;
    }

    public static int getAppVersion(Context context) {

        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            getAppVersion(context);
            e.printStackTrace();
        }

        return info.versionCode;
    }

    public static void showConnectivityError(final Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context/*, R.style.MaterialAlertDialog*/);
        alertDialogBuilder.setTitle("Network Connectivity Error");
        alertDialogBuilder
                .setMessage("This app requires an internet connection. Make sure you are connected to a wifi network or have switched on your network data.")
                .setCancelable(false)
                .setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent callGPSSettingIntent = new Intent(Settings.ACTION_SETTINGS);
                        context.startActivity(callGPSSettingIntent);
                    }
                })
                .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if (!isConnectedToInternet(context))
                            showConnectivityError(context);
                        else {
                            context.startActivity(new Intent(context, SplashScreen.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        }
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void handleResponse(Context context, int responseCode, JSONObject result) {

        try {
            JSONObject jsonResponse = new JSONObject(result.optString(Constants.RESPONSE));

            if (responseCode == Constants.EXCEPTION_CODE || (responseCode >= 400 && responseCode < 500)) {
//                if (Constants.SHOW_DETAILED_ERROR)
                if (!jsonResponse.optString(Constants.MODELSTATE).isEmpty())
                    Utils.showToast(context, jsonResponse.optString(Constants.MODELSTATE));
                else
                    Utils.showToast(context, jsonResponse.optString(Constants.MESSAGE));

            } else if ((responseCode >= 500 && responseCode < 600)) {
                Utils.showToast(context, jsonResponse.optString(Constants.MESSAGE));
            } else
                Utils.showToast(context, "server: " + result);


        } catch (JSONException e) {
            Utils.showToast(context, "server: " + result);
            e.printStackTrace();
        }
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /*public static void showAlert(final Context context, String message) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context*//*, R.style.MaterialAlertDialog*//*);
        alertDialogBuilder.setTitle("Match result");
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((Activity) context).finish();
                        context.startActivity(new Intent(context, MatchResultsActivity.class));
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }*/

    public static void hideKeyboard(View view) {
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 2);
    }

    public static void hideKeyboard(Context context) {
        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static String getText(EditText c) {
        if (c.getText() != null)
            return c.getText().toString().trim();
        else
            return "";
    }

    public static String getText(AutoCompleteTextView c) {
        if (c.getText() != null)
            return c.getText().toString().trim().toLowerCase();
        else
            return "";
    }

    public static boolean validateDate(String from_date, String to_date) {

        return new Date(from_date).after(new Date(to_date));
    }

    public static String getFormatedDate(Date date, String reqFormat) {

        SimpleDateFormat outputFormat = new SimpleDateFormat(reqFormat);

        return outputFormat.format(date);
    }

    public static String getCurrencyFormat(float value) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        String amount = formatter.format(value);
        if (amount.contains("Rs"))
            return amount.substring(4, amount.length());
        else
            return amount.substring(2, amount.length());
    }

    public static Date getDateObj2(String stringDate) {
        Date date = null;
        try {
            date = sdf.parse(stringDate);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static void setSuperScript(TextView textView, String s) {

        SpannableString wordtoSpan = new SpannableString(s);
        wordtoSpan.setSpan(new SuperscriptSpan(), 1, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new RelativeSizeSpan(0.7f), 1, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(wordtoSpan);
    }

    public static String getGUID() {
        return UUID.randomUUID().toString();
    }

    public static String reverseScore(String score) {
        String tempArr[] = score.split("-");
        if (tempArr.length > 1)
            return tempArr[1] + "-" + tempArr[0];
        else
            return "";
    }

    public static String toCamelCaseWord(AutoCompleteTextView c) {
        String capString = getText(c);
        if (capString.isEmpty())
            return "";

        capString = capString.trim().replaceAll("\\s+", " ");

        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);

        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }

    public static ProgressDialog showProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        return progressDialog;
    }

    public static Date getCurrentFormattedDate() {
        Date date = null;
        try {
            date = sdf.parse(sdf.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static long dateStringToMiliSec(String s) {
        try {
            Date mDate = sdf.parse(s);
            return mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String miliSecToDateString(long time) {
        return sdf.format(new Date(time));
    }

    public static Date miliSecToFormattedDate(long time) {
        Date date = null;
        try {
            date = sdf.parse(miliSecToDateString(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}

package com.nubiz.answerandwin.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.nubiz.answerandwin.R;
import com.nubiz.answerandwin.util.Constants;
import com.nubiz.answerandwin.util.Messages;
import com.nubiz.answerandwin.util.SharedPref;
import com.nubiz.answerandwin.util.Utils;

public class SplashScreen extends AppCompatActivity {

    private SharedPref sharedPref;
    private boolean error;
    private boolean s1, s2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        ACT.init(this, ErrorActivity.class);

        sharedPref = SharedPref.getInstance(this);
        error = false;
        s1 = s2 = true;//false bydefault

    }

    @Override
    protected void onResume() {
        super.onResume();
        ifDownloadCompletes();
    }

    private void ifDownloadCompletes() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (s1 && s2)
                    askForPermission();
            }
        }, Constants.SPLASH_TIME_OUT);
    }

    private void askForPermission() {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            sharedPref.saveString(Constants.DEVICE_ID, Utils.getDeviceId(this));
            moveToLogin();
        } else {
            final String[] permissions = new String[]{android.Manifest.permission.READ_PHONE_STATE};
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 0);
            } else {
                moveToLogin();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isGranted = false;
        if (requestCode == 0) {
            for (int i = 0; i < permissions.length; i++) {
                int grantResult = grantResults[i];
                if (grantResult == PackageManager.PERMISSION_GRANTED)
                    isGranted = true;
                else {
                    isGranted = false;
                    break;
                }
            }
            if (!isGranted) /*moveToLogin();
            else */ {
                Toast.makeText(SplashScreen.this, Messages.COMPULSORY_PERMISSION_MSG, Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);
            }
        }
    }

    private void moveToLogin() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (error) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashScreen.this);
                    alertDialogBuilder.setTitle("Network Connectivity Error");
                    alertDialogBuilder
                            .setMessage(Messages.TRY_AFTER_SOME_TIME)
                            .setCancelable(false)
                            .setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    finish();

                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    Intent startApp = null;
//                    sharedPref.saveBoolean(Constants.LOGGED_IN, true);
//                    sharedPref.saveString(Constants.USER_NAME, "Test");

                    if (sharedPref.getBoolean(Constants.LOGGED_IN))
                        startApp = new Intent(SplashScreen.this, MainActivity.class);
                    else
                        startApp = new Intent(SplashScreen.this, LoginActivity.class);

                    startApp.putExtra("OrderId", getIntent().getIntExtra("OrderId", 0));
                    startActivity(startApp);
                    finish();
                }

            }
        }, Constants.SPLASH_TIME_OUT);
    }

    private boolean checkPlayServices() {

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, 1).show();
            } else {
                Toast.makeText(getApplicationContext(), Messages.GCM_ERROR_MSG, Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }
}

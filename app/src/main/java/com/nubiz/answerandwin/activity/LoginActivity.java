package com.nubiz.answerandwin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.nubiz.answerandwin.R;
import com.nubiz.answerandwin.service.CompleteListener;
import com.nubiz.answerandwin.service.PostAsync;
import com.nubiz.answerandwin.util.Constants;
import com.nubiz.answerandwin.util.Messages;
import com.nubiz.answerandwin.util.SharedPref;
import com.nubiz.answerandwin.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText input_mobile_no, input_password;
    private SharedPref sharedPref;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPref = SharedPref.getInstance(this);
        context = this;

        initializeControls();
    }

    private void initializeControls() {

        input_mobile_no = (EditText) findViewById(R.id.input_user_name);
        input_password = (EditText) findViewById(R.id.input_password);

        input_mobile_no.addTextChangedListener(new MyTextWatcher(input_mobile_no));
        input_password.addTextChangedListener(new MyTextWatcher(input_password));

        findViewById(R.id.txt_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, SignUpActivity.class));
            }
        });

        findViewById(R.id.btn_logIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(v);
                submitForm();
            }
        });
    }

    private boolean validateMobileNo() {
        if (Utils.getText(input_mobile_no).isEmpty()) {
            input_mobile_no.setError(Messages.LOGIN_VALIDATE_USERMANE);
            requestFocus(input_mobile_no);
            return false;
        }
        return true;
    }

    private boolean validatePassword() {
        if (Utils.getText(input_password).isEmpty()) {
            input_password.setError(Messages.LOGIN_VALIDATE_PASSWORD);
            return false;
        }
        return true;
    }

    private void submitForm() {
        if (!validateMobileNo()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }

        if (!Utils.isConnectedToInternet(this))
            Utils.showConnectivityError(this);
        else {
            JSONObject tempJson = new JSONObject();
            try {
                tempJson.put("Mobile", Utils.getText(input_mobile_no));
                tempJson.put("Password", Utils.getText(input_password));
                tempJson.put("DeviceID", sharedPref.getString(Constants.DEVICE_ID));
                tempJson.put("Token", sharedPref.getString(Constants.FCM_TOKEN_ID));

            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            new PostAsync(this, Messages.LOGIN_PROGRESS_DIALOG, tempJson, new CompleteListener() {

                @Override
                public void onTaskCompleted(String result) {
                    try {
                        JSONObject jsonResponse = new JSONObject(result);
                        int responseCode = jsonResponse.optInt(Constants.STATUS_CODE);
                        JSONObject jsonResponseData = new JSONObject(jsonResponse.optString(Constants.RESPONSE));
                        if (responseCode == 200) {
                            if (jsonResponseData.optString("User_Guid").contains("00000000-0000-0000-0000-000000000000"))
                                Toast.makeText(context, "Please enter valid credentails", Toast.LENGTH_SHORT).show();
                            else {
                                sharedPref.saveString(Constants.USER_ID, jsonResponseData.optString("User_Guid"));
                                sharedPref.saveString(Constants.USER_NAME, jsonResponseData.optString("Name"));
                                sharedPref.saveString(Constants.USER_MOBILE, Utils.getText(input_mobile_no));
                                sharedPref.saveBoolean(Constants.LOGGED_IN, true);
                                startActivity(new Intent(context, MainActivity.class));
                                finish();
                            }

                        } else Utils.handleResponse(context, responseCode, jsonResponse);

                    } catch (JSONException e) {
                        if (Constants.SHOW_CUSTOM_MSG)
                            Utils.showToast(context, Constants.CUSTOM_MSG);
                        else
                            Utils.showToast(context, "app: " + result);
                        e.printStackTrace();
                    }
                }
            }).execute(Constants.LOGIN_URL);
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.input_user_name:
                    validateMobileNo();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
                default:
                    break;
            }
        }
    }
}

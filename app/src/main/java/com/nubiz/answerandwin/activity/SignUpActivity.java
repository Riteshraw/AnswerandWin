package com.nubiz.answerandwin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
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


public class SignUpActivity extends AppCompatActivity {
    private EditText input_display_name, input_email, input_mobile, input_password, input_password_1;
    private Context context;
    private SharedPref sharedPref;

    private static boolean isValidMobile(String mobile) {
        String pattern = "[789][0-9]{9}";
        return mobile.matches(pattern);
    }

    private static boolean isValidName(String name) {
        String pattern = "[a-zA-Z ]*";
        return name.matches(pattern);
    }

    private static boolean isValidEmail(String email) {
        final String EMAIL_PATTERN = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,5}(\\.[a-zA-Z]{2,5}){0,1}";
        return !TextUtils.isEmpty(email) && email.matches(EMAIL_PATTERN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
//        setAppTitleBar(false, "Sign up user");
        initializeControls();
    }

    private void initializeControls() {
        context = this;
        sharedPref = SharedPref.getInstance(context);
        input_display_name = (EditText) findViewById(R.id.input_display_name);
        input_email = (EditText) findViewById(R.id.input_email);
        input_mobile = (EditText) findViewById(R.id.input_mobile);
        input_password = (EditText) findViewById(R.id.input_password);
        input_password_1 = (EditText) findViewById(R.id.input_password_1);

        input_display_name.addTextChangedListener(new MyTextWatcher(input_display_name));
        input_email.addTextChangedListener(new MyTextWatcher(input_email));
        input_mobile.addTextChangedListener(new MyTextWatcher(input_mobile));
        input_password.addTextChangedListener(new MyTextWatcher(input_password));
        input_password_1.addTextChangedListener(new MyTextWatcher(input_password_1));

        findViewById(R.id.btn_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(v);
                submitForm();
            }
        });
    }

    private boolean validateName(EditText input_name) {
        if (Utils.getText(input_name).isEmpty()) {
            input_name.setError(Messages.SIGNUP_VALIDATE_USERMANE);
            requestFocus(input_name);
            return false;
        } else if (Utils.getText(input_name).length() < 3) {
            input_name.setError("Name should have atleast 3 characters");
            requestFocus(input_name);
            return false;
        }
        return true;
    }

    private boolean validateEmail() {
        String mobile_no = Utils.getText(input_email);

        if (mobile_no.isEmpty() || !isValidEmail(mobile_no)) {
            input_email.setError("Enter valid mobile no");
            requestFocus(input_email);
            return false;
        }
        return true;
    }

    private boolean validateMobileNo() {
        String mobile_no = Utils.getText(input_mobile);

        if (mobile_no.isEmpty() || !isValidMobile(mobile_no)) {
            input_mobile.setError("Enter valid mobile no");
            requestFocus(input_mobile);
            return false;
        }
        return true;
    }

    private boolean validatePassword() {
        if (Utils.getText(input_password).isEmpty()) {
            input_password.setError(Messages.LOGIN_VALIDATE_PASSWORD);
            requestFocus(input_password);
            return false;
        } else if (Utils.getText(input_password).length() < 5) {
            input_password.setError("Password should have atleast 5 characters");
            requestFocus(input_password);
            return false;
        }
        return true;
    }

    private boolean validateCnfPassword() {
        if (!Utils.getText(input_password_1).equals(Utils.getText(input_password))) {
            input_password_1.setError(Messages.LOGIN_VALIDATE_CNG_PASSWORD);
            requestFocus(input_password_1);
            return false;
        }
        return true;
    }

    private void submitForm() {
        if (!validateName(input_display_name)) return;
        if (!validateEmail()) return;
        if (!validateMobileNo()) return;
        if (!validatePassword()) return;
        if (!validateCnfPassword()) return;

        if (!Utils.isConnectedToInternet(this))
            Utils.showConnectivityError(this);
        else {
            JSONObject tempJson = new JSONObject();
            try {
                tempJson.put("DisplayName", Utils.getText(input_display_name));
                tempJson.put("Email", Utils.getText(input_email));
                tempJson.put("Mobile", Utils.getText(input_mobile));
                tempJson.put("Password", Utils.getText(input_password));
                tempJson.put("DeviceID", sharedPref.getString(Constants.DEVICE_ID));
                tempJson.put("Token", sharedPref.getString(Constants.FCM_TOKEN_ID));

            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            new PostAsync(this, Messages.LOADING, tempJson, new CompleteListener() {

                @Override
                public void onTaskCompleted(String result) {
                    try {
                        JSONObject jsonResponse = new JSONObject(result);
                        int responseCode = jsonResponse.optInt(Constants.STATUS_CODE);
                        JSONObject jsonResponseData = new JSONObject(jsonResponse.optString(Constants.RESPONSE));
                        if (responseCode == 200) {
                            if (jsonResponseData.optString("User_Guid").contains("00000000-0000-0000-0000-000000000000"))
                                Toast.makeText(context, "User already exits", Toast.LENGTH_SHORT).show();
                            else {
                                Toast.makeText(context, "User created", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(context, LoginActivity.class));
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
            }).execute(Constants.SIGN_UP_URL);
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
                case R.id.input_display_name:
                    validateName(input_display_name);
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_mobile:
                    validateMobileNo();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
                case R.id.input_password_1:
                    validateCnfPassword();
                    break;
                default:
                    break;
            }
        }
    }
}

package com.nubiz.answerandwin.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

//import com.nubiz.answerandwin.CreateNewGroupActivity;
import com.nubiz.answerandwin.R;
import com.nubiz.answerandwin.dao.UserInfo;
import com.nubiz.answerandwin.service.CompleteListener;
import com.nubiz.answerandwin.service.PostAsync;
import com.nubiz.answerandwin.util.Constants;
import com.nubiz.answerandwin.util.SharedPref;
import com.nubiz.answerandwin.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 04-Jul-17.
 */

public class DialogAddFriend extends AppCompatDialogFragment {

    private EditText edt_number;
    private Dialog dialog;
    private String type;
    private boolean isFromCreateGroup;
    private static final String KEY_TEXT_REPLY = "key_text_reply";
    int mRequestCode = 1000;

    public DialogAddFriend(String s, boolean isFromCreateGroup) {
        type = s;
        this.isFromCreateGroup = isFromCreateGroup;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_add_friend, container, false);

        ((TextView) rootView.findViewById(R.id.txt_header)).setText(type);
        if (type.contains("Add Group")) {
            ((TextView) rootView.findViewById(R.id.txt)).setText("Group Name");
            ((Button) rootView.findViewById(R.id.btn_submit)).setText("Add Group");
        } else {
            ((TextView) rootView.findViewById(R.id.txt)).setText("Mobile Number");
            ((Button) rootView.findViewById(R.id.btn_submit)).setText("Send Request");
        }

        edt_number = ((EditText) rootView.findViewById(R.id.edt_number));
        ((Button) rootView.findViewById(R.id.btn_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });

        ((ImageView) rootView.findViewById(R.id.img_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        return rootView;
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setStyle(STYLE_NO_TITLE, 0);
        return dialog;
    }

    private void sendRequest() {

        if (validateNumber()) return;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("InviterMobile", SharedPref.getInstance(getContext()).getString(Constants.USER_MOBILE));
            jsonObject.put("InviteeMobile", edt_number.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

       /* Utils.showToast(getContext(), "Service to send sms to " + Utils.getText(edt_number));
        if (isFromCreateGroup) {
            UserInfo userInfo = new UserInfo();
            //userInfo.setUserId(Utils.getGUID());//recieved from server response
            userInfo.setUserName(edt_number.getText().toString().trim());
            userInfo.setAddedToGroup(true);
            userInfo.setRequestPending(false);

            ((CreateNewGroupActivity) getActivity()).addToFriendList(userInfo);
        }
*/

        new PostAsync(getContext(), "Sending friend request", jsonObject, new CompleteListener() {

            @Override
            public void onTaskCompleted(String result) {
                try {
                    JSONObject jsonResponse = new JSONObject(result);
                    int responseCode = jsonResponse.optInt(Constants.STATUS_CODE);
                    if (responseCode == 200) {
                        if (jsonResponse.optString(Constants.RESPONSE).contains("Request Send Successfully")) {
                            Utils.showToast(getContext(), "Request Send Successfully");
                        } else
                            Utils.showToast(getContext(), "" + jsonResponse.optString(Constants.RESPONSE).replace("\"",""));

                        if (isFromCreateGroup) {
                            UserInfo userInfo = new UserInfo();
                            //userInfo.setUserId(Utils.getGUID());//recieved from server response
                            userInfo.setUserName(edt_number.getText().toString().trim());
                            userInfo.setAddedToGroup(true);
                            userInfo.setRequestPending(false);

//                            ((CreateNewGroupActivity) getActivity()).addToFriendList(userInfo);
                        }

                    } else
                        Utils.handleResponse(getContext(), responseCode, jsonResponse);
                    dismiss();
                } catch (JSONException e) {

                    if (Constants.SHOW_CUSTOM_MSG)
                        Utils.showToast(getContext(), Constants.CUSTOM_MSG);
                    else
                        Utils.showToast(getContext(), "app: " + result);
                    e.printStackTrace();
                }
            }
        }).execute("http://answerandwin.nubiz.co.in/api/Group/SendFriendRequest");
    }

    private boolean validateNumber() {
        if (edt_number.getText().toString().trim().isEmpty()) {
            edt_number.setError("Please enter a valid number");
            return true;
        } else if (!isValidMobile()) {
            edt_number.setError("Please enter a valid number");
            return true;
        } else if (edt_number.getText().toString().trim().equals(SharedPref.getInstance(getContext()).getString(Constants.USER_MOBILE))) {
            edt_number.setError("Cannot send request to yourself");
            return true;
        }

        return false;
    }

    private boolean isValidMobile() {
        if (edt_number.getText().toString().trim().matches("^[7-9][0-9]{9}$"))
            return true;
        return false;
    }

}


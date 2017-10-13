package com.nubiz.answerandwin.util;

import com.nubiz.answerandwin.R;

/**
 * Created by admin on 3/21/2016.
 */
public class Constants {

    public static final String SHARED_PREF_KEY = "pref_constants";
    public static final String DEVICE_ID = "device_id";
    public static final String FCM_TOKEN_ID = "fcmTokenID";

    public static final int EXCEPTION_CODE = 100;
    public static final String RESPONSE = "Response";
    public static final String STATUS_CODE = "StatusCode";
    public static final String MODELSTATE = "ModelState";

    public final static int PAGE_SIZE = 20;
    public static final int VISIBLE_THRESHOLD = 10;
    public static final int GET_LATLONG_BACK = 125;


    public static final boolean SHOW_CUSTOM_MSG = false;
    public static final boolean SHOW_DETAILED_ERROR = true;
    public static final String CUSTOM_MSG = "Error in parsing response from server";
    public final static String ERROR_MSG = "Error in fetching result from server";


    public static final String REGISTRATION_COMPLETE = "reg_completed";

    public final static int SPLASH_TIME_OUT = 1000;

    public static final String RESULT = "Result";
    public static final String DATA = "Data";
    public static final String MESSAGE = "Message";

    public static final String SAVED_ADDRESS = "saved_address";
    public static final String LAT = "latitude";
    public static final String LNG = "longitude";

    public static final String USER_ID = "UserID";
    public static final String ROLE_ID = "RoleID";
    public static final String USER_NAME = "UserName";
    public static final String USER_MOBILE = "UserMobile";
    public static final String LOGGED_IN = "loggedIn";


    // Service URL
//    public final static String SERVER_HOST_URL = "https://webapiservices.ezeely.in/";
    public final static String SERVER_HOST_URL = "http://answerandwin.nubiz.co.in";

    public final static String SERVICES_PATH_GROUP = SERVER_HOST_URL + "/api/Group/";
    public final static String SERVICES_PATH_LOGIN = SERVER_HOST_URL + "/api/Login/";

    // Used service in this application..........
    public static final String LOGIN_URL = SERVICES_PATH_LOGIN + "ValidateUser";
    public static final String SIGN_UP_URL = SERVICES_PATH_LOGIN + "AddNewUser";

    public static final String SEND_INVITE = SERVICES_PATH_GROUP + "GetInvitationList";
    public static final String GET_FRIEND_LIST = SERVICES_PATH_GROUP + "GetFriendList";

    public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String TAG = "#mobi";


    public static final String MATCH_ID = "match_id";
    public static final String SET_ID = "set_id";
    public static final String GAME_ID = "game_id";

    public static final String TEAM_1 = "team_1";
    public static final String TEAM_2 = "team_2";
    public static final String SELECTED_SERVICE_TEAM = "serivce_team";
    public static final String NO_OF_SETS = "no_of_sets";
    public static final String PLAYER_AND_SET_DATA = "player_&_set_date";
    public static final String PLAYER_AND_SET_DATA_RESUME_MATCH = "player_&_set_date_resume_match";
    public static final String MATCH_STATUS = "matchStats";
    public static final String BEST_OF_3_SETS = "Best of 3 SETS";
    public static final String BEST_OF_5_SETS = "Best of 5 SETS";
    public static final long SET_OF_3 = 11;
    public static final long SET_OF_5 = 12;
    public static final String MATCH_NOT_STARTED = "0";
    public static final String MATCH_STARTED = "1";
    public static final String MATCH_COMPLETED = "2";
    public static final String GROUP_GUID = "group_guid";
    public static String SERVICE_CHOOSER_DAILOG = "service_chooser_dailog";

    public static final String SWAP = "swap";
    public static final String UNDO = "undo";
    public static final String UNSUCCESSFULL = "failure";


    public final static int[] MENU_ITEM_ID_ARRAY = {0000, 1000, 1001, 1002, 1003};
    public final static String[] MENU_ITEM_ARRAY = {"Dashboard", "My Groups", "Friend List", "Invites", "Logout"};
    public final static int[] MENU_ITEM_ICON_ARRAY = {R.mipmap.home_menu, R.mipmap.quick_menu, R.mipmap.schedule_menu, R.mipmap.upcoming_menu, R.mipmap.schedule_menu};


}

package com.nubiz.answerandwin.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nubiz.answerandwin.R;
import com.nubiz.answerandwin.adapter.DrawerListAdapter;
import com.nubiz.answerandwin.dao.DrawerItem;
import com.nubiz.answerandwin.fragment.DashBoardFragment;
import com.nubiz.answerandwin.util.Constants;
import com.nubiz.answerandwin.util.SharedPref;
import com.nubiz.answerandwin.util.Utils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SharedPref sharedPref;
    public DrawerLayout drawerLayout;
    public ListView mDrawerList;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerListAdapter drawerAdapter;
    private ArrayList<DrawerItem> drawerItems = new ArrayList<>();

    private Toolbar toolBar;
    private boolean isDrawerOpen = false;

    protected Context context;
    private LinearLayout layout_drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseControls();
        selectItem(0);
    }

    private void initialiseControls() {
        context = this;
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        sharedPref = SharedPref.getInstance(context);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        layout_drawer = (LinearLayout) findViewById(R.id.layout_drawer);

        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutPopup();
            }
        });

        setMenuListItems();
    }

    private void setMenuListItems() {
        if (drawerItems.size() > 0) return;

        for (int i = 0; i < Constants.MENU_ITEM_ARRAY.length; i++) {
            drawerItems.add(new DrawerItem(Constants.MENU_ITEM_ID_ARRAY[i], Constants.MENU_ITEM_ARRAY[i], "" + Constants.MENU_ITEM_ICON_ARRAY[i], true));
        }

        drawerAdapter = new DrawerListAdapter(context, R.layout.item_drawer_list, drawerItems);
        mDrawerList.setAdapter(drawerAdapter);

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = new DashBoardFragment();
        Bundle args = new Bundle();
//        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.layout_container, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setAppTitleBar(true, Constants.MENU_ITEM_ARRAY[position]);
        drawerLayout.closeDrawer(layout_drawer);
    }


    public void setAppTitleBar(boolean menuEnabled, String title) {
        ((TextView) findViewById(R.id.tv_toolbar_title)).setText(title);
        if (menuEnabled) {
            overridePendingTransition(0, Animation.REVERSE);
            setMenuIcon();
            findViewById(R.id.img_home).setVisibility(View.VISIBLE);
            findViewById(R.id.img_back).setVisibility(View.GONE);
            findViewById(R.id.img_home).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isDrawerOpen)
                        drawerLayout.closeDrawer(Gravity.LEFT);
                    else
                        drawerLayout.openDrawer(Gravity.LEFT);
                }
            });
        } else {

//            overridePendingTransition(R.anim.layout_slide_in_right, R.anim.layout_slide_out_left);
            findViewById(R.id.img_home).setVisibility(View.GONE);
            findViewById(R.id.img_back).setVisibility(View.VISIBLE);

            findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goBack();
                }
            });
        }
    }

    private void setMenuIcon() {
        if (isDrawerOpen)
            ((ImageView) findViewById(R.id.img_home)).setImageResource(R.mipmap.icon_back);
        else
            ((ImageView) findViewById(R.id.img_home)).setImageResource(R.mipmap.icon_menu);
    }

    protected void goBack() {
        finish();
    }

    public void showLogoutPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MaterialAlertDialog);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to logout?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(final DialogInterface dialog, int which) {

                final ProgressDialog pd = Utils.showProgressDialog(context);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sharedPref.saveBoolean(Constants.LOGGED_IN, false);
                        sharedPref.saveString(Constants.USER_ID, "");
                        sharedPref.saveString(Constants.USER_NAME, "");
                        sharedPref.saveInt(Constants.ROLE_ID, 0);

                        Utils.showToast(context, "You have logged out successfully");

                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        dialog.dismiss();
                        pd.dismiss();
                    }
                }, Constants.SPLASH_TIME_OUT);


            }

        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    /** Swaps fragments in the main content view */

}



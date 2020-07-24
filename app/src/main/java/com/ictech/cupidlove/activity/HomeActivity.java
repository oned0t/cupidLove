package com.ictech.cupidlove.activity;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pixplicity.easyprefs.library.Prefs;
import com.ictech.cupidlove.R;
import com.ictech.cupidlove.adapter.AdViewPager;
import com.ictech.cupidlove.adapter.SideMenuAdapter;
import com.ictech.cupidlove.customview.textview.TextViewRegular;
import com.ictech.cupidlove.fragment.BlockedByAdminFragment;
import com.ictech.cupidlove.fragment.ChatListFragment;
import com.ictech.cupidlove.fragment.LocationFragment;
import com.ictech.cupidlove.fragment.DoChatFragment;
import com.ictech.cupidlove.fragment.MyProfileFragment;
import com.ictech.cupidlove.fragment.NotificationFragment;
import com.ictech.cupidlove.fragment.SettingFragment;
import com.ictech.cupidlove.fragment.SwipeViewFragment;
import com.ictech.cupidlove.model.ChatList;
import com.ictech.cupidlove.model.ChatMessage;
import com.ictech.cupidlove.model.Friends;
import com.ictech.cupidlove.model.InAppPurchase;
import com.ictech.cupidlove.utils.AppProperties;
import com.ictech.cupidlove.utils.BaseActivity;
import com.ictech.cupidlove.utils.Config;
import com.ictech.cupidlove.utils.Constant;
import com.ictech.cupidlove.utils.LocalChatListner;
import com.ictech.cupidlove.utils.MyApplication;
import com.ictech.cupidlove.utils.RequestParamUtils;
import com.ictech.cupidlove.utils.URLS;
import com.ictech.cupidlove.utils.apicall.AsyncHttpRequest;
import com.ictech.cupidlove.utils.apicall.Debug;
import com.ictech.cupidlove.utils.apicall.ResponseHandler;
import com.ictech.cupidlove.utils.apicall.ResponseListener;
import com.ictech.cupidlove.utils.purchase.IInAppBillingService.aidl.util.IabHelper;
import com.ictech.cupidlove.utils.purchase.IInAppBillingService.aidl.util.IabResult;
import com.ictech.cupidlove.utils.purchase.IInAppBillingService.aidl.util.Purchase;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class

HomeActivity extends BaseActivity implements ResponseListener, LocalChatListner, RewardedVideoAdListener, IabHelper.OnIabSetupFinishedListener, IabHelper.OnIabPurchaseFinishedListener {

    private static final int REQUEST_LOCATION_PURCHASE = 1330;
    private static final int REQUEST_REMOVE_AD_PURCHASE = 1331;
    private static final int REQUEST_SUPERLIKE_PURCHASE = 1332;
    private static final int REQUEST_CHAT_PURCHASE = 1333;
    //TODO: Bind The All XML View With JAVA file
    @BindView(R.id.ivMenu)
    ImageView ivMenu;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer_layout;

    @BindView(R.id.nav_view)
    NavigationView nav_view;

    @BindView(R.id.listMenuContent)
    ListView listMenuContent;

    @BindView(R.id.ivProfileImage)
    RoundedImageView ivProfileImage;

    @BindView(R.id.tv_Profile_Name)
    TextViewRegular tv_Profile_Name;

    @BindView(R.id.tvCancelSearch)
    TextViewRegular tvCancelSearch;

    @BindView(R.id.llBannerAd)
    LinearLayout llBannerAd;

    //TODO: Variable Declaration

    private ActionBarDrawerToggle actionBarDrawerToggle;
    public static SideMenuAdapter sideMenuAdapter;
    FragmentManager fm;
    Handler h = new Handler();
    int delay = 120 * 1000; //1 second=1000 milisecond, 15*1000=15seconds
    Runnable runnable;
    private boolean isDialogShow = true;

    public AdView adView;
    public RewardedVideoAd mRewardedVideoAd;
    private AdViewPager viewPager;
    private TextView[] dots;
    private Dialog dialog;
    private IabHelper billingHelper;
    private String sku = Constant.SKULOCATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);
        setHomeData();
    }

    public void setHomeData() {
        Log.e("Home Data", "Set");
        adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getPreferences().getString(Config.ADMOBKEY, ""));
        llBannerAd.addView(adView);
        showOrHideAdview();
        showOrHideVideoAdview();
        if (MyApplication.mydb.getFriend() == null) {
            getChatList();
        }
        doBindService();
        setScreenLayoutDirection();
        hideSearch();
        hidetvTitle();
        Constant.ISConnectionDone = true;

        String str = getIntent().getStringExtra(RequestParamUtils.DATA);
        getNotificationCount();

        if (str != null) {
            loadChatFragment(getResources().getString(R.string.chat), getIntent().getStringExtra(RequestParamUtils.XMPPUSERNAME));
        } else if (getIntentData() != null) {
            if (type.equals("1")) {
                //notification
                loadFragment(new NotificationFragment(), getResources().getString(R.string.notification));
            } else if (type.equals("2")) {
                //its a match
                loadFragment(new SwipeViewFragment(), getResources().getString(R.string.start_playing));
                Intent intent = new Intent(this, ItsMatchActivity.class);
                intent.putExtra(RequestParamUtils.FRIEND_USER_ID, fid);
                intent.putExtra(RequestParamUtils.FRIEND_FIRST_NAME, fname);
                intent.putExtra(RequestParamUtils.FRIEND_LAST_NAME, flname);
                intent.putExtra(RequestParamUtils.FRIEND_PROFILE_PICTURE, profile_image);
                startActivity(intent);
            } else if (type.equals("3")) {
                //chat
                loadChatFromPush();
            } else if (type.equals("4")) {
                //admin push
                pushByAdmin(message);
                loadFragment(new SwipeViewFragment(), getResources().getString(R.string.start_playing));
            }
        } else {
            loadFragment(new SwipeViewFragment(), getResources().getString(R.string.start_playing));
        }

        if (getPreferences().getBoolean(RequestParamUtils.USER_BLOCKED, false)) {
            loadFragment(new BlockedByAdminFragment(), getResources().getString(R.string.blocked));
        }

        DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.FILL_PARENT);
        params.gravity = Gravity.END;
        nav_view.setLayoutParams(params);
        initDrawer();
        setChatLister();
        getPrice();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Log.e("Configration ", "Change");
    }


    public void showOrHideVideoAdview() {
        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        mRewardedVideoAd.loadAd(getPreferences().getString(Config.ADMOBVIDEOKEY, ""),
                new AdRequest.Builder().build());

        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    @Override
    public void onRewarded(RewardItem reward) {
//        Toast.makeText(this, "onRewarded! currency: " + reward.getType() + "  amount: " + reward.getAmount(), Toast.LENGTH_SHORT).show();
        // Reward the user.
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
//        Toast.makeText(this, "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
//        Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
        mRewardedVideoAd.loadAd(getPreferences().getString(Config.ADMOBVIDEOKEY, ""),
                new AdRequest.Builder().build());
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
//        Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
//        Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
//        Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
//        Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }

    public void showOrHideAdview() {
        if (getPreferences().getString(Config.ADMOBKEY, "") == "") {
            adView.setVisibility(View.GONE);
            return;
        }
        if (getPreferences().getBoolean(RequestParamUtils.ADENABLED, false)) {
            adView.setVisibility(View.GONE);
        } else {
            adView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    // Check the LogCat to get your test device ID
                    .addTestDevice("370C314F02FF43497AB897D782D77D80")
                    .build();

            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                }

                @Override
                public void onAdClosed() {
//                    Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
//                    Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdLeftApplication() {
//                    Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                }
            });

            adView.loadAd(adRequest);
        }
    }


    //TODO: get user detail when user alredy login
    public String loadChatFromPush() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fid = bundle.getString("friendid");
            type = bundle.getString("type");
            fname = bundle.getString("friend_Fname");
            flname = bundle.getString("friend_Lname");
            message = (bundle.getString("message"));
            profile_image = bundle.getString("friend_profileImg_url");
            Constant.FRIEND_LAST_NAME = flname;
            Constant.FRIEND_FIRST_NAME = fname;
            Constant.EJUSERID = bundle.getString(RequestParamUtils.XMPPUSERNAME);

            loadChatFragment(getResources().getString(R.string.chat), bundle.getString(RequestParamUtils.XMPPUSERNAME) + Constant.XMPP_USERNAME_POSTFIX + Constant.NAME_POSTFIX);
            return type;
        }
        return null;
    }

    //TODO: Set The Chat Listner
    public void setChatLister() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getmService() != null) {
                    getmService().xmpp.addChatLister(HomeActivity.this);
                } else {
                    setChatLister();
                }

            }
        }, 100);
    }

    //TODO : GET Chat List
    public void getChatList() {
        try {
            RequestParams params = new RequestParams();
            params.put("AuthToken", getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", getPreferences().getString(RequestParamUtils.USER_ID, ""));
            Debug.e("getChatList", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().CHAT_LIST, params, new ResponseHandler(this, this, "chat"));
        } catch (Exception e) {
            Debug.e("getChatList Exception", e.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)

    //TODO: Create locat chat method for notification and message
    @Override
    public void onLocalChat(ChatMessage chatMessage) {
        String message = chatMessage.sender;

        if (message.contains("@")) {
//            message = message.substring(0,message.indexOf("_"));
        }

        if (chatMessage.messagetype == 2) {
            message = chatMessage.body;
        } else {
            message = "Date Request....";
        }


//        message = "You have new Message";

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(RequestParamUtils.DATA, "chat");
        intent.putExtra(RequestParamUtils.XMPPUSERNAME, chatMessage.sender);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);


        Bitmap icon;
        icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle("" +
                "You have a new message");
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pIntent);
        notificationBuilder.setContentInfo("");
        notificationBuilder.setLargeIcon(icon);
        notificationBuilder.setContentText(message);
        notificationBuilder.setColor(Color.GRAY);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setLights(Color.YELLOW, 1000, 300);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
        int count = Prefs.getInt(RequestParamUtils.UNREADCHAT, 0) + 1;
        Prefs.putInt(RequestParamUtils.UNREADCHAT, count);

        if (Constant.CHATLISTFRAGMENT != null) {
            Constant.CHATLISTFRAGMENT.onResume();
        }
    }

    //TODO: OnREsume method for aaplication
    @Override
    protected void onResume() {
        //start handler as activity become visible

        h.postDelayed(new Runnable() {
            public void run() {
                //do something
                if (isDialogShow) {
                    getBlockStatus();
                    isDialogShow = false;
                }
                runnable = this;
                h.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();
    }

    //TODO: stop handler when activity not visible
    @Override
    protected void onPause() {
        h.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }

    //TODO: When Push BY Admin
    public void pushByAdmin(String msg) {
        final Dialog dialogAdmin = new Dialog(this);
        dialogAdmin.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAdmin.setContentView(R.layout.dialog_message_from_admin);
        TextViewRegular tvMessageFromAdmin = (TextViewRegular) dialogAdmin.findViewById(R.id.tvMessageFromAdmin);
        TextViewRegular tvAdminOk = (TextViewRegular) dialogAdmin.findViewById(R.id.tvAdminOk);

        tvMessageFromAdmin.setText(msg);

        tvAdminOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAdmin.dismiss();
            }
        });

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(dialogAdmin.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogAdmin.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogAdmin.show();
        dialogAdmin.getWindow().setAttributes(params);
    }

    //TODO: When User Logout
    @OnClick(R.id.llLogout)
    public void llLogoutClick() {
        logout();
    }

    //TODO: When Drawer Open
    public void initDrawer() {

        tv_Profile_Name.setText(getPreferences().getString(RequestParamUtils.FIRST_NAME, "") + " " + getPreferences().getString(RequestParamUtils.LAST_NAME, "") + ", " + getPreferences().getString(RequestParamUtils.AGE, "0"));
        Picasso.with(this).load(new URLS().UPLOAD_URL + getPreferences().getString(RequestParamUtils.PROFILE_IMAGE, "")).error(R.drawable.image_not_found).into(ivProfileImage);

//        ivProfileImage.setBackgroundResource(R.drawable.round_corner_image);
        sideMenuAdapter = new SideMenuAdapter(this);
        listMenuContent.setAdapter(sideMenuAdapter);

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager inputMethodManager =
                        (InputMethodManager) HomeActivity.this.getSystemService(
                                HomeActivity.this.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(
                        HomeActivity.this.getCurrentFocus().getWindowToken(), 0);

                if (Prefs.getBoolean(RequestParamUtils.IS_RTL, false)) {
                    drawer_layout.openDrawer(Gravity.LEFT);
                } else {
                    drawer_layout.openDrawer(Gravity.RIGHT);
                }
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(HomeActivity.this, drawer_layout, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                Log.e("drawer", "close");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                InputMethodManager inputMethodManager =
                        (InputMethodManager) HomeActivity.this.getSystemService(
                                HomeActivity.this.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(
                        HomeActivity.this.getCurrentFocus().getWindowToken(), 0);

                getNotificationCount();
                Picasso.with(HomeActivity.this).load(new URLS().UPLOAD_URL + getPreferences().getString(RequestParamUtils.PROFILE_IMAGE, "")).error(R.drawable.com_facebook_profile_picture_blank_square).into(ivProfileImage);
                Log.e("drawer", "open");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer_layout.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        listMenuContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                selectItemFragment(position);
            }
        });

        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                handle profile image click
                Intent intent = new Intent(HomeActivity.this, FriendProfileActivity.class);
                intent.putExtra(RequestParamUtils.ID, Prefs.getString(RequestParamUtils.USER_ID, ""));
                startActivity(intent);
                drawer_layout.closeDrawer(nav_view);
            }
        });

    }

    //TODO: Select Item from Fragment
    private void selectItemFragment(int position) {

        if (position == -1) {


        } else {
            if (mXmppService != null) {
                mXmppService.xmpp.removeChatLister();
            }

            //handle fragment over here
            if (getPreferences().getBoolean(RequestParamUtils.USER_BLOCKED, false)) {
                loadFragment(new BlockedByAdminFragment(), getResources().getString(R.string.blocked));
            } else if (position == 0) {

                //play now
                loadFragment(new SwipeViewFragment(), getResources().getString(R.string.start_playing));

            } else if (position == 1) {

                //chat
                showSearch();
                loadFragment(new ChatListFragment(), getResources().getString(R.string.chat));

            } else if (position == 2) {

                //notification
                showSearch();
                loadFragment(new NotificationFragment(), getResources().getString(R.string.notification));

            } else if (position == 3) {

                //notification
                showSearch();
                loadFragment(new MyProfileFragment(), getResources().getString(R.string.my_profile));

            } else if (position == 4) {

                //settings
                loadFragment(new SettingFragment(), getResources().getString(R.string.my_preferance));
                adView.setVisibility(View.VISIBLE);

            } else if (position == 5) {

                String in_app_purchase = Prefs.getString(Config.PAID_LOCATION, "OFF");
                if (in_app_purchase != null && !in_app_purchase.isEmpty() && !in_app_purchase.equals("OFF")) {
                    if (Prefs.getBoolean(RequestParamUtils.ALLOWLOCATIONINAPPPURCHASE, false) == true) {
                        loadFragment(new LocationFragment(), getResources().getString(R.string.location));
                    } else {
                        showContarctDialog(Constant.SKULOCATION);
                    }
                } else {
                    loadFragment(new LocationFragment(), getResources().getString(R.string.location));
                }

            } else if (position == 6) {
                if (!Prefs.getBoolean(RequestParamUtils.ADENABLED, false) == true) {
                    showContarctDialog(Constant.SKUREMOVEAD);
                }
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                drawer_layout.closeDrawer(nav_view);
            }
        }, 200);
    }

    //TODO : Load fragment when call any fragment class
    public void loadFragment(Fragment fragment, String tag) {
        // create a FragmentManager
        fm = getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.llMain, fragment, tag);
        fragmentTransaction.commit(); // save the changes
    }

    public void loadChatFragment(String tag, String userName) {
        // create a FragmentManager
        fm = getSupportFragmentManager();
        MyApplication.mydb.getFriend();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        Friends friends = MyApplication.mydb.getFriendDetail(userName);
        if (friends != null) {
            // replace the FrameLayout with new Fragment
            DoChatFragment doChatFragment = new DoChatFragment();
            Bundle args = new Bundle();
            args.putString(RequestParamUtils.XMPPUSERNAME, userName);
            args.putString(RequestParamUtils.ID, friends.getFriend_id());
            Constant.FRIEND_FIRST_NAME = friends.getFname();
            Constant.FRIEND_LAST_NAME = friends.getLname();
            Constant.FRIEND_PROFILE_PICTURE = friends.getProfileImage();
            doChatFragment.setArguments(args);

            fragmentTransaction.replace(R.id.llMain, doChatFragment, tag);
            fragmentTransaction.commit(); // save the changes
        } else {
            DoChatFragment doChatFragment = new DoChatFragment();
            Bundle args = new Bundle();
            args.putString(RequestParamUtils.XMPPUSERNAME, userName);
            doChatFragment.setArguments(args);

            fragmentTransaction.replace(R.id.llMain, doChatFragment, tag);
            fragmentTransaction.commit();
        }


    }


    //TODO: GET The BLock User Satus
    private void getBlockStatus() {

        try {
            RequestParams params = new RequestParams();
            params.put("AuthToken", getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", getPreferences().getString(RequestParamUtils.USER_ID, ""));
            Debug.e("getBlockStatus", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();
            asyncHttpClient.post(new URLS().GET_BLOCK_STATUS, params, new ResponseHandler(this, this, "getBlockStatus"));
        } catch (Exception e) {
            Debug.e("enableAd Exception", e.getMessage());
        }

    }

    //TODO: Backpress method
    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.llMain);
        Log.e("Current Fragment is ", f.getTag() + " String tag is " + getApplicationContext().getResources().getString(R.string.start_playing));
        Locale current = getResources().getConfiguration().locale;

        Log.e("current is ", current.getDisplayName() + getResources().getString(R.string.remove_ads));
        if (drawer_layout.isDrawerOpen(GravityCompat.END)) {
            //drawer is open

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawer_layout.closeDrawer(nav_view);
                }
            }, 200);

        } else {
            if (f.getTag().equals(getResources().getString(R.string.start_playing))) {
                super.onBackPressed();
            } else {
                if (f.getTag().equals(getResources().getString(R.string.chat))
                        || f.getTag().equals(getResources().getString(R.string.notification))
                        || f.getTag().equals(getResources().getString(R.string.my_profile))
                        || f.getTag().equals(getResources().getString(R.string.my_preferance))
                        || f.getTag().equals(getResources().getString(R.string.remove_ads))
                        || f.getTag().equals(getResources().getString(R.string.location))) {
                    selectItemFragment(0);
                } else {
                    if (f.getTag().equals(getResources().getString(R.string.do_chat))) {
                        selectItemFragment(1);
                    }
                }
            }
        }
    }

    //TODO: Notification count method,howmany notification get user
    public void getNotificationCount() {

        try {
            RequestParams params = new RequestParams();
            params.put("AuthToken", getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", getPreferences().getString(RequestParamUtils.USER_ID, ""));

            Debug.e("getNotificationCount", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().GET_NOTIFICATION_COUNT, params, new ResponseHandler(this, this, "getNotificationCount"));
        } catch (Exception e) {
            Debug.e("getNotificationCount Exception", e.getMessage());
        }
    }

    //TODO: Logout method from login
    public void logout() {

        try {
            showProgress("");

            RequestParams params = new RequestParams();
            params.put("AuthToken", getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", getPreferences().getString(RequestParamUtils.USER_ID, ""));

            Debug.e("logout", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().LOGOUT, params, new ResponseHandler(this, this, "logout"));
        } catch (Exception e) {
            Debug.e("logout Exception", e.getMessage());
        }
    }

    //TODO : Report by user method
    public void reportUser(String userID) {

        try {
            showProgress("");

            RequestParams params = new RequestParams();
            params.put("AuthToken", getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", getPreferences().getString(RequestParamUtils.USER_ID, ""));
            params.put("report_to_id", userID);

            Debug.e("reportUser", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().REPORT_USER, params, new ResponseHandler(this, this, "reportUser"));
        } catch (Exception e) {
            Debug.e("reportUser Exception", e.getMessage());
        }
    }

    //TODO: Blockuser method
    public void blockuser(String userID) {

        try {
            showProgress("");

            RequestParams params = new RequestParams();
            params.put("AuthToken", getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", getPreferences().getString(RequestParamUtils.USER_ID, ""));
            params.put("blockid", userID);
            params.put("blockstatus", "1");

            Debug.e("blockuser", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().BLOCK_USER, params, new ResponseHandler(this, this, "blockUser"));
        } catch (Exception e) {
            Debug.e("blockuser Exception", e.getMessage());
        }
    }

    //TODO : Unmetch User method
    public void unMatchUser(String userID) {

        try {
            showProgress("");

            RequestParams params = new RequestParams();
            params.put("AuthToken", getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", getPreferences().getString(RequestParamUtils.USER_ID, ""));
            params.put("friendid", userID);

            Debug.e("unMatchUser", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().USER_UNFRIEND, params, new ResponseHandler(this, this, "unMatchUser"));
        } catch (Exception e) {
            Debug.e("unMatchUser Exception", e.getMessage());
        }
    }

    //TODO : Response off all api call
    public void onResponse(String response, String methodName) {
        dismissProgress();

        Log.e("response", response);
        if (response == null || response.equals("")) {
            return;
        } else if (methodName.equals("getBlockStatus")) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("error")) {
                    //done
                    SharedPreferences.Editor pre = getPreferences().edit();
                    if (jsonObject.getInt("status") == 1) {
                        //block user
                        pre.putBoolean(RequestParamUtils.USER_BLOCKED, true);
                        loadFragment(new BlockedByAdminFragment(), getResources().getString(R.string.blocked));

                    } else {
                        pre.putBoolean(RequestParamUtils.USER_BLOCKED, false);
                    }
                    pre.commit();
                }
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        } else if (methodName.equals("reportUser")) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("error")) {
                    //done
                    Fragment f = getSupportFragmentManager().findFragmentById(R.id.llMain);
                    if (!f.getTag().equals(getResources().getString(R.string.start_playing))) {
                        onBackPressed();
                    }
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        } else if (methodName.equals("blockUser")) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("error")) {
                    //done
                    onBackPressed();
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        } else if (methodName.equals("unMatchUser")) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("error")) {
                    //done
                    onBackPressed();
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        } else if (methodName.equals("getNotificationCount")) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("error")) {
                    //done
                    sideMenuAdapter.addAll(jsonObject.getInt("count"));
                    Constant.UNREADCHAt = Prefs.getInt(RequestParamUtils.UNREADCHAT, 0);
                } else {
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        } else if (methodName.equals("logout")) {

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("error")) {
                    //done
                    sessionExpiredCode(this);
                    Intent intent = new Intent(this, LoginOrSignUpActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else {
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        } else if (methodName.equals("updateDeviceToken")) {
            Log.e(methodName + " Response :-", response);
            try {
                JSONObject jsonObject = new JSONObject(response);

                //do here for updated Device token
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        } else if (methodName.equals("chat")) {

            Log.e("response", response);
            //login
            ChatList chatListRider = new Gson().fromJson(
                    response, new TypeToken<ChatList>() {
                    }.getType());

            if (chatListRider.body.size() != 0) {
                //got data of friends
                if (chatListRider.body.size() != 0) {
                    MyApplication.mydb.deleteFriend();
                    for (int i = 0; i < chatListRider.body.size(); i++) {
                        Friends friends = new Friends(chatListRider.body.get(i).fname, chatListRider.body.get(i).lname,
                                chatListRider.body.get(i).ejUser + Constant.XMPP_USERNAME_POSTFIX + Constant.NAME_POSTFIX, chatListRider.body.get(i).profileImage,
                                chatListRider.body.get(i).fid);
                        MyApplication.mydb.insertFriend(friends);
                    }
                }
            }
        }
    }

    //TODO: Other option for users
    @OnClick(R.id.ivMore)
    public void ivMoreClick() {
        moreOption();
    }

    //TODO: More option provide to user
    private void moreOption() {
        final CharSequence[] items = {"Block", "UnMatch", "Report User", "Cancel"};
        TextViewRegular title = new TextViewRegular(this);
        title.setText("Options");
        title.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        title.setPadding(10, 25, 10, 25);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(22);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCustomTitle(title);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Block")) {
                    //block
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
                    alertDialog.setTitle(getResources().getString(R.string.app_name));
                    alertDialog.setMessage(getResources().getString(R.string.block_user));
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            blockuser(Constant.FRIEND_USER_ID);
                        }
                    });
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();

                } else if (items[item].equals("UnMatch")) {
                    //Unmatch
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
                    alertDialog.setTitle(getResources().getString(R.string.app_name));
                    alertDialog.setMessage(getResources().getString(R.string.unmatch_user));
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            unMatchUser(Constant.FRIEND_USER_ID);
                        }
                    });
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                } else if (items[item].equals("Report User")) {
                    //Report User
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
                    alertDialog.setTitle(getResources().getString(R.string.app_name));
                    alertDialog.setMessage(getResources().getString(R.string.report_user));
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            reportUser(Constant.FRIEND_USER_ID);
                        }
                    });
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void showContarctDialog(String skuCode) {
        this.sku = skuCode;
        dialog = new Dialog(this, android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_ad);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        setAdView(dialog);
        dialog.findViewById(R.id.tvContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInAPpPurchase();
            }
        });

        dialog.findViewById(R.id.tvNoThanks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        }, 200);


    }

    public void setTextContract(int position, Dialog view) {
        ((TextView) view.findViewById(R.id.tvPrice)).setText(InAppPurchase.getList().get(position).getPrice() + "");
        ((TextView) view.findViewById(R.id.tvName)).setText(InAppPurchase.getList().get(position).getName() + "");
        ((TextView) view.findViewById(R.id.tvDescriptions)).setText(InAppPurchase.getList().get(position).getDescription() + "");
    }

    public void setAdView(final Dialog view) {

        ViewPager view_pager = view.findViewById(R.id.view_pager);

        final LinearLayout layoutDots = view.findViewById(R.id.layoutDots);

        viewPager = new AdViewPager(this);
        view_pager.setAdapter(viewPager);
        if (sku != null) {
            int position = getInAppPosition(sku);
            view_pager.setCurrentItem(position);
            setTextContract(position, view);
            addBottomDots(position, layoutDots);
        }


        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position, layoutDots);
                setTextContract(position, view);
                sku = InAppPurchase.getList().get(position).getSku();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private void addBottomDots(int currentPage, LinearLayout layoutDots) {
        dots = new TextView[InAppPurchase.getList().size()];
        layoutDots.removeAllViews();
        for (int i = 0; i < InAppPurchase.getList().size(); i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setPadding(2, 0, 2, 0);
            dots[i].setTextColor(Color.BLACK);
            layoutDots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.WHITE);
    }

    public void setInAPpPurchase() {

        billingHelper = new IabHelper(this, AppProperties.BASE_64_KEY);
        billingHelper.startSetup(this);
    }

    @Override
    public void onIabSetupFinished(IabResult result) {

        Log.e("IabResult ", result.isSuccess() + "");
        if (result.isSuccess()) {
            if (sku.equals(Constant.SKULOCATION)) {
                billingHelper.launchPurchaseFlow(this, sku, REQUEST_LOCATION_PURCHASE, this);
            } else if (sku.equals(Constant.SKUREMOVEAD)) {
                billingHelper.launchPurchaseFlow(this, sku, REQUEST_REMOVE_AD_PURCHASE, this);
            } else if (sku.equals(Constant.SKUSPERLIKE)) {
                billingHelper.launchPurchaseFlow(this, sku, REQUEST_SUPERLIKE_PURCHASE, this);
            } else if (sku.equals(Constant.SKUCHAT)) {
                billingHelper.launchPurchaseFlow(this, sku, REQUEST_CHAT_PURCHASE, this);
            }

        }
    }


    @Override
    public void onIabPurchaseFinished(IabResult result, Purchase info) {
        if (result.isFailure()) {
            Toast.makeText(this, "fail", Toast.LENGTH_LONG).show();
            Log.e("Problem upInappBilling", String.valueOf(result));

        } else {
            billingHelper.consumeAsync(info, null);
            Log.e("In-app Billing Success", String.valueOf(result));
            if (dialog != null) {
                dialog.dismiss();
            }
            InAppPurchaseApiCall(sku);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("On Activity Result", "Called");
        billingHelper.handleActivityResult(requestCode, resultCode, data);


    }


}
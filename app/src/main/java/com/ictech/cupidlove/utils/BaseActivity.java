package com.ictech.cupidlove.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.crash.FirebaseCrash;
import com.ictech.cupidlove.R;
import com.ictech.cupidlove.activity.FriendProfileActivity;
import com.ictech.cupidlove.activity.HomeActivity;
import com.ictech.cupidlove.customview.edittext.EditTextMedium;
import com.ictech.cupidlove.customview.textview.TextViewBold;
import com.ictech.cupidlove.customview.textview.TextViewRegular;
import com.ictech.cupidlove.enums.Puchase;
import com.ictech.cupidlove.helper.InstagramSession;
import com.ictech.cupidlove.model.InAppPurchase;
import com.ictech.cupidlove.utils.apicall.AsyncHttpRequest;
import com.ictech.cupidlove.utils.apicall.AsyncProgressDialog;
import com.ictech.cupidlove.utils.apicall.AsyncResponseHandler;
import com.ictech.cupidlove.utils.apicall.Debug;
import com.ictech.cupidlove.utils.purchase.IInAppBillingService.aidl.util.IabHelper;
import com.ictech.cupidlove.utils.purchase.IInAppBillingService.aidl.util.IabResult;
import com.ictech.xmpp.LocalBinder;
import com.ictech.xmpp.MyXmppService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class BaseActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    //TODO : Variable Declaration
    public ImageView ivSearch, ivMenu;
    private TextViewBold tvTitle;
    private FrameLayout flText;
    private LinearLayout llChat;
    AsyncProgressDialog ad;
    public SharedPreferences sharedpreferences;
    public TextViewBold tvLastSeen;
//    public AdView adView;

    public static MyXmppService mXmppService;
    private boolean mBounded;
    Dialog dialog;
    public LinearLayout llSearch;
    public EditTextMedium etSearch;
    public TextViewRegular tvCancelSearch;
    private Bundle bundle;
    public String id, fid, fname, flname, profile_image, message, type;
    public CustomProgressDialog progressDialog;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 101;
    Location mLastLocation;
    public String lat, lon;

    ServiceConnection mConnection = new ServiceConnection() {

        @SuppressWarnings("unchecked")
        @Override
        public void onServiceConnected(final ComponentName name,
                                       final IBinder service) {
            mXmppService = ((LocalBinder<MyXmppService>) service).getService();
            mXmppService.onCreate();
            mBounded = true;
            Log.e("serviceConnected", "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            mXmppService = null;
            mBounded = false;
            Log.e("serviceDisconnected", "onServiceDisconnected");
        }
    };

    private boolean flag;
    private String Tag = "BaseActivity";
    private LinearLayout llMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showInternateDialog();
        checkInternateConnection();
        final Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            public void uncaughtException(Thread thread, Throwable ex) {
                // get the crash info
                //log it into the file
                if (defaultHandler != null && ex != null && ex.getMessage() != null) {
                    FirebaseCrash.report(new Exception(ex));
                    defaultHandler.uncaughtException(thread, ex);
                }
            }
        });
    }

    //TODO : Set Title
    public void settvTitle(String title) {
        hideChat();
        tvTitle = (TextViewBold) findViewById(R.id.tvTitle);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
    }

    //TODO : Hide Chat
    public void hideChat() {
        flText = (FrameLayout) findViewById(R.id.flText);
        if (flText != null) {
            flText.setVisibility(View.VISIBLE);
        }

        llChat = (LinearLayout) findViewById(R.id.llChat);
        if (llChat != null) {
            llChat.setVisibility(View.GONE);
        }
        hideSearchPanel();
    }

    //TODO : Hide Search Panel
    public void hideSearchPanel() {
        llSearch = (LinearLayout) findViewById(R.id.llSearch);
        if (llSearch != null) {
            llSearch.setVisibility(View.GONE);
        }
    }

    //TODO : Show Search Panel
    public void showSearchPanel() {
        llSearch = (LinearLayout) findViewById(R.id.llSearch);
        llSearch.setVisibility(View.VISIBLE);

        etSearch = (EditTextMedium) findViewById(R.id.etSearch);
        etSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
//        tvCancelSearch = (TextViewRegular) findViewById(R.id.tvCancelSearch);
//        tvCancelSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                llSearch.setVisibility(View.GONE);
//            }
//        });
    }


    public int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    //TODO : Hide Title
    public void hidetvTitle() {
        hideChat();
        tvTitle = (TextViewBold) findViewById(R.id.tvTitle);
        tvTitle.setVisibility(View.GONE);
    }

    //TODO:Check Internate Connectivity
    public void checkInternateConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            changeTextStatus(true);
        } else {
            changeTextStatus(false);
        }
    }

    public void changeTextStatus(boolean isConnected) {

        // Change status according to boolean value
        Log.e("Internate Connected", isConnected + "");
        if (isConnected) {
            if (dialog != null)
                dialog.dismiss();
        } else {
            if (dialog != null)
                dialog.show();
        }
    }

    //TODO:Dialog For NoInternate Connection
    public void showInternateDialog() {
        dialog = new Dialog(this, android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_no_internate_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
        dialog.findViewById(R.id.ivDismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.activity = this;
        MyApplication.activityResumed();// On Resume notify the Application
        checkInternateConnection();
        Log.e("Activity Resume", MyApplication.isActivityVisible() + "");
    }

    //TODO: Bind Service
    public void doBindService() {

//        ComponentName cnmae = startService(new Intent(this, MyXmppService.class));
        Log.e("doBindService", "Called");

        Boolean xmpp_enabled = getPreferences().getBoolean(Config.XMPP_ENABLED, false);

        if (!xmpp_enabled || Constant.NAME_POSTFIX.isEmpty() || Constant.XMPP_PASSWORD.isEmpty() || Constant.XMPP_HOST.isEmpty()
                || Constant.NAME_POSTFIX.equals("_") || Constant.XMPP_PASSWORD.equals("_") || Constant.XMPP_HOST.equals("_")
                || Constant.NAME_POSTFIX.equals("-") || Constant.XMPP_PASSWORD.equals("-") || Constant.XMPP_HOST.equals("-")) {
            return;
        }

        if (Constant.ISConnectionDone) {
            Thread t = new Thread() {
                public void run() {
//                    bindService(startIntent, mConnection, Context.BIND_AUTO_CREATE);
                    if (isAlive()) {
                        Intent startIntent = new Intent(BaseActivity.this, MyXmppService.class);
                        startService(startIntent);
                        bindService(startIntent, mConnection, Context.BIND_AUTO_CREATE);
                        flag = true;
                    }
                }
            };
            t.start();
        }
        // Registering the user
    }

    //TODO : Unbind Service
    @SuppressLint("LongLogTag")
    public void doUnbindService(String activity) {

        Boolean xmpp_enabled = getPreferences().getBoolean(Config.XMPP_ENABLED, false);

        if (!xmpp_enabled || Constant.NAME_POSTFIX == "" || Constant.XMPP_PASSWORD == "" || Constant.XMPP_HOST == "") {
            return;
        }
        try {
            if (mConnection != null) {
                Log.e("Xmpp :Unbind Service", "Closed :-" + activity);
                flag = false;
                try {
                    mXmppService.onDestroy();
                    unbindService(mConnection);

                } catch (Exception e) {
                    mXmppService.onDestroy();
                    Log.e("Xmpp : doUnbindService Exception", e.getMessage());
                }
            }
        } catch (Exception e) {
            Log.e("Xmpp :  Exception", e.getMessage());
        }
    }

    //TODO : Check The Status of Online Or Offline
    @Override
    protected void onStop() {
        try {
            if (!new ForegroundCheckTask().execute(getApplicationContext()).get()) {
                doUnbindService("Baseactivity");

                Log.e("You are", " Offline Now");
            }
        } catch (Exception e) {
            Log.e("Exception occured", e.getMessage());
        }
        Constant.IsOnline = false;
        super.onStop();
    }

    //TODO : Check The Status of Online Or Offline
    @Override
    protected void onStart() {
        super.onStart();
        buildGoogleApiClient();
        if (!Constant.IsOnline)
            try {
                if (new ForegroundCheckTask().execute(getApplicationContext()).get()) {
                    doBindService();
                    Constant.IsOnline = true;
                    Log.e("You are", " Online Now");
                }
            } catch (Exception e) {
                Log.e("Exception occured", e.getMessage());
            }

    }


    public synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    //TODO : Check The Status Restart
    @Override
    protected void onRestart() {
        super.onRestart();
        if (!Constant.IsOnline)
            try {
                if (new ForegroundCheckTask().execute(getApplicationContext()).get()) {
                    Log.e("Online ", "Online");
                    doBindService();
                    Constant.IsOnline = true;
                    Constant.XMPPIALOG = new CustomProgressDialog(this);
                }
            } catch (Exception e) {
                Log.e("Exception occured", e.getMessage());
            }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        } else {
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(100); // Update location every second


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            } else {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                if (mLastLocation != null) {
                    lat = String.valueOf(mLastLocation.getLatitude());
                    lon = String.valueOf(mLastLocation.getLongitude());
                    Constant.LAT = Float.parseFloat(lat);
                    Constant.LNG = Float.parseFloat(lon);
//                    SharedPreferences sharedpreferences = getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
//                    SharedPreferences.Editor pre = sharedpreferences.edit();
//                    pre.putString(RequestParamUtils.LATITUDE, Constant.LAT + "");
//                    pre.putString(RequestParamUtils.LONGITUDE, Constant.LNG + "");
//                    pre.commit();


                }
            }

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        buildGoogleApiClient();
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());
        Constant.LAT = Float.parseFloat(lat);
        Constant.LNG = Float.parseFloat(lon);
        SharedPreferences sharedpreferences = getSharedPreferences(
                Constant.MyPREFERENCES, Context.MODE_PRIVATE);

//        SharedPreferences.Editor pre = sharedpreferences.edit();
//        pre.putString(RequestParamUtils.LATITUDE, Constant.LAT + "");
//        pre.putString(RequestParamUtils.LONGITUDE, Constant.LNG + "");
//        pre.commit();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.


                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                    if (mLastLocation != null) {
                        lat = String.valueOf(mLastLocation.getLatitude());
                        lon = String.valueOf(mLastLocation.getLongitude());
                        Constant.LAT = Float.parseFloat(lat);
                        Constant.LNG = Float.parseFloat(lon);
                        SharedPreferences sharedpreferences = getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);

//                        SharedPreferences.Editor pre = sharedpreferences.edit();
//                        pre.putString(RequestParamUtils.LATITUDE, Constant.LAT + "");
//                        pre.putString(RequestParamUtils.LONGITUDE, Constant.LNG + "");
//                        pre.commit();

                    }
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Context... params) {
            final Context context = params[0].getApplicationContext();
            return isAppOnForeground(context);
        }

        private boolean isAppOnForeground(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null) {
                return false;
            }
            final String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }
    }

    public MyXmppService getmService() {
        return mXmppService;
    }


    public void showChat() {
        flText = (FrameLayout) findViewById(R.id.flText);
        flText.setVisibility(View.GONE);
        llChat = (LinearLayout) findViewById(R.id.llChat);
        llChat.setVisibility(View.VISIBLE);
    }

    public void hideSearch() {
        hideChat();
        ivSearch = (ImageView) findViewById(R.id.ivSearch);
        ivSearch.setVisibility(View.GONE);
    }

    public void hideMenu() {
        hideChat();
        ivMenu = (ImageView) findViewById(R.id.ivMenu);
        ivMenu.setVisibility(View.GONE);
    }

    public void showSearch() {
        hideChat();
        ivSearch = findViewById(R.id.ivSearch);
        ivSearch.setVisibility(View.VISIBLE);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchPanel();
            }
        });
    }


    public SharedPreferences getPreferences() {
        sharedpreferences = getSharedPreferences(
                Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences;
    }


    //TODO : Show Progress
    public void showProgress(String val) {
        progressDialog = new CustomProgressDialog(this);
        progressDialog.showCustomDialog();
    }

    //TODO : Set Screen Layout Direction
    public void setScreenLayoutDirection() {
        llMain = (LinearLayout) findViewById(R.id.llMain);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);


        if (Prefs.getBoolean(RequestParamUtils.IS_RTL, false)) {
            Constant.isRtl = 1;
        } else {
            Constant.isRtl = 0;
        }

        if (drawerLayout != null) {
            if (Constant.isRtl == 1)
                drawerLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            else
                drawerLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        if (Constant.isRtl == 1) {
            llMain.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            llMain.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

    }


    //TODO : Dismiss progress
    public void dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dissmissDialog();
        }

    }

    //TODO : Chat Clicked
    public void llChatClicked(final Activity activity) {
        LinearLayout llChat = (LinearLayout) findViewById(R.id.llChat);

        CircleImageView civHeaderImage = (CircleImageView) findViewById(R.id.civHeaderImage);
        TextViewBold tvName = (TextViewBold) findViewById(R.id.tvName);
        tvLastSeen = (TextViewBold) findViewById(R.id.tvLastSeen);


        tvName.setText(Constant.FRIEND_FIRST_NAME + " " + Constant.FRIEND_LAST_NAME);

        tvLastSeen.setText("Online");
        Picasso.with(activity).load(new URLS().UPLOAD_URL + Constant.FRIEND_PROFILE_PICTURE).placeholder(R.drawable.no_image_profile).error(R.drawable.no_image_profile).into(civHeaderImage);


        llChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, FriendProfileActivity.class);
                intent.putExtra(RequestParamUtils.ID, Constant.FRIEND_ID);
                activity.startActivity(intent);

            }
        });
    }

    @Override
    protected void onDestroy() {
        try {
            if (!new ForegroundCheckTask().execute(getApplicationContext()).get()) {
                doUnbindService("Baseactivity");

                Log.e("You are", " Offline Now");
            }
        } catch (Exception e) {
            Log.e("Exception occured", e.getMessage());
        }
        Constant.IsOnline = false;
        super.onDestroy();
    }

    //TODO : Session Expired
    public void sessionExpiredCode(Activity activity) {

        //remove all data from Shared Preferences
        String insta_callback_base = getPreferences().getString(Config.INSTAGRAM_CALLBACK_BASE, "");
        String insta_client_secret = getPreferences().getString(Config.INSTAGRAM_CLIENT_SECRET, "");
        String insta_client_id = getPreferences().getString(Config.INSTAGRAM_CLIENT_ID, "");
        String admob_key = getPreferences().getString(Config.ADMOBKEY, "");
        String admob_video = getPreferences().getString(Config.ADMOBVIDEOKEY, "");
        String in_app_purchase = getPreferences().getString(Config.INAPPPURCHASEAD, "");

        Boolean xmpp_enable = getPreferences().getBoolean(Config.XMPP_ENABLED, false);


        SharedPreferences.Editor pre = getPreferences().edit();
        pre.clear();
        pre.commit();
        Prefs.clear().commit();

        //fb logout
        AccessToken.setCurrentAccessToken(null);

        //instalogout
        CookieManager cookieManager = CookieManager.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                // a callback which is executed when the cookies have been removed
                @Override
                public void onReceiveValue(Boolean aBoolean) {
                    Log.e("", "Cookie removed: " + aBoolean);
                }
            });
        } else {
            cookieManager.removeAllCookie();
        }

        CookieManager cookieManagers = CookieManager.getInstance();
        cookieManagers.removeAllCookie();
        if (mXmppService.xmpp.connection != null) {
            mXmppService.xmpp.connection.disconnect();
        }
        doUnbindService("Baseactivity");
        InstagramSession msession = new InstagramSession(this);
        msession.resetAccessToken();
        LoginManager.getInstance().logOut();
        pre.putString(RequestParamUtils.FIRST_VISIT, "done");

        pre.putString(Config.INSTAGRAM_CALLBACK_BASE, insta_callback_base);
        pre.putString(Config.INSTAGRAM_CLIENT_SECRET, insta_client_secret);
        pre.putString(Config.INSTAGRAM_CLIENT_ID, insta_client_id);
        pre.putString(Config.ADMOBKEY, admob_key);
        pre.putString(Config.ADMOBVIDEOKEY, admob_video);
        pre.putString(Config.APP_XMPP_SERVER, Constant.NAME_POSTFIX);
        pre.putString(Config.XMPP_DEFAULT_PASSWORD, Constant.XMPP_PASSWORD);
        pre.putString(Config.APP_XMPP_HOST, Constant.XMPP_HOST);
        pre.putString(Config.GOOGLE_PLACE_API_KEY, Constant.GOOGLE_PLACE_API_KEY);
        pre.putString(Config.INAPPPURCHASEAD, in_app_purchase);
        pre.putBoolean(Config.XMPP_ENABLED, xmpp_enable);

        pre.commit();
//        Intent intent = new Intent(activity, LoginOrSignUpActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
    }

    //TODO : Get Intent Data
    public String getIntentData() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            fid = bundle.getString("friendid");
            type = bundle.getString("type");
            fname = bundle.getString("friend_Fname");
            flname = bundle.getString("friend_Lname");
            message = (bundle.getString("message"));
            profile_image = bundle.getString("friend_profileImg_url");
            Constant.FRIEND_LAST_NAME = flname;
            Constant.FRIEND_FIRST_NAME = fname;
            return type;
        }
        return null;
    }


    @Override
    public void attachBaseContext(Context base) {
        Log.e("Attache", "called");
        super.attachBaseContext(updateBaseContextLocale(base));

    }

    public Context updateBaseContextLocale(Context context) {
        String language = "en";
        if (!Prefs.getString(RequestParamUtils.LANGUAGE, "").equals("")) {

            String selectedLang = Prefs.getString(RequestParamUtils.LANGUAGE, "");

            if (selectedLang.equalsIgnoreCase("French"))
                language = "fr";
            else if (selectedLang.equalsIgnoreCase("Russian"))
                language = "ru";

            else if (selectedLang.equalsIgnoreCase("arabic"))
                language = "ar";

            else if (selectedLang.equalsIgnoreCase("hindi"))
                language = "hi";

            else
                language = "en";
        } else {
            Prefs.putString(RequestParamUtils.LANGUAGE, "en");
        }
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResourcesLocale(context, locale);
        }

        return updateResourcesLocaleLegacy(context, locale);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private Context updateResourcesLocale(Context context, Locale locale) {
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private Context updateResourcesLocaleLegacy(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }

    public void getPrice() {
        final IabHelper billingHelper = new IabHelper(this, AppProperties.BASE_64_KEY);

        billingHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (result.isSuccess()) {
                    try {
                        billingHelper.getPricesDev();
                    } catch (Exception e) {
                        Log.e("Exception is ", e.getMessage());

                    }
                }
            }
        });
    }

    public Integer getInAppPosition(String sku) {
        for (int i = 0; i < InAppPurchase.getList().size(); i++) {
            if (InAppPurchase.getList().get(i).getSku().equals(sku)) {
                return i;
            }
        }
        return null;
    }

    public void InAppPurchaseApiCall(String key) {
        final String finalKey = key;
        if (key.equals(Constant.SKUCHAT)) {
            key = Puchase.PAID_CHAT.getPurchase();
        } else if (key.equals(Constant.SKULOCATION)) {
            key = Puchase.PAID_LOCATION.getPurchase();
        } else if (key.equals(Constant.SKUSPERLIKE)) {
            key = Puchase.PAID_SUPERLIKE.getPurchase();
        } else if (key.equals(Constant.SKUREMOVEAD)) {
            key = Puchase.PAID_AD.getPurchase();
        }
        try {
            RequestParams params = new RequestParams();
            params.put("AuthToken", getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", getPreferences().getString(RequestParamUtils.USER_ID, ""));
            params.put("purchasekey", key);

            Debug.e("chatPurchase", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();


            asyncHttpClient.post(new URLS().PURCHASE, params, new AsyncResponseHandler(this) {
                @Override
                public void onSuccess(String reponse) {
                    if (reponse.toString() == null || reponse.toString().equals("")) {
                        return;
                    } else {
                        try {
                            Log.e("Response is ", reponse);
                            JSONObject jsonObject = new JSONObject(reponse.toString());
                            if (!jsonObject.getBoolean("error")) {
                                getPrice();
                                if (finalKey.equals(Constant.SKUCHAT)) {
                                    Prefs.putBoolean(RequestParamUtils.ALLOWCHATINAPPPURCHASE, true);
                                } else if (finalKey.equals(Constant.SKULOCATION)) {
                                    Prefs.putBoolean(RequestParamUtils.ALLOWLOCATIONINAPPPURCHASE, true);
                                } else if (finalKey.equals(Constant.SKUREMOVEAD)) {
                                    Prefs.putBoolean(RequestParamUtils.ADENABLED, true);
                                    if (HomeActivity.sideMenuAdapter != null) {
                                        HomeActivity.sideMenuAdapter.addList(
                                                Arrays.asList(
                                                        getResources().getString(R.string.start_playing),
                                                        getResources().getString(R.string.chat),
                                                        getResources().getString(R.string.notification),
                                                        getResources().getString(R.string.my_profile),
                                                        getResources().getString(R.string.my_preferance),
                                                        getResources().getString(R.string.location))


                                        );
                                    }
                                } else if (finalKey.equals(Constant.SKUSPERLIKE)) {
                                    Prefs.putBoolean(RequestParamUtils.ALLOWSUPERLIKEINAPPPURCHASE, true);
                                }
                            } else {
                                Toast.makeText(BaseActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Log.e("Exception is ", e.getMessage());
                        }
                        dismissProgress();
                    }
                }

                @Override
                public void onFailure(Throwable e, String content) {

                }
            });

        } catch (Exception e) {
            Debug.e("chatPurchase Exception", e.getMessage());
        }
    }

}
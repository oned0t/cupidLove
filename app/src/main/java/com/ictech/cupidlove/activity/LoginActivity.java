package com.ictech.cupidlove.activity;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.pixplicity.easyprefs.library.Prefs;
import com.ictech.cupidlove.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ictech.cupidlove.customview.edittext.EditTextRegular;
import com.ictech.cupidlove.enums.Puchase;
import com.ictech.cupidlove.model.Configration;
import com.ictech.cupidlove.model.Login;
import com.ictech.cupidlove.utils.Config;
import com.ictech.cupidlove.utils.apicall.AsyncHttpRequest;
import com.ictech.cupidlove.utils.BaseActivity;
import com.ictech.cupidlove.utils.Constant;
import com.ictech.cupidlove.utils.apicall.Debug;
import com.ictech.cupidlove.utils.RequestParamUtils;
import com.ictech.cupidlove.utils.apicall.ResponseHandler;
import com.ictech.cupidlove.utils.apicall.ResponseListener;
import com.ictech.cupidlove.utils.URLS;
import com.ictech.cupidlove.utils.Utils;
import com.ictech.xmpp.MyXmppService;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements ResponseListener {

    //TODO : Bind The All XML View With JAVA File
    @BindView(R.id.tvLogin)
    TextView tvLogin;

    @BindView(R.id.etEmail)
    EditTextRegular etEmail;

    @BindView(R.id.etPassword)
    EditTextRegular etPassword;

    @BindView(R.id.back)
    ImageView back;

    //TODO : Variable Declaration
    public static MyXmppService mXmppService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        ButterKnife.bind(this);
        setScreenLayoutDirection();
        isFullButton();
        Constant.ISConnectionDone = false;
        getAllConfiguration();
    }

    @OnClick(R.id.back)
    public void backClick() {
        onBackPressed();
    }


    //TODO: Click On SignUp Button
    @OnClick(R.id.tvSignUp)
    public void tvSignUpClick() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    //TODO: Click On Login Button
    @OnClick(R.id.tvLogin)
    public void tvLoginClick() {
        if (etEmail.getText().toString().isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.enter_username), Toast.LENGTH_SHORT).show();
        } else {
            if (Utils.isValidEmail(etEmail.getText().toString())) {
                if (etPassword.getText().toString().isEmpty()) {
                    Toast.makeText(this, getResources().getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
                } else {
                    //call login
                    loginWithEmail();
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.enter_valid_email_address), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //TODO : click On Forget password for Login
    @OnClick(R.id.tvForgetPassword)
    public void tvForgetPasswordClick() {
        if (etEmail.getText().toString().isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.enter_username), Toast.LENGTH_SHORT).show();
        } else {
            if (Utils.isValidEmail(etEmail.getText().toString())) {
                //call forget password
                forgetPassword();
            } else {
                Toast.makeText(this, getResources().getString(R.string.enter_valid_email_address), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //TODO: Show The Full Button
    public void isFullButton() {
        if (Constant.IS_FULL_BUTTON_SHOW) {
            tvLogin.setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)));
        } else {
            tvLogin.setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)));
        }
    }

    //TODO : Send the Link for Reset New Password On user email ID
    public void forgetPassword() {
        try {
            showProgress("");
            RequestParams params = new RequestParams();
            params.put("email", etEmail.getText().toString());

            Debug.e("forgetPassword", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().FORGET_PASSWORD, params, new ResponseHandler(LoginActivity.this, this, "forget_password"));
        } catch (Exception e) {
            Debug.e("forgetPassword Exception", e.getMessage());
        }
    }

    //TODO : Get All Configration
    public void getAllConfiguration() {
        try {

            RequestParams params = new RequestParams();
            Debug.e("getAllConfiguration", params.toString() + "Called");
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().CONFIGURATION, params, new ResponseHandler(this, this, "getAllConfiguration"));
        } catch (Exception e) {
            Debug.e("getAllConfiguration Exception", e.getMessage());
        }
    }

    //TODO : User Login With Email ID
    public void loginWithEmail() {
        try {
            showProgress("");
            RequestParams params = new RequestParams();
            params.put("email", etEmail.getText().toString());
            params.put("password", etPassword.getText().toString());
            params.put("device", "android");

            String refreshToken = FirebaseInstanceId.getInstance().getToken();

            SharedPreferences.Editor pre = getPreferences().edit();
            pre.putString(RequestParamUtils.DEVICE_TOKEN, refreshToken);
            pre.commit();

            params.put("device_token", refreshToken);


            Debug.e("loginWithEmail", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().LOGIN, params, new ResponseHandler(LoginActivity.this, this, "login"));
        } catch (Exception e) {
            Debug.e("loginWithEmail Exception", e.getMessage());
        }
    }

    //TODO : User Login Response
    @Override
    public void onResponse(String response, String methodName) {
        dismissProgress();


        if (response == null || response.equals("")) {
            return;
        } else if (methodName.equals("forget_password")) {
            Log.e(methodName + " Response is ", response);
            try {
                JSONObject obj = new JSONObject(response);

                new AlertDialog.Builder(this)
                        .setTitle(getResources().getText(R.string.app_name))
                        .setMessage(obj.getString("message"))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

            } catch (Throwable t) {
                Debug.e("error", response);
            }

        } else if (methodName.equals("login")) {
            Log.e("Login Response is:-", response.toString());
            //login
            final Login loginRider = new Gson().fromJson(
                    response, new TypeToken<Login>() {
                    }.getType());

            if (!loginRider.error) {
                //Login Successfull
                SharedPreferences.Editor pre = getPreferences().edit();
                pre.putString(RequestParamUtils.USER_DATA, response);

                pre.putString(RequestParamUtils.LATITUDE, loginRider.loginUserDetails.locationLat + "");
                pre.putString(RequestParamUtils.LONGITUDE, loginRider.loginUserDetails.locationLong + "");

                pre.putString(RequestParamUtils.USER_ID, loginRider.loginUserDetails.id + "");
                pre.putString(RequestParamUtils.AUTH_TOKEN, loginRider.loginUserDetails.authToken + "");
                pre.putString(RequestParamUtils.FIRST_NAME, loginRider.loginUserDetails.fname + "");
                pre.putString(RequestParamUtils.EMAIL, loginRider.loginUserDetails.email + "");
                pre.putString(RequestParamUtils.EDUCATION, loginRider.loginUserDetails.education + "");
                pre.putString(RequestParamUtils.PROFETION, loginRider.loginUserDetails.profession + "");
                pre.putString(RequestParamUtils.BIRTHDATE, loginRider.loginUserDetails.dob + "");
                pre.putString(RequestParamUtils.LAST_NAME, loginRider.loginUserDetails.lname + "");
                pre.putString(RequestParamUtils.AGE, loginRider.loginUserDetails.age + "");
                Prefs.putString(RequestParamUtils.USER_ID, loginRider.loginUserDetails.id + "");
                Prefs.putString(RequestParamUtils.ROSTER_NICK_NAME, loginRider.loginUserDetails.fname + "");
                Prefs.putString(RequestParamUtils.FIRST_NAME, loginRider.loginUserDetails.ejUser + "");
                pre.putString(RequestParamUtils.DATE_PREF, loginRider.loginUserDetails.datePref);

                if (loginRider.loginUserDetails.enableAdd.equals("0")) {
                    pre.putBoolean(RequestParamUtils.ADENABLED, true);
                } else {
                    pre.putBoolean(RequestParamUtils.ADENABLED, false);
                }

//                Prefs.putString(RequestParamUtils.XMPPUSERNAME, loginRider.loginUserDetails.id + "_" + loginRider.loginUserDetails.fname);
//                Prefs.putString(RequestParamUtils.XMPPUSERPASSWORD, "potenza@123");

                if (loginRider.userGallary.img1 != null) {
                    pre.putString(RequestParamUtils.PROFILE_IMAGE, loginRider.userGallary.img1 + "");
                } else if (loginRider.userGallary.img2 != null) {
                    pre.putString(RequestParamUtils.PROFILE_IMAGE, loginRider.userGallary.img2 + "");
                } else if (loginRider.userGallary.img3 != null) {
                    pre.putString(RequestParamUtils.PROFILE_IMAGE, loginRider.userGallary.img3 + "");
                } else if (loginRider.userGallary.img4 != null) {
                    pre.putString(RequestParamUtils.PROFILE_IMAGE, loginRider.userGallary.img4 + "");
                } else if (loginRider.userGallary.img5 != null) {
                    pre.putString(RequestParamUtils.PROFILE_IMAGE, loginRider.userGallary.img5 + "");
                } else if (loginRider.userGallary.img6 != null) {
                    pre.putString(RequestParamUtils.PROFILE_IMAGE, loginRider.userGallary.img6 + "");
                }

                pre.commit();

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                setPurchase(loginRider.userPurchase);
//                doBindService();


            } else {
                Toast.makeText(this, loginRider.message, Toast.LENGTH_SHORT).show();
            }
        }else if (methodName.equals("getAllConfiguration")) {
            Log.e("response", response);

            try {
                final Configration configurationRider = new Gson().fromJson(
                        response, new TypeToken<Configration>() {
                        }.getType());

                if (configurationRider.gOOGLEPLACEAPIKEY != null) {
                    getPreferences().edit().putString(Config.GOOGLE_PLACE_API_KEY, configurationRider.gOOGLEPLACEAPIKEY).commit();

                    getPreferences().edit().putString(Config.APP_XMPP_HOST, configurationRider.aPPXMPPHOST).commit();
                    getPreferences().edit().putString(Config.APP_XMPP_SERVER, configurationRider.aPPXMPPSERVER).commit();
                    getPreferences().edit().putString(Config.XMPP_DEFAULT_PASSWORD, configurationRider.xMPPDEFAULTPASSWORD).commit();
                    Boolean boolean1 = Boolean.valueOf(configurationRider.xMPPENABLE);
                    getPreferences().edit().putBoolean(Config.XMPP_ENABLED, boolean1).commit();
                    getPreferences().edit().putString(Config.INSTAGRAM_CALLBACK_BASE, configurationRider.iNSTAGRAMCALLBACKBASE).commit();
                    getPreferences().edit().putString(Config.INSTAGRAM_CLIENT_SECRET, configurationRider.iNSTAGRAMCLIENTSECRET).commit();
                    getPreferences().edit().putString(Config.INSTAGRAM_CLIENT_ID, configurationRider.iNSTAGRAMCLIENTID).commit();
                    getPreferences().edit().putString(Config.ADMOBKEY, configurationRider.adMobKey).commit();
                    getPreferences().edit().putString(Config.ADMOBVIDEOKEY, configurationRider.adMobVideoKey).commit();
                    getPreferences().edit().putString(Config.TERMS_CONDITION, configurationRider.termsAndConditionsUrl).commit();


                    Prefs.putString(Config.INAPPPURCHASEAD, configurationRider.removeAddInAppBilling);
                    Prefs.putString(Config.INAPPPURCHASECHAT, configurationRider.paidChatInAppBilling);
                    Prefs.putString(Config.INAPPPURCHASELOCATION, configurationRider.locationInAppBilling);
                    Prefs.putString(Config.INAPPPURCHASESUPERLIKE, configurationRider.superLikeInAppBilling);


                    Prefs.putString(Config.PAID_LOCATION, configurationRider.pAIDLOCATION);
                    Prefs.putString(Config.PAID_SUPERLIKE, configurationRider.pAIDSUPERLIKE);
                    if (configurationRider.removeAddInAppBilling == null || configurationRider.removeAddInAppBilling.equals("")) {
                        Prefs.putString(Config.PAID_AD, "OFF");
                    } else {
                        Prefs.putString(Config.PAID_AD, configurationRider.pAIDAD);
                        Constant.SKUREMOVEAD=configurationRider.removeAddInAppBilling;
                    }

                    if (configurationRider.paidChatInAppBilling == null || configurationRider.paidChatInAppBilling.equals("")) {
                        Prefs.putString(Config.PAID_CHAT, "OFF");
                    } else {
                        Prefs.putString(Config.PAID_CHAT, configurationRider.pAIDCHAT);
                        Constant.SKUCHAT=configurationRider.paidChatInAppBilling;
                    }

                    if (configurationRider.locationInAppBilling == null || configurationRider.locationInAppBilling.equals("")) {
                        Prefs.putString(Config.PAID_LOCATION, "OFF");
                    } else {
                        Prefs.putString(Config.PAID_LOCATION, configurationRider.pAIDLOCATION);
                        Constant.SKULOCATION=configurationRider.locationInAppBilling;
                    }
                    if (configurationRider.superLikeInAppBilling == null || configurationRider.superLikeInAppBilling.equals("")) {
                        Prefs.putString(Config.PAID_SUPERLIKE, "OFF");
                    } else {
                        Prefs.putString(Config.PAID_SUPERLIKE, configurationRider.pAIDSUPERLIKE);
                        Constant.SKUSPERLIKE=configurationRider.superLikeInAppBilling;
                    }
                }

            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        }
    }


    public void setPurchase(List<Login.UserPurchase> purchase) {

        for (int i = 0; i < purchase.size(); i++) {
            if (purchase.get(i).purchasekey.equals(Puchase.PAID_AD.getPurchase())) {
                Prefs.putBoolean(RequestParamUtils.ADENABLED, true);
            }
            if (purchase.get(i).purchasekey.equals(Puchase.PAID_SUPERLIKE.getPurchase())) {
                Prefs.putBoolean(RequestParamUtils.ALLOWSUPERLIKEINAPPPURCHASE, true);
            }
            if (purchase.get(i).purchasekey.equals(Puchase.PAID_LOCATION.getPurchase())) {
                Prefs.putBoolean(RequestParamUtils.ALLOWLOCATIONINAPPPURCHASE, true);
            }
            if (purchase.get(i).purchasekey.equals(Puchase.PAID_CHAT.getPurchase())) {
                Prefs.putBoolean(RequestParamUtils.ALLOWCHATINAPPPURCHASE, true);
            }
        }

    }

}

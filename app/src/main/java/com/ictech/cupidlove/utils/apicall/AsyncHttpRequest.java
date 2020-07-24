package com.ictech.cupidlove.utils.apicall;

import android.app.Activity;

import com.loopj.android.http.AsyncHttpClient;
import com.ictech.cupidlove.utils.Constant;

import java.security.KeyStore;


public class AsyncHttpRequest extends AsyncHttpClient {

    //TODO : Variabl Declaration
    Activity activity;


    public AsyncHttpRequest(Activity activity) {
        this.activity = activity;
    }


    public static AsyncHttpClient newRequest() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(Constant.TIMEOUT);
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            MySSLSocketFactory  sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            client.setSSLSocketFactory(sf);
        }
        catch (Exception e) {
        }
        return client;
    }



}

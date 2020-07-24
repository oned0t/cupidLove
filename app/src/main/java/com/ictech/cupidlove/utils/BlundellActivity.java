package com.ictech.cupidlove.utils;

import android.app.Activity;
import android.os.Bundle;


/**
 * Here we keep common functionality that will be used across multiple activities
 * making our life easier
 * 
 * @author Blundell
 * 
 */
public abstract class BlundellActivity extends Activity {

    private NavigatorChat navigator;
    private Toaster toaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navigator = new NavigatorChat(this);
        toaster = new Toaster(this);
    }

    protected NavigatorChat navigate() {
        return navigator;
    }

    protected void popBurntToast(String msg) {
        toaster.popBurntToast(msg);
    }

    protected void popToast(String msg) {
        toaster.popToast(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        navigator = null;
        toaster = null;
    }
}

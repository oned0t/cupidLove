package com.ictech.cupidlove.utils;

import android.app.Activity;
import android.os.Bundle;

public class BlundellSuperlikeActivity extends Activity {

    private NavigationSuperLike navigator;
    private Toaster toaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navigator = new NavigationSuperLike(this);
        toaster = new Toaster(this);
    }

    protected NavigationSuperLike navigate() {
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

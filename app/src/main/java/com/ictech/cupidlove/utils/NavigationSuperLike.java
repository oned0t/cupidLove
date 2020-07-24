package com.ictech.cupidlove.utils;

import android.app.Activity;
import android.content.Intent;

import com.ictech.cupidlove.activity.HomeActivity;
import com.ictech.cupidlove.activity.PurchaseSuperLikeActivity;

public class NavigationSuperLike {

    public static final int REQUEST_SUPERLIKE_PURCHASE = 2014;

    private final Activity activity;

    public NavigationSuperLike(Activity activity) {
        this.activity = activity;
    }

    public void toMainActivity() {
        Intent intent = new Intent(activity, HomeActivity.class);
        activity.startActivity(intent);
    }

    public void toPurchaseSuperLikeActivityForResult() {
        Intent intent = new Intent(activity, PurchaseSuperLikeActivity.class);
        activity.startActivityForResult(intent, REQUEST_SUPERLIKE_PURCHASE);
    }

}

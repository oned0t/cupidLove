package com.ictech.cupidlove.utils;

import android.app.Activity;
import android.content.Intent;

import com.ictech.cupidlove.activity.HomeActivity;
import com.ictech.cupidlove.activity.PurchaseLocationActivity;

public class NavigatorLocation {

    public static final int REQUEST_Location_PURCHASE = 2013;

    private final Activity activity;

    public NavigatorLocation(Activity activity) {
        this.activity = activity;
    }

    public void toMainActivity() {
        Intent intent = new Intent(activity, HomeActivity.class);
        activity.startActivity(intent);
    }

    public void toPurchaseLocationActivityForResult() {
        Intent intent = new Intent(activity, PurchaseLocationActivity.class);
        activity.startActivityForResult(intent, REQUEST_Location_PURCHASE);
    }

}

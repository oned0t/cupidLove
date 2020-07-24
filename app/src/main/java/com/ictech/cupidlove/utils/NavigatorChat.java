package com.ictech.cupidlove.utils;

import android.app.Activity;
import android.content.Intent;

import com.ictech.cupidlove.activity.HomeActivity;
import com.ictech.cupidlove.activity.PurchaseSuperLikeActivity;

/**
 * A wrapper around the Android Intent mechanism
 * 
 * @author Blundell
 * 
 */
public class NavigatorChat {

    public static final int REQUEST_PASSPORT_PURCHASE = 2012;

    private final Activity activity;

    public NavigatorChat(Activity activity) {
        this.activity = activity;
    }

    public void toMainActivity() {
        Intent intent = new Intent(activity, HomeActivity.class);
        activity.startActivity(intent);
    }

    public void toPurchaseChatActivityForResult() {
        Intent intent = new Intent(activity, PurchaseSuperLikeActivity.class);
        activity.startActivityForResult(intent, REQUEST_PASSPORT_PURCHASE);
    }

}

package com.ictech.cupidlove.utils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.pixplicity.easyprefs.library.Prefs;
import com.ictech.cupidlove.R;
import com.ictech.cupidlove.utils.purchase.IInAppBillingService.aidl.util.IabHelper;
import com.ictech.cupidlove.utils.purchase.IInAppBillingService.aidl.util.IabHelper.OnIabPurchaseFinishedListener;
import com.ictech.cupidlove.utils.purchase.IInAppBillingService.aidl.util.IabHelper.OnIabSetupFinishedListener;
import com.ictech.cupidlove.utils.purchase.IInAppBillingService.aidl.util.IabResult;
import com.ictech.cupidlove.utils.purchase.IInAppBillingService.aidl.util.Purchase;

/**
 * This class should be the parent of any Activity that wants to do in app purchases
 * it makes our life easier by wrapping up the talking to the IabHelper and just exposing what is needed.
 * 
 * When this activity starts it will bind to the Google Play IAB service and check for its availability
 * 
 * After that you can purchase items using purchaseItem(String sku) and listening for the result
 * by overriding dealWithPurchaseFailed(IabResult result) and dealWithPurchaseSuccess(IabResult result, Purchase info)
 * 
 * @author Blundell
 * 
 */
public abstract class PurchaseActivity extends BlundellActivity implements OnIabSetupFinishedListener, OnIabPurchaseFinishedListener {

    private IabHelper billingHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_progress);
        setResult(RESULT_CANCELED);

        billingHelper = new IabHelper(this, AppProperties.BASE_64_KEY);
        billingHelper.startSetup(this);
    }

    @Override
    public void onIabSetupFinished(IabResult result) {
        if (result.isSuccess()) {
            Log.e("In-app Billing set up" , String.valueOf(result));
            dealWithIabSetupSuccess();
        } else {
            Log.e("Problem upInappBilling" , String.valueOf(result));
            dealWithIabSetupFailure();
        }
    }

    protected abstract void dealWithIabSetupSuccess();

    protected abstract void dealWithIabSetupFailure();

    protected void purchaseItem(String sku) {
        billingHelper.launchPurchaseFlow(this, sku, 123, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        billingHelper.handleActivityResult(requestCode, resultCode, data);
    }

    /**
     * Security Recommendation: When you receive the purchase response from Google Play, make sure to check the returned data
     * signature, the orderId, and the developerPayload string in the Purchase object to make sure that you are getting the
     * expected values. You should verify that the orderId is a unique value that you have not previously processed, and the
     * developerPayload string matches the token that you sent previously with the purchase request. As a further security
     * precaution, you should perform the verification on your own secure server.
     */
    @Override
    public void onIabPurchaseFinished(IabResult result, Purchase info) {
        if (result.isFailure()) {
            dealWithPurchaseFailed(result);
        } else if (Constant.SKUCHAT.equals(info.getSku())) {
            dealWithPurchaseSuccess(result, info);
        }
        finish();
    }

    protected void dealWithPurchaseFailed(IabResult result) {
        Prefs.putBoolean(RequestParamUtils.ALLOWCHATINAPPPURCHASE, false);
        Log.e("Error purchasing: " , String.valueOf(result));
    }

    protected void dealWithPurchaseSuccess(IabResult result, Purchase info) {
        Log.e("Item purchased: " , String.valueOf(result));
        // DEBUG XXX
        // We consume the item straight away so we can test multiple purchases
        Prefs.putBoolean(RequestParamUtils.ALLOWCHATINAPPPURCHASE, true);
        billingHelper.consumeAsync(info, null);
        // END DEBUG
    }

    @Override
    protected void onDestroy() {
        disposeBillingHelper();
        super.onDestroy();
    }

    private void disposeBillingHelper() {
        if (billingHelper != null) {
            billingHelper.dispose();
        }
        billingHelper = null;
    }
}

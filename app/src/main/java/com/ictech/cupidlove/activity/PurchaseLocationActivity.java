package com.ictech.cupidlove.activity;

import android.os.Bundle;

import com.ictech.cupidlove.utils.Constant;
import com.ictech.cupidlove.utils.PurchaseLocation;
import com.ictech.cupidlove.utils.purchase.IInAppBillingService.aidl.util.IabResult;
import com.ictech.cupidlove.utils.purchase.IInAppBillingService.aidl.util.Purchase;

public class PurchaseLocationActivity extends PurchaseLocation {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the result as cancelled in case anything fails before we purchase the item
        setResult(RESULT_CANCELED);
        // Then wait for the callback if we have successfully setup in app billing or not (because we extend PurchaseActivity)
    }

    @Override
    protected void dealWithIabSetupFailure() {
        popBurntToast("Sorry buying a location is not available at this current time");
        finish();
    }

    @Override
    protected void dealWithIabSetupSuccess() {
        purchaseItem(Constant.SKULOCATION);
    }

    @Override
    protected void dealWithPurchaseSuccess(IabResult result, Purchase info) {
        super.dealWithPurchaseSuccess(result, info);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void dealWithPurchaseFailed(IabResult result) {
        super.dealWithPurchaseFailed(result);
        setResult(RESULT_CANCELED);
        finish();
    }

}

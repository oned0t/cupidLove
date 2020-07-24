package com.ictech.cupidlove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ictech.cupidlove.R;
import com.ictech.cupidlove.customview.textview.TextViewRegular;
import com.ictech.cupidlove.utils.BlundellLocationActivity;
import com.ictech.cupidlove.utils.NavigatorLocation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationInAppPurchaseActivity extends BlundellLocationActivity {

    @BindView(R.id.tvLocationPurchase)
    TextViewRegular tvLocationPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_in_app_purchase);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tvLocationPurchase)
    public void locationPurchaseClick() {
        navigate().toPurchaseLocationActivityForResult();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (NavigatorLocation.REQUEST_Location_PURCHASE == requestCode) {
            if (RESULT_OK == resultCode) {
                dealWithSuccessfulPurchase();
            } else {
                dealWithFailedPurchase();
            }
        }
    }

    private void dealWithSuccessfulPurchase() {

        Log.d("Location purchased Result", "Location Purchased");
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        popToast("Location Successfully purchased");
    }

    private void dealWithFailedPurchase() {
        Log.d("Location purchased Result", "Location purchase failed");
        popToast("Failed to purchase Chat");
    }


}

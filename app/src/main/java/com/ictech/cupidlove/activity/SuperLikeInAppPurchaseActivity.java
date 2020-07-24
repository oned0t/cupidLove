package com.ictech.cupidlove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import butterknife.ButterKnife;
import com.ictech.cupidlove.R;
import com.ictech.cupidlove.customview.textview.TextViewRegular;
import com.ictech.cupidlove.utils.BlundellSuperlikeActivity;
import com.ictech.cupidlove.utils.NavigationSuperLike;

import butterknife.BindView;
import butterknife.OnClick;

public class SuperLikeInAppPurchaseActivity extends BlundellSuperlikeActivity {

    @BindView(R.id.tvChatPurchase)
    TextViewRegular tvChatPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_like_in_app_purchase);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tvChatPurchase)
    public void purchaseClick(){
        navigate().toPurchaseSuperLikeActivityForResult();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("On Activity Result","Called");
        if (NavigationSuperLike.REQUEST_SUPERLIKE_PURCHASE == requestCode) {
            if (RESULT_OK == resultCode) {
                dealWithSuccessfulPurchase();
            } else {
                dealWithFailedPurchase();
            }
        }
    }

    private void dealWithSuccessfulPurchase() {

        Log.e("Chat purchased Result","Chat purchased");
        Intent i = new Intent(this,HomeActivity.class);
        startActivity(i);
        popToast("Chat Successfully purchased");
    }

    private void dealWithFailedPurchase() {
        Log.d("Chat purchased Result","Chat purchase failed");
        popToast("Failed to purchase Chat");
    }

}

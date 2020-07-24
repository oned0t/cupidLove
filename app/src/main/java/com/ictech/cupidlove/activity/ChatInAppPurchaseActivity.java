package com.ictech.cupidlove.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ictech.cupidlove.R;
import com.ictech.cupidlove.customview.textview.TextViewRegular;
import com.ictech.cupidlove.utils.BlundellActivity;
import com.ictech.cupidlove.utils.NavigatorChat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChatInAppPurchaseActivity extends BlundellActivity {

    @BindView(R.id.tvChatPurchase)
    TextViewRegular tvChatPurchase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_in_app_purchase);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.tvChatPurchase)
    public void purchaseClick(){
        navigate().toPurchaseChatActivityForResult();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (NavigatorChat.REQUEST_PASSPORT_PURCHASE == requestCode) {
            if (RESULT_OK == resultCode) {
                dealWithSuccessfulPurchase();
            } else {
                dealWithFailedPurchase();
            }
        }
    }

    private void dealWithSuccessfulPurchase() {
        Log.d("Chat purchased Result","Chat purchased");
        finish();
        popToast("Chat Successfully purchased");
    }

    private void dealWithFailedPurchase() {
        Log.d("Chat purchased Result","Chat purchase failed");
        popToast("Failed to purchase Chat");
    }

}

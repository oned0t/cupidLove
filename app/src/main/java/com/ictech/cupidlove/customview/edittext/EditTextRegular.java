package com.ictech.cupidlove.customview.edittext;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by Bhumi Shah on 11/9/2017.
 */


public class EditTextRegular extends androidx.appcompat.widget.AppCompatEditText {


    public EditTextRegular(Context context) {
        super(context);
        init();
    }

    public EditTextRegular(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextRegular(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/Lato-Regular.ttf");
        setTypeface(tf, 1);

    }

}

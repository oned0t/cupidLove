package com.ictech.cupidlove.customview.textview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class TextViewRegular extends TextView {


    public TextViewRegular(Context context) {
        super(context);
        init();
    }

    public TextViewRegular(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewRegular(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/Lato-Regular.ttf");
        setTypeface(tf, 1);

    }

}
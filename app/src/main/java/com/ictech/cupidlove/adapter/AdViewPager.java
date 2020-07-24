package com.ictech.cupidlove.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ictech.cupidlove.R;
import com.ictech.cupidlove.customview.textview.TextViewMedium;
import com.ictech.cupidlove.model.InAppPurchase;

import java.util.List;

public class AdViewPager extends PagerAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<InAppPurchase> list = InAppPurchase.getList();

    public AdViewPager(Context context) {
        this.context = context;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_ad, container, false);
        container.addView(view);
        TextViewMedium tvTitle = view.findViewById(R.id.tvTitle);
        TextViewMedium tvDescription = view.findViewById(R.id.tvDescription);
        tvTitle.setText(list.get(position).getName()+"");
        tvDescription.setText(list.get(position).getDescription()+"");
        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}

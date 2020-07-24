package com.ictech.cupidlove.activity;

import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ictech.cupidlove.R;
import com.ictech.cupidlove.adapter.RecentVisitedAdapter;
import com.ictech.cupidlove.interfaces.OnItemClickListner;
import com.ictech.cupidlove.utils.BaseActivity;
import com.ictech.cupidlove.utils.GridSpacingItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecentVisitedActivity extends BaseActivity implements OnItemClickListner {

    @BindView(R.id.rvRecentVisited)
    RecyclerView rvRecentVisited;

    private RecentVisitedAdapter recentVisitedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_visited);
        ButterKnife.bind(this);
        setAdapter();
    }

    public void setAdapter() {
        recentVisitedAdapter = new RecentVisitedAdapter(this, this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        rvRecentVisited.setLayoutManager(mLayoutManager);
        rvRecentVisited.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        rvRecentVisited.setItemAnimator(new DefaultItemAnimator());
        rvRecentVisited.setAdapter(recentVisitedAdapter);
        rvRecentVisited.setNestedScrollingEnabled(false);
    }

    @Override
    public void onItemClick(int position, String value, int outerpos) {

    }
}

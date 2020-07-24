package com.ictech.cupidlove.adapter;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ictech.cupidlove.R;
import com.ictech.cupidlove.interfaces.OnItemClickListner;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class RecentVisitedAdapter extends RecyclerView.Adapter<RecentVisitedAdapter.RecentViewHolder> {

    //TODO : VAriable Declaration
    private List<String> list = new ArrayList<>();
    private Activity activity;
    private OnItemClickListner onItemClickListner;
    ProgressBar loader;

    public RecentVisitedAdapter(Activity activity, OnItemClickListner onItemClickListner) {
        this.activity = activity;
        this.onItemClickListner = onItemClickListner;
    }

    public void addAll(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public RecentVisitedAdapter.RecentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recent_visit, parent, false);

        return new RecentVisitedAdapter.RecentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecentVisitedAdapter.RecentViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class RecentViewHolder extends RecyclerView.ViewHolder {

        //TODO : Bind All XML View With JAVA File
//        @BindView(R.id.llMain)
//        LinearLayout llMain;

        public RecentViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


}
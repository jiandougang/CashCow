package com.advertisement.cashcow.module.main;

import android.content.Context;
import android.view.ViewGroup;

import com.advertisement.cashcow.thirdLibs.rxretrofitlibrary.retrofit_rx.download.DownInfo;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * Created by WZG on 2016/10/21.
 */

public class DownAdapter extends RecyclerArrayAdapter<DownInfo> {

    public DownAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new DownHolder(parent);
    }

}
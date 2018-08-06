package com.advertisement.cashcow.thirdLibs.avplayer.play;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.provider.BaseDataProvider;

/**
 * Created by Taurus on 2018/4/15.
 */

public class MonitorDataProvider extends BaseDataProvider {

    private DataSource mDataSource;




    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    @Override
    public void handleSourceData(DataSource sourceData) {
        this.mDataSource = sourceData;

    }



    @Override
    public void cancel() {
    }

    @Override
    public void destroy() {
        cancel();
    }
}

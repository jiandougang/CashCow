package com.advertisement.cashcow.thirdLibs.avplayer.AudioService;

import android.content.Context;
import android.content.ContextWrapper;

/**
 * 作者：吴国洪 on 2018/6/13
 * 描述：Fixes a leak caused by AudioManager using an Activity context
 * 接口：
 */
public class AudioServiceActivityLeak extends ContextWrapper {

    AudioServiceActivityLeak(Context base) {
        super(base);
    }

    public static AudioServiceActivityLeak preventLeakOf(Context base) {
        return new AudioServiceActivityLeak(base);
    }

    @Override public Object getSystemService(String name) {
        if (Context.AUDIO_SERVICE.equals(name)) {
            return getApplicationContext().getSystemService(name);
        }
        return super.getSystemService(name);
    }
}

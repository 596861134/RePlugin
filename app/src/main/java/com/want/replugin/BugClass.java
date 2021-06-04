package com.want.replugin;

import android.content.Context;

import com.meituan.android.walle.WalleChannelReader;

public class BugClass {
    private static final String TAG = "BugClass";

    public String bug(Context applicationContext) {
//        String str = null;
        String channel = WalleChannelReader.getChannel(applicationContext);
//        return "This is a bug class:"+channel;
        return "This is a fix bug class:"+channel;
    }

}

package com.ethink.lineup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.ethink.lineup.server.LineUpService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BootReceiver extends BroadcastReceiver {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String TAG="BootReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        logger.info("AdmService start");
        Log.i(TAG, "onReceive: -----开始启动LineUpService");
        Intent startIntent = new Intent(context, LineUpService.class);
        context.startService(startIntent);
    }
}

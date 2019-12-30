package com.ethink.signature.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ethink.signature.server.SignatureService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BootReceiver extends BroadcastReceiver {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String TAG="BootReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        logger.info("SignatureService start");
        Log.i(TAG, "onReceive: -----开始启动SignatureService");
        Intent startIntent = new Intent(context, SignatureService.class);
        context.startService(startIntent);
    }
}

package com.ethink.vfd.plugin;

import android.content.Context;

import com.ethink.plugin.BasePlugin;
import com.ethink.plugin.FunctionHandler;
import com.ethink.plugin.message.PluginMessage;
import com.ethink.vfd.controller.PrintController;

/***
 *
 * 存折打印机
 * **/
public class PrintPlugin extends BasePlugin implements FunctionHandler {
    private Context context;
    private PrintController printController;

    public PrintPlugin(Context context) {
        super("PRINT");
        this.context = context;
        printController=new PrintController("/dev/ttyS2",9600);
    }

    @Override
    public void onStart() throws Throwable {
        registerFunction("PRINT", this);
    }

    @Override
    public void onStop() throws Throwable {

    }

    @Override
    public PluginMessage onFunction(PluginMessage pluginMessage) {
        logger.info("pluginName " + pluginMessage.getFunctionName());
        String functionName = pluginMessage.getFunctionName();
        switch (functionName) {
            case "PRINT":
                printController.initPrint();
                break;

        }
        return pluginMessage;
    }
}

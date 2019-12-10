package com.ethink.lineup.plugin;

import android.content.Context;

import com.ethink.lineup.controller.PrintController;
import com.ethink.plugin.BasePlugin;
import com.ethink.plugin.FunctionHandler;
import com.ethink.plugin.message.PluginMessage;


/**
 * 打印机插件
 */
public class PrintPlugin extends BasePlugin implements FunctionHandler {
private static final String TAG="PrintPlugin";
    private PrintController printController;
    public PrintPlugin(Context context) {
        super("PRINT_TICKET");
        printController=new PrintController(context);
        printController.printConnStatus();
    }
    @Override
    public PluginMessage onFunction(PluginMessage pluginMessage) {
        switch (pluginMessage.getFunctionName()){
            case "PRINT":
                String title=pluginMessage.getString("title");
                String num=pluginMessage.getString("number");
                int current=pluginMessage.getInt("current");
                if(printController!=null){
                    printController.print(title,num,String.format("您当前号码%s，前面还有%s人",num,current),current);
                }
                break;

        }
        return pluginMessage;
    }


    @Override
    public void onStart() throws Throwable {
        registerFunction("PRINT",this);

    }




    @Override
    public void onStop()     {

    }



}



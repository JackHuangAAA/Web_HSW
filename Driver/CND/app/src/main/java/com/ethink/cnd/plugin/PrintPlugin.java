package com.ethink.cnd.plugin;

import android.content.Context;

import com.ethink.cnd.controller.PrintController;
import com.ethink.plugin.BasePlugin;
import com.ethink.plugin.FunctionHandler;
import com.ethink.plugin.message.EventMessage;
import com.ethink.plugin.message.PluginMessage;

/***
 *
 * 存折打印机
 * **/
public class PrintPlugin extends BasePlugin implements FunctionHandler{
    private Context context;
    private PrintController printController;

    public PrintPlugin(Context context) {
        super("PRINT_BOOK");
        this.context = context;
        printController=new PrintController("/dev/ttyS0",19200);
        //解决开机第一次打印无效
        printController.exitPaper();
        printController.clearError();

    }

    @Override
    public void onStart() throws Throwable {
        registerFunction("PRINT_INIT", this);
        registerFunction("PRINT_PROVINCE_ZHEJIANG", this);
        registerFunction("PRINT_CITY_HANGZHOU", this);
        registerFunction("PRINT_EXIT_PAPER", this);
    }

    @Override
    public void onStop() throws Throwable {

    }

    @Override
    public PluginMessage onFunction(PluginMessage pluginMessage) {
        logger.info("pluginName " + pluginMessage.getFunctionName());
        String functionName = pluginMessage.getFunctionName();
        switch (functionName) {
            case "PRINT_INIT":
                printController.initPrint();
                break;
            case "PRINT_PROVINCE_ZHEJIANG":
                //浙江省疫苗本
                int num = pluginMessage.getInt("num");
                boolean need_name = pluginMessage.getBool("need_name");
                String vaccine = pluginMessage.getString("vaccine");
                String vaccine_date = pluginMessage.getString("vaccine_date");
                String lot_no = pluginMessage.getString("lot_no");
                String vaccine_unit = pluginMessage.getString("vaccine_unit");
                String vaccine_site = pluginMessage.getString("vaccine_site");
                String signature = pluginMessage.getString("signature");
                String data  = PrintController.provinceFormat(need_name,vaccine,vaccine_date,lot_no,vaccine_unit,vaccine_site,signature);
                printController.zheProvince(6, data, new PrintResult() {
                    @Override
                    public void printResult(Boolean res) {
                        logger.info("打印结果：{}",res);
                        EventMessage eventMessage = new EventMessage("PRINT_RESULT");
                        eventMessage.setBool("data",res);
                        pluginManager.post(eventMessage);
                    }
                });
                break;
            case "PRINT_CITY_HANGZHOU":
                int num1 = pluginMessage.getInt("num");
                String vaccine1 = pluginMessage.getString("vaccine");
                String vaccine_date1 = pluginMessage.getString("vaccine_date");
                String lot_no1 = pluginMessage.getString("lot_no");
                String vaccine_unit1 = pluginMessage.getString("vaccine_unit");
                String vaccine_site1 = pluginMessage.getString("vaccine_site");
                String signature1 = pluginMessage.getString("signature");
                String data1 = PrintController.cityFormat(vaccine1,vaccine_date1,lot_no1,vaccine_unit1,vaccine_site1,signature1);
                printController.hangCity(num1, data1);
                break;
            case "PRINT_EXIT_PAPER":
                printController.exitPaper();
                break;

        }
        return pluginMessage;
    }



    public  interface  PrintResult{
        void printResult(Boolean res);
    }


}

// 与硬件交互
import config from '@/config';
export default {
    subscribe(eventName, callback) {
      return $d.subscribe(eventName, callback);
    },
    getVersion() {
      return $d.invoke('UPDATER', 'GetCurrentVersion');
    },
    async getDeviceCode() {
        if (config.env == 'development'){
            return 'IST0001';
        } else {
            let rsp = await $d.invoke('SERVER', 'GetDeviceId');
            console.log('getDeviceCode===>' + JSON.stringify(rsp));
            return rsp.deviceId;
        }
    },
    //打印机操作
    async printBook(params){
        console.log("PRINT_BOOK===>PRINT_PROVINCE_ZHEJIANG");
        return await $d.invoke('PRINT_BOOK','PRINT_PROVINCE_ZHEJIANG',params);
    },
    //注销打印机事件
    async printExitPaper(){
        console.log("PRINT_BOOK===>PRINT_EXIT_PAPER");
        return await $d.invoke('PRINT_BOOK','PRINT_EXIT_PAPER');
    },
    //挂号打印
    async printTicket(params){
        console.log("PRINT_TICKET===>PRINT");
        return await $d.invoke('PRINT_TICKET','PRINT',params);
    }
};


// 与硬件交互

// export default {
//     subscribe(eventName, callback){
//         return $d.subscribe(eventName, callback)
//     },
    
    // checkDeviceModule(){
    //     return $d.invoke('DRIVER', 'CheckAll')
    // },
    // startHandleRecipe(params){
    //     return $d.invoke('DRIVER', 'ProcessRecipe', params)
    // }


// };

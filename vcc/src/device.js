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
            return { deviceId: 'DT0001' };
        } else {
            let deviceid = await $d.invoke('SERVER', 'GetDeviceId');
            console.log('getDeviceCode===>' + JSON.stringify(deviceid));
            return deviceid.res;
        }
    },
    //打开抽屉
    openDrawer(val){
        console.log('DEVICE===>OPEN_DRAWER');
        
    },
    //注册指纹
    fingerRegister(){
        console.log('FINGER===>REGISTER');
        return $d.invoke('FINGER', 'REGISTER');
    },
    //校验指纹
    fingerVerify(){
        console.log('FINGER===>VERIFY');
        return $d.invoke('FINGER', 'VERIFY');
    },
    //删除全部指纹
    fingerDelAll(){
        console.log('FINGER===>DEL_TEMPLATE_ALL');
        return $d.invoke('FINGER', 'DEL_TEMPLATE_ALL');
    },
    //查询指纹总数
    fingerQuerySum(){
        console.log('FINGER===>TEMPLATE_TOTAL');
        return $d.invoke('FINGER', 'TEMPLATE_TOTAL');
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

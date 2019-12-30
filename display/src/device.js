// 与硬件交互
import config from '@/config';
export default {
    subscribe(eventName, callback) {
      return $d.subscribe(eventName, callback);
    },
    // getVersion() {
    //   return $d.invoke('UPDATER', 'GetCurrentVersion');
    // },
    async getDeviceCode() {
        if (config.env == 'development'){
            return 'DT0001';
        } else {
            let rsp = await $d.invoke('SERVER', 'GetDeviceId');
            console.log('getDeviceCode===>' + JSON.stringify(rsp));
            return rsp.deviceId;
        }
    },
    //语音队列播报
    async audio(params){
        console.log("SPEECH===>LINE_UP")
        return await $d.invoke('SPEECH','LINE_UP',params)
    }
};

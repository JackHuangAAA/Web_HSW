// 与硬件交互
import config from '@/config';
export default {
  async finger(functionName, params) {
    if (typeof params !== 'undefined') {
      return await $d.invoke('FINGER', functionName, params);
    } else {
      return await $d.invoke('FINGER', functionName);
    }
  },
  subscribe(eventName, callback) {
    return $d.subscribe(eventName, callback);
  },
  getVersion() {
    return $d.invoke('UPDATER', 'GetCurrentVersion');
  },
  async getDeviceCode() {
    console.log('GetDeviceId');
    if (config.env == 'development') return { deviceId: 'DT0001' };
    else {
      let deviceid = await $d.invoke('SERVER', 'GetDeviceId');
      console.log(JSON.stringify(deviceid));
      return deviceid.res;
    }
  }
};

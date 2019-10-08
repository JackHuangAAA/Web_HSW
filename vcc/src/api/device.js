// 与硬件交互
import config from '@/config'
export default {
  finger(functionName, params) {
    if (typeof params !== 'undefined') {
      return $d.invoke('FINGER', functionName, params)
    } else {
      return $d.invoke('FINGER', functionName)
    }
  },
  subscribe(eventName, callback) {
    return $d.subscribe(eventName, callback)
  },
  getVersion() {
    return $d.invoke('UPDATER', 'GetCurrentVersion')
  },
  async getDeviceId() {
    console.log('GetDeviceId')
    if (config.env == 'development') return 'DT0001'
    else return $d.invoke('DRIVER', 'GetDeviceId')
  }
}

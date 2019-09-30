// 与硬件交互
require('./bridge.js')
export default {
  andriod(eventName, params) {
    return $d.invoke('FINGER', eventName, params)
  },
  subscribe(eventName, callback) {
    return $d.subscribe(eventName, callback)
  }
  //     checkDeviceModule(){
  //         return $d.invoke('DRIVER', 'CheckAll')
  //     },
  //     getVersion(){
  //          return $d.invoke('UPDATER', 'GetCurrentVersion')
  //     },
  //     getDeviceId(){
  //          return $d.invoke('DRIVER', 'GetDeviceId')
  //     },
  //     startHandleRecipe(params){
  //         return $d.invoke('DRIVER', 'ProcessRecipe', params)
  //     },
  //     takeJarOut(params){
  //         return $d.invoke('DRIVER', 'GetJarOut', params)
  //     },
  //     checkJarIn(params){
  //         return $d.invoke('DRIVER', 'CheckJarIn', params)
  //     },
  //      // 测试DRIVER_GET_JAR_OUT
  //     triggerJarOutEvent(params){
  //          return $d.invoke('DRIVER', 'TriggerJarOutEvent', params)
  //     },
  //     // 测试DRIVER_JAR_IN
  //     triggerJarInEvent(params){
  //          return $d.invoke('DRIVER', 'TriggerJarInEvent', params)
  //     },
  //     // 测试DRIVER_SPLIT_PERCENT
  //     triggerSplitPercentEvent(params){
  //          return $d.invoke('DRIVER', 'TriggerSplitPercentEvent', params)
  //     },
  //     // 测试   DRIVER_SPLIT_ONCE
  //     triggerSplitOnceEvent(params){
  //          return $d.invoke('DRIVER', 'TriggerSplitOneEvent', params)
  //     },
  //     // 测试  DRIVER_WARING
  //     triggerWaringEvent(params){
  //          return $d.invoke('DRIVER', 'TriggerWaringEvent', params)
  //     },
  //     // 测试  DRIVER_STATUS_CHANGE
  //     triggerStatusChangeEvent(params){
  //          return $d.invoke('DRIVER', 'TriggerStatusChangeEvent', params)
  //     },
  //       // 测试  DRIVER_EXCHANGE_FIND_JAR
  //  triggerExchangeFindEvent(params){
  //       return $d.invoke('DRIVER', 'TriggerExchangeFindEvent', params)
  //  }
}

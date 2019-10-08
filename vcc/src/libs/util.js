/**
 *  dateFmt 用于格式化时间
 *  dateFtt("yyyy-MM-dd hh:mm:ss",Time)
 * @param {String} fmt
 * @param {Date} date
 * @returns {String}
 */
export const dateFmt = (fmt, date) => {
  let o = {
    'M+': date.getMonth() + 1, //月份
    'd+': date.getDate(), //日
    'h+': date.getHours(), //小时
    'm+': date.getMinutes(), //分
    's+': date.getSeconds(), //秒
    'q+': Math.floor((date.getMonth() + 3) / 3), //季度
    S: date.getMilliseconds() //毫秒
  }
  if (/(y+)/.test(fmt))
    fmt = fmt.replace(
      RegExp.$1,
      (date.getFullYear() + '').substr(4 - RegExp.$1.length)
    )
  for (var k in o)
    if (new RegExp('(' + k + ')').test(fmt))
      fmt = fmt.replace(
        RegExp.$1,
        RegExp.$1.length == 1 ? o[k] : ('00' + o[k]).substr(('' + o[k]).length)
      )
  return fmt
}
export const dbDateFmt = datetime => {
  let date = datetime.substr(0, 10)
  let time = datetime.substr(11, 8)
  return `${date} ${time}`
}

export const SetCache = (key, value) => {
  sessionStorage.setItem(key, value)
  return value
}
export const GetCache = key => {
  let value = sessionStorage.getItem(key)
  return value
}
export const CleanCache = key => {
  sessionStorage.removeItem(key)
  return true
}
export const SaveStorage = (key, value) => {
  localStorage.setItem(key, value)
}
export const GetStorage = key => {
  let value = localStorage.getItem(key)
  return value
}
export const CleanStorage = key => {
  localStorage.removeItem(key)
  return true
}
export const Storages = {
  SetCache,
  GetCache,
  CleanCache,
  SaveStorage,
  GetStorage,
  CleanStorage
}

const alarmType = {
  1: {
    info: '温度异常'
  },
  2: {
    info: '库存不足'
  }
}
export const discernAlarmType = type => {}

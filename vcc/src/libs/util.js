import api from '@/api';
import store from '@/store';
import device from '@/api/device.js';
import { resolve } from 'path';
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
  };
  if (/(y+)/.test(fmt))
    fmt = fmt.replace(
      RegExp.$1,
      (date.getFullYear() + '').substr(4 - RegExp.$1.length)
    );
  for (var k in o)
    if (new RegExp('(' + k + ')').test(fmt))
      fmt = fmt.replace(
        RegExp.$1,
        RegExp.$1.length == 1 ? o[k] : ('00' + o[k]).substr(('' + o[k]).length)
      );
  return fmt;
};
export const dbDateFmt = datetime => {
  let date = datetime.substr(0, 10);
  let time = datetime.substr(11, 8);
  return `${date} ${time}`;
};

export const SetCache = (key, value) => {
  if (typeof value ==='object') value = JSON.stringify(value)
  sessionStorage.setItem(key, value);
  return value;
};
export const GetCache = key => {
  let value = sessionStorage.getItem(key);
  if (value) return JSON.parse(value);
  else return false;
};
export const CleanCache = key => {
  sessionStorage.removeItem(key);
  return true;
};
export const SaveStorage = (key, value) => {
  console.log('Save----->LS', key, value);
  if (typeof value ==='object') value = JSON.stringify(value)
  localStorage.setItem(key, value);
  return value;
};
export const GetStorage = key => {
  const value = localStorage.getItem(key);
  console.log('Get----->LS', key, value);
  if (value) return value;
  else return false;
};
export const CleanStorage = key => {
  localStorage.removeItem(key);
  return true;
};
export const Storages = {
  SetCache,
  GetCache,
  CleanCache,
  SaveStorage,
  GetStorage,
  CleanStorage
};
export const getcode = async () => {
  const deviceCode = GetStorage('deviceCode');
  api.setHeaders('deviceid', deviceCode);
  if (deviceCode) return deviceCode;
  else {
    return await GetDeviceCode();
  }
};

const GetDeviceCode = () => {
  device.getDeviceCode().then(res => {
    let deviceCode = res.deviceId;
    api.setHeaders('deviceid', deviceCode);
    SaveStorage('deviceCode', deviceCode);
    return deviceCode;
  });
};

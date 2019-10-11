import api from '@/api';
import device from '@/api/device.js';

export const dbDateFmt = datetime => {
  let date = datetime.substr(0, 10);
  let time = datetime.substr(11, 8);
  return `${date} ${time}`;
};

export const SetCache = (key, value) => {
  if (typeof value === 'object') value = JSON.stringify(value);
  sessionStorage.setItem(key, value);
  return value;
};
export const GetCache = key => {
  let value = sessionStorage.getItem(key);
  if (value) return value;
  else return false;
};
export const CleanCache = key => {
  if (sessionStorage.getItem(key) != null) {
    sessionStorage.removeItem(key);
    return true;
  } else return false;
};
export const SaveStorage = (key, value) => {
  console.log('Save----->LS', key, value);
  if (typeof value === 'object') value = JSON.stringify(value);
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
  if (localStorage.getItem(key) != null) {
    localStorage.removeItem(key);
    return true;
  } else return false;
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

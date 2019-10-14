import axios from '@/api';
export const queryDeviceByCondition= async deviceId => {
  return await axios.get(`/device/queryDeviceByCondition`, { code: deviceId });
};

export const queryDrawerByCondition = async device => {
  return await axios.get(`/drawer/queryDrawerByCondition`, { device: device });
};

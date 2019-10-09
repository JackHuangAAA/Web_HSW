import axios from '@/api'
export const queryDeviceByCondition = async deviceId => {
  return await axios.get(`/device/queryDeviceByCondition`, deviceId)
}

export const queryVaccineKinds = async () => {
  return await axios.get(`/zcy/queryVaccineKinds`)
}

export const queryDrawerByCondition = async () => {
  return await axios.get(`/drawer/queryDrawerByCondition`)
}

/**
 * 静态变量
 */
'use strict';
export const alarminfo = {
  1: {
    title: '温度异常',
    part: '接种柜温度异常',
    solution: '接种柜温度过高,请及将温度调整至2-8℃范围内!'
  },
  2: {
    title: '库存不足',
    part: '库存疫苗过少',
    solution: '库存不足，请及时补充疫苗!'
  }
};
export default {
  alarminfo: alarminfo
};

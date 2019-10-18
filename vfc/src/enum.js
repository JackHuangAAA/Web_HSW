/**
 * 静态变量
 */
'use strict';


export default {
	
	//设备状态
    DEVICE_STATUS:{
        DISABLED: 0, //未启用
        FREE: 1,     //在线（空闲状态）
        BUSY: 2,     //忙（使用中）
        HWERROR: 3,  //硬件故障
        BROKENNET: 5 //断网
    }

};

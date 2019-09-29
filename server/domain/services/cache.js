/**
 * Created by Administrator on 2018/9/25 0025.
 */

const logger = Libs.logger.getLogger('cache');
const moment = require('moment');

module.exports = {
    /** 
     *缓存设备状态
     * @param devId
     * @returns {Promise.<*>}
     */
    getDeviceStatus: async function (devId) {
        if(await Domain.redis.client.existsAsync(devId) == 1 ){
            return await Domain.redis.client.getAsync(devId);
        }else{
            return Domain.redis.client.setAsync(devId, devId);//保存成功，返回OK
        }
    },

    /**
     * 缓存string
     * @param key
     * @param value
     * @returns {Promise.<*>}
     */
    setCacheToString: async function (key, value) {
        logger.info(`setCacheToString:{ key:${key},value:${value}}`);
        return await Domain.redis.client.setAsync(key, value);//set成功，返回ok
    },

    /**
     * 从缓存获取string
     * @param key
     * @returns {Promise.<*>}
     */
    getCacheToString: async function (key) {
        return await Domain.redis.client.getAsync(key);//add成功，返回1
    },

    /**
     * list缓存信息
     * @param key
     * @param value
     * @returns {Promise.<*>}
     */
    setCacheToList: async function (key, value) {
        return await Domain.redis.client.lpushAsync(key, value);//add成功，返回1
    },

    /**
     * 从list缓存获取信息
     * @param key
     * @returns {Promise.<*>}
     */
    getCacheFromList: async function (key) {
        let members = await Domain.redis.client.rpopAsync(key);
        return members;
    },

    /**
     * 移除list缓存中信息
     * @param keys
     * @returns {*}
     */
    delCacheFromList: function (keys, position, value) {
        return Domain.redis.client.lremAsync(keys, position, value);
    },

    /**
     * set缓存信息
     * @param key
     * @param value
     * @returns {Promise.<*>}
     */
    setCacheToSet: async function (key, value) {
        return Domain.redis.client.saddAsync(key, value);//add成功，返回1
    },

    /**
     * 从set缓存获取信息
     * @param key
     * @returns {Promise.<*>}
     */
    getCacheFromSet: async function (key) {
        let members = await Domain.redis.client.smembersAsync(key);
        return members;
    },

    /**
     * 移除set中缓存
     * @param key
     * @param value
     * @returns {*}
     */
    delCacheFromSet: async function (key, value) {
        return await Domain.redis.client.sremAsync(key, value);
    },

    /**
     *修改/设置参数
     * @param devId
     * @returns {Promise.<*>}
     */
    updataParam: async function (ckey, value) {
        return Domain.redis.client.setAsync(ckey, value);//保存成功，返回OK
    },

    /**
     *查看参数
     * @param devId
     * @returns {Promise.<*>}
     */
    getParam: async function (ckey) {
        return Domain.redis.client.getAsync(ckey);//保存成功，返回OK
    },

    /**
     *初始化所有系统参数到缓存
     * @param devId
     * @returns {Promise.<*>}
     */
    initParam: async function () {
        let params = await Domain.services.param.getAllParams();
        _.forEach(params, (param) => {
            Domain.redis.client.setAsync(param.ckey, JSON.stringify(param));
        })
        logger.info('config parament cache to redis success!');
    },

    /**
     *初始化所有中药到缓存
     * @param devId
     * @returns {Promise.<*>}
     */
    initMedDic: async function () {
        let granules = await Domain.services.granule.queryAllGranules();
        return Domain.redis.client.setAsync(Domain.enum.REDIS_KEY.equivalent.medDic, JSON.stringify(granules));
    },

    /**
     *初始化所有颗粒当量到缓存
     * @param devId
     * @returns {Promise.<*>}
     */
    initEquivalent: async function () {
        let equivalents = await Domain.services.equivalent.getEquivalents();
        return Domain.redis.client.setAsync(Domain.enum.REDIS_KEY.equivalent, JSON.stringify(equivalents));
    }

}
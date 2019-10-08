/**
 * Created by Administrator on 2019/9/27 0027.
 */
const moment = require('moment')
const logger = Libs.logger.getLogger('device')

module.exports = {
  /**
   * 查询设备信息
   * @param requestBody
   * @returns {Promise.<{rs: *, total: (*|number)}>}
   */
  queryDevices: async function(requestBody) {
    logger.debug(`queryDevices param: ${JSON.stringify(requestBody)}`)
    let query = []
    if (!_.isEmpty(requestBody.alias)) {
      query.push({ alias: requestBody.alias })
    }
    if (!_.isEmpty(requestBody.code)) {
      query.push({ code: requestBody.code })
    }
    if (!_.isEmpty(requestBody.type)) {
      query.push({ type: requestBody.type })
    }
    if (!_.isEmpty(requestBody.unitCode)) {
      query.push({ unitCode: requestBody.unitCode })
    }
    if (!_.isEmpty(requestBody.cabinetNo)) {
      query.push({ cabinetNo: requestBody.cabinetNo })
    }
    query =
      query.length == 2 ? { $and: query } : query.length == 1 ? query[0] : {}
    let result = await Domain.models.device.paginate(query, {
      sort: { _id: -1 },
      page: requestBody.page,
      limit: parseInt(requestBody.size)
    })
    return { rs: result.docs, total: result.total }
  },

  /**
   * 保存设备信息
   * @param requestBody
   * @returns {Promise.<requestBody>}
   */
  saveDevice: async function(requestBody) {
    logger.debug(`saveDevice param: ${JSON.stringify(requestBody)}`)
    return Domain.models.device.create(requestBody)
  },

  /**
   * 按条件查询设备信息
   * @param requestBody
   * @returns {Promise.<T|Query|*>}
   */
  queryDeviceByCondition: async function(requestBody) {
    logger.debug(`queryDeviceByCondition param: ${JSON.stringify(requestBody)}`)
    let query = []
    if (!_.isEmpty(requestBody.alias)) {
      query.push({ alias: requestBody.alias })
    }
    if (!_.isEmpty(requestBody.code)) {
      query.push({ code: requestBody.code })
    }
    if (!_.isEmpty(requestBody.cabinetNo)) {
      query.push({ cabinetNo: requestBody.cabinetNo })
    }
    query =
      query.length == 2 ? { $and: query } : query.length == 1 ? query[0] : {}
    return await Domain.models.device.find(query)
  }
}

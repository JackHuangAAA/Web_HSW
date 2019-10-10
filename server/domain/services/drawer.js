/**
 * Created by Administrator on 2019/9/24.
 */
const logger = Libs.logger.getLogger('drawer')

module.exports = {
  /**
   *  查询抽屉里疫苗为空的数据
   *
   * @param {any} requestBody
   * @returns
   */
  queryDrawerEmpty: async function(requestBody) {
    logger.debug(`queryDrawerEmpty param: ${JSON.stringify(requestBody)}`)
    let query = []
    if (!_.isEmpty(requestBody.device)) {
      query.push({ device: requestBody.device })
    }
    if (!_.isEmpty(requestBody.unitCode)) {
      query.push({ unitCode: requestBody.unitCode })
    }
    query =
      query.length == 2 ? { $and: query } : query.length == 1 ? query[0] : {}
    let result = await Domain.models.drawer.find(query).populate('vaccine')
    result = result.filter(item => {
      return item.vaccine == null
    })
    return { rs: result, total: result.length }
  },

  /**
   * 增加抽屉信息
   * @param requestBody
   * @returns {Promise.<requestBody>}
   */
  saveDrawer: async function(requestBody) {
    logger.debug(`saveDrawer param: ${JSON.stringify(requestBody)}`)
    return Domain.models.drawer.insertMany(requestBody)
  },

  /**
   *   根据条件查询抽屉信息，并按坐标排序
   *
   * @param {any} requestBody
   * @returns
   */
  queryDrawerByCondition: async function(requestBody) {
    logger.debug(`queryDrawerByCondition param: ${JSON.stringify(requestBody)}`)
    let query = []
    if (!_.isEmpty(requestBody.device)) {
      query.push({ device: requestBody.device })
    }
    if (!_.isEmpty(requestBody.id)) {
      query.push({ device: requestBody.id })
    }
    if (!_.isEmpty(requestBody.unitCode)) {
      query.push({ unitCode: requestBody.unitCode })
    }
    query =
      query.length == 2 ? { $and: query } : query.length == 1 ? query[0] : {}
    let result = await Domain.models.drawer
      .find(query)
      .sort({ x: 1, y: 1 })
      .populate('vaccine')

    return { rs: result, total: result.length }
  },

  /**
   *
   * 根据抽屉id更新抽屉信息 增加
   * @param {any} requestBody
   * @returns
   */
  modifyDrawerById: async function(requestBody) {
    logger.debug(`modifyDrawerById param: ${JSON.stringify(requestBody)}`)
    let drawerData = await Domain.models.drawer.findOne({ _id: requestBody.id })
    let vaccineData = await Domain.models.vaccine.create(requestBody.vaccine)

    logger.debug(`drawerData param: ${JSON.stringify(drawerData)}`)
    logger.debug(`vaccineData param: ${JSON.stringify(vaccineData)}`)
    let vaccineArr = drawerData.vaccine || []
    let vaccineId = vaccineData._id
    vaccineArr.push(vaccineId)
    await Domain.models.drawer.update(
      { _id: requestBody.id },
      {
        $set: { vaccine: vaccineArr }
      }
    )
    return { vaccineData: vaccineData }
  },
  /**
   *
   * 根据抽屉id更新抽屉信息 减少
   * @param {any} requestBody
   * @returns
   */
  modifyDrawerByIdDec: async function(requestBody) {
    logger.debug(`modifyDrawerByIdDec param: ${JSON.stringify(requestBody)}`)
    let drawerData = await Domain.models.drawer.findOne({ _id: requestBody.id })
    let vaccineData = await Domain.models.vaccine.findOneAndRemove({
      _id: requestBody.vaccineId
    })
    let vaccineArr = drawerData.vaccine
    let vaccineId = vaccineData._id
    vaccineArr = vaccineArr.filter(item => {
      return item != vaccineId
    })
    return Domain.models.drawer.update(
      { _id: requestBody.id },
      {
        $set: { vaccine: vaccineArr }
      }
    )
  }
}

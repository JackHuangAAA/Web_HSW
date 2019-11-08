const logger = Libs.logger.getLogger('menu');


module.exports={
    /**
   * 查询用户权限分配菜单
   * @param requestBody
   * @returns {Promise.<*>}
   */
  queryMenus: async function () {
    // logger.debug(`queryMenus param: ${JSON.stringify(requestBody)}`);
    return await Domain.models.menu.find();
  }
}
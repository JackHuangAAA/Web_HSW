/**
 * Created by Administrator on 2019/9/24.
 */
const logger = Libs.logger.getLogger('drawer');

module.exports = {

    /**
     *  查询抽屉里疫苗为空的数据 
     * 
     * @param {any} requestBody 
     * @returns 
     */
    queryDrawerEmpty: async function (requestBody) {
        logger.debug(`queryDrawerEmpty param: ${JSON.stringify(requestBody)}`);
        let query = [{ "vaccine": [] }];
        if (!_.isEmpty(requestBody.device)) {
            query.push({ "device": requestBody.device });
        }
        if (!_.isEmpty(requestBody.unitCode)) {
            query.push({ "unitCode": requestBody.unitCode });
        }
        if (!_.isEmpty(requestBody.unitName)) {
            query.push({"unitName":  new RegExp(requestBody.unitName)});
        }
        query = query.length == 2 ? { "$and": query } : query.length == 1 ? query[0] : {};
        let result = await Domain.models.drawer.find(query).populate("vaccine");
        return { rs: result, total: result.length };
    },

    /**
     * 增加抽屉信息
     * @param requestBody
     * @returns {Promise.<requestBody>}
     */
    saveDrawer: async function (requestBody) {
        logger.debug(`saveDrawer param: ${JSON.stringify(requestBody)}`);
        return Domain.models.drawer.insertMany(requestBody);
    },

    /**
     *   根据条件查询抽屉信息，并按坐标排序
     * 
     * @param {any} requestBody 
     * @returns 
     */
    queryDrawerByCondition: async function (requestBody) {
        logger.debug(`queryDrawerByCondition param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.id)) {
            query.push({ "_id": requestBody.id });
        }
        if (!_.isEmpty(requestBody['ids[]'])) {
            query.push({ "_id": { $in: requestBody['ids[]'] } });
        }
        if (!_.isEmpty(requestBody.device)) {
            query.push({ "device": requestBody.device });
        }
        if (!_.isEmpty(requestBody.unitCode)) {
            query.push({ "unitCode": requestBody.unitCode });
        }
        if (!_.isEmpty(requestBody.unitName)) {
            query.push({"unitName":  new RegExp(requestBody.unitName)});
        }
        if (!_.isEmpty(requestBody.vaccineCode)) {
            query.push({ "vaccine": requestBody.vaccineCode });
        }
        query = query.length == 2 ? { "$and": query } : query.length == 1 ? query[0] : {};
        let result = await Domain.models.drawer.find(query).sort({ "y": 1, "x": 1 }).populate("vaccine");
        return result;
    },

    /**
  * 
  * 根据抽屉id更新抽屉信息 增加
  * @param {any} requestBody 
  * @returns 
  */
    modifyDrawerById: async function (requestBody) {
        logger.debug(`modifyDrawerById param: ${JSON.stringify(requestBody)}`);
        let vaccineData = await Domain.models.vaccine.create(requestBody.vaccine);
        await Domain.models.drawer.update(
            { _id: requestBody.id },
            {
                $push: { vaccine: vaccineData._id }
            }
        );
        return { vaccineData: vaccineData };
    },
    /**
  * 
  * 根据抽屉id更新抽屉信息 减少
  * @param {any} requestBody 
  * @returns 
  */
    modifyDrawerByIdDec: async function (requestBody) {
        logger.debug(`modifyDrawerByIdDec param: ${JSON.stringify(requestBody)}`);
        let vaccineData = await Domain.models.vaccine.findOneAndRemove({ _id: requestBody.vaccineId });
        return Domain.models.drawer.update(
            { _id: requestBody.id },
            {
                $pull: { vaccine: vaccineData._id }
            },
        );
    },

    /**
      * 
      *按疫苗 分组合计抽屉信息
      * @param {any} requestBody 
      * @returns 
      */
    queryDrawerByVaccineArr: async function (requestBody) {
        logger.debug(`queryDrawerByVaccineArr param: ${JSON.stringify(requestBody)}`);
        return await Domain.models.drawer.aggregate([
            {$match: {device: requestBody.device}},
            { "$unwind": "$vaccine" },
            {
                $lookup:
                {
                    from: "vaccines",
                    localField: "vaccine",
                    foreignField: "_id",
                    as: "inventory_docs"
                }
            },
                {
                    $replaceRoot: { newRoot: { $mergeObjects: [ { $arrayElemAt: [ "$inventory_docs", 0 ] }, "$$ROOT" ] } }
               },
               { $project: { inventory_docs: 0 } }
        ])
    },
};

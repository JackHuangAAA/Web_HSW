/**
 * Created by Administrator on 2019/9/27 0027.
 */

const moment = require('moment');
const logger = Libs.logger.getLogger('inout');

module.exports = {

    /**
     * 查询出入库信息(以流水号分组展示)
     * @param requestBody
     * @returns {Promise.<{rs: *, total: (*|number)}>}
     */
    queryInoutsBybatchId: async function(requestBody){
        logger.debug(`queryInouts param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.deviceId)) {
            query.push({"device": requestBody.deviceId});
        }
        if (!_.isEmpty(requestBody.type)) {
            query.push({"type": requestBody.type});
        }
        if (!_.isEmpty(requestBody.code)) {
            query.push({"code": requestBody.code});
        }
        if (!_.isEmpty(requestBody.name)) {
            query.push({"name": new RegExp(requestBody.name)});
        }
        if (!_.isEmpty(requestBody.unitName)) {
            query.push({"unitName":  new RegExp(requestBody.unitName)});
        }
        if (!_.isEmpty(requestBody.deviceType)) {
            query.push({"deviceType": requestBody.deviceType});
        }
        if (!_.isEmpty(requestBody.batchId)) {
            query.push({"batchId": requestBody.batchId});
        }
        if (!_.isEmpty(requestBody.begin)) {
            let begin = moment(requestBody.begin);
            begin = begin.startOf('day').toDate();
            query.push({"createDate": {"$gte": begin}});
        }
        if (!_.isEmpty(requestBody.end)) {
            let end = moment(requestBody.end);
            end = end.endOf('day').toDate();
            query.push({"createDate": {"$lte": end}});
        }

        query = query.length>1?{"$and": query} : query.length==1 ? query[0] : {};
        let total,docs;
        docs = await Domain.models.inout.aggregate([{$match:query},{$group:{
                _id:{
                    "batchId":"$batchId"
                },
                count:{$sum:"$total"},
            }}]);
        total=docs.length;

        docs = await Domain.models.inout.aggregate([{$match:query},
            {$group:{
                _id: "$batchId",
                "type":{"$first":"$type"},
                "deviceType":{"$first":"deviceType"},
                "unitName":{"$first":"unitName"},
                count:{$sum:"$total"}
            }},
            {$skip:(requestBody.page-1)*requestBody.size},
            {$limit:parseInt(requestBody.size)}
        ]);

        return {rs: docs, total: total};
    },

    /**
     * 保存出入库信息
     * @param requestBody
     * @returns {Promise.<requestBody>}
     */
    saveInout: async function(requestBody){
        logger.debug(`saveInout param: ${JSON.stringify(requestBody)}`);
        return Domain.models.inout.create(requestBody);
    },

    /**
     * 批量保存出入库信息
     * @param requestBody
     * @returns {Promise.<requestBody>}
     */
    insertManyInout: async function(requestBody){
        logger.debug(`insertManyInout param: ${JSON.stringify(requestBody)}`);
        return Domain.models.inout.insertMany(requestBody);
    },

    /**
     * 查询各单位冷藏柜、接种柜的历史出入库信息
     * @param requestBody
     * @returns {Promise.<{rs: *, total: (*|number)}>}
     */
    queryInoutsByUnitCode: async function(requestBody){
        logger.debug(`queryInouts param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.deviceType)) {
            query.push({"deviceType": requestBody.deviceType});
        }
        if (!_.isEmpty(requestBody.type)) {
            query.push({"type": requestBody.type});
        }
        if (!_.isEmpty(requestBody.unitCode)) {
            query.push({"unitCode": requestBody.unitCode});
        }
        if (!_.isEmpty(requestBody.unitName)) {
            query.push({"unitName":  new RegExp(requestBody.unitName)});
        }
        if (!_.isEmpty(requestBody.begin)) {
            let begin = moment(requestBody.begin);
            begin = begin.startOf('day').toDate();
            query.push({"createDate": {"$gte": begin}});
        }
        if (!_.isEmpty(requestBody.end)) {
            let end = moment(requestBody.end);
            end = end.endOf('day').toDate();
            query.push({"createDate": {"$lte": end}});
        }
        query = query.length>1?{"$and": query} : query.length==1 ? query[0] : {};
        return await Domain.models.inout.find(query);
    },

    /**
     * 查询出入库信息(详细信息)
     * @param requestBody
     * @returns {Promise.<{rs: *, total: (*|number)}>}
     */
    queryInouts: async function(requestBody){
        logger.debug(`queryInouts param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.deviceId)) {
            query.push({"device": requestBody.deviceId});
        }
        if (!_.isEmpty(requestBody.type)) {
            query.push({"type": requestBody.type});
        }
        if (!_.isEmpty(requestBody.code)) {
            query.push({"code": requestBody.code});
        }
        if (!_.isEmpty(requestBody.name)) {
            query.push({"name": new RegExp(requestBody.name)});
        }
        if (!_.isEmpty(requestBody.unitName)) {
            query.push({"unitName":  new RegExp(requestBody.unitName)});
        }
        if (!_.isEmpty(requestBody.deviceType)) {
            query.push({"deviceType": requestBody.deviceType});
        }
        if (!_.isEmpty(requestBody.batchId)) {
            query.push({"batchId": requestBody.batchId});
        }
        if (!_.isEmpty(requestBody.begin)) {
            let begin = moment(requestBody.begin);
            begin = begin.startOf('day').toDate();
            query.push({"createDate": {"$gte": begin}});
        }
        if (!_.isEmpty(requestBody.end)) {
            let end = moment(requestBody.end);
            end = end.endOf('day').toDate();
            query.push({"createDate": {"$lte": end}});
        }

        query = query.length>1?{"$and": query} : query.length==1 ? query[0] : {};
        let result = await Domain.models.inout.paginate(query, {
            sort: {"_id": -1},
            populate:[{path:'user',select:'name'}],
            page: requestBody.page,
            limit: parseInt(requestBody.size),
            lean:true
        });
        return {rs: result.docs, total: result.total};
    },

};
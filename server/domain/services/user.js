/**
 * Created by Administrator on 2019/9/24.
 */
const FlakeId = require('flake-idgen');
const generator = new FlakeId({worker: process.env.NODE_APP_INSTANCE || 0});
const moment = require('moment');
const logger = Libs.logger.getLogger('user');
const crypto = require('crypto');

module.exports = {

    /**
     * 检查token
     * @param token
     * @returns {Promise.<{_id, code, name, password, role: *, phone: (*|string|Array)}>}
     */
    checkToken: async function (token) {
        logger.debug(`checkToken param: ${token}`);
        let op = await Domain.models.user.findOne({token: token},null,{lean:true});
        if (op != null) {
            await this.refreshLastLogin(op.code);
            return {
                    _id: op._id,
                    code: op.code,
                    name: op.name,
                    password: op.password,
                    role: op.role,
                    phone: op.phone
            };
        } else {
            throw Libs.error('0001','无效token');
        }
    },

    /**
     * 登录
     * @param code
     * @param password
     * @returns {Promise.<*>}
     */
    login: async function (code, password) {
        logger.debug(`login param code: ${code}, password: ${password}`);
        let op = await Domain.models.user.findOne({code: code});
        logger.debug(op, code)
        if(op != null) {
            if (op.password == password) {
                let token = null;
                if (moment().diff(moment(op.lastLogin), 'minutes') <= 30 && !_.isEmpty(op.token)) {
                    token = op.token;
                } else {
                    token = generator.next().toString('hex').toUpperCase();
                }
                await this.updateUserToken(code, token);
                return token;

            } else {
                throw Libs.error('0003','密码错误');     //密码错误
            }
        }else{
            throw Libs.error('0002',`用户${code}不存在`);//用户${code}不存在
        }
    },

    /**
     * 更新用户最近一次token
     * @param code
     * @param token
     * @returns {*}
     */
    updateUserToken: function (code, token) {
        logger.debug(`updateUserToken param code: ${code}, token: ${token}`);
        return Domain.models.user.update({code: code}, {$set: {token: token, lastLogin: new Date()}});
    },

    /**
     * 更新用户最近一次时间
     * @param code
     * @returns {*}
     */
    refreshLastLogin: function (code) {
        logger.debug(`refreshLastLogin param code: ${code}`);
        return Domain.models.user.update({code: code}, {$set: {lastLogin: new Date()}});
    },

    /**
     * 查询用户信息
     * @param requestBody
     * @returns {Promise.<{rs: *, total: (*|number)}>}
     */
    queryUsers: async function(requestBody){
        logger.debug(`queryUsers param: ${json.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.code)) {
            query.push({"code": new RegExp(requestBody.code)});
        }
        if (!_.isEmpty(requestBody.name)) {
            query.push({"name": new RegExp(requestBody.name)});
        }
        query = query.length==2?{"$and": query} : query.length==1 ? query[0] : {};
        let result = await Domain.models.user.paginate(query, {
            sort: {"_id": -1},
            page: requestBody.page,
            limit: parseInt(requestBody.size),
            lean:true
        });
        return {rs: result.docs, total: result.total};
    },

    /**
     * 保存用户信息
     * @param requestBody
     * @returns {Promise.<requestBody>}
     */
    saveUser: async function(requestBody){
        logger.debug(`saveUser param: ${json.stringify(requestBody)}`);
        requestBody.password = _.toUpper(crypto.createHash('md5').update("000000").digest('hex'));
        let role = await this.getRoleById(requestBody.roles);
        requestBody.role = role;
        return Domain.models.user.create(requestBody);
    },

    /**
     * 修改用户信息
     * @param requestBody
     * @returns {Promise.<*>}
     */
    modifyUser: async function(requestBody){
        logger.debug(`modifyUser param: ${json.stringify(requestBody)}`);
        let role = await this.getRoleById(requestBody.roles);
        return Domain.models.user.updateOne({_id: requestBody.id}, {
            $set: {
                name: requestBody.name,
                phone: requestBody.phone,
                remark: requestBody.remark,
                role: role
            }
        });
    },

    /**
     * 删除用户信息
     * @param requestBody
     * @returns {*}
     */
    removeUserById: function(requestBody){
        logger.debug(`removeUserById param: ${json.stringify(requestBody)}`);
        return Domain.models.user.findOneAndRemove({_id: requestBody.id});
    },

    /**
     * 按指定条件查询用户信息
     * @param requestBody
     * @returns {*|T|Query}
     */
    queryUserByCondition: function(requestBody){
        logger.debug(`queryUserByCondition param: ${json.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.code)) {
            query.push({"code": new RegExp(requestBody.code)});
        }
        if (!_.isEmpty(requestBody.name)) {
            query.push({"name": new RegExp(requestBody.name)});
        }
        query = query.length==2?{"$and": query} : query.length==1 ? query[0] : {};
        return Domain.models.user.find(query);
    }
};
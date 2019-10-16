/**
 * Created by Administrator on 2019/9/24.
 */
'use strict';

const logger = Libs.logger.getLogger('filter');
const excluded = ['/zcy/checkUser',
'/user/modifyUserByCode',
'/device/queryDeviceByCondition',
'/device'];
const bindUserFilter = async (ctx,next) => {
    try {
        if (_.find(excluded, v => {
                return _.startsWith(ctx.url, v);
            }) == undefined) {console.log('11---->'+ctx.url);
            let token = ctx.cookies.get('token');
            if (!_.isEmpty(token)) {
                let result = await Domain.services.user.checkToken(token);
                ctx.currentUser = result;
                await next();
            } else {
                ctx.body = Libs.response.error(Libs.error('0001', '未登录'));
            }
        } else {
            await next();
        }
    }catch(err){
        ctx.body = Libs.response.error(err);
    }
};

module.exports = {
    register: async (app) => {
        app.use(bindUserFilter);
        return app;
    }
};
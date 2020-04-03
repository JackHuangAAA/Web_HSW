
module.exports = (cb)=> {
    return async  (ctx,next) => {
        try {
            let result = await cb(ctx,next);
            ctx.body = Libs.response.success(result);
        } catch (e) {
            ctx.body = Libs.response.error(e);
        }
    };
};
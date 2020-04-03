/**
 * Created by Administrator on 2019/9/24.
 */
'use strict';
const walk = require('walk');
const logger = Libs.logger.getLogger('router');


const removeExtend = function (name) {
    let index = name.lastIndexOf('.');
    return name.substr(0, index);
};

module.exports = {


    register: (app) => {
        return new Promise((resolve,reject)=>{
            let walker = walk.walk(`${__dirname}/routes`, {followLinks: false});
            walker.on('file', (root, stat, next) => {
                let url = root + '/' + removeExtend(stat.name);
                let router = require(url.replace(`${__dirname}/routes`, './routes'));
                router.prefix(url.replace(`${__dirname}/routes`, ''));
                app.use(router.routes());
                next();
            });
            walker.on('end', () => {
                resolve(app);
            });
        });
    }
};
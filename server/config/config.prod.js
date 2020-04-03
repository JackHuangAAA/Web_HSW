/**
 * Created by Administrator on 2019/9/24.
 */
'use strict';

module.exports = {
    mode: 'prod',
    logger: {
        admin: {
            appenders: [{
                type: 'console',
                layout: {
                    type: 'pattern',
                    pattern: '%[[%d] [%p] [%z] %c%] %m'
                }
            }, {
                type: 'dateFile',
                filename: 'log/',
                pattern: 'yyyy-MM/admin-yyyy-MM-dd.log',
                alwaysIncludePattern: true,
                makers: {},
                layout: {
                    type: 'pattern',
                    pattern: '[%d] [%p] [%z] %c %m',
                }
            }],
            levels: 'INFO'
        },
    },
    mongoose: {
        uri: 'mongodb://121.40.241.237/web',
    },
    redis: {
        host: '192.168.0.96',
        port: 6379
    },
    admin: {
        host: '0.0.0.0',
        port: 7000,
    },
	push:{
		host: '0.0.0.0',
        port: 7004
	}
};
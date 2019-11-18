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
        vcc: {
            appenders: [{
                type: 'console',
                layout: {
                    type: 'pattern',
                    pattern: '%[[%d] [%p] [%z] %c%] %m'
                }
            }, {
                type: 'dateFile',
                filename: 'log/',
                pattern: 'yyyy-MM/vcc-yyyy-MM-dd.log',
                alwaysIncludePattern: true,
                makers: {},
                layout: {
                    type: 'pattern',
                    pattern: '[%d] [%p] [%z] %c %m',
                }
            }],
            levels: 'INFO'
        },
        vfc: {
            appenders: [{
                type: 'console',
                layout: {
                    type: 'pattern',
                    pattern: '%[[%d] [%p] [%z] %c%] %m'
                }
            }, {
                type: 'dateFile',
                filename: 'log/',
                pattern: 'yyyy-MM/vfc-yyyy-MM-dd.log',
                alwaysIncludePattern: true,
                makers: {},
                layout: {
                    type: 'pattern',
                    pattern: '[%d] [%p] [%z] %c %m',
                }
            }],
            levels: 'INFO'
        },
        ist: {
            appenders: [{
                type: 'console',
                layout: {
                    type: 'pattern',
                    pattern: '%[[%d] [%p] [%z] %c%] %m'
                }
            }, {
                type: 'dateFile',
                filename: 'log/',
                pattern: 'yyyy-MM/ist-yyyy-MM-dd.log',
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
        uri: 'mongodb://192.168.0.96/iviotp',
    },
    redis: {
        host: '192.168.0.96',
        port: 6379
    },
    admin: {
        host: '0.0.0.0',
        port: 9999,
    },
    vcc: {
        host: '0.0.0.0',
        port: 9998
    },
    vfc: {
        host: '0.0.0.0',
        port: 9997
    },
    ist: {
        host: '0.0.0.0',
        port: 9995
    }
};
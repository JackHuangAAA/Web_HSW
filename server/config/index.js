/**
 * Created by Administrator on 2019/9/24.
 */
'use strict';

let env;

if (process.argv[2] == 'dev') {
    env = require('./config.dev.js');
} else {
    env = require('./config.prod.js');
}

module.exports = env;
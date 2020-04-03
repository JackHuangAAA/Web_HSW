/**
 * Created by Administrator on 2017/10/11.
 */
let _ = require('lodash');

module.exports = function (code, message) {
    return _.create(Error(), {code: code, message: message});
};
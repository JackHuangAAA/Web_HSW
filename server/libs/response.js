/**
 * Created by apple on 2017/9/23.
 */
module.exports = {
    error: function (err) {
        return {
            code: err.code || '9999',
            message: err.message
        };
    },

    success: function (data) {
        return {
            code: '0000',
            message: 'success',
            data: data
        };
    }
};
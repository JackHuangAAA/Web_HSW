/**
 * Created by Administrator on 2019/9/24.
 */
const mongoose = require('mongoose');
const paginate = require('mongoose-paginate');
const logger = Libs.logger.getLogger('mongodb');

if (Config.mongoose.user) {
    mongoose.connect(Config.mongoose.uri, {
        user: Config.mongoose.user,
        pass: Config.mongoose.password,
        useNewUrlParser: true,
        useUnifiedTopology: true,
        auto_reconnect: true,
        poolSize: 10
    });
} else {
    mongoose.connect(Config.mongoose.uri,{
        useNewUrlParser: true,
        useUnifiedTopology: true,
        auto_reconnect: true,
        poolSize: 10  });
}
if(Config.mode == 'dev') {
    mongoose.set('debug', true)
}else{
    mongoose.set('debug', (collectionName, methodName, arg1, arg2) => {
        logger.info(`${collectionName}.${methodName}(${JSON.stringify(arg1)},${JSON.stringify(arg2)})`)
    })
}
const db = mongoose.connection;
db.on('error', () => {
    logger.error('连接mongodb失败');
});
db.once('open', function () {
    logger.debug('连接mongodb成功');
});
exports.mongoose = mongoose;

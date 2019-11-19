/**
 * Created by Administrator on 2019/9/24.
 */
'use strict';
const logger = Libs.logger.getLogger('redis');
let redis = require('redis');
const client = redis.createClient(Config.redis);
const pub = redis.createClient(Config.redis);
Promise.promisifyAll(redis.RedisClient.prototype);
Promise.promisifyAll(redis.Multi.prototype);

client.on("error", function (err) {
    logger.error(err);
});

module.exports = {
    client: client,
    pub:pub
};

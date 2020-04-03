/**
 * Created by Administrator on 2019/9/26 0026.
 * 政采云接口服务
 */

const logger = Libs.logger.getLogger('zcy')
var moment = require('moment')

module.exports = {
    /**
     * 用户登陆校验
     * @param requestBody
     * @returns {Promise.<boolean>}
     */
    checkUser: async function(requestBody) {
        logger.debug(`checkUser param: ${JSON.stringify(requestBody)}`);
        if (requestBody.code == 'admin' && requestBody.password == '000000') {
            return { code: 'admin', name: '小米', check: true };
        }
        return { check: false };
    },

    /**
     * 查询行政区域信息
     * @param requestBody
     * @returns {Promise.<{}>}
     */
    queryDistrict: async function(requestBody) {
        logger.debug(`queryDistrict param: ${JSON.stringify(requestBody)}`);
        let result = {
            name: '浙江省',
            code: '330000000000',
            child: [
                {
                    name: '杭州市',
                    code: '330101000000',
                    child: [
                        {
                            name: '上城区',
                            code: '330101000000',
                            child: [
                                {
                                    name: '清波街道',
                                    code: '330102001000'
                                },
                                {
                                    name: '湖滨街道',
                                    code: '330102003000'
                                }
                            ]
                        },
                        {
                            name: '滨江区',
                            code: '330102000000',
                            child: [
                                {
                                    name: '西兴街道',
                                    code: '330102001000'
                                },
                                {
                                    name: '长河街道',
                                    code: '330102003000'
                                }
                            ]
                        }
                    ]
                },
                {
                    name: '绍兴市',
                    code: '330600000000',
                    child: [
                        {
                            name: '越城区',
                            code: '330602000000',
                            child: [
                                {
                                    name: '塔山街道',
                                    code: '330602001000'
                                },
                                {
                                    name: '蕺山街道',
                                    code: '330602003000'
                                }
                            ]
                        },
                        {
                            name: '柯桥区',
                            code: '330102000000',
                            child: [
                                {
                                    name: '柯岩街道',
                                    code: '330603002000'
                                },
                                {
                                    name: '湖塘街道',
                                    code: '330603004000'
                                }
                            ]
                        }
                    ]
                }
            ]
        };
        return result;
    },

    /**
     * 查询单位信息
     * @param requestBody
     * @returns {Promise.<{}>}
     */
    queryUnit: async function(requestBody) {
        logger.debug(`queryUnit param: ${JSON.stringify(requestBody)}`);
        let result = [
            { code: '3200001', name: '武林' },
            { code: '3200002', name: '望江' },
            { code: '3200003', name: '南星' },
            { code: '3200004', name: '湖滨' },
            { code: '3200005', name: '朝晖' }
        ];
        return result;
    },

    /**
     * 查询柜台编号信息
     * @param requestBody
     * @returns {Promise.<[*,*,*,*,*]>}
     */
    queryCabinetNo: async function(requestBody) {
        logger.debug(`queryCabinetNo param: ${JSON.stringify(requestBody)}`);
        let result = [
            { value: '1100001' },
            { value: '1100002' },
            { value: '1100003' },
            { value: '1100004' },
            { value: '1100005' }
        ]
        return result;
    },

    /**
     * 查询疫苗信息
     * @param requestBody
     * @returns {Promise.<[*,*,*,*,*]>}
     */
    queryVaccine: async function(requestBody) {
        logger.debug(`queryVaccine param: ${JSON.stringify(requestBody)}`);
        let vaccineMap = new Map([
            ["ym991",{code: 'ym991',name: '卡介苗', batchNo:'B-998',supervisionCode: 'jg20190920001', expiry: moment().add(30,'days').format('YYYY-MM-DD HH:mm:ss'), product: '上海生物'}],
            ["ym992",{code: 'ym992',name: '免费乙肝',batchNo:'B-998',supervisionCode: 'jg20190920001',expiry: moment().add(30,'days').format('YYYY-MM-DD HH:mm:ss'),product: '大连汉信' }],
            ["ym993",{code: 'ym993',name: '脊灰灭活疫苗',batchNo:'B-998',supervisionCode: 'jg20190920001',expiry: moment().add(30,'days').format('YYYY-MM-DD HH:mm:ss'),product: '医科院' }],
            ["ym994",{code: 'ym994',name: '百白破',batchNo:'B-998',supervisionCode: 'jg20190920001',expiry: moment().add(30,'days').format('YYYY-MM-DD HH:mm:ss'),product: '武汉生物'}],
            ["ym995",{code: 'ym995',name: '麻风',batchNo:'B-998',supervisionCode: 'jg20190920001',expiry: moment().add(30,'days').format('YYYY-MM-DD HH:mm:ss'),product: '北京天坛'}],
            ["ym996",{code: 'ym996',name: '麻腮风',batchNo:'B-998',supervisionCode: 'jg20190920001',expiry: moment().add(30,'days').format('YYYY-MM-DD HH:mm:ss'),product: '上海生物'}],
            ["ym997",{code: 'ym997',name: 'AC群结合疫苗',batchNo:'B-998',supervisionCode: 'jg20190920001',expiry: moment().add(30,'days').format('YYYY-MM-DD HH:mm:ss'),product: '无锡罗益'}],
            ["ym998",{code: 'ym998',name: '水痘减毒活疫苗',batchNo:'B-998',supervisionCode: 'jg20190920001',expiry: moment().add(30,'days').format('YYYY-MM-DD HH:mm:ss'),product: '长春祁健'}],
            ["ym999",{code: 'ym999',name: '23价肺炎',batchNo:'B-998',supervisionCode: 'jg20190920001',expiry: moment().add(30,'days').format('YYYY-MM-DD HH:mm:ss'),product: '成都生物'}],
            ["ym1000",{code: 'ym1000',name: '甲肝减毒活疫苗',batchNo:'B-998',supervisionCode: 'jg20190920001',expiry: moment().add(30,'days').format('YYYY-MM-DD HH:mm:ss'),product: '浙江普康'}],
            ["ym1001",{code: 'ym1001',name: '流脑A群多糖',batchNo:'B-998',supervisionCode: 'jg20190920001',expiry: moment().add(30,'days').format('YYYY-MM-DD HH:mm:ss'),product: '武汉生物'}]
        ]);
        let result= vaccineMap.get(requestBody.code);
        return result;
    },
    /**
     * 查询疫苗总种类信息
     * @param requestBody
     * @returns {Promise.<[*,*,*,*,*]>}
     */
    queryVaccineKinds: async function(requestBody) {
        logger.debug(`queryVaccine param: ${JSON.stringify(requestBody)}`);
        let result = [
            {
                code: 'ym991',
                name: '卡介苗',
                batchNo:'B-998',
                supervisionCode: 'jg20190920001',
                expiry: moment().add(30,'days').format('YYYY-MM-DD HH:mm:ss'),
                product: '上海生物'
            },
            {
                code: 'ym992',
                name: '免费乙肝',
                batchNo:'B-998',
                supervisionCode: 'jg20190920001',
                expiry: moment().add(30,'days').format('YYYY-MM-DD HH:mm:ss'),
                product: '大连汉信'
            },
            {
                code: 'ym993',
                name: '脊灰灭活疫苗',
                batchNo:'B-998',
                supervisionCode: 'jg20190920001',
                expiry: moment().add(30,'days').format('YYYY-MM-DD HH:mm:ss'),
                product: '医科院'
            },
            {
                code: 'ym994',
                name: '百白破',
                batchNo:'B-998',
                supervisionCode: 'jg20190920001',
                expiry: moment().add(30,'days').format('YYYY-MM-DD HH:mm:ss'),
                product: '武汉生物'
            },
            {
                code: 'ym995',
                name: '麻风',
                batchNo:'B-998',
                supervisionCode: 'jg20190920001',
                expiry: moment().add(30,'days').format('YYYY-MM-DD HH:mm:ss'),
                product: '北京天坛'
            },
            {
                code: 'ym996',
                name: '麻腮风',
                batchNo:'B-998',
                supervisionCode: 'jg20190920001',
                expiry: moment().add(30,'days').format('YYYY-MM-DD HH:mm:ss'),
                product: '上海生物'
            },
            {
                code: 'ym997',
                name: 'AC群结合疫苗',
                batchNo:'B-998',
                supervisionCode: 'jg20190920001',
                expiry: moment().add(30,'days').format('YYYY-MM-DD HH:mm:ss'),
                product: '无锡罗益'
            },
            {
                code: 'ym998',
                name: '水痘减毒活疫苗',
                batchNo:'B-998',
                supervisionCode: 'jg20190920001',
                expiry: moment().add(30,'days').format('YYYY-MM-DD HH:mm:ss'),
                product: '长春祁健'
            },
            {
                code: 'ym999',
                name: '23价肺炎',
                batchNo:'B-998',
                supervisionCode: 'jg20190920001',
                expiry: moment().add(30,'days').format('YYYY-MM-DD HH:mm:ss'),
                product: '成都生物'
            },
            {
                code: 'ym1000',
                name: '甲肝减毒活疫苗',
                batchNo:'B-998',
                supervisionCode: 'jg20190920001',
                expiry: moment().add(30,'days').format('YYYY-MM-DD HH:mm:ss'),
                product: '浙江普康'
            },
            {
                code: 'ym1001',
                name: '流脑A群多糖',
                batchNo:'B-998',
                supervisionCode: 'jg20190920001',
                expiry: moment().add(30,'days').format('YYYY-MM-DD HH:mm:ss'),
                product: '武汉生物'
            }
        ];
        return result;
    },

    /**
     * 接收接种信息
     * @param requestBody
     * @returns {Promise.<[*,*,*,*,*]>}
     */
    receiveVaccination: async function(requestBody) {
        logger.debug(`queryVaccine param: ${JSON.stringify(requestBody)}`);
        let result = {
            customer:{
                code: '089',
                name: '李义',
                age: 4,
                vaccineCode: 'ym991',
                vaccineName: '卡介苗',
                vaccineNum: 1
            },
            cabinetNo:"1100002"
        };
        let device_result = await Domain.models.device.find({cabinetNo:result.cabinetNo});

        let channel = "receiveVaccination";
        result.code = device_result[0].code;
        result.type = channel;

        let message = JSON.stringify(result);

        await Domain.redis.pub.publishAsync(channel, message);

    },

    /**
     * 收任务完成状态
     * @param requestBody
     * @returns {Promise.<{customer: {code: string, name: string, age: number, vaccineCode: string, vaccineName: string, vaccineNum: number}}>}
     */
    receiveVaccinationStatus: async function(requestBody) {
        logger.debug(`receiveVaccinationStatus param: ${JSON.stringify(requestBody)}`);
        let result = {
            status: "finish",
            cabinetNo:"1100002"
        };

        let device = await Domain.models.device.find({cabinetNo:result.cabinetNo});

        let channel = "receiveVaccinationStatus";
        result.code = device[0].code;
        result.type = channel;

        let message = JSON.stringify(result);

        Domain.redis.pub.publishAsync(channel, message);
        //return result;
    },

    /**
     * 根据条形码查询疫苗信息
     * @param requestBody
     * @returns {Promise.<{customer: {code: string, name: string, age: number, vaccineCode: string, vaccineName: string, vaccineNum: number}}>}
     */
    queryVaccineByBarcode: async function(requestBody) {
        logger.debug(`queryVaccineByBarcode param: ${JSON.stringify(requestBody)}`);
        let result = {
            code: '001',
            name:'疫苗0',
            batchNo:'B-999',
        };
        return result;
    },

    /**
     * queryExceptionVaccine
     * @param requestBody
     * @returns {Promise.<{code: number, name: string, batchNo: string}>}
     */
    queryExceptionVaccine: async function(requestBody) {
        logger.debug(`queryExceptionVaccine param: ${JSON.stringify(requestBody)}`);
        let result = {
            code: '002',
            name:'疫苗0',
            batchNo:'B-999'
        };
        return result;
    }

}

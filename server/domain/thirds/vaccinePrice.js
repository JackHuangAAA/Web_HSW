/**
 * Created by Administrator on 2019/11/22 0026.
 * 疫苗价格接口
 */

const logger = Libs.logger.getLogger('vaccinePrice')
var mongoose = require('mongoose')
const moment = require('moment');

module.exports = {

    /**
     * 查询疫苗信息
     * @param requestBody
     * @returns {Promise.<[*,*,*,*,*]>}
     */
    queryVaccinePrice: async function(requestBody) {
        logger.debug(`queryVaccine param: ${JSON.stringify(requestBody)}`);
        let vaccineMap = new Map([
            ["卡介苗",{free:{code: 'ym991',name: '卡介苗', batchNo:'B-998',supervisionCode: 'FT20190920001',expiry: moment('2020-11-20T06:14:02.478Z ').format('YYYY-MM-DD HH:mm:ss'), product: '上海生物',dosage:'0.5ml/支',cost:'0.00'},
                        paid:{code: 'ym991',name: '卡介苗', batchNo:'IS-1842',supervisionCode: 'ZF20190921121',expiry:  moment('2020-06-12T12:11:02.478Z ').format('YYYY-MM-DD HH:mm:ss'), product: '葛兰素史克（进口）',dosage:'0.5ml/支',cost:'50.00'}}],
            ["免费乙肝",{free:{code: 'ym992',name: '免费乙肝',batchNo:'B-998',supervisionCode: 'ZJ20190920001',expiry:  moment('2020-11-20T06:14:02.478Z ').format('YYYY-MM-DD HH:mm:ss'),product: '大连汉信',dosage:'0.5ml/支',cost:'0.00'},
                        paid:{code: 'ym992',name: '免费乙肝',batchNo:'GV-230',supervisionCode: 'FR20190920001',expiry:  moment('2021-05-01T03:26:02.478Z ').format('YYYY-MM-DD HH:mm:ss'),product: '巴斯德（进口）',dosage:'0.5ml/支',cost:'150.00'}}],
            ["脊灰灭活疫苗",{free:{code: 'ym993',name: '脊灰灭活疫苗',batchNo:'B-998',supervisionCode: 'AE20190920001',expiry:  moment('2020-11-20T06:14:02.478Z ').format('YYYY-MM-DD HH:mm:ss'),product: '医科院',dosage:'0.5ml/支',cost:'0.00'},
                            paid:{code: 'ym993',name: '脊灰灭活疫苗',batchNo:'KL-234',supervisionCode: 'IT20190920001',expiry:  moment('2020-06-12T12:11:02.478Z ').format('YYYY-MM-DD HH:mm:ss'),product: '默沙东（进口）',dosage:'0.5ml/支',cost:'350.00'}}],


            ["百白破",{free:{code: 'ym994',name: '百白破',batchNo:'B-998',supervisionCode: 'BT20190920001',expiry:  moment('2020-11-20T06:14:02.478Z ').format('YYYY-MM-DD HH:mm:ss'),product: '武汉生物',dosage:'0.5ml/支',cost:'0.00'},
                paid:{code: 'ym994',name: '百白破',batchNo:'HQ-14W',supervisionCode: 'ME20190920001',expiry:  moment('2021-05-01T03:26:02.478Z ').format('YYYY-MM-DD HH:mm:ss'),product:  '葛兰素史克（进口）',dosage:'0.5ml/支',cost:'80.00'}}],

            ["麻风",{free:{code: 'ym995',name: '麻风',batchNo:'B-998',supervisionCode: 'RJ20190920001',expiry:  moment('2020-11-20T06:14:02.478Z ').format('YYYY-MM-DD HH:mm:ss'),product: '北京天坛',dosage:'0.5ml/支',cost:'0.00'},
                paid:{code: 'ym995',name: '麻风',batchNo:'XSD-2',supervisionCode: 'KL20190920001',expiry:  moment('2020-06-12T12:11:02.478Z ').format('YYYY-MM-DD HH:mm:ss'),product: '默沙东（进口）',dosage:'0.5ml/支',cost:'100.00'}}],

            ["麻腮风",{free:{code: 'ym996',name: '麻腮风',batchNo:'B-998',supervisionCode: 'RF20190920001',expiry:  moment('2020-11-20T06:14:02.478Z ').format('YYYY-MM-DD HH:mm:ss'),product: '上海生物',dosage:'0.5ml/支',cost:'0.00'},
                paid:{code: 'ym996',name: '麻腮风',batchNo:'L-203',supervisionCode: 'GR20190920001',expiry:  moment('2021-05-01T03:26:02.478Z ').format('YYYY-MM-DD HH:mm:ss'),product: '巴斯德（进口）',dosage:'0.5ml/支',cost:'180.00'}}],
        ]);
        let result= vaccineMap.get(requestBody.name);
        return result;
    },

}

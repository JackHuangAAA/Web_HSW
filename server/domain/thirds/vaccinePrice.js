/**
 * Created by Administrator on 2019/11/22 0026.
 * 疫苗价格接口
 */

const logger = Libs.logger.getLogger('vaccinePrice')
var mongoose = require('mongoose')
Date.prototype.format = function(fmt)
{ //author: meizz
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "h+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}

module.exports = {

    /**
     * 查询疫苗信息
     * @param requestBody
     * @returns {Promise.<[*,*,*,*,*]>}
     */
    queryVaccinePrice: async function(requestBody) {
        logger.debug(`queryVaccine param: ${JSON.stringify(requestBody)}`);
        let vaccineMap = new Map([
            ["卡介苗",{free:{code: 'ym991',name: '卡介苗', batchNo:'B-998',supervisionCode: 'FT20190920001',expiry: new Date("2020-11-20T06:14:02.478Z").format("yyyy-MM-dd hh:mm:ss"), product: '上海生物',dosage:'0.5ml/支',cost:'0.00'},
                        paid:{code: 'ym991',name: '卡介苗', batchNo:'B-998',supervisionCode: 'ZF20190921121',expiry: new Date("2020-11-20T06:14:02.478Z").format("yyyy-MM-dd hh:mm:ss"), product: '上海生物',dosage:'0.5ml/支',cost:'50.00'}}],
            ["免费乙肝",{free:{code: 'ym992',name: '免费乙肝',batchNo:'B-998',supervisionCode: 'ZJ20190920001',expiry: new Date("2020-11-20T06:14:02.478Z").format("yyyy-MM-dd hh:mm:ss"),product: '大连汉信',dosage:'0.5ml/支',cost:'0.00'},
                        paid:{code: 'ym992',name: '免费乙肝',batchNo:'B-998',supervisionCode: 'FR20190920001',expiry: new Date("2020-11-20T06:14:02.478Z").format("yyyy-MM-dd hh:mm:ss"),product: '大连汉信',dosage:'0.5ml/支',cost:'50.00'}}],
            ["脊灰灭活疫苗",{free:{code: 'ym993',name: '脊灰灭活疫苗',batchNo:'B-998',supervisionCode: 'AE20190920001',expiry: new Date("2020-11-20T06:14:02.478Z").format("yyyy-MM-dd hh:mm:ss"),product: '医科院',dosage:'0.5ml/支',cost:'0.00'},
                            paid:{code: 'ym993',name: '脊灰灭活疫苗',batchNo:'B-998',supervisionCode: 'IT20190920001',expiry: new Date("2020-11-20T06:14:02.478Z").format("yyyy-MM-dd hh:mm:ss"),product: '医科院',dosage:'0.5ml/支',cost:'50.00'}}],


            ["百白破",{free:{code: 'ym994',name: '百白破',batchNo:'B-998',supervisionCode: 'BT20190920001',expiry: new Date("2020-11-20T06:14:02.478Z").format("yyyy-MM-dd hh:mm:ss"),product: '武汉生物',dosage:'0.5ml/支',cost:'0.00'},
                paid:{code: 'ym994',name: '百白破',batchNo:'B-998',supervisionCode: 'ME20190920001',expiry: new Date("2020-11-20T06:14:02.478Z").format("yyyy-MM-dd hh:mm:ss"),product: '武汉生物',dosage:'0.5ml/支',cost:'50.00'}}],

            ["麻风",{free:{code: 'ym995',name: '麻风',batchNo:'B-998',supervisionCode: 'RJ20190920001',expiry: new Date("2020-11-20T06:14:02.478Z").format("yyyy-MM-dd hh:mm:ss"),product: '北京天坛',dosage:'0.5ml/支',cost:'0.00'},
                paid:{code: 'ym995',name: '麻风',batchNo:'B-998',supervisionCode: 'KL20190920001',expiry: new Date("2020-11-20T06:14:02.478Z").format("yyyy-MM-dd hh:mm:ss"),product: '北京天坛',dosage:'0.5ml/支',cost:'50.00'}}],

            ["麻腮风",{free:{code: 'ym996',name: '麻腮风',batchNo:'B-998',supervisionCode: 'RF20190920001',expiry: new Date("2020-11-20T06:14:02.478Z").format("yyyy-MM-dd hh:mm:ss"),product: '上海生物',dosage:'0.5ml/支',cost:'0.00'},
                paid:{code: 'ym996',name: '麻腮风',batchNo:'B-998',supervisionCode: 'GR20190920001',expiry: new Date("2020-11-20T06:14:02.478Z").format("yyyy-MM-dd hh:mm:ss"),product: '上海生物',dosage:'0.5ml/支',cost:'50.00'}}],
        ]);
        let result= vaccineMap.get(requestBody.name);
        return result;
    },

}

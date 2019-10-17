/**
 * Created by Administrator on 2019/9/26 0026.
 * 政采云接口服务
 */

const logger = Libs.logger.getLogger('vaccine')
var mongoose = require('mongoose')

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
        let result = {
          code: 'ym20190920134508999',
          name: '脊髓灰质炎疫苗',
          supervisionCode: 'jg20190920001',
          expiry: new Date(),
          producer: '长春生物制药'
        };
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
          code: 'ym20190920134508991',
          name: '脊髓灰质炎疫苗',
          supervisionCode: 'jg20190920001',
          expiry: new Date(),
          producer: '长春生物制药'
        },
          {
          code: 'ym20190920134508992',
          name: '麻疹疫苗',
          supervisionCode: 'jg20190920001',
          expiry: new Date(),
          producer: '长春生物制药'
        },
          {
          code: 'ym20190920134508993',
          name: '乙肝疫苗',
          supervisionCode: 'jg20190920001',
          expiry: new Date(),
          producer: '长春生物制药'
        },
          {
          code: 'ym20190920134508994',
          name: '水痘',
          supervisionCode: 'jg20190920001',
          expiry: new Date(),
          producer: '长春生物制药'
        }
      ];
        return result;
      },

      /**
       * 接收接种信息
       * @param requestBody
       * @returns {Promise.<[*,*,*,*,*]>}
       */
      reciveVaccination: async function(requestBody) {
        logger.debug(`queryVaccine param: ${JSON.stringify(requestBody)}`);
        let result = {
            customer:{
                code: '089',
                name: '李义',
                age: 4,
                vaccineCode: 'ym20190920134508999',
                vaccineName: '脊髓灰质炎疫苗',
                vaccineNum: 1
            }
        };
        return result;
      },

    /**
     * 收任务完成状态
     * @param requestBody
     * @returns {Promise.<{customer: {code: string, name: string, age: number, vaccineCode: string, vaccineName: string, vaccineNum: number}}>}
     */
    reciveVaccinationStatus: async function(requestBody) {
        logger.debug(`reciveVaccinationStatus param: ${JSON.stringify(requestBody)}`);
        let result = {
            status: finish
        };
        return result;
    }
}

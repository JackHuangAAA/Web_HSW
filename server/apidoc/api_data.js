define({ "api": [
  {
    "type": "GET",
    "url": "/queryAlarmByByCondition",
    "title": "查询报警次数",
    "group": "Alarm",
    "version": "1.0.0",
    "description": "<p>查询报警次数</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "type",
            "description": "<p>报警类型(1:温度异常;2:库存不足,不传查询全部)</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Array",
            "optional": false,
            "field": "rs",
            "description": "<p>当天报警次数信息数组</p>"
          },
          {
            "group": "Success 200",
            "type": "Number",
            "optional": false,
            "field": "total",
            "description": "<p>当天报警次数</p>"
          }
        ]
      }
    },
    "filename": "vcc/routes/alarm.js",
    "groupTitle": "Alarm",
    "name": "GetQueryalarmbybycondition"
  },
  {
    "type": "GET",
    "url": "/queryAlarmDailyInfo",
    "title": "查询当天报警信息",
    "group": "Alarm",
    "version": "1.0.0",
    "description": "<p>查询当天报警信息</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Array",
            "optional": false,
            "field": "rs",
            "description": "<p>当天报警信息数组</p>"
          },
          {
            "group": "Success 200",
            "type": "Number",
            "optional": false,
            "field": "total",
            "description": "<p>当天报警次数</p>"
          }
        ]
      }
    },
    "filename": "vcc/routes/alarm.js",
    "groupTitle": "Alarm",
    "name": "GetQueryalarmdailyinfo"
  },
  {
    "type": "GET",
    "url": "/modifyDrawerById",
    "title": "根据抽屉id更新抽屉信息",
    "group": "Drawer",
    "version": "1.0.0",
    "description": "<p>根据抽屉id更新抽屉信息</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "id",
            "description": "<p>抽屉id</p>"
          },
          {
            "group": "Parameter",
            "type": "Array",
            "optional": false,
            "field": "vaccine",
            "description": "<p>疫苗数组数组</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Object",
            "optional": false,
            "field": "data",
            "description": "<p>操作返回数据</p>"
          }
        ]
      }
    },
    "filename": "vcc/routes/drawer.js",
    "groupTitle": "Drawer",
    "name": "GetModifydrawerbyid"
  },
  {
    "type": "GET",
    "url": "/queryDrawerEmpty",
    "title": "查询抽屉里疫苗为空的数据",
    "group": "Drawer",
    "version": "1.0.0",
    "description": "<p>查询抽屉里疫苗为空的数据</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "device",
            "description": "<p>设备id.</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Array",
            "optional": false,
            "field": "rs",
            "description": "<p>疫苗为空的数据数组</p>"
          },
          {
            "group": "Success 200",
            "type": "Number",
            "optional": false,
            "field": "total",
            "description": "<p>疫苗为空的数据总数</p>"
          }
        ]
      }
    },
    "filename": "vcc/routes/drawer.js",
    "groupTitle": "Drawer",
    "name": "GetQuerydrawerempty"
  },
  {
    "type": "GET",
    "url": "/modifyUserByCode",
    "title": "按用户code更新用户信息，不存在时插入，存在时修改用户名",
    "group": "User",
    "version": "1.0.0",
    "description": "<p>按用户code更新用户信息，不存在时插入，存在时修改用户名</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Object",
            "optional": false,
            "field": "data",
            "description": "<p>操作返回数据</p>"
          }
        ]
      }
    },
    "filename": "vcc/routes/user.js",
    "groupTitle": "User",
    "name": "GetModifyuserbycode"
  },
  {
    "type": "GET",
    "url": "/queryDrawerByCondition",
    "title": "按设备id查询抽屉信息，并按坐标排序",
    "group": "User",
    "version": "1.0.0",
    "description": "<p>按设备id查询抽屉信息，并按坐标排序</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "id",
            "description": "<p>抽屉id</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Array",
            "optional": false,
            "field": "data",
            "description": "<p>操作返回数据数组</p>"
          }
        ]
      }
    },
    "filename": "vcc/routes/drawer.js",
    "groupTitle": "User",
    "name": "GetQuerydrawerbycondition"
  },
  {
    "type": "GET",
    "url": "/queryUserByCondition",
    "title": "按指定条件查询用户信息",
    "group": "User",
    "version": "1.0.0",
    "description": "<p>按指定条件查询用户信息</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "code",
            "description": "<p>用户code</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Array",
            "optional": false,
            "field": "data",
            "description": "<p>操作返回数据数组</p>"
          }
        ]
      }
    },
    "filename": "vcc/routes/user.js",
    "groupTitle": "User",
    "name": "GetQueryuserbycondition"
  },
  {
    "type": "GET",
    "url": "/queryUserByCondition",
    "title": "按用户id更新指纹信息",
    "group": "User",
    "version": "1.0.0",
    "description": "<p>按用户id更新指纹信息</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "id",
            "description": "<p>用户id</p>"
          },
          {
            "group": "Parameter",
            "type": "Array",
            "optional": false,
            "field": "finger",
            "description": "<p>用户指纹数组</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Object",
            "optional": false,
            "field": "data",
            "description": "<p>操作返回数据</p>"
          }
        ]
      }
    },
    "filename": "vcc/routes/user.js",
    "groupTitle": "User",
    "name": "GetQueryuserbycondition"
  },
  {
    "type": "GET",
    "url": "/queryVaccine",
    "title": "查询疫苗种类",
    "group": "Vaccine",
    "version": "1.0.0",
    "description": "<p>查询疫苗种类</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "device",
            "description": "<p>设备id</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Array",
            "optional": false,
            "field": "rs",
            "description": "<p>查询疫苗种类数组</p>"
          },
          {
            "group": "Success 200",
            "type": "Number",
            "optional": false,
            "field": "total",
            "description": "<p>查询疫苗种类总数</p>"
          }
        ]
      }
    },
    "filename": "vcc/routes/vaccine.js",
    "groupTitle": "Vaccine",
    "name": "GetQueryvaccine"
  },
  {
    "type": "GET",
    "url": "/queryVaccineLowerThreshold",
    "title": "查询疫苗剩余数量小于报警阈值的疫苗信息",
    "group": "Vaccine",
    "version": "1.0.0",
    "description": "<p>查询疫苗剩余数量小于报警阈值的疫苗信息</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "device",
            "description": "<p>设备id</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Array",
            "optional": false,
            "field": "rs",
            "description": "<p>疫苗剩余数量小于报警阈值的疫苗信息数组</p>"
          },
          {
            "group": "Success 200",
            "type": "Number",
            "optional": false,
            "field": "total",
            "description": "<p>查询疫苗剩余数量小于报警阈值的疫苗信息总数</p>"
          }
        ]
      }
    },
    "filename": "vcc/routes/vaccine.js",
    "groupTitle": "Vaccine",
    "name": "GetQueryvaccinelowerthreshold"
  }
] });

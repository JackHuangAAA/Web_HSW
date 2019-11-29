
import axios from "axios";
import config from "@/config";
import router from "@/router";
import Message from 'iview/src/components/message';
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
axios.defaults.withCredentials = true;

const RSP_CODE = {
    NO_LOGIN: '0001',
    SYSTEM_ERROR: '9999'
};

const commonResponseHandler = (response,resolve,reject)=>{
    if(config.env == 'development'){
        console.log(`<= `,response.data);
    }
    if (response.code == RSP_CODE.NO_LOGIN) {
        // router.push('/login');
    } else if (response.code == '9999') {
        Message.error({content:response.message,closable: true,duration: 0});
    } else {
        if(response){
            resolve(response);
        }
    }
};

const errorHandler = (err)=>{
    if(config.env == 'development'){
        //console.error(`=> ${err}`);
    }
    Message.error({content:'请求异常',closable: true,duration: 0});
};

export default {
    get: function (url, data) {
        if(config.env == 'development'){
            console.log(`=> ${url}`,data || {});
            axios.defaults.headers.common['deviceid'] = 'IST0001'; //todo 测试使用
            return new Promise((resolve,reject)=>{
                axios.get(`/ist${url}?t=${new Date().getTime()}`, {params: data})
                    .then(response =>{
                        commonResponseHandler(response.data,resolve,reject)
                    }).catch(errorHandler);
            });
        }else{
            let param = '?';
            _.forIn(data, (value, key)=>{
                param+= `${key}=${value}&`;
            });
            url= url+param.substr(0,param.length-1);
            return new Promise((resolve,reject)=>{
                $d.invoke('SERVER', 'Get', {path: `/ist${url}`}).then((res)=>{
                    commonResponseHandler(JSON.parse(res.rsp),resolve,reject)
                }).catch(errorHandler)
            });
        }
    },

    post: function (url, data) {
        if(config.env == 'development'){
            console.log(`=> ${url}`,data || {});
            axios.defaults.headers.common['deviceid'] = 'IST0001';  //todo 测试使用
            return new Promise((resolve,reject)=>{
                axios.post(`/ist${url}?t=${new Date().getTime()}`, data)
                    .then(response =>{
                        commonResponseHandler(response.data,resolve,reject)
                    }).catch(errorHandler);
            });
        }else{
            return new Promise((resolve,reject)=>{
                $d.invoke('SERVER', 'Post', {path: `/ist${url}?t=${new Date().getTime()}`, data : JSON.stringify(data)}).then((res)=>{
                    commonResponseHandler(JSON.parse(res.rsp),resolve,reject)
                }).catch(errorHandler)
            });
        }
    }
};

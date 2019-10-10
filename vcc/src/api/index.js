import axios from 'axios';
import config from '@/config';
import router from '@/router';
import Message from 'iview/src/components/message';
import { mapState } from 'vuex';
axios.defaults.headers.post['Content-Type'] =
  'application/x-www-form-urlencoded';
axios.defaults.withCredentials = true;
axios.defaults.headers.common['type'] = config.type;
const RSP_CODE = {
  NO_LOGIN: '0001',
  SYSTEM_ERROR: '9999'
};

const commonResponseHandler = (response, resolve, reject) => {
  if (config.env == 'development') {
    // console.log(`<= ${response.config.url}`,response.data);
  }
  if (response.data.code == RSP_CODE.NO_LOGIN) {
    router.push('/login');
  } else if (response.data.code == RSP_CODE.SYSTEM_ERROR) {
    Message.error({
      content: response.data.message,
      closable: true,
      duration: 0
    });
    router.push('/login');
  } else {
    if (response.data) {
      resolve(response.data);
    }
  }
};

const errorHandler = err => {
  if (config.env == 'development') {
    console.error(`=> ${err}`);
  }
  Message.error({ content: '请求异常', closable: true, duration: 0 });
  router.push('/login'); //有问题时也跳转到login
};

export default {
  get: function(url, data) {
    if (config.env == 'development') {
      //   console.log(`=> ${url}`, data || {})
    }
    return new Promise((resolve, reject) => {
      axios
        .get(`/vcc${url}?t=${new Date().getTime()}`, { params: data })
        .then(response => {
          commonResponseHandler(response, resolve, reject);
        })
        .catch(errorHandler);
    });
  },

  post: function(url, data) {
    if (config.env == 'development') {
      //   console.log(`=> ${url}`, data || {})
    }
    return new Promise((resolve, reject) => {
      axios
        .post(`/vcc${url}?t=${new Date().getTime()}`, data)
        .then(response => {
          commonResponseHandler(response, resolve, reject);
        })
        .catch(errorHandler);
    });
  },
  setHeaders: function(common, value) {
    if (value != undefined) {
      axios.defaults.headers.common[common] = value;
    }
  }
};

import axios from 'axios'
import Vue from 'vue'

export default {
  myVue: new Vue(),
  url: {
    baseUrl: 'http://localhost:8080',
    mealImgUrl: 'http://localhost:8090/meal/'
  },
  isShow: false,
  user: {
    uid: '',
    name: '',
    credit: 0,
    tid: ''
  },
  mealList: [],
  unsubOrderList: [], //未提交的订单
  unpaidOrderList: [],  //未付款的订单
  paidOrderList: [],  //已付款的订单
  paidMealList: [], //点过的菜
}

let base = ''
export const postRequest = (url, params) => {
  return axios({
    method: 'post',
    url: `${base}${url}`,
    data: params,
    transformRequest: [function (data) {
      let ret = ''
      for (let it in data) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
      }
      return ret
    }],
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  });
}
export const postJsonRequest = (url, params) => {
  return axios({
    method: 'post',
    url: `${base}${url}`,
    data: JSON.stringify(params),
    headers: {
      'Content-Type': 'application/json;charset=utf-8'
    }
  });
}
export const getRequest = (url) => {
  return axios({
    method: 'get',
    url: `${base}${url}`
  });
}

export const setDate = (date) => {
  let _odate = ''
  for(let i = 0; i < date.length; i++) {
    if(date[i] == ' ') {
      break
    }
    _odate += date[i]
  }
  return _odate
}

//去重
export const removeDuplication = (list, val) => {
  for (let i = 0; i < list.length - 1; i++) {
    for (let j = i + 1; j < list.length; j++) {
      if(val == null) {
        if (list[i] == list[j]) {
          list.splice(j, 1)
          j--
        }
      }else {
        if (list[i][val] == list[j][val]) {
          list.splice(j, 1)
          j--
        }
      }
    }
  }
}

Date.prototype.Format = function(fmt) {
  var o = {
    "M+": this.getMonth() + 1, //月份
    "d+": this.getDate(), //日
    "H+": this.getHours(), //小时
    "m+": this.getMinutes(), //分
    "s+": this.getSeconds(), //秒
    "q+": Math.floor((this.getMonth() + 3) / 3), //季度
    "S": this.getMilliseconds() //毫秒
  };
  if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
  for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
  return fmt;
}

export const back1 = () => {
  history.go(-1)
}



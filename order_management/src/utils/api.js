import axios from 'axios'
import Vue from 'vue'

export default {
  myVue: new Vue,
  mealList: [],
  userList: [],
  url: 'http://localhost:8080',
  socketMsg: '',
  user: [],
  dateOptions: {
    shortcuts: [{
      text: '最近一周',
      onClick(picker) {
        const end = new Date();
        const start = new Date();
        start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
        picker.$emit('pick', [start, end]);
      }
    }, {
      text: '最近一个月',
      onClick(picker) {
        const end = new Date();
        const start = new Date();
        start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
        picker.$emit('pick', [start, end]);
      }
    }, {
      text: '最近三个月',
      onClick(picker) {
        const end = new Date();
        const start = new Date();
        start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
        picker.$emit('pick', [start, end]);
      }
    }]
  },
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

export const export2Excel = (tHeader, filterVal, dataList, fileName) => {
  require.ensure([], () => {
    const { export_json_to_excel } = require('../excel/Export2Excel')
    const data = dataList.map(v => filterVal.map(j => v[j]))
    export_json_to_excel(tHeader, data, fileName)
  })
}

export const import2Excel = (obj, tHeader, filterVal) => {
  let _this = this;
  // 通过DOM取文件数据
  this.file = obj
  var rABS = false; //是否将文件读取为二进制字符串
  var f = this.file;
  var reader = new FileReader();
  //if (!FileReader.prototype.readAsBinaryString) {
  FileReader.prototype.readAsBinaryString = function(f) {
    var binary = "";
    var rABS = false; //是否将文件读取为二进制字符串
    var pt = this;
    var wb; //读取完成的数据
    var outdata;
    var reader = new FileReader();
    reader.onload = function(e) {
      var bytes = new Uint8Array(reader.result);
      var length = bytes.byteLength;
      for(var i = 0; i < length; i++) {
        binary += String.fromCharCode(bytes[i]);
      }
      var XLSX = require('xlsx');
      if(rABS) {
        wb = XLSX.read(btoa(fixdata(binary)), { //手动转化
          type: 'base64'
        });
      } else {
        wb = XLSX.read(binary, {
          type: 'binary'
        });
      }
      outdata = XLSX.utils.sheet_to_json(wb.Sheets[wb.SheetNames[0]]);  //outdata就是想要的东西
      this.dataList = [...outdata]
      let arr = []
      for(let data of this.dataList) {
        let obj = {}
        for(let i = 0; i < tHeader.length; i++) {
          data[tHeader[i]] == undefined ? data[tHeader[i]] = null : ''
          obj[filterVal[i]] = data[tHeader[i]]
        }
        arr.push(obj)
      }
      console.log(arr);
      return arr
    }
    reader.readAsArrayBuffer(f);
  }
  if(rABS) {
    reader.readAsArrayBuffer(f);
  } else {
    reader.readAsBinaryString(f);
  }
}

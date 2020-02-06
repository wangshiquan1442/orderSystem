// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import axios from 'axios';
import jquery from 'jquery';
import store from './store'

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import {postRequest, postJsonRequest, getRequest} from "./utils/api";
import './assets/css/index.css'
import './excel/Blob'
import './excel/Export2Excel'

Vue.use(ElementUI);

Vue.prototype.$axios = axios
Vue.prototype.$postRequest = postRequest
Vue.prototype.$postJsonRequest = postJsonRequest
Vue.prototype.$getRequest = getRequest
Vue.prototype.$ = jquery

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})

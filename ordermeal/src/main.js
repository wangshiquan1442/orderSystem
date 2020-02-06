// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue';
import App from './App';
import router from './router';
import axios from 'axios';
import qs from 'qs';
import jquery from 'jquery';
import store from './store';

// Mint
import 'mint-ui/lib/style.css';
import {
  Checklist,
  MessageBox,
  Badge,
} from 'mint-ui';

Vue.component(Checklist.name, Checklist);
Vue.component(MessageBox);
Vue.component(Badge.name, Badge);

// Vant
import {
  Checkbox, CheckboxGroup, Icon, Image, Lazyload,
  Stepper, RadioGroup, Radio, Button, TreeSelect,
  Tabbar, TabbarItem, Search, Dialog, Toast, Field,
  Tag, Tab, Tabs, Rate, Divider
} from 'vant';
import 'vant/lib/index.css';
Vue.use(Checkbox);
Vue.use(CheckboxGroup);
Vue.use(Icon);
Vue.use(Image);
Vue.use(Lazyload);
Vue.use(Stepper);
Vue.use(RadioGroup);
Vue.use(Radio);
Vue.use(Button);
Vue.use(TreeSelect);
Vue.use(Tabbar).use(TabbarItem);
Vue.use(Search);
Vue.use(Dialog);
Vue.use(Toast);
Vue.use(Field);
Vue.use(Tag);
Vue.use(Tab);
Vue.use(Tabs)
Vue.use(Rate)
Vue.use(Divider)

import './assets/css/global.css';
import {postRequest, getRequest, setDate, postJsonRequest, back1} from "./utils/api";
import myData from './utils/api';

Vue.config.productionTip = false
Vue.config.devtools = true

Vue.prototype.$axios = axios
Vue.prototype.$qs = qs
Vue.prototype.$postRequest = postRequest
Vue.prototype.$postJsonRequest = postJsonRequest
Vue.prototype.$getRequest = getRequest
Vue.prototype.$setDate = setDate
Vue.prototype.$messageBox = MessageBox
Vue.prototype.$myData = myData
Vue.prototype.$ = jquery
Vue.prototype.$dialog = Dialog
Vue.prototype.$toast = Toast
Vue.prototype.$back1 = back1

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})

import Vue from 'vue'
import Router from 'vue-router'
import MealPage from '../components/MealPage'
import MealDesc from '../components/MealDesc'
import MealOrder from '../components/Cart'
import MainPage from "../components/MainPage";
import Account from "../components/Account";
import PaidMeal from "../components/PaidMeal";
import Login from "../components/Login"
import Evaluation from '../components/Evaluation'
import SearchMeal from '../components/SearchMeal'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: '首页',
      component: MealPage,
    },
    {
      path: '/Login',
      name: 'Login',
      component: Login,
    },
    {
      path: '/MealPage',
      name: 'MealPage',
      component: MealPage,
    },
    {
      path: '/Meal/MealDesc',
      name: '菜品详情',
      component: MealDesc,
    },
    {
      path: '/MealOrder',
      name: '点餐',
      component: MealOrder,
    },
    {
      path: '/Account',
      name: '我的',
      component: Account,
    },
    {
      path: '/Account/PaidMeal',
      name: '我的订单',
      component: PaidMeal,
    },
    {
      path: '/Evaluation',
      name: 'Evaluation',
      component: Evaluation,
    },
    {
      path: '/SearchMeal',
      name: 'SearchMeal',
      component: SearchMeal,
    },
  ]
})

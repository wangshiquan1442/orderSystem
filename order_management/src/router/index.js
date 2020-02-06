import Vue from 'vue'
import Router from 'vue-router'
import MealPage from '../components/MealPage'
import ReservePage from '../components/ReservePage'
import BillPage from '../components/BillPage'
import OrderPage from "../components/OrderPage"
import RevenuePage from "../components/RevenuePage";
import Discount from "../components/Discount";
import UserPage from "../components/UserPage"
import Evaluation from "../components/Evaluation"
import Login from "../components/Login"

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Login',
      component: Login
    },
    {
      path: '/Login',
      name: 'Login',
      component: Login
    },
    {
      path: '/MealPage',
      name: 'MealPage',
      component: MealPage
    },
    {
      path: '/ReservePage',
      name: 'ReservePage',
      component: ReservePage
    },
    {
      path: '/BillPage',
      name: '账单',
      component: BillPage
    },
    {
      path: '/OrderPage',
      name: '订单',
      component: OrderPage,
    },
    {
      path: '/RevenuePage',
      name: '营收',
      component: RevenuePage,
    },
    {
      path: '/Discount',
      name: '折扣',
      component: Discount,
    },
    {
      path: '/UserPage',
      name: '用户',
      component: UserPage,
    },
    {
      path: '/Evaluation',
      name: '评论',
      component: Evaluation,
    }
  ]
})

<template>
  <div>
    <!-- 内容区 -->
    <router-view/>
    <!-- 底部导航栏 -->
    <van-tabbar v-model="active" route replace>
      <van-tabbar-item icon="home-o" to="/MealPage">首页</van-tabbar-item>
      <van-tabbar-item icon="shopping-cart-o" to="/MealOrder" v-if="info <= 0">订单</van-tabbar-item>
      <van-tabbar-item icon="shopping-cart-o" :info="info" to="/MealOrder" v-if="info > 0">订单</van-tabbar-item>
      <van-tabbar-item icon="user-o" to="/Account">我的</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<script>
  import myData from "../utils/api"

  export default {
    name: "MainPage",
    data() {
      return {
        active: 0,
        info: '',
      }
    },
    created() {
      if(this.$store.state.searchShow == '') {
        this.$store.commit('setSearchShow', false)
      }else {
        this.$store.commit('setSearchShow', true)
      }
      const toast = this.$toast.loading({
        duration: 0, // 持续展示 toast
        forbidClick: true,
        message: '加载中'
      });
      let second = 5;
      const timer = setInterval(() => {
        second--;
        if (second) {
          // toast.message = `倒计时 ${second} 秒`;
          toast.message = `加载中`
        } else {
          clearInterval(timer);
          // 手动清除 Toast
          this.$toast.clear();
        }
      }, 1000);

      //被调用方
      var _this = this
      myData.myVue.$on('setInfo', function () {
        _this.setInfo();
      })

      if(myData.unpaidOrderList.length != 0) {
        myData.unpaidOrderList.length = 0
      }
      this.selectUnpaidOrderByUid()
      this.getUser()
    },
    methods: {
      // toMealPage() {
      //     this.$router.push({path: '/MealPage'})
      // },
      // toMealOrder() {
      //     this.$router.push({path: '/MealOrder'})
      // },
      getUser() {
        var _this = this
        const url = myData.url.baseUrl + '/User/selectUserById'
        let params = {"id": 1}
        _this.$postRequest(url, params).then(res => {
          myData.user.uid = res.data.id
          myData.user.name = res.data.name
          myData.user.credit = res.data.credit
          this.$toast.clear();
        })
      },
      selectUnpaidOrderByUid() {
        const _this = this
        let params = {"userId": myData.user.uid}
        let _url = myData.url.baseUrl + '/Order/selectUnpaidOrderByUid'
        _this.$postRequest(_url, params).then(res => {
          for (let i = 0; i < res.data.length; i++) {
            myData.unpaidOrderList.push(
              {
                "userId": myData.user.uid,
                "mealId": res.data[i].mealId,
                "mealName": res.data[i].mealName,
                "quantity": res.data[i].quantity,
                "price": res.data[i].price,
              }
            );
          }
          _this.$store.commit('setQuantity', myData.unsubOrderList.length)
          _this.setInfo()
        })
      },
      setInfo() {
        this.info = this.$store.state.quantity
      }
    },
  }
</script>

<style scoped>

</style>

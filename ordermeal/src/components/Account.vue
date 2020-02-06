<template>
  <div class="account">
    <div class="avatar">
      <img src="../../static/img/meal.jpg">
      <div class="desc">
        <span class="name">{{ user.name }}</span>
        <span class="credit">积分：{{ user.credit }}</span>
      </div>
    </div>

    <div class="order">
      <div class="tab">
        <span class="tab-text">未结算订单</span>
        <div class="tab-content">
          <div v-for="(order, index) in unpaidOrderList" :key="index" class="tab-content-row1">
            <p>{{ order.mealName }}&nbsp;&nbsp;x{{ order.quantity }}</p>
            <p style="color: #FF4400;">￥{{ order.price }}</p>
          </div>
          <van-button round block size="small" color="#FF8D00" style="margin-top: 5px;" @click="payment" v-show="unpaidOrderList.length > 0">结算(￥{{ totalPrice
            }})
          </van-button>
        </div>
        <div class="non-tab-content" v-show="unpaidOrderList.length == 0"><p>暂无订单</p></div>
      </div>
    </div>

    <router-link class="order" to="/Account/PaidMeal">
      <div class="tab">
        <span class="tab-text">已结算订单</span>
        <div class="tab-img">
          <van-image v-for="i in 4" :key="i" class="meal-img"
            width="60"
            height="60"
            lazy-load
            fit="cover"
            :src="mealImg[i]"
          />
        </div>
      </div>
    </router-link>

    <!--    <div class="discounts">-->
    <!--      <div class="tab">-->
    <!--        <span class="tab-text">优惠提醒</span>-->
    <!--      </div>-->
    <!--    </div>-->

    <div class="recommend">
      <div class="tab">
        <span class="tab-text">推荐</span>
        <div class="meal-list">
          <MealPanel :add-btn="false" :mealList="recommendList"></MealPanel>
        </div>
      </div>
    </div>

    <!-- 在这里引入MainPage组件 -->
    <MainPage></MainPage>
  </div>
</template>

<script>
  import MainPage from "./MainPage"
  import myData from "../utils/api"
  import MealPanel from "./MealPanel";
  import {Button} from 'mint-ui'
  import Vue from 'vue'

  Vue.component(Button.name, Button)

  export default {
    name: "Account",
    components: {
      MainPage,
      MealPanel
    },
    data() {
      return {
        unpaidOrderList: myData.unpaidOrderList,
        user: {
          uid: myData.user.uid,
          name: myData.user.name,
          credit: myData.user.credit,
        },
        mealImg: [],
        totalPrice: 0,
        paidOrderList: myData.paidOrderList,
        recommendList: []
      }
    },
    created() {
      this.selectPaidOrderByUid()
      this.calTotalPrice()
      this.randomList()
    },
    methods: {
      calTotalPrice() {
        this.totalPrice = 0
        for (let order of this.unpaidOrderList) {
          this.totalPrice += (order.price * order.quantity)
        }
      },
      payment() {
        const _this = this
        const url = myData.url.baseUrl + '/Order/Settlement'
        let mealIdList = []
        for (let order of _this.unpaidOrderList) {
          mealIdList.push(order.mealId)
        }
        // console.log(mealIdList);
        const params = {
          'mealIdList[]': mealIdList,
          'userId': myData.user.uid
        }
        _this.$postRequest(url, params).then(res => {
          if (res.data) {
            _this.unpaidOrderList = myData.unpaidOrderList = []
          }
        })
      },
      selectPaidOrderByUid() {
        myData.paidOrderList = []
        var _this = this
        const url = myData.url.baseUrl + '/Order/orderGroupByRef'
        let params = {
          userId: myData.user.uid,
        }
        _this.$postRequest(url, params).then(res => {
          myData.paidOrderList = res.data
          _this.setMealImg()
        })
      },
      setMealImg() {
        let status = true
        for(let order of myData.paidOrderList) {
          for(let meal of order.meal) {
            this.mealImg.push(myData.url.mealImgUrl +meal.mealId + '.png')
            if(this.mealImg.length > 3) {
              status = false
              break
            }
          }
          if(status == false) {
            break
          }
        }
      },
      randomList() {
        let randomList = []
        let similar = false
        for(let i = 0; i < 3; i++) {
          let x = Math.floor(Math.random()*myData.mealList.length)
          if(i > 0) {
            for(let j = 0; j < randomList.length; j++) {
              if(randomList[j] == x) {
                i--
                similar = true
                break
              }else {
                similar = false
              }
            }
            similar == false ? randomList.push(x) : ''
          }else {
            //初次插入
            randomList.push(x)
          }
        }
        // console.log(randomList);
        for(let random of randomList) {
          this.recommendList.push(myData.mealList[random])
        }
      }
    },
    watch: {
      unpaidOrderList() {
        this.calTotalPrice()
      },
      paidOrderList() {
        this.setMealImg()
      }
    }
  }
</script>

<style scoped>
  html {
    background-color: #F9F9F9;
  }

  .account {
    width: 100%;
    height: 100%;
    position: absolute;
    background-color: #F9F9F9;
    padding-bottom: 70px;
    margin-bottom: 70px;
  }

  .avatar {
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    background-color: #F9F9F9;
    margin: 40px 10% 5% 10%;
  }

  .avatar img {
    width: 60px;
    height: 60px;
    border-radius: 50%;
    margin-right: 15px;
  }

  .desc {
    display: flex;
    flex-direction: column;
    align-self: center;
    text-align: left;
  }

  .name {
    font-size: 16px;
    font-weight: bold;
    margin-bottom: 5px;
  }

  .credit {
    font-size: 14px;
  }

  .order {
    background: white;
    height: auto;
    min-height: 10%;
    margin: 5% 5% 5% 5%;
    padding: 12px;
    border-radius: 15px;
    display: flex;
    flex-direction: column;
    box-shadow: 0px 0px 8px 0px #00000008;
  }

  .tab {
    display: flex;
    flex-direction: column;
  }

  .tab-text {
    font-size: 12px;
    color: #00000099;
    /*font-weight: bold;*/
    float: left;
    margin-bottom: 5px;
    padding-bottom: 5px;
    border-bottom: 0.5px solid #00000022;
  }

  .tab-content {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }

  .tab-content p {
    margin: 2px 0px 2px 0px;
    font-size: 12px;
  }

  .tab-content-row1 {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  }

  .non-tab-content {
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    margin: 10px;
  }

  .non-tab-content p {
    color: #00000055;
    font-size: 14px;
    margin: auto;
  }

  .tab-img {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    margin-top: 5px;
  }

  .meal-img >>> .van-image__img, .meal-img >>> .van-image__error {
    border-radius: 8px;
  }

  .evaluation {
    background: white;
    height: 5%;
    margin: 5% 5% 5% 5%;
    padding: 12px;
    border-radius: 15px;
    display: flex;
    align-items: center;
    box-shadow: 0px 0px 8px 0px #00000008;
  }

  .evaluation span {
    font-size: 14px;
    font-weight: bold;
    float: left;
    margin-bottom: 5px;
  }

  .discounts {
    background: white;
    height: 15%;
    margin: 5% 5% 5% 5%;
    padding: 12px;
    border-radius: 15px;
    display: flex;
    flex-direction: column;
    box-shadow: 0px 0px 8px 0px #00000008;
  }

  .recommend {
    background: white;
    min-height: 10%;
    height: auto;
    margin: 5% 5% 5% 5%;
    padding: 12px;
    border-radius: 15px;
    display: flex;
    flex-direction: column;
    box-shadow: 0px 0px 8px 0px #00000008;
  }
</style>

<template>
  <div class="paid-meal">
    <div class="back" @click="back()">
      <van-icon name="arrow-left"/>
      <span icon="back" class="back-btn">返回</span>
    </div>

    <div class="order-content">
      <div class="mealist-tab" v-for="(order, i) in paidOrderList" :key="i">
        <div style="display: flex; flex-direction: row; justify-content: space-between;">
          <span class="tab-title">{{ order.odate }}</span>
          <router-link :to="{ name:'Evaluation',query:{order: order.meal} }" class="tab-title">评论</router-link>
        </div>
        <span class="tab-border"></span>
        <div class="meal-area">
          <div class="meal-text" v-for="(meal, j) in order.meal" :key="j">
            <div>
              <span class="meal-name">{{ meal.name }}</span>
              <span class="meal-quantity">x{{ meal.quantity }}</span>
            </div>
            <span class="meal-price">￥{{ meal.price }}</span>
          </div>
          <div class="meal-text" style="margin-top: 15px; font-size: 16px">
            <div>
              <span class="meal-name" style="font-size: 16px; font-weight: bold">合计</span>
              <span class="meal-quantity">x{{ order.quantities }}</span>
            </div>
            <span class="meal-price" style="font-size: 16px; font-weight: bold">￥{{ order.prices }}</span>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
  import myData from '../utils/api'

  export default {
    name: "paidMealPage",
    components: {},
    data() {
      return {
        paidOrderList: myData.paidOrderList,
      }
    },
    created() {
    },
    methods: {
      back() {
        this.$back1()
      }
    },
    watch: {}
  }
</script>

<style scoped>
  .paid-meal {
    width: 100%;
    height: 100%;
  }

  .order-content {
    padding-top: 40px;
  }

  .mealist-tab {
    background: white;
    height: 15%;
    margin: 5% 5% 5% 5%;
    padding: 12px;
    border-radius: 15px;
    display: flex;
    flex-direction: column;
    box-shadow: 0px 0px 8px 0px #00000008;
  }

  .tab {
    margin-bottom: 8px;
  }

  .tab-title {
    font-size: 13px;
    margin: 0px 10px 8px 10px;
    align-self: flex-start;
    color: #000000cc;
  }

  .tab-border {
    height: 1.5px;
    background-image: linear-gradient(to right, #00000050 0%, #00000050 50%, transparent 50%);
    background-size: 15px 1px;
    background-repeat: repeat-x;
    margin-bottom: 8px;
  }

  .meal-area >>> .meal-text {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    margin: 5px 10px 5px 10px;
  }

  .meal-area >>> .meal-name {
    font-size: 14px;
    margin-right: 10px;
  }

  .meal-area >>> .meal-quantity {
    font-size: 13px;
    color: #00000055;
  }

  .meal-area >>> .meal-price {
    font-size: 14px;
    color: #FF4400;
  }
</style>

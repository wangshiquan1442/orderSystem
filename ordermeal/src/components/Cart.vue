<template>
  <div class="main-content">
    <div class="header">
      <span>订单</span>
    </div>

    <div class="meal-order">
      <ul class="order">
        <li v-for="(meal, index) in unsubOrderList" :key="index" class="meal-li">
          <div style="display: flex; flex-direction: row">
            <div class="my-checkbox">
              <van-checkbox v-model="checkList[index]"></van-checkbox>
            </div>
            <a class="meal-item">
              <div class="meal-img">
                <van-image width="80" height="80"
                           fit="cover"
                           lazy-load
                           :src="meal.img"
                />
              </div>
              <div class="meal-desc">
                <router-link class="name" :to="{path: 'Meal/MealDesc', query: {id: meal.mealId}}">{{ meal.mealName }}
                </router-link>
                <div @click="stepperValSet(index)">
                  <van-stepper class="my-stepper" v-model="unsubOrderList[index].quantity" input-width="20px"
                               button-size="20px" min="1" max="9" integer/>
                </div>
                <p class="price">￥{{ meal.price }}</p>
              </div>
            </a>
          </div>
          <div class="del">
            <van-icon name="clear" size="22" @click="delUnsubOrder(index)"/>
          </div>
        </li>
      </ul>
    </div>

    <nav class="bottom-tab">
      <van-checkbox class="select-all-check" v-model="selectAll" @click="changeAllCheck()">全选</van-checkbox>
      <div class="tab-right">
        <span id="totalPrice" class="total-price">合计:￥0</span>
        <van-button type="default" color="#FF8D00" size="normal" @click="submitOrder()">提交</van-button>
      </div>
    </nav>

    <MainPage></MainPage>
  </div>
</template>

<script>
  import MainPage from "./MainPage";
  import myData from "../utils/api";

  export default {
    name: "MealOrder",
    components: {MainPage},
    data() {
      return {
        // 屏幕高度
        // screenHeight: document.documentElement.clientHeight,
        checkList: [],
        unpaidOrderList: myData.unpaidOrderList,
        unsubOrderList: myData.unsubOrderList,
        totalPrice: 0,
        selectAll: false,
        loadImg: true,
      };
    },
    created() {
      this.initCheckList()
    },
    methods: {
      initCheckList() {
        for(let i in this.unsubOrderList) {
          this.checkList.push(false)
        }
      },
      setMealImg() {
        for(let i = 0; i < this.unpaidOrderList.length; i++) {
          for(let j = 0; j < myData.mealList.length; j++) {
            if(this.unpaidOrderList[i].mealId == myData.mealList[j].mealId) {
              this.$set(this.unpaidOrderList[i], 'img', myData.mealList[j].img)
            }
          }
        }
      },
      stepperValSet(index) {
        //选中checkbox，改变numbox，重新计算总价
        for (let i in this.checkList) {
          if (this.checkList[i] == true) {
            this.calTotalPrice()
            break
          }
        }
      },
      calTotalPrice() {
        this.totalPrice = 0
        for (let i = 0; i < this.checkList.length; i++) {
          if (this.checkList[i] == true) {
            this.totalPrice += this.unsubOrderList[i].price * this.unsubOrderList[i].quantity
          }
        }
        document.getElementById("totalPrice").innerHTML = "合计:￥" + this.totalPrice
      },
      //提交订单，没有结算
      submitOrder() {
        var _this = this
        let isShow = false
        for (let i = 0; i < _this.checkList.length; i++) {
          if (_this.checkList[i] == true) {
            isShow = true
            break
          }
        }
        if (isShow) {
          myData.unpaidOrderList = []
          for (let i = 0; i < _this.checkList.length; i++) {
            if (_this.checkList[i] == true) {
              myData.unpaidOrderList.push(myData.unsubOrderList[i])
              myData.unsubOrderList.splice(i, 1)
              _this.checkList.splice(i, 1)
              i--
            }
          }
          const url = myData.url.baseUrl + '/Order/insertOrder'
          _this.$postJsonRequest(url, myData.unpaidOrderList).then(res => {
            if (res.data == true) {
              _this.$toast({
                position: 'bottom',
                message: '订单提交成功',
                // icon: 'like-o'
              });
              _this.$store.commit('setQuantity', myData.unsubOrderList.length)
            }
          })
        }else {
          _this.$toast({
            position: 'bottom',
            message: '未选择订单',
            // icon: 'like-o'
          });
        }
        // this.$axios({
        //     url: url,
        //     method:'post',
        //     //发送格式为json
        //     data:JSON.stringify(params),
        //     headers: {
        //        'Content-Type': 'application/json'
        //     }
        // }).then(res => {
        //     console.log(res);
        // })
        // console.log(myData.unpaidOrderList);
        // console.log(myData.unsubOrderList);
        // console.log(_this.checkList);
      },
      payment() {
        var _this = this
        let isShow = false
        for (let i = 0; i < _this.checkList.length; i++) {
          if (_this.checkList[i] == true) {
            isShow = true
            break
          }
        }
        // console.log(isShow);
        if (isShow) {
          let mealIdList = []
          //删除结算完的checkList的记录
          let _checkList = []
          let str = '<div style="display: flex; flex-direction: column; max-height: 400px"">'
          this.$('.van-dialog__content p').remove()
          for (let i = 0; i < this.checkList.length; i++) {
            if (this.checkList[i] == true) {
              mealIdList.push(this.unpaidOrderList[i].mealId)
              _checkList.push(i)
              str += '<div style="display: flex; justify-content: space-between; flex-direction: row;">' +
                '<p style="margin: 5px 0px 5px 0px">' +
                this.unpaidOrderList[i].mealName + '</p>'
              str += '<p style="margin: 5px 0px 5px 0px">x' +
                this.unpaidOrderList[i].quantity +
                '</p></div>'
            }
          }
          str += '</div>'
          //清除message: '内容'
          this.$('.van-dialog__message').html('')
          this.$('.van-dialog__message').prepend(str)

          this.$dialog.confirm({
            title: '确认订单',
            message: '内容'
          }).then(action => {
            if (action == "confirm") {
              var _this = this
              const url = myData.url.baseUrl + '/Order/Settlement'
              const params = {
                'mealIdList[]': mealIdList,
                'userId': myData.user.uid
              }
              _this.$postRequest(url, params).then(res => {
                if (res.data) {
                  let j = 0
                  for (let i = 0; i < _checkList.length; i++) {
                    //unpaidOrderList和checkList的length都缩小i
                    j = _checkList[i] - i
                    _this.unpaidOrderList.splice(j, 1)
                    _this.checkList.splice(j, 1)
                  }
                  _this.getMsgFromBackEnd()
                }
              })
            }
          }).catch(() => {
            // on cancel
          });
        }
      },
      changeAllCheck() {
        for (let i = 0; i < this.checkList.length; i++) {
          this.$set(this.checkList, i, !(this.selectAll))
        }
      },
      delUnsubOrder(index) {
        this.unsubOrderList.splice(index, 1)
        this.checkList.splice(index, 1)
      },
      delUnpaidOrderByMid(index) {
        const _this = this
        const url = myData.url.baseUrl + '/Order/delUnpaidOrderByMid'
        const params = {
          'mealId': _this.unpaidOrderList[index].mealId,
          'userId': myData.user.uid
        }
        _this.$postRequest(url, params).then(res => {
          if(res.data == true) {
            _this.unpaidOrderList.splice(index, 1)
            _this.checkList.splice(index, 1)
            //订单总量
            _this.$store.commit('setQuantity', myData.unsubOrderList.length)
            //调用方
            myData.myVue.$emit('setInfo')
          }
        }).catch(err => {
          console.log(err);
        })
      },
      getMsgFromBackEnd() {
        let socket = new WebSocket("ws" + myData.url.baseUrl.slice(4) + "/WebSocket")
        socket.onopen = function () {
          console.log("Socket已打开")
        }
        socket.onmessage = function (msg) {
          // console.log(msg.data);
        }
        socket.onclose = function () {
          // console.log("Socket已关闭");
        }
        socket.onerror = function () {
          alert("Socket发生了错误");
        }
      }
    },
    watch: {
      checkList() {
        this.calTotalPrice()
        let clength = this.checkList.length - 1
        //没有订单
        if(clength == -1) {
          this.selectAll = false
        }else {
          let sameState = true  //若checkbox由部分未选中变为全部选中，sameState为true

          for (let i = 0; i < clength + 1; i++) {
            //每个checkbox的状态是否相同
            if (i < clength && this.checkList[i] != this.checkList[i + 1]) {
              sameState = false
            }
          }

          if (sameState == true && this.checkList[0] == true && this.selectAll == false) {
            //各个checkbox都选中，全选按钮没有选中
            this.selectAll = true
          } else if (sameState == false && this.selectAll == true) {
            //存在checkbox没选中，全选按钮选中
            this.selectAll = false
          }
        }
      },
      unpaidOrderList: {
        handler(nval, oval) {
          if(this.loadImg == true) {
            this.setMealImg()
            this.loadImg = false
          }
        },
        deep: true
      }
    },
  }
</script>

<style scoped>
  .main-content {
    width: 100%;
    height: 100%;
    position: absolute;
    background-color: #F9F9F9;
  }

  .header {
    width: 100%;
    position: fixed;
    z-index: 99;
    top: 0px;
    display: flex;
    align-items: center;
    background-color: #F9F9F9;
    padding: 20px 0px 15px 0px;
  }

  .header span {
    font-size: 25px;
    font-weight: bold;
    margin: 0 5% 0 5%;
    text-align: left;
  }

  .meal-order {
    width: 90%;
    background: white;
    margin: 70px 5% 110px 5%;
    border-radius: 15px;
    box-shadow: 0px 0px 8px 0px #00000008;
  }

  .meal-item {
    display: flex;
    flex-direction: row;
  }

  .order li {
    margin: 0px 2.5% 0px 2.5%;
    width: 95%;
    text-align: left;
    display: flex;
    padding: 10px 0px 10px 0px;
    border-bottom: 0.5px solid #00000022;
  }

  .my-checkbox {
    display: flex;
    align-items: center;
    margin: 0px 10px 0px 5px;
  }

  .del {
    display: flex;
    align-items: center;
    margin: 0px 5px 0px 0px;
    color: #F63C36cc;
  }

  .meal-img {
    display: flex;
    align-items: center;
    background-color: #FAFAFA;
    width: 80px;
    height: 80px;
  }
  .meal-img >>> img {
    border-radius: 8px;
  }

  .my-stepper {
    margin: 8px 0px 8px 10px;
  }

  .bottom-tab {
    display: flex;
    align-items: center;
    justify-content: space-between;
    position: fixed;
    bottom: 50px;
    background: white;
    /*border-top: 0.5px solid #00000022;*/
    right: 0;
    left: 0;
    height: 45px;
    padding: 0px 15px 0px 15px;
  }

  .bottom-tab >>> .van-button {
    border-radius: 20px;
    width: auto;
    height: 30px;
  }

  .bottom-tab >>> .van-button__text {
    height: 30px;
    display: flex;
    align-items: center;
  }

  .tab-right {
    display: flex;
    align-items: center;
  }

  .name {
    font-size: 15px;
    font-weight: bold;
    color: #000000cc;
    margin: 0px 10px 0px 7px;
  }

  .price {
    color: #FF4400;
    font-size: 15px;
    margin: 0px 10px 0px 7px;
  }

  .select-all-check {
    display: flex;
    align-self: center;
    left: 0px;
    margin-top: 3px;
  }

  .total-price {
    font-size: 15px;
    color: #FF8D00;
    float: right;
    margin: 0px 10px 0px 0px;
  }

  .meal-li {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  }
</style>

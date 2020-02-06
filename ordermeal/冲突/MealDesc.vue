<template>
  <div class="meal-desc">
    <mt-header title="">
      <router-link to="/MealOrder" slot="left">
        <mt-button icon="back" class="back-btn">返回</mt-button>
        <mt-button></mt-button>
      </router-link>
    </mt-header>

    <van-image class="meal-img"
               fit="contain"
               lazy-load
               src="../../static/img/meal.jpg"
    />
    <!--    <img class="meal-img" src="../../static/img/meal.jpg"/>-->

    <div class="meal-content">
      <div class="m-row-1">
        <span class="price">￥{{ meal.price }}</span>
        <span class="m-credit">下单可获得5积分</span>
      </div>
      <div class="m-row-2">
        <span class="m-name">{{ meal.name }}</span>
        <div class="category"><span>{{ meal.category }}</span></div>
      </div>
      <div class="m-row-3"><span class="discounts">感恩节可享8折优惠！</span></div>
      <div class="desc">
        <span class="title">介绍：</span>
        <span class="d-content" id="desc">{{ meal.desc }}</span>
      </div>
    </div>

    <div class="credit">
      <span class="c-title">评价</span>
      <div class="c-content" v-for="(eva, index) in evaluation" :key="index">
        <span class="c-name">{{ eva.uname }}</span>
        <div class="c-row-1">
          <div :id="setStarId(index)" class="s-color"></div>
          <span class="c-date">{{ eva.edate }}</span>
        </div>
        <span class="c-row-2">{{ eva.desc }}</span>
      </div>
    </div>

  </div>
</template>

<script>
  import myData from '../utils/api'

  export default {
    name: "MealDesc",
    data() {
      return {
        mealId: '',
        meal: {
          'name': '',
          'price': '',
          'category': '',
          'desc': '',
        },
        evaluation: [],
        nextRequest: {
          'getMeal': false,
          'getEva': false,
        },
        discountList: []
      }
    },
    mounted() {
      this.mealId = this.$route.query.id
      if (this.mealId != '') {
        this.selectMealById()
        // this.selectPreferenceById()
      }
      this.getPreference()
    },
    methods: {
      getPreference() {
        let socket = new WebSocket("ws" + myData.url.baseUrl.slice(4) + "/WebSocket")
        socket.onopen = function () {
          // console.log("Socket已打开")
        }
        socket.onmessage = function (msg) {
          const regex = /([a-z]*)([0-9]*)/gm
          let preference = []
          preference = regex.exec(msg.data)
          console.log(preference);
        }
        socket.onclose = function () {
          // console.log("Socket已关闭");
        }
        socket.onerror = function () {
          // alert("Socket发生了错误");
        }
      },
      setStarId(index) {
        return 'star' + index
      },
      selectPreferenceById() {
        const _this = this
        let params = {"id": _this.mealId}
        const url = myData.url.baseUrl  + '/Preference/selectPreferenceById/'
        _this.$postRequest(url, params).then(res => {
          switch (params.type) {
            case 'discount':
              _this.discountList.push({

              })
              break
          }
        })
      },
      selectMealById() {
        var _this = this
        const url = myData.url.baseUrl + '/Meal/selectMealById'
        let params = {"id": _this.mealId}
        _this.$postRequest(url, params).then(res => {
          _this.meal.name = res.data.name,
            _this.meal.price = res.data.price,
            _this.meal.category = res.data.category,
            _this.meal.desc = res.data.desc
          if (_this.meal.desc == '') {
            _this.$set(_this.meal, 'desc', '暂无')
          }
          _this.nextRequest.getMeal = true
        })

        if (_this.meal.desc == '') {
          _this.$set(_this.meal, 'desc', '暂无')
        }
      },
      selectEvaByMealId() {
        var _this = this
        const url = myData.url.baseUrl + '/Evaluation/selectEvaByMealId'
        let params = {"mealId": _this.mealId}
        let _edate = ''
        _this.$postRequest(url, params).then(res => {
          for (let i = 0; i < res.data.length; i++) {
            _edate = new Date(+new Date(res.data[i].edate) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
            _edate = _this.$setDate(_edate)
            _this.evaluation.push({
              "uid": res.data[i].userId,
              "desc": res.data[i].desc,
              "star": res.data[i].star,
              "edate": _edate,
            })
          }
          _this.nextRequest.getEva = true
        })
      },
      selectUserByIds() {
        var _this = this
        const url = myData.url.baseUrl + '/User/selectUserByIds'
        let ids = []
        for (let i = 0; i < _this.evaluation.length; i++) {
          ids[i] = _this.evaluation[i].uid
        }
        let params = {'ids[]': ids}
        _this.$postRequest(url, params).then(res => {
          for (let i = 0; i < res.data.length; i++) {
            _this.$set(_this.evaluation[i], "uname", res.data[i])
            this.setStar(i)
          }
        })
      },
      setStar(index) {
        setTimeout(() => {
          let str = ''
          for (let i = 0; i < this.evaluation[index].star; i++) {
            str += '<i class="van-icon van-icon-star"></i>'
          }
          for (let i = 0; i < 5 - this.evaluation[index].star; i++) {
            str += '<i class="van-icon van-icon-star-o"></i>'
          }
          // console.log(str);
          this.$('#' + 'star' + index).append(str)
        }, 100)
      }
    },
    watch: {
      nextRequest: {
        handler(nval, oval) {

        },
        deep: true
      },
      'nextRequest.getMeal'() {
        this.selectEvaByMealId()
      },
      'nextRequest.getEva'() {
        this.selectUserByIds()
      }
    }
  }
</script>

<style scoped>
  .meal-desc {
    margin-top: 20px;
  }

  .mint-header {
    margin-left: 10px
  }

  .mintui-back {
    color: black;
  }

  .mint-header {
    background-color: #f9f9f9;
  }

  .back-btn {
    color: #000000cc;
  }

  .meal-img {
    margin: 4% 5% 0px 5%;
    width: 90%;
    height: 35%;
    border-radius: 15px;
    box-shadow: 0px 0px 8px 0px #00000008;
  }

  .meal-img >>> .van-image__img {
    border-radius: 15px !important;
  }

  .meal-content {
    display: flex;
    flex-direction: column;
    width: 90%;
    margin-left: 5%;
  }

  .m-row-1 {
    display: flex;
    justify-content: space-between;
    margin-top: 4%;
  }

  .price {
    font-size: 25px;
    color: #FF4400;
  }

  .m-credit {
    font-size: 13px;
    color: #000000cc;
  }

  .m-row-2 {
    display: flex;
    justify-content: flex-start;
    margin-top: 4%;
  }

  .m-name {
    font-size: 18px;
    font-weight: bold;
    color: #000000cc;
  }

  .category {
    display: flex;
    align-self: center;
    background-color: #00000015;
    border-radius: 5px;
    height: 15px;
    margin-left: 3%;
    padding: 5px 8px 5px 8px;
  }

  .category span {
    font-size: 12px;
    color: #000000cc;
    text-align: center;
    align-self: center;
  }

  .m-row-3 {
    background-color: #FAD17E;
    margin-top: 4%;
    height: 40px;
    border-radius: 8px;
    box-shadow: 0px 0px 8px 0px #00000008;
    display: flex;
    align-items: center;
    padding: 0px 10px 0px 10px;
  }

  .discounts {
    font-size: 13px;
    color: #000000cc;
  }

  .desc {
    display: flex;
    justify-content: flex-start;
    flex-direction: column;
    margin-top: 4%;
    font-size: 14px;
    color: #000000cc;
  }

  .title {
    font-weight: bold;
    text-align: left;
    margin: 0px 0px 5px 0px;
    font-size: 15px;
  }

  .d-content {
    text-align: left;
    font-size: 14px;
  }

  .credit {
    margin-top: 4%;
    margin-left: 5%;
    width: 90%;
    font-size: 13px;
  }

  .c-title {
    font-size: 15px;
    font-weight: bold;
    display: flex;
    justify-content: flex-start;
    margin-bottom: 4%;
  }

  .c-content {
    background-color: #ffffff;
    margin-bottom: 3%;
    border-radius: 8px;
    box-shadow: 0px 0px 8px 0px #00000008;
    padding: 10px 10px 10px 10px;
  }

  .c-name {
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
  }

  .c-row-1 {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    margin: 3px 0px 3px 0px;
  }

  .s-color >>> .van-icon-star-o:before {
    color: orange !important;
    margin: 0px 1px 0px 1px;
  }

  .s-color >>> .van-icon-star:before {
    color: orange !important;
    margin: 0px 1px 0px 1px;
  }

  .c-date {
    font-size: 12px;
    color: #00000050;
  }

  .c-row-2 {
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
  }

</style>

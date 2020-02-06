<template>
  <div>
    <!-- 搜索 -->
    <div class="search-tabber">
      <van-icon name="arrow-left" class="back-icon" @click="back"/>
      <van-search
        v-model.trim="searchData" clearable
        placeholder="请输出菜品"
        show-action
        shape="round"
        @search="searchMeal"
        class="m-search"
      >
        <div slot="action" @click="searchMeal">{{ sText }}</div>
      </van-search>
    </div>
    <div class="search-item">
      <MealPanel :mealList="mealList" :addBtn="true" @addToUnsubOrder='addToUnsubOrder'></MealPanel>
    </div>
    <!-- 内容区 -->
    <router-view/>
  </div>
</template>

<script>
  import myData from '../utils/api'
  import MealPanel from '../components/MealPanel'

  export default {
    name: "SearchMeal",
    components: {
      MealPanel
    },
    data() {
      return {
        searchData: '',
        mealList: [],
        sText: '搜索',
      }
    },
    methods: {
      addToUnsubOrder(meal) {
        //调用方
        myData.myVue.$emit('addToUnsubOrder', meal)
      },
      searchMeal() {
        var _this = this
        //进入搜索界面
        if (_this.searchData != '') {
          let params = {"name": _this.searchData}
          const url = myData.url.baseUrl + '/Meal/searchMeal'
          _this.$postRequest(url, params).then(res => {
            _this.mealList = res.data
            for (let meal of _this.mealList) {
              _this.$set(meal, 'img', myData.url.mealImgUrl + meal.id + '.png')
            }
            // console.log(_this.mealList);
          })
          this.sText = '取消'
        } else if (_this.searchData != '') {
          this.sText = '搜索'
          _this.searchData = ''
          _this.mealList = []
        }
      },
      back() {
        this.$back1();
      }
    }
  }
</script>

<style scoped>
  .search-tabber {
    background-color: white;
    display: flex;
    flex-direction: row;
    align-items: center;
    width: 100%;
    height: 100%;
  }

  .back-icon {
    z-index: 9999;
    width: 5%;
    height: 100%;
    background-color: white;
    margin-left: 10px;
  }

  .search-item {
    margin: 5px 0px 5px 0px;
    padding: 0px 20px 0px 20px;
  }

  li {
    text-align: left;
    display: flex;
    padding: 5px 0px 5px 0px;
    border-bottom: 0.5px solid #00000022;
  }

  ul {
    padding: 5px 15px 5px 15px;
    background: white;
    margin-block-start: 0em;
    margin-block-end: 0em;
  }

  .m-search {
    width: 90%;
  }

  .name {
    font-size: 15px;
    font-weight: bold;
    color: #000000cc;
    margin: 1.5px 10px 1.5px 7px;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .desc {
    font-size: 12px;
    color: #000000aa;
    margin: 1.5px 10px 1.5px 7px;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
  }

  .price {
    color: #FF4400;
    font-size: 15px;
    margin: 1.5px 10px 1.5px 7px;
  }

  .meal-item {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  }

  .meal-a {
    display: flex;
  }

  .meal-img {
    display: flex;
    align-items: center;
    background-color: #FAFAFA;
    width: 40%;
    height: auto;
    border-radius: 10px
  }

  .meal-img >>> .van-image__img {
    border-radius: 10px;
  }

  .meal-desc {
    width: 60%;
    display: flex;
    flex-direction: column;
    align-self: center;
  }

  .add-meal {
    margin: 0px 5px 0px 0px;
    align-self: center;
  }
</style>

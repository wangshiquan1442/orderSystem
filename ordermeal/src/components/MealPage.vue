<template>
  <div>
    <van-search placeholder="搜索菜品" @focus="toSearch"/>
    <van-divider style="margin: 0px"/>
    <!-- 筛选栏 -->
    <div class="tree" v-show="!(this.$store.state.searchShow)">
      <van-tree-select class="my-tree-select"
        :height="setHeight()"
        :items="category"
        :main-active-index.sync="categoryIndex">
        <div slot="content" class="meal-panel">
          <!-- 内容展示 -->
          <MealPanel :mealList="mealByCategory" :addBtn="true" @addToUnsubOrder='addToUnsubOrder'></MealPanel>
        </div>
      </van-tree-select>
    </div>

    <MainPage></MainPage>
  </div>
</template>

<script>
  import MainPage from "./MainPage";
  import SearchMeal from "./SearchMeal"
  import myData from '../utils/api'
  import MealPanel from './MealPanel'
  import {removeDuplication}  from '../utils/api'
  import $ from '../utils/api'

  export default {
    name: 'MealPage',
    // 在这里注册MainPage组件
    components: {
      MainPage,
      SearchMeal,
      MealPanel
    },
    data() {
      return {
        mealImg: [],
        categoryIndex: 0,
        category: [],
        mealByCategory: [],
      }
    },
    created() {
      this.selectAllMeal();
      const _this = this
      //被调用方
      myData.myVue.$on('addToUnsubOrder', function (meal) {
        _this.addToUnsubOrder(meal);
      })
    },
    methods: {
      toSearch() {
        this.$router.push({
          name: 'SearchMeal',
          params: {}
        })
      },
      setHeight() {
        let height = this.$(window).height() - 50
        return height + 'px'
      },
      selectAllMeal() {
        //避免数据重复加载
        myData.mealList.length = 0

        var _this = this
        const url = myData.url.baseUrl + '/Meal/selectAllMeal'
        this.$getRequest(url).then(res => {
          for (let i = 0; i < res.data.length; i++) {
            myData.mealList.push(
              {
                "mealId": res.data[i].id,
                "name": res.data[i].name,
                "desc": res.data[i].desc,
                "category": res.data[i].category,
                "price": res.data[i].price
              }
            );
            _this.category.push({
              text: res.data[i].category
            })
            // console.log(myData.mealList);
          }
          _this.setMealImg()
        })
        // this.$axios.get(url).then(function (res) {
        // })
      },
      setMealImg() {
        const _this = this
        let url = ''
        let imgUrl = []
        _this.$getRequest('/mealImg/meal/').then((res) => {
          const pattern = /[a-z]*[0-9]*\w*.jpg|[a-z]*[0-9]*\w*.png|([a-z]*[0-9]*\w*.jpeg)\1/gm
          imgUrl = res.data.match(pattern)
          removeDuplication(imgUrl, null)
          // console.log(imgUrl);
          for (let i = 0; i < myData.mealList.length; i++) {
            let mealId = myData.mealList[i]['mealId']
            for(let j = 0; j < imgUrl.length; j++) {
              switch(imgUrl[j]) {
                case mealId + '.jpg':
                  url = mealId + '.jpg'
                  break
                case mealId + '.jpeg':
                  url = mealId + '.jpeg'
                  break
                case mealId + '.png':
                  url = mealId + '.png'
                  break
              }
            }
            url == '' ? url = '../../static/img/meal.jpg' : url = 'http://localhost:8090/meal/' + url
            _this.$set(myData.mealList[i], 'img', url)
            url = ''
          }
          removeDuplication(_this.category, 'text')
          _this.showMealByCate()
        }).catch((err) => {
          // console.log(err.response.status)
          // _this.$set(myData.mealList[i], 'img', '../../static/img/meal.jpg')
        })
      },
      showMealByCate() {
        let i = this.categoryIndex
        this.mealByCategory = []
        for (let j = 0; j < myData.mealList.length; j++) {
          if (this.category[i].text == myData.mealList[j].category) {
            this.mealByCategory.push({
              'mealId': myData.mealList[j].mealId,
              'name': myData.mealList[j].name,
              'price': myData.mealList[j].price,
              'desc': myData.mealList[j].desc,
              'img': myData.mealList[j].img,
            })
          }
        }
      },
      addToUnsubOrder(meal) {
        let ordered = -1
        let len = myData.unsubOrderList.length
        for (let i = 0; i < len; i++) {
          if (meal.mealId == myData.unsubOrderList[i].mealId) {
            myData.unsubOrderList[i].quantity = myData.unsubOrderList[i].quantity + 1
            ordered = i
            break
          }
        }
        //第一次点这个菜
        if (ordered == -1) {
          myData.unsubOrderList.push({
            'mealId': meal.mealId,
            'mealName': meal.name,
            'userId': myData.user.uid,
            'quantity': 1,
            'price': meal.price,
            'img': meal.img,
            'paid': 0
          })
        }
        // console.log(myData.unsubOrderList);
        this.$toast({
          position: 'bottom',
          message: '（' + meal.name + '）成功加入订单',
          // icon: 'like-o'
        });
        this.$store.commit('setQuantity', myData.unsubOrderList.length)
        //调用方
        myData.myVue.$emit('setInfo')
      }
    },
    watch: {
      categoryIndex() {
        this.showMealByCate()
      }
    }
  }
</script>

<style scoped>
  .tree {
    height: 100%;
    width: 100%;
  }

  .my-tree-select >>> .van-tree-select__nav-item {
    padding: 12px 12px 12px 12px;
  }

  .my-tree-select >>> .van-tree-select__nav {
    overflow-y: auto;
    background-color: #fafafa;
    -webkit-overflow-scrolling: touch;
    flex: unset;
  }

  .meal-panel {
    padding: 10px 10px 10px 10px;
    background: white;
  }
</style>

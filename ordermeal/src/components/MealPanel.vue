<template>
  <div>
    <!-- 内容展示 -->
    <ul>
      <li v-for="(meal, index) in mealList" :key="index" class="meal-item">
        <a class="meal-a">
          <div class="meal-img">
            <van-image width="80" height="80"
                       fit="cover"
                       lazy-load
                       :src="meal.img"
            />
          </div>
          <div class="meal-desc">
            <router-link class="name" :to="{path: 'Meal/MealDesc', query: {id: meal.mealId}}">{{ meal.name }}
            </router-link>
            <p class="desc">{{ meal.desc }}</p>
            <p class="price">￥{{ meal.price }}</p>
          </div>
        </a>
        <van-icon name="add" color="#1989FA" size="25px" class="add-meal" @click="addToUnsubOrder(index)" v-show="addBtn == true"/>
      </li>
    </ul>
  </div>
</template>

<script>
  import myData from '../utils/api'

  export default {
    name: "MealPanel",
    props: {
      mealList: {
        type: Array,
        default: []
      },
      addBtn: {
        type: Boolean,
        default: false
      },
    },
    data() {
      return {

      }
    },
    created() {
    },
    methods: {
      addToUnsubOrder(i) {
        this.$emit('addToUnsubOrder', this.mealList[i])
      },
    }
  }
</script>

<style scoped>
  li {
    text-align: left;
    display: flex;
    padding: 5px 0px 5px 0px;
    border-bottom: 0.5px solid #00000022;
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
    width: 80px;
    height: 80px;
    border-radius: 10px
  }

  .meal-img >>> img {
    border-radius: 8px;
  }

  .meal-desc {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    width: auto;
    align-self: center;
  }

  .add-meal {
    margin: 0px 5px 0px 0px;
    align-self: center;
  }
</style>

<template>
  <div class="aside-page">
    <div class="aside-left">
      <el-container class="container">
        <el-aside width="150px" style="background-color: rgb(238, 241, 246)">
          <el-menu :router="true" :default-active="setActiveMenu">
            <el-menu-item index="1" route="/ReservePage">
              <i class="el-icon-menu"></i>
              <span slot="title">预约</span>
            </el-menu-item>
            <el-menu-item index="2" route="/MealPage">
              <i class="el-icon-food"></i>
              <span slot="title">菜品</span>
            </el-menu-item>
            <!--            <el-submenu index="3">-->
            <!--              <template slot="title"><i class="el-icon-setting"></i>优惠</template>-->
            <!--              <el-menu-item index="3-1" route="/Discount">折扣</el-menu-item>-->
            <!--              <el-menu-item index="3-2" route="/BillPage">满减</el-menu-item>-->
            <!--              <el-menu-item index="3-3" route="/BillPage">红包</el-menu-item>-->
            <!--            </el-submenu>-->
            <el-menu-item index="3" route="/OrderPage">
              <i class="el-icon-edit-outline"></i>
              <span slot="title">订单</span>
            </el-menu-item>
            <el-menu-item index="4" route="/BillPage">
              <i class="el-icon-document-checked"></i>
              <span slot="title">账单</span>
            </el-menu-item>
            <el-menu-item index="5" route="/RevenuePage">
              <i class="el-icon-pie-chart"></i>
              <span slot="title">明细</span>
            </el-menu-item>
            <el-menu-item index="6" route="/UserPage">
              <i class="el-icon-user"></i>
              <span slot="title">用户</span>
            </el-menu-item>
            <el-menu-item index="7" route="/Evaluation">
              <i class="el-icon-chat-dot-square"></i>
              <span slot="title">评论</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
      </el-container>

      <router-view></router-view>
    </div>
  </div>
</template>

<script>
  import myData from "../utils/api"
  import {mapMutations} from 'vuex'

  export default {
    name: "MainPage",
    components: {},
    data() {
      return {

      };
    },
    computed:{
      setActiveMenu() {
        console.log(this.$route.path);
        return this.$route.path;
      }
    },
    methods: {
      ...mapMutations([
        'setSocketMsg',
      ]),
      handleOpen(key, keyPath) {
        console.log(key, keyPath);
      },
      handleClose(key, keyPath) {
        console.log(key, keyPath);
      },
      receiveFromServer() {
        const _this = this
        let socket = new WebSocket("ws" + myData.url.slice(4) + "/WebSocket")
        socket.onopen = function () {
          console.log("Socket已打开")
        }
        socket.onmessage = function (msg) {
          console.log(msg.data);
          if (msg.data == "支付成功") {
            // _this.$store.commit('setSocketMsg', true)
            _this.setSocketMsg()
          }
        }
        socket.onclose = function () {
          // console.log("Socket已关闭");
        }
        socket.onerror = function () {
          alert("Socket发生了错误");
        }
      },
      setSocketMsg() {
        this.$store.dispatch('setSocketMsg')
      },
      selectUserExceptAdmin() {
        const _this = this
        const url = myData.url + '/User/selectUserExceptAdmin'
        _this.$getRequest(url).then(res => {
          myData.userList = res.data
        })
      }
    },
    created() {
      //被调用方
      const _this = this
      myData.myVue.$on('selectUserExceptAdmin', function () {
        _this.selectUserExceptAdmin();
      })
      _this.receiveFromServer()
      _this.selectUserExceptAdmin()
    },
    watch: {}
  }
</script>

<style scoped>
  .aside-left {
    margin-top: 40px;
    height: 100%;
  }

  .el-aside {
    color: #333;
  }

  .el-aside >>> ul {
    height: 100%;
  }

  .el-aside >>> .el-menu {
    position: fixed;
    width: 150px;
  }

  .el-menu-vertical-demo:not(.el-menu--collapse) {
    width: 200px;
    min-height: 400px;
  }
</style>

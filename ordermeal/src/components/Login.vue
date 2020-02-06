<template>
  <div class="main-content">
    <div class="login-panel">
      <p style="font-size: 30px">登录</p>
      <van-field
        v-model="name"
        placeholder="请输入用户名"
        clearable
      />
      <van-field
        v-model="passwd"
        placeholder="请输入密码"
        clearable
        type="password"
      />
      <van-button round type="info" @click="userLogin" style="margin-top: 20px">下一步</van-button>
    </div>
    <van-tag round color="#E9F0FE" text-color="#000000cc" size="large" class="my-tag">餐桌号：{{ tid == '' ? '未预定' : tid }}</van-tag>
  </div>
</template>

<script>
  import { Field } from 'vant';
  import myData from '../utils/api'

  export default {
    name: "Login",
    data() {
      return {
        name: '',
        passwd: '',
        tid: ''
      }
    },
    created() {
      this.tid = this.$route.query.tid ? this.$route.query.tid : ''
    },
    methods: {
      nextAction() {
        // console.log(this.tid);
        if(this.tid != '') {
          this.$router.push({
            name: 'MealPage',
            params: {}
          })
        }
      },
      userLogin() {
        const url = myData.url.baseUrl + '/User/userLogin'
        const params = {
          name: this.name,
          passwd: this.passwd,
          type: ''
        }
        // console.log(params);
        const _this = this
        _this.$postRequest(url, params).then(res => {
          if(res.data != null) {
            myData.user.name = res.data.name
            myData.user.credit = res.data.credit
            myData.user.uid = res.data.id
            myData.user.tid = _this.tid
            _this.nextAction()
          }
        })
      }
    }
  }
</script>

<style scoped>
  html {
    width: 100%;
    height: 100%;
    position: absolute;
    background-color: white;
  }

  .main-content {
    position: absolute;
    top: 40%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 100%;
    height: 100%;
    box-sizing: border-box;
    border-radius: 15px;
    /*border: 1.5px solid #DCDFE6;*/
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    background-color: white;
    opacity: 98%;
    z-index: 2;
  }

  .login-panel {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
  }

  van-cell-group {
    width: 100%;
  }

  .login-panel >>> .van-field__control {
    height: 40px;
    font-size: 20px;
    padding-left: 10px;
    border-bottom: 1px solid #00000055;
  }

  .login-panel >>> .van-cell {
    width: 80%;
    margin-left: auto;
    margin-right: auto;
    margin-bottom: 15px;
  }

  .login-panel >>> .van-button {
    width: 40%;
  }

  .login-panel >>> .van-cell:not(:last-child)::after {
    border-bottom: 0px solid #ebedf0;
  }

  .my-tag {
    position: fixed;
    bottom: 10px;
  }
</style>

<template>
  <div class="login-bg">
    <el-image src="../../static/img/loginBg.png" fit="cover" class="login-img" lazy></el-image>
    <div class="main-content">
      <div class="login-panel">
        <p class="title">登录</p>
        <el-form :model="form" status-icon :rules="rules" ref="form" style="width: 300px">
          <el-form-item label="" prop="uname">
            <el-input v-model="form.uname" placeholder="用户名" clearable></el-input>
          </el-form-item>
          <el-form-item label="" prop="pass">
            <el-input type="password" v-model="form.pass" autocomplete="off" placeholder="密码" clearable></el-input>
          </el-form-item>
          <el-form-item v-if="loginState == false">
            <p style="font-size: 16px; color: #F56C6C; margin: 0px">信息错误，登录失败</p>
          </el-form-item>
          <el-form-item style="display: flex; justify-content: center">
            <el-button type="primary" @click="submitForm('form')">下一步</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
  import myData from "../utils/api"

  export default {
    name: "Login",
    data() {
      var checkName = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('姓名不能为空'));
        }else {
          callback();
        }
      };
      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.form.checkPass !== '') {
            this.$refs.form.validateField('checkPass');
          }
          callback();
        }
      };
      var validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.form.pass) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        form: {
          uname: '',
          pass: '',
          checkPass: ''
        },
        loginState: true,
        rules: {
          uname: [
            { validator: checkName, trigger: 'blur' }
          ],
          pass: [
            { validator: validatePass, trigger: 'blur' }
          ],
          checkPass: [
            { validator: validatePass2, trigger: 'blur' }
          ],
        }
      }
    },
    methods: {
      submitForm() {
        const _this = this
        const url = myData.url + '/User/userLogin'
        const params = {
          name: _this.form.uname,
          passwd: _this.form.pass,
          type: 0
        }
        _this.$postRequest(url, params).then(res => {
          // console.log(res.data);
          if(res.data != '') {
            myData.user = res.data
            console.log(res.data);
            _this.$router.push({
              name: 'ReservePage',
              // params: {
              //   id: res.data.id,
              //   name: res.data.name
              // }
            })
          }else {
            _this.loginState = false
          }
        })
      },
    },
    created() {
    }
  }
</script>

<style scoped>
  .login-bg {
    /*background: url("../../static/img/loginBg.png");*/
    position: absolute;
    width: 100%;
    height: 100%;
  }

  .login-img {
    /*filter: blur(10px);*/
    width: 100%;
    height: 100%;
  }

  .login-img::before {
    content: "";
    position: absolute;
    left: 0;
    right: 0;
    bottom: 0;
    top: 0;
    background-color: rgba(0, 0, 0, .5);
    z-index: 2;
  }

  .title {
    font-size: 30px;
    margin-bottom: 30px;
  }

  .main-content {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 400px;
    height: 400px;
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
    width: 100%;
    border-radius: 10px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }
</style>

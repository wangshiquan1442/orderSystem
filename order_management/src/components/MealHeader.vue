<template>
  <div>
    <el-header class="my-header" style="height: 40px;">
      <el-dropdown>
        <i class="el-icon-setting" style="margin-right: 5px; color: white; font-size: 18px;"></i>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item>注销</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
      <span style="color: white; font-weight: bold">{{ name }}</span>
    </el-header>
    <el-divider></el-divider>
  </div>
</template>

<script>
  import myData from '../utils/api'

  export default {
    name: "MealHeader",
    data() {
      return {
        name: myData.user.name
      }
    },
    methods: {

    }
  }
</script>

<style scoped>
  .my-header {
    height: 40px;
    width: 100%;
    position: fixed;
    background-color: #409EFF;
    color: white;
    z-index: 999;
    display: flex;
    flex-direction: row;
    justify-content: flex-end;
    align-items: center;
  }

  .el-divider--horizontal {
    display: block;
    height: 1px;
    width: 100%;
    margin: 0px;
  }
</style>

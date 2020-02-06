<template>
  <div>
    <MealHeader></MealHeader>

    <div class="discount-page">
      <MainAside></MainAside>

      <div class="left-content">
        <el-main style="padding: 0px 20px 0px 20px">
          <el-table :data="searchState == false ? discountList : resultList" highlight-current-row
                    @current-change="currentChange" tooltip-effect="dark"
                    @selection-change="selectionChange" ref="multipleTable"
                    :default-sort="{prop: 'id', order: 'descending'}" lazy :load="getMoreInf">
            <el-table-column type="expand">
              <template slot-scope="props">
                <el-form label-position="left" inline class="demo-table-expand">
                  <el-form-item label="商品名称">
                    <span>{{ props.row.name }}</span>
                  </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <!--索引列-->
            <el-table-column type="index" width="50"></el-table-column>
            <el-table-column prop="desc" label="描述"></el-table-column>
            <el-table-column prop="mealIdList" label="适用菜品的ID"></el-table-column>
            <el-table-column prop="val" label="折扣"></el-table-column>
            <el-table-column prop="sdate" label="开始日期" sortable></el-table-column>
            <el-table-column prop="edate" label="结束日期" sortable></el-table-column>
            <el-table-column label="操作" width="180">
              <template slot="header" slot-scope="scope">
                <el-button round size="small" class="add-meal" @click="dialogShow('add')">新增折扣</el-button>
              </template>
              <template slot-scope="scope">
                <el-button type="primary" icon="el-icon-edit" circle @click="dialogShow('update')"></el-button>
                <el-button type="danger" icon="el-icon-delete" circle @click="delDiscount"></el-button>
                <el-button type="success" icon="el-icon-upload2" circle @click="pushToUsers"></el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-main>
      </div>
    </div>
  </div>
</template>

<script>
  import MealHeader from "./MealHeader";
  import MainAside from "./MainAside";
  import myData from "../utils/api"

  export default {
    name: "discount",
    components: {
      MealHeader,
      MainAside
    },
    data() {
      return {
        searchState: false,
        resultList: [],
        discountList: [],
        form: {
          id: '',
          desc: '',
          mealIdList: '',
          category: '',
          val: '',
          sdate: '',
          edate: '',
        },
        multipleSelection: '',
      }
    },
    mounted() {
      this.selectDiscountList()
    },
    methods: {
      selectDiscountList() {
        const _this = this
        const url = myData.url + '/Preference/selectDiscount'
        _this.$getRequest(url).then(res => {
          for(let i = 0; i < res.data.length; i++) {
            _this.discountList.push({
              id: res.data[i].id,
              desc: res.data[i].desc,
              mealIdList: res.data[i].mealIdList,
              val: res.data[i].val,
              sdate: res.data[i].sdate,
              edate: res.data[i].edate,
            })
          }
        })
      },
      currentChange(val) {
        //单选
        this.form = val
      },
      selectionChange(val) {
        //多选
        this.multipleSelection = val;
      },
      getMoreInf() {

      },
      dialogShow() {

      },
      delDiscount() {

      },
      pushToUsers() {
        const _this = this
        const url = myData.url + '/Preference/sendToUsers'
        const params = {
          msg: 'discount' + '' + this.form.id
        }
        console.log(params);
        _this.$postRequest(url, params).then(res => {

        })
      }
    }
  }
</script>

<style scoped>
  .discount-page {
    display: flex;
    justify-content: space-between;
  }

  .left-content {
    width: 100%;
    display: flex;
    flex-direction: column;
  }
</style>

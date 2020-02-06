<template>
  <div>

    <MealHeader></MealHeader>

    <div class="main-page">
      <MainAside></MainAside>

      <div class="main-content">
        <!-- 筛选 -->
        <div class="screen">
          <div class="screen-item">
<!--            <p>用户名：</p>-->
            <el-select v-model="screenData.userId" filterable placeholder="请选择用户名" size="mini" clearable>
              <el-option
                v-for="(user, index) in userList"
                :key="index"
                :label="user.name"
                :value="user.id">
              </el-option>
            </el-select>
          </div>
          <div class="screen-item">
            <i class="el-icon-circle-plus input-switch" v-if="inputSwitch == false" @click="inputSwitchChange"/>
            <i class="el-icon-remove input-switch" v-if="inputSwitch == true" @click="inputSwitchChange"/>
            <p>消费金额：</p>
            <el-input-number v-model="screenData.minPrices" :step="10" controls-position="right" :min="0" :max="9999" size="mini" :disabled="!inputSwitch"/>
            &nbsp;~&nbsp;
            <el-input-number v-model="screenData.maxPrices" :step="10" controls-position="right" :min="0" :max="9999" size="mini" :disabled="!inputSwitch"/>
          </div>
          <div class="screen-item">
<!--            <p>日期：</p>-->
            <el-date-picker
              v-model="screenData.dateVal"
              type="daterange"
              align="right"
              unlink-panels
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :picker-options="dateOptions" size="mini"
              format="yyyy 年 MM 月 dd 日"
              value-format="yyyy-MM-dd"
              clearable>
            </el-date-picker>
          </div>
          <div class="screen-item">
            <el-input v-model="screenData.ref" placeholder="请输入订单号" size="mini" clearable></el-input>
          </div>
          <div class="screen-item">
            <el-button round style="height: 28px" size="mini" @click="screenBtn">搜索订单</el-button>
            <el-button round style="height: 28px" size="mini" type="danger" @click="cancelScreen" v-if="screenState">
              取消搜索
            </el-button>
          </div>
        </div>
        <el-divider class="my-divider"></el-divider>
        <!-- 表格 -->
        <div class="my-table">
          <el-table :data="orderList" style="width: 98%">
            <el-table-column type="expand">
              <div slot-scope="scope" class="card-div">
                <el-card class="box-card card-margin" v-for="(item, index) in scope.row.meal" :key="index" shadow="hover">
                  <div slot="header" class="clearfix">
                    <el-button type="text">{{ item.name }}</el-button>
                    <!--                  <el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>-->
                  </div>
                  <div class="text item">
                    <p>菜系：{{ item.category }}</p>
                    <p>价钱：{{ item.price }}</p>
                    <p>数量：{{ item.quantity }}</p>
                  </div>
                </el-card>
              </div>
            </el-table-column>
            <el-table-column
              type="index" align="center" header-align="center"
              width="100">
              <template slot="header" slot-scope="scope">
                <el-button round size="small" class="add-meal" @click="dialogFormVisible = true">新增订单</el-button>
              </template>
            </el-table-column>
            <el-table-column
              prop="userName"
              label="用户名"
              width="180">
            </el-table-column>
            <el-table-column
              label="菜品">
              <div slot-scope="scope" class="meal-div">
                <el-tag size="medium" effect="plain" v-for="(item, index) in scope.row.meal" :key="index"
                        class="meal-tag">{{ item.name }}
                </el-tag>
              </div>
            </el-table-column>
            <el-table-column
              prop="prices"
              label="消费金额"
              sortable
              width="100">
            </el-table-column>
            <el-table-column
              prop="quantities"
              sortable
              label="数量"
              width="100">
            </el-table-column>
            <el-table-column
              prop="odate"
              sortable
              label="下单日期"
              width="150">
            </el-table-column>
            <el-table-column
              prop="ref"
              label="订单号"
              width="180">
            </el-table-column>
          </el-table>
          <!-- 分页 -->
          <el-pagination
            class="pagination"
            layout="prev, pager, next, jumper"
            :total="100"
            prev-click="changePage('prev')"
            next-click="changePage('next')"
            @current-change="changePage"
            :current-page.sync="currentPage">
          </el-pagination>
        </div>
      </div>
    </div>

    <!-- 弹窗 -->
    <el-dialog title="新增订单" :visible.sync="dialogFormVisible">
      <el-form class="my-form">
        <el-form-item label="创建人：" label-width="formLabelWidth">
          {{ user.name }}
        </el-form-item>
        <el-form-item label="选择菜品：" label-width="formLabelWidth">
          <el-select v-model="mealIndex" filterable placeholder="请选择" style="margin-right: 10px;" size="small">
            <el-option v-for="(meal, index) in mealList" :key="index" :label="meal.name" :value="index"/>
          </el-select>
          <el-input-number v-model="quantity" controls-position="right" :min="1" :max="999" size="small"/>
        </el-form-item>
        <el-form-item label="已选择的菜品：" label-width="formLabelWidth">
          <el-tag v-for="(meal, index) in mealTags" :key="index" class="dialog-tag"
                  closable @close="delMealTag(index)">
            {{ meal.mealName }} x {{ meal.quantity }}
          </el-tag>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="" @click="addMealToTags">添 加</el-button>
        <el-button @click="cancelDialog">取 消</el-button>
        <el-button type="primary" @click="confirmDialog">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import MealHeader from "./MealHeader";
  import MainAside from "./MainAside";
  import myData from "../utils/api"
  import { mapMutations } from 'vuex'

  export default {
    name: "OrderPage",
    components: {
      MealHeader,
      MainAside,
    },
    data() {
      return {
        orderList: [],
        mealList: myData.mealList,
        currentPage: 1,
        pageSize: 10,
        formLabelWidth: 120,
        dialogFormVisible: false,
        mealIndex: '',
        quantity: 0, //产品数量
        mealTags: [],
        userList: [],
        inputSwitch: false,
        dateOptions: myData.dateOptions,
        screenData: {},
        screenState: false,
        user: myData.user
      }
    },
    created() {
      this.getOrder()
      this.selectAllUser()
      this.resetScreenData()
    },
    methods: {
      ...mapMutations ([
        'setSocketMsg',
        'resetSocketMsg'
      ]),
      selectAllUser() {
        const _this = this
        const url = myData.url + '/User/selectAllUser'
        _this.$getRequest(url).then(res => {
          _this.userList = res.data
        })
      },
      resetScreenData() {
        this.screenData = {
          userId: null,
            minPrices: 0,
            maxPrices: 0,
            ref: null,
            dateVal: null
        }
      },
      getOrder() {
        const _this = this
        const url = myData.url + '/Order/orderGroupByRef'
        const params = {
          currentPage: (_this.currentPage - 1) * _this.pageSize,
          pageSize: _this.pageSize
        }
        _this.$postRequest(url, params).then(res => {
          _this.orderList = []
          _this.orderList = res.data
        })
      },
      changePage(action) {
        switch (action) {
          case 'prev':
            this.currentPage > 0 ? this.currentPage -= 1 : ''
            break
          case 'next':
            if (this.orderList.length % this.pageSize == 0) {
              this.currentPage < this.orderList.length / this.pageSize ? this.currentPage += 1 : ''
            } else {
              this.currentPage < this.orderList.length / this.pageSize + 1 ? this.currentPage += 1 : ''
            }
            break
        }
        // console.log(this.currentPage);
        if(this.screenState == false) {
          this.getOrder()
        }else {
          this.screenBtn()
        }
      },
      resetSocketMsg() {
        this.$store.dispatch('resetSocketMsg')
      },
      addMealToTags() {
        let meal = this.mealList[this.mealIndex]
        this.mealTags.push({
          mealId: meal.id,
          mealName: meal.name,
          price: meal.price,
          quantity: this.quantity,
          userId: myData.user.id,
        })
      },
      delMealTag(i) {
        this.mealTags.splice(i, 1)
      },
      confirmDialog() {
        if(this.mealTags.length > 0) {
          const _this = this
          const url = myData.url + '/Order/addOrder'
          const params = {
            'orderList': _this.mealTags
          }
          _this.$postJsonRequest(url, params).then(res => {
            if(res.data == true) {
              _this.getOrder()
              _this.cancelDialog()
            }
          })
        }
        this.dialogFormVisible = false
      },
      cancelDialog() {
        this.mealTags = []
        this.mealIndex = ''
        this.quantity = 0
        this.dialogFormVisible = false
      },
      screenBtn() {
        let screenData = this.screenData
        let minPrices = this.inputSwitch == false ? null : screenData.minPrices
        let maxPrices = this.inputSwitch == false ? null : screenData.maxPrices
        let userId = this.screenData.userId
        let ref = this.screenData.ref
        let dateVal = this.screenData.dateVal
        if(this.notNull(userId) || this.notNull(ref) || this.notNull(dateVal)) {
          this.screenState = true
          const _this = this
          const url = myData.url + '/Order/screenOrder'
          const params = {
            userId: userId,
            date: dateVal,
            ref: ref,
            //undefined => null
            minPrices: minPrices ? minPrices : null,
            maxPrices: maxPrices ? maxPrices : null,
            currentPage: (_this.currentPage - 1) * _this.pageSize,
            pageSize: _this.pageSize
          }
          console.log(params);
          _this.$postJsonRequest(url, params).then(res => {
            _this.orderList = res.data
          })
        }
      },
      cancelScreen() {
        this.resetScreenData()
        this.screenState = false
        this.getOrder()
      },
      notNull(val) {
        if(val != null) {
          return true
        }
        return false
      },
      inputSwitchChange() {
        this.inputSwitch = !this.inputSwitch
      },
    },
    watch: {
      '$store.state.socketMsg'(nval, oval) {
        // console.log(nval);
        if(nval == true) {
          this.getOrder()
          this.resetSocketMsg()
        }
      }
    }
  }
</script>

<style scoped>
  .input-switch {
    font-size: 20px;
    color: #DCDFE6;
    margin-right: 10px;
  }

  .meal-div {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
  }

  .meal-tag {
    margin: 2px 5px 2px 0px;
  }

  .card-div {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
  }

  .card-margin {
    margin: 2px 5px 2px 0px;
  }

  .pagination {
    margin: 10px 0px 10px 0px;
  }

  .dialog-tag {
    margin: 0px 10px 0px 0px;
  }

  .my-divider {
    margin: 0px;
  }
</style>

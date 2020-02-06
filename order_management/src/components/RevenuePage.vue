<template>
  <div>
    <MealHeader></MealHeader>

    <div class="main-page">
      <MainAside></MainAside>
      <div class="main-content">
        <!-- 筛选栏 -->
        <div class="screen">
          <div class="screen-item">
            <p>日期：</p>
            <el-date-picker
              v-model="dateVal"
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
            <p>类别：</p>
            <el-select v-model="cateVal" filterable placeholder="请选择" size="mini" style="width: 120px;">
              <el-option
                v-for="item in cateList"
                :key="item"
                :label="item"
                :value="item"
              >
              </el-option>
            </el-select>
          </div>
          <div class="screen-item">
            <p>排序：</p>
            <el-select v-model="sortVal" clearable placeholder="请选择" size="mini" style="width: 120px;">
              <el-option v-for="item in sortOptions" :key="item.value" :label="item.label" :value="item.value"/>
            </el-select>
          </div>
          <div class="screen-item">
            <el-button round size="mini" @click="startScreen">开始筛选</el-button>
            <el-button type="danger" round size="mini" @click="clearScreen" v-show="screenState">取消筛选</el-button>
            <el-button id="showTableBtn" round size="mini" @click="showTable">显示图表</el-button>
          </div>
        </div>
        <!-- 表格 -->
        <div class="my-table">
          <el-table :data="tableData" :default-sort = "{prop: 'mealName', order: 'descending'}" v-show="chartShow == false"
            :summary-method="getSummaries" show-summary highlight-current-row border class="my-table">
            <el-table-column align="center"
              type="index"
              width="80">
            </el-table-column>
            <el-table-column
              prop="mealName"
              label="菜名"
              sortable>
            </el-table-column>
            <el-table-column
              prop="category"
              label="类别">
            </el-table-column>
            <el-table-column
              prop="quantities"
              label="销售量">
            </el-table-column>
            <el-table-column
              prop="prices"
              label="营业额">
            </el-table-column>
          </el-table>
        </div>
        <!-- 图表 -->
        <div id="revenueChart" class="my-chart" v-show="tableData.length > 0 && chartShow == true"></div>
      </div>
    </div>
  </div>
</template>

<script>
  import MealHeader from "./MealHeader";
  import MainAside from "./MainAside";
  import myData from "../utils/api";

  export default {
    name: "RevenuePage",
    components: {
      MealHeader,
      MainAside
    },
    data() {
      return {
        dateVal: [],
        dateOptions: myData.dateOptions,
        sortOptions: [
          {
            value: 'turnoverDesc',
            label: '营业额降序'
          }, {
            value: 'turnoverAsc',
            label: '营业额升序'
          }, {
            value: 'salesDesc',
            label: '销售量降序'
          }, {
            value: 'salesAsc',
            label: '销售量升序'
          }
        ],
        sortVal: '',
        screenState: false,
        revenueList: [],
        cateList: [],
        cateVal: [],
        tableData: [],
        chartShow: false,
      }
    },
    methods: {
      selectRevenue() {
        let url = myData.url + '/Bill/selectBillGroupByMealId'
        const _this = this
        let billList = []
        _this.$getRequest(url).then(res => {
          for (let i = 0; i < res.data.length; i++) {
            billList.push({
              mealId: res.data[i].mealId,
              mealName: res.data[i].mealName,
              category: res.data[i].category,
              quantities: res.data[i].quantities,
              prices: res.data[i].prices,
            })
          }
          url = myData.url + '/Meal/selectMealCate'
          _this.$getRequest(url).then(res => {
            for (let cate of res.data) {
              _this.cateList.push(cate.category)
              _this.revenueList.push({
                cate: cate.category,
                mealList: []
              })
            }
            for (let bill of billList) {
              for (let revenue of _this.revenueList) {
                if (bill.category == revenue.cate) {
                  revenue.mealList.push(bill)
                }
              }
            }
            _this.cateVal = _this.cateList[0]
            _this.tableData = _this.revenueList[0].mealList
            _this.$('my-table').height(this.$(window).height() - this.$('.el-header').height() - this.$('.screen').height())
          })
        })
      },
      draw() {
        this.$('#revenueChart').width(this.$('.main-content').width() * 0.98)
        this.$('#revenueChart').height((this.$(window).height() - this.$('.el-header').height() - this.$('.screen').height()) * 0.94)

        var echarts = require('echarts');
        // 基于准备好的dom，初始化echarts实例
        this.myChart = echarts.init(document.getElementById('revenueChart'));
        // 绘制图表
        let quantities = []
        let prices = []
        let xAxisData = []
        for (let i = 0; i < this.tableData.length; i++) {
          xAxisData.push(this.tableData[i].mealName)
          quantities.push(this.tableData[i].quantities)
          prices.push(this.tableData[i].prices)
        }
        this.myChart.setOption({
          toolbox: {
            feature: {
              magicType: {show: true, type: ['line', 'bar']},
              restore: {show: true},
              saveAsImage: {show: true}
            }
          },
          legend: {
            data: ['营业额', '销售量']
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross',
              crossStyle: {
                color: '#999'
              }
            }
          },
          dataZoom: [{
            type: 'inside'
          }, {
            type: 'slider'
          }],
          grid: {
            left: 5,
            right: 5,
            containLabel: true,
            top: 80,
            bottom: 50
          },
          xAxis: {
            show: true,
            type: 'category',
            data: xAxisData,
            axisPointer: {
              type: 'shadow'
            }
          },
          yAxis: [
            {
              type: 'value',
              name: '营业额',
              min: 0,
              interval: 100,
              axisLabel: {
                formatter: '{value} 元'
              }
            },
            {
              type: 'value',
              name: '销售量',
              min: 0,
              interval: 10,
              axisLabel: {
                formatter: '{value} 单'
              }
            }
          ],
          series: [
            {
              name: '营业额',
              type: 'bar',
              data: prices,
              markPoint: {
                data: [
                  {type: 'max', name: '最大值'},
                  {type: 'min', name: '最小值'}
                ]
              },
            },
            {
              name: '销售量',
              type: 'line',
              yAxisIndex: 1,
              data: quantities,
              markPoint: {
                data: [
                  {type: 'max', name: '最大值'},
                  {type: 'min', name: '最小值'}
                ]
              },
            }
          ]
        });
      },
      startScreen() {
        if(this.notNull(this.dateVal) || this.notNull(this.sortVal) || this.notNull(this.cateVal)) {
          const _this = this
          const url = myData.url + '/Revenue/screenRevenue'
          const params = {
            'odate[]': _this.dateVal,
            'cate': _this.cateVal,
            'sort': _this.sortVal,
          }
          _this.$postRequest(url, params).then(res => {
            this.screenState = true
            this.tableData = []
            this.tableData = res.data
          })
        }
      },
      clearScreen() {
        this.screenState = false
        this.dateVal = null
        this.cateVal = this.cateList[0]
        this.sortVal = ''
        this.tableData = this.revenueList[0].mealList
      },
      getSummaries(param) {
        const { columns, data } = param;
        const sums = [];
        columns.forEach((column, index) => {
          if (index === 0) {
            sums[index] = '统计';
            return;
          }
          const values = data.map(item => Number(item[column.property]));
          if (!values.every(value => isNaN(value))) {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr);
              if (!isNaN(value)) {
                return prev + curr;
              } else {
                return prev;
              }
            }, 0);
            sums[index] += '';
          } else {
            sums[index] = '';
          }
        });
        return sums;
      },
      notNull(val) {
        let params = val instanceof Array ? val.length : (val != '' && val != null)
        if(params) {
          return true
        }else {
          return false
        }
      },
      showTable() {
        this.chartShow = !this.chartShow
      }
    },
    created() {
      this.selectRevenue()
    },
    watch: {
      tableData: {
        handler(nval, oval) {
          if(nval.length != 0) {
            this.draw()
          }
        }
      },
      chartShow(nval, oval) {
        if (nval == true) {
          this.$('#showTableBtn').text('显示表格')
        } else {
          this.$('#showTableBtn').text('显示图表')
        }
      },
    }
  }
</script>

<style scoped>

</style>

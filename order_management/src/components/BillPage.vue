<template>
  <div>
    <MealHeader></MealHeader>

    <div class="bill-page">
      <MainAside></MainAside>
      <div class="main-content">
        <!-- 筛选栏 -->
        <div id="screenDiv">
          <div class="screen">
            <div class="specific">
              <p style="width: 45px">具体：</p>
              <el-select v-model="specificVal" multiple placeholder="请选择" filterable clearable size="mini"
                         style="width: 100%;">
                <el-option
                  v-for="(item, index) in specificOptinos"
                  :key="index"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </div>
          </div>
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
              <p>范围：</p>
              <el-select v-model="rangeVal" :disabled="rangeHide" placeholder="请选择" clearable size="mini"
                         style="width: 100px;">
                <el-option
                  v-for="(item, index) in rangeOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </div>
            <div class="screen-item">
              <p>排序：</p>
              <el-select v-model="sortVal" clearable placeholder="请选择" size="mini" style="width: 120px;">
                <el-option
                  v-for="(item, index) in sortOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </div>
            <div class="screen-item">
              <el-button round size="mini" @click="startScreen">开始筛选</el-button>
              <el-button type="danger" round size="mini" @click="clearScreen" v-show="screenState">取消筛选</el-button>
              <el-button id="showTableBtn" round size="mini" @click="showTable">显示图表</el-button>
            </div>
          </div>
        </div>
        <!-- 表格 -->
        <div v-show="chartShow == false" class="bill-table">
          <el-table :data="billList" :summary-method="getSummaries" show-summary :height="tableHeight"
                    highlight-current-row border>
            <el-table-column align="center" type="index" width="80">
            </el-table-column>
            <el-table-column v-if="!(rangeVal == 'category' && screenState == true)" sortable
                             property="mealName"
                             label="菜名">
            </el-table-column>
            <el-table-column sortable
                             property="category"
                             label="菜系">
            </el-table-column>
            <el-table-column
              property="quantities"
              label="销售量">
            </el-table-column>
            <el-table-column
              property="prices"
              label="营业额">
            </el-table-column>
          </el-table>
        </div>
        <!-- 图表 -->
        <div id="billChart" class="my-chart" v-show="billList.length > 0 && chartShow == true"></div>
      </div>
    </div>

  </div>
</template>

<script>
  import MealHeader from "./MealHeader";
  import MainAside from "./MainAside";
  import myData from "../utils/api";
  import { mapMutations } from 'vuex'

  export default {
    name: "BillPage",
    components: {
      MealHeader,
      MainAside
    },
    data() {
      return {
        billList: [],
        currentDate: {
          year: new Date().getFullYear(),
          month: new Date().getMonth() + 1,
          day: new Date().getDate()
        },
        dateOptions: myData.dateOptions,
        rangeOptions: [
          {
            value: 'mealName',
            label: '按菜名'
          }, {
            value: 'category',
            label: '按类别'
          }
        ],
        specificOptinos: [],
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
        dateVal: [],
        rangeVal: '',
        sortVal: '',
        specificVal: [],
        optionVal: '',
        chartShow: false,
        myChart: false,
        rangeHide: false,
        screenState: false,
        tableHeight: 0,
      }
    },
    created() {
      this.getBill()
      this.receiveFromServer()
    },
    mounted() {
    },
    methods: {
      ...mapMutations ([
        'setSocketMsg',
      ]),
      receiveFromServer() {
        const _this = this
        let socket = new WebSocket("ws" + myData.url.slice(4) + "/WebSocket")
        socket.onopen = function () {
          console.log("Socket已打开")
        }
        socket.onmessage = function (msg) {
          console.log(msg.data);
          if(msg.data == "支付成功") {
            // _this.$store.commit('setSocketMsg', true)
            _this.getBill()
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
      getBill() {
        const url = myData.url + '/Bill/selectBillGroupByMealId'
        const _this = this
        _this.$getRequest(url).then(res => {
          for (let i = 0; i < res.data.length; i++) {
            _this.billList.push({
              mealId: res.data[i].mealId,
              mealName: res.data[i].mealName,
              category: res.data[i].category,
              quantities: res.data[i].quantities,
              prices: res.data[i].prices,
            })
          }
          _this.setSpecificOptions()
          _this.tableHeight = this.$(window).height() - this.$('.el-header').height() - this.$('#screenDiv').height()
          // _this.$('bill-table').height(this.$(window).height() - this.$('.el-header').height() - this.$('#screenDiv').height())
        })
      },
      getSummaries(param) {
        const {columns, data} = param;
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
      setSpecificOptions() {
        this.specificOptinos.length = 0
        if (this.rangeVal == 'category') {
          let _billList = []
          for (let i = 0; i < this.billList.length; i++) {
            _billList.push(this.billList[i].category)
          }
          for (let i = 0; i < _billList.length - 1; i++) {
            for (let j = i + 1; j < _billList.length; j++) {
              if (_billList[i] == _billList[j]) {
                _billList.splice(j, 1)
                j--
              }
            }
          }
          for (let i = 0; i < _billList.length; i++) {
            this.specificOptinos.push({
              value: _billList[i],
              label: _billList[i]
            })
          }
        } else {
          for (let i = 0; i < this.billList.length; i++) {
            this.specificOptinos.push({
              value: this.billList[i].mealId,
              label: this.billList[i].mealName
            })
          }
        }
      },
      draw() {
        this.$('#billChart').width(this.$('.main-content').width() * 0.98)
        this.$('#billChart').height((this.$(window).height() - this.$('.el-header').height() - this.$('#screenDiv').height()) * 0.94)

        var echarts = require('echarts');
        // 基于准备好的dom，初始化echarts实例
        this.myChart = echarts.init(document.getElementById('billChart'));
        // 绘制图表
        let quantities = []
        let prices = []
        let xAxisData = []
        if (this.rangeVal == 'mealName' || this.rangeVal == '') {
          for (let i = 0; i < this.billList.length; i++) {
            xAxisData.push(this.billList[i].mealName)
            quantities.push(this.billList[i].quantities)
            prices.push(this.billList[i].prices)
          }
        }
        if (this.rangeVal == 'category') {
          for (let i = 0; i < this.billList.length; i++) {
            xAxisData.push(this.billList[i].category)
            quantities.push(this.billList[i].quantities)
            prices.push(this.billList[i].prices)
          }
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
        if ((this.specificVal != null && this.specificVal.length != 0) ||
          (this.dateVal != null && this.dateVal.length != 0) ||
          this.rangeVal != '' || this.sortVal != '') {
          var _this = this
          _this.screenState = true
          const url = myData.url + '/Bill/screenBill'
          const params = {
            'specific[]': _this.specificVal,
            'odate[]': _this.dateVal,
            'range': _this.rangeVal,
            'sort': _this.sortVal
          }
          this.$postRequest(url, params).then(res => {
            _this.billList.length = 0
            if (res.data.length == 0) {
              _this.billList = []
            }
            if (_this.rangeVal == 'category') {
              for (let i = 0; i < res.data.length; i++) {
                _this.billList.push({
                  category: res.data[i].category,
                  prices: res.data[i].prices,
                  quantities: res.data[i].quantities,
                })
              }
            } else {
              for (let i = 0; i < res.data.length; i++) {
                _this.billList.push({
                  mealId: res.data[i].mealId,
                  mealName: res.data[i].mealName,
                  quantities: res.data[i].quantities,
                  prices: res.data[i].prices,
                })
              }
            }
          })
        }
      },
      clearScreen() {
        this.screenState = false
        this.dateVal = this.specificVal = null
        this.billList.length = 0
        this.rangeVal = this.sortVal = ''
        this.getBill()
      },
      showTable() {
        this.chartShow = !this.chartShow
      }
    },
    watch: {
      billList: {
        handler(nval, oval) {
          if (nval.length != 0) {
            this.draw()
          }
        },
        deep: true
      },
      rangeVal() {
        this.setSpecificOptions()
      },
      specificVal: {
        handler(nval, oval) {
          if (nval != null) {
            if (nval.length == 0) {
              this.rangeHide = false
            } else {
              this.rangeHide = true
            }
          } else {
            this.rangeHide = false
          }
        },
        deep: true
      },
      chartShow(nval, oval) {
        if (nval == true) {
          this.$('#showTableBtn').text('显示表格')
        } else {
          this.$('#showTableBtn').text('显示图表')
        }
      },
    },
  }
</script>

<style scoped>
  .bill-page {
    display: flex;
    justify-content: space-between;
  }

  .specific {
    width: 100%;
    display: flex;
    flex-direction: row;
  }

  .specific p {
    font-size: 14px;
    margin: auto;
    justify-content: center;
  }

  .screen-item button {
    margin: 0px 0px 0px 15px;
  }

  .bill-table {
    width: 98%;
    margin: 0px auto 10px auto;
  }
</style>

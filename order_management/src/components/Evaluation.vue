<template>
  <div>
    <MealHeader></MealHeader>

    <div class="main-page">
      <MainAside></MainAside>

      <div class="main-content">
        <el-table
          :data="tableData"
          style="width: 100%">
          <el-table-column type="expand">
            <div slot-scope="scope" class="eva-card">
              <el-card v-for="(item, index) in scope.row.evaList" :key="index" style="margin-bottom: 5px" shadow="hover">
                <div class="card-item">
                  <div class="card-left">
                    <p class="card-desc">{{ item.desc }}</p>
                    <div class="card-row-1">
                      <p style="margin-right: 20px;">{{ item.name }}</p>
                      <p>{{ item.edate }}</p>
                    </div>
                  </div>
                  <div class="card-right">
                    <p class="card-num">{{ item.star }}</p>
                    <p style="color: #F56C6C88; font-size: 25px; margin: 0px;">分</p>
                  </div>
                </div>
              </el-card>
            </div>
          </el-table-column>
          <el-table-column type="index" width="80" align="center"/>
          <el-table-column
            prop="mealName"
            label="菜品"
            sortable>
          </el-table-column>
          <el-table-column
            prop="category"
            label="菜系"
            sortable>
          </el-table-column>
          <el-table-column
            prop="rate"
            label="评分"
            width="180"
            sortable>
          </el-table-column>
          <el-table-column
            prop="quantity"
            label="评论数目"
            sortable>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script>
  import MealHeader from "./MealHeader";
  import MainAside from "./MainAside";
  import myData from "../utils/api"

  export default {
    name: "Evaluation",
    components: {
      MealHeader,
      MainAside,
    },
    data() {
      return {
        tableData: [],
      }
    },
    created() {
      this.selectGroupByMeal()
    },
    methods: {
      selectGroupByMeal() {
        const _this = this
        const url = myData.url + '/Evaluation/selectGroupByMeal'
        _this.$getRequest(url).then(res => {
          // console.log(res.data);
          _this.tableData = res.data
        })
      },
    }
  }
</script>

<style scoped>
  .card-item {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }

  .card-left {
    display: flex;
    flex-direction: column;
  }

  .card-row-1 {
    display: flex;
    flex-direction: row;
    font-size: 12px;
    color: #00000088;
  }

  .card-row-1 p {
    margin: 0px 0px 5px 0px;
  }

  .card-right {
    display: flex;
    flex-direction: row;
    font-size: 20px;
    align-items: center;
    margin-left: 15px;
  }

  .card-desc {
    font-size: 14px;
    margin: 5px 0px 5px 0px;
  }

  .card-num {
    font-weight: bold;
    font-size: 25px;
    color: #F56C6C;
    margin: 3px;
  }

  .eva-card >>> .el-card__body {
    padding: 5px 10px 5px 10px;
  }
</style>

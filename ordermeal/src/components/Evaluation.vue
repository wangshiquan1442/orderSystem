<template>
  <div class="main-content">
    <div class="back" @click="back">
      <van-icon name="arrow-left" />
      <span icon="back" class="back-btn">返回</span>
    </div>

    <div class="eva-content">
      <div class="eva">
        <div :class="{'evaPanel' : true, 'panel-select': currentEva == index, 'panel-unselect' : currentEva != index}"
             v-for="(meal, index) of mealList" :key="index"
             @click="selectPanel(index)">
          <p>{{ meal.name }}</p>
          <van-rate v-model="rate[index]" void-color="#FFD21E"/>
        </div>
      </div>
      <div class="eva-text">
        <p>{{ '评论（' + mealList[currentEva].name + '）：' }}</p>
        <van-field
          v-model="evaText[currentEva]"
          rows="2"
          autosize
          type="textarea"
          maxlength="100"
          placeholder="请输入评论"
          show-word-limit
        />
      </div>
    </div>

    <div class="submit-btn">
      <van-button block size="small" round type="info" @click="submitEva">提交评论</van-button>
    </div>
  </div>
</template>

<script>
  import myData, {back1} from '../utils/api'

  export default {
    name: "Evaluation",
    data() {
      return {
        mealList: [],
        rate: [],
        evaText: [],
        currentEva: 0,
      }
    },
    created() {
      this.mealList = this.$route.query.order
      for(let i = 0; i < this.mealList.length; i++) {
        this.rate.push(0)
        this.evaText.push('')
      }
    },
    methods: {
      back() {
        this.$back1()
      },
      selectPanel(i) {
        this.currentEva = i
      },
      submitEva() {
        const _this = this
        const url = myData.url.baseUrl + '/Evaluation/insertEval'
        let evaList = []
        for(let i in _this.mealList) {
          if(_this.rate[i] != 0) {
            evaList.push({
              mealId: _this.mealList[i].mealId,
              userId: myData.user.uid,
              desc: _this.evaText[i],
              star: _this.rate[i]
            })
          }else {

          }
        }
        _this.$postJsonRequest(url, evaList).then(res => {
          // console.log(res.data);
          if(res.data) {
            _this.$toast({
              position: 'bottom',
              message: '评论成功',
            });
          }else {
            _this.$toast({
              position: 'bottom',
              message: '评论失败',
            });
          }
        })
      }
    }
  }
</script>

<style scoped>
  .eva {
    display: flex;
    flex-direction: column;
    padding-top: 50px;
  }

  .evaPanel {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    background-color: white;
    border-radius: 15px;
    padding: 10px;
    margin: 0px 20px 10px 20px;
    box-shadow: 0px 0px 8px 0px #00000008;
  }

  .panel-select {
    background-color: #1989FAcc;
    color: white;
  }

  .panel-unselect {
    background-color: white;
    color: #000000cc;
  }

  .eva-content {
    padding-bottom: 50px;
  }

  .evaPanel p {
    margin: 0px;
    font-size: 14px;
  }

  .eva-text {
    margin: 20px;
    font-size: 14px;
  }

  .submit-btn {
    position: fixed;
    bottom: 0px;
    left: 0px;
    right: 0px;
    width: 90%;
    height: auto;
    margin: 20px auto 20px auto;
  }
</style>

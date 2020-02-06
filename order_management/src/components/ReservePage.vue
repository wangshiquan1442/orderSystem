<template>
  <div class="">

    <MealHeader></MealHeader>

    <div class="book-page">
      <MainAside></MainAside>

      <div class="main-content">
        <div class="screen" style="flex-direction: row;">
          <div class="screen-item">
            <el-radio-group style="margin-right: 20px" size="small" v-model="screenData.cate">
              <el-radio-button :label="cate.value" v-for="(cate, index) in tableCate" :key="index">{{ cate.label }}</el-radio-button>
            </el-radio-group>
            <el-radio-group style="margin-right: 20px" size="small" v-model="screenData.status">
              <el-radio-button :label="status.label" v-for="(status, index) in tableStatus" :key="index">
                {{ status.label }}
                {{ '(' + numInfo[index] + ')' }}
              </el-radio-button>
            </el-radio-group>
            <el-select style="margin-right: 20px" v-model="selectTid" placeholder="请选择餐桌号" size="mini" filterable clearable>
              <el-option
                v-for="(id, index) in idList"
                :key="index"
                :label="id"
                :value="id">
              </el-option>
            </el-select>
          </div>
          <div class="screen-item">
            <canvas ref="allQRCode" v-show="false"></canvas>
            <el-popover
              placement="top"
              width="160"
              v-model="popVisible">
              <p>确认下载？</p>
              <div style="text-align: right; margin: 0">
                <el-button size="mini" type="text" @click="popVisible = false">取消</el-button>
                <el-button type="primary" size="mini" @click="saveAllQRCode">确定</el-button>
              </div>
              <el-button round style="height: 28px; margin-left: 10px;" size="mini" slot="reference">批量下载二维码</el-button>
            </el-popover>
          </div>
        </div>
        <el-divider class="my-divider"></el-divider>
        <div class="reserve-table">
          <div class="dining-table">
            <div v-for="(table, index) in tableData" :key="index" class="table-div" @click="tableDialog('edit', index)"
                 :class="{'reserve-color': table.status == '预定中' ? true : false, 'use-color': table.status == '使用中' ? true : false}">
              <div class="table-content">
                <div class="t-row1">
                  <p class="tid">{{ table.tableId }}</p>
                  <p>{{ table.capacity }}人</p>
                </div>
                <p>{{ table.status }}</p>
                <p v-if="table.note != ''">{{ table.note }}</p>
              </div>
            </div>
            <div class="non-table" @click="tableDialog('new', tableData.length)"><i class="el-icon-plus"></i></div>
          </div>
        </div>
      </div>
    </div>

    <!-- 餐桌弹窗 -->
    <el-dialog :title="dialogAction == 'new' ? '添加餐桌' : '编辑餐桌'" :visible.sync="dialogFormVisible">
      <el-form class="table-dialog">
        <div class="form-left">
          <el-form-item label="餐桌号" :label-width="formLabelWidth">
            <el-input v-model="form.tableId" style="width: auto" v-show="dialogAction == 'new'"></el-input>
            <p v-show="dialogAction == 'edit'" style="margin: 0px 10px 0px 10px">{{ form.tableId }}</p>
          </el-form-item>
          <el-form-item label="容纳人数" :label-width="formLabelWidth">
            <el-input-number v-model="form.capacity"></el-input-number>
          </el-form-item>
          <el-form-item label="餐桌状态" :label-width="formLabelWidth">
            <el-select v-model="form.status" placeholder="请选择" :disabled="dialogAction == 'new'">
              <el-option
                v-for="(status, index) in statusList"
                :key="index"
                :label="status"
                :value="status">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="备注" :label-width="formLabelWidth">
            <el-autocomplete
              clearable
              :fetch-suggestions="tableNote"
              show-word-limit
              placeholder="请输入内容"
              v-model="form.note">
            </el-autocomplete>
          </el-form-item>
        </div>
        <div class="form-right">
          <canvas ref="imageDom" style="width: 15px; height: 150px;"></canvas>
          <el-button @click="saveQRCode">下载二维码</el-button>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmBtn">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import MainAside from "./MainAside";
  import MealHeader from "./MealHeader";
  import myData from '../utils/api'
  import {removeDuplication} from '../utils/api'
  import QRCode from 'qrcode'
  import html2canvas from "html2canvas"
  import JSZip from 'jszip'
  import FileSaver from "file-saver"

  export default {
    name: "BookPage",
    components: {
      MainAside,
      MealHeader
    },
    data() {
      return {
        tableList: [],
        hallList: [],
        seatList: [],
        boxList: [],
        tableData: [],
        nonTableList: [],
        form: {},
        formLabelWidth: '120px',
        dialogFormVisible: false,
        tableStatus: [
          {
            value: 'all',
            label: '全部',
          },
          {
            value: 'idle',
            label: '空闲中',
          },
          {
            value: 'booked',
            label: '预定中',
          },
          {
            value: 'used',
            label: '使用中',
          },
        ],
        numInfo: [],
        tableCate: [
          {
            value: 'hall',
            label: '大厅',
          },
          {
            value: 'seat',
            label: '卡座',
          },
          {
            value: 'box',
            label: '包厢',
          },
          {
            value: 'all',
            label: '全部',
          }
        ],
        screenData: {
          cate: 'hall',
          status: '全部',
        },
        statusNum: [],
        selectTid: '',
        statusList: [],
        idList: [],
        popVisible: false,
        dialogAction: '',
        nextRequest: false,
      }
    },
    created() {
      this.selectAllReserve()
      this.resetFormData()
      this.setTableByCate()
      for(let i = 1; i < this.tableStatus.length; i++) {
        this.statusList.push(this.tableStatus[i].label)
      }
    },
    methods: {
      resetFormData() {
        this.form = {
          tableId: '',
          capacity: 0,
          note: '',
          status: '空闲中'
        }
      },
      setTableByStatus() {
        let temp = []
        for(let table of this.tableData) {
          if(table.status == this.screenData.status) {
            temp.push(table)
          }
        }
        this.tableData = temp
      },
      setTableByCate() {
        switch (this.screenData.cate) {
          case "hall":
            this.tableData = this.hallList
            break
          case "seat":
            this.tableData = this.seatList
            break
          case "box":
            this.tableData = this.boxList
            break
          case "all":
            this.tableData = []
            for(let table of this.tableList) {
              for(let item of table.list)
              this.tableData.push(item)
            }
            break
        }
        this.nextRequest = true
      },
      showTableNum() {
        for(let num of this.statusNum) {
          if(num.cate == this.screenData.cate) {
            this.numInfo[0] = num.numList['idle'] + num.numList['booked'] + num.numList['used']
            this.numInfo[1] = num.numList['idle']
            this.numInfo[2] = num.numList['booked']
            this.numInfo[3] = num.numList['used']
          }
        }
      },
      selectAllReserve() {
        const _this = this
        const url = myData.url + '/DiningTable/selectAllDTable'
        _this.$getRequest(url).then(res => {
          for (let i = 0; i < res.data.length; i++) {
            switch (res.data[i].tableId[0]) {
              case 'H':
                _this.classifyTable(_this.hallList, res.data[i])
                break
              case 'S':
                _this.classifyTable(_this.seatList, res.data[i])
                break
              case 'B':
                _this.classifyTable(_this.boxList, res.data[i])
                break
              default:
                break
            }
            _this.idList.push(res.data[i].tableId)
          }
          removeDuplication(_this.idList)
          _this.tableList = [
            {
              cate: 'hall',
              list: _this.hallList,
            },
            {
              cate: 'seat',
              list: _this.seatList,
            },
            {
              cate: 'box',
              list: _this.boxList,
            }
          ]
          for(let table of _this.tableList) {
            _this.statusNum.push({
              cate: table.cate,
              numList: _this.setTableNum(table.list)
            })
          }
          let numList = {
            idle: 0,
            booked: 0,
            used: 0,
          }
          for(let num of _this.statusNum) {
            numList['idle'] += num.numList['idle']
            numList['booked'] += num.numList['booked']
            numList['used'] += num.numList['used']
          }
          _this.statusNum.push({
            cate: 'all',
            numList: numList
          })
        }).catch(err => {
          console.log(err);
        })
      },
      setTableNum(tableList) {
        let num = {
          idle: 0,
          booked: 0,
          used: 0
        }
        for(let table of tableList) {
          switch (table.status) {
            case '空闲中':
              num.idle++
              break
            case '预定中':
              num.booked++
              break
            case '使用中':
              num.used++
              break
          }
        }
        return num
      },
      classifyTable(list, data) {
        list.push({
          'tableId': data.tableId,
          'capacity': data.capacity,
          'status': data.status,
          'note': data.note,
        })
      },
      tableDialog(caller, index) {
        this.dialogFormVisible = true
        this.dialogAction = caller
        if(caller == 'edit') {
          this.form = this.tableData[index]
          this.qrcode('edit')
        } else if(caller == 'new'){
          this.resetFormData()
          switch (this.screenData.cate) {
            case 'hall':
              this.setTableDialogData('H', this.hallList)
              break
            case 'seat':
              this.setTableDialogData('S', this.seatList)
              break
            case 'box':
            case 'all':
              this.setTableDialogData('B', this.boxList)
              break
            default:
              break
          }
          this.qrcode('new')
        }
      },
      setTableDialogData(str, tableList) {
        let len = tableList.length
        len++
        let tableId = str + len
        let capacity = tableList[0].capacity
        this.form.tableId = tableId
        this.form.capacity = capacity
      },
      //餐桌备注提示选项
      tableNote(str, cb) {
        var results = []
        switch (this.screenData.cate) {
          case 'hall':
            var temp = this.hallList
            break
          case 'seat':
            var temp = this.seatList
            break
          case 'box':
            var temp = this.boxList
            break
        }
        for (let i = 0; i < temp.length; i++) {
          results.push({
            "value": temp[i].note
          })
        }
        cb(results)
      },
      qrcode(caller) {
        let qrcode = window.location.href + '?tid=' + this.form.tableId;  //生成的二维码为URL地址
        let canvas = caller == 'all' ? this.$refs.allQRCode : this.$refs.imageDom
        if (canvas != null) {
          // 将获取到的数据（val）画到canvas上
          QRCode.toCanvas(canvas, qrcode, function (error) {
            // console.log(error);
          });
        } else {
          setTimeout(() => {
            this.qrcode()
            return 0
          }, 500)
        }
      },
      saveAllQRCode() {
        this.popVisible = false
        var blogTitle = 'tableList';
        var zip = new JSZip();
        const cache = {}
        var imgNameList = []
        //设置所有图片的文件名
        for (let table of this.tableData) {
          imgNameList.push(table.tableId)
        }
        var urlList = []
        let canvas = this.$refs.allQRCode
        for (let imgName of imgNameList) {
          this.qrcode('all', imgName)
          let url = canvas.toDataURL("image/png");
          urlList.push(url)
        }
        for (let i in urlList) {
          zip.file(imgNameList[i] + '.png', urlList[i].substring(22),{base64:true})
          cache[imgNameList[i]] = urlList[i]
        }
        zip.generateAsync({type:"blob"}).then(content => { // 生成二进制流
          FileSaver.saveAs(content, blogTitle) // 利用file-saver保存文件
        })
      },
      saveQRCode() {
        html2canvas(this.$refs.imageDom).then(canvas => {
          // 转成图片，生成图片地址
          let imgUrl = canvas.toDataURL("image/png");
          // console.log(imgUrl);
          var eleLink = document.createElement("a");
          eleLink.href = imgUrl; // 转换后的图片地址
          eleLink.download = this.form.tableId;
          document.body.appendChild(eleLink);
          // 触发点击
          eleLink.click();
          // 然后移除
          document.body.removeChild(eleLink);
        });
      },
      confirmBtn() {
        this.dialogFormVisible = false
        // console.log(this.form);
        const _this = this
        const url = myData.url + (_this.dialogAction == 'edit' ? '/DiningTable/updateDTable' : '/DiningTable/insertDTable')
        _this.$postJsonRequest(url, _this.form).then(res => {
          if(res.data == true) {
            _this.resetTableList()
            _this.selectAllReserve()
            setTimeout(() => {
              _this.setTableByCate()
            }, 500)
          }
        })
      },
      resetTableList() {
        this.tableList = []
        this.hallList = []
        this.seatList =  []
        this.boxList = []
      }
    },
    watch: {
      screenData: {
        handler(nval, oval) {
          this.setTableByCate()
          this.selectTid = ''
        },
        deep: true,
      },
      selectTid(nval, oval) {
        if(nval != '') {
          let tableTemp = []
          for(let table of this.tableList) {
            tableTemp = tableTemp.concat(table.list)
          }
          for(let table of tableTemp) {
            if(table.tableId == nval) {
              this.tableData = []
              this.tableData[0] = table
            }
          }
        }else {
          this.setTableByCate()
        }
      },
      tableList: {
        handler(nval, oval) {
          this.setTableByCate()
        },
        deep: true
      },
      nextRequest(nval, oval) {
        if(nval) {
          if(this.screenData.status != '全部') {
            this.setTableByStatus()
          }
        }
        this.showTableNum()
        this.nextRequest = false
      }
    }
  }
</script>

<style scoped>
  .table-dialog {
    width: 100%;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: flex-start;
  }

  .form-left {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    align-items: flex-start;
    margin: 10px 0px 10px 0px;
  }

  .form-right {
    width: 150px;
    height: auto;
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
    align-items: center;
    margin-right: 40px;
  }

  .book-page {
    display: flex;
    justify-content: flex-start;
  }

  .book-page >>> .el-menu--horizontal > .el-menu-item.is-active {
    border-bottom: 0px;
  }

  .dining-table {
    display: flex;
    flex-direction: row;
    width: auto;
    flex-wrap: wrap;
  }

  .table-div {
    min-width: 90px;
    max-width: 90px;
    min-height: 70px;
    max-height: 70px;
    padding: 10px 10px 10px 10px;
    margin: 5px 5px 5px 5px;
    background-color: #ffffff;
    border-radius: 15px;
    border: 2px solid #00000010;
  }

  .table-content {
    display: flex;
    flex-direction: column;
  }

  .table-content p {
    font-size: 12px;
    margin: 2.5px 0px 2.5px 0px;
  }

  .reserve-color {
    color: white;
    background-color: #66B1FF;
    border: 2px solid #00000020;
  }

  .use-color {
    color: white;
    background-color: #ffa054;
    border: 2px solid #00000020;
  }

  .t-row1 {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    /*align-items: flex-end;*/
  }

  .table-content >>> .tid {
    font-size: 18px;
    font-weight: bold;
  }

  .non-table {
    display: flex;
    justify-content: center;
    align-items: center;
    min-width: 90px;
    max-width: 90px;
    min-height: 70px;
    max-height: 70px;
    padding: 10px 10px 10px 10px;
    margin: 5px 5px 5px 5px;
    background-color: #f9f9f9;
    border: 2px solid #00000010;
    border-radius: 15px;
    box-shadow: 0px 0px 8px 0px #00000008;
    font-size: 50px;
    color: #00000010;
  }

  .my-divider {
    width: 98%;
    margin: 5px auto 5px auto;
  }

  .reserve-table {
    display: flex;
    flex-direction: column;
    width: 98%;
    margin: 10px auto 10px auto;
    flex-wrap: wrap;
  }
</style>

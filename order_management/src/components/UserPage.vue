<template>
  <div>
    <MealHeader></MealHeader>

    <div class="main-page">
      <MainAside></MainAside>

      <div class="main-content">
        <!-- 筛选 -->
        <div class="screen">
          <div class="screen-item">
            <p>用户姓名：</p>
            <el-select v-model="screenData.name" filterable placeholder="请选择" size="mini">
              <el-option
                v-for="(user, index) in userList"
                :key="index"
                :label="user.name"
                :value="user.name">
              </el-option>
            </el-select>
          </div>
          <div class="screen-item">
            <i class="el-icon-circle-plus input-switch" v-if="inputSwitch == false" @click="inputSwitchChange"/>
            <i class="el-icon-remove input-switch" v-if="inputSwitch == true" @click="inputSwitchChange"/>
            <p>积分范围：</p>
            <el-input-number v-model="screenData.minCredit" :step="10" controls-position="right" :min="0" :max="9999" size="mini" :disabled="!inputSwitch"/>
            &nbsp;~&nbsp;
            <el-input-number v-model="screenData.maxCredit" :step="10" controls-position="right" :min="0" :max="9999" size="mini" :disabled="!inputSwitch"/>
          </div>
          <div class="screen-item">
            <p>用户类型：</p>
            <el-select v-model="screenData.type" placeholder="请选择" size="mini">
              <el-option
                v-for="(type, index) in typeList"
                :key="index"
                :label="type.desc"
                :value="type.val">
              </el-option>
            </el-select>
          </div>
          <div class="screen-item">
            <el-button round style="height: 28px" size="mini" @click="screenBtn">搜索用户</el-button>
            <el-button round style="height: 28px" size="mini" type="danger" @click="cancelScreen" v-if="screenState">
              取消搜索
            </el-button>
          </div>
        </div>
        <!-- 表格 -->
        <div class="my-table">
          <el-table :data="userList" class="user-table" :height="tableHeight" highlight-current-row @current-change="currentChange" border>
            <el-table-column type="index" width="80" align="center"/>
            <el-table-column
              prop="id"
              label="用户ID"
              width="180">
            </el-table-column>
            <el-table-column sortable
              prop="name"
              label="用户名"
              width="180">
            </el-table-column>
            <el-table-column sortable
              prop="credit"
              label="积分">
            </el-table-column>
            <el-table-column sortable
              prop="typeDesc"
              label="类型">
            </el-table-column>
            <el-table-column sortable
              prop="date"
              label="注册日期">
            </el-table-column>
            <el-table-column label="操作" width="180" align="center" header-align="center">
              <template slot="header" slot-scope="scope">
<!--                <el-button icon="el-icon-plus" circle @click="dialogShow('add')"/>-->
                <el-button icon="el-icon-upload2" circle @click="uploadDialogShow = true"/>
                <el-popover
                  placement="bottom"
                  width="200"
                  trigger="click"
                  v-model="excelDLShow">
                  <div class="popover-dl-excel">
                    <el-button slot="reference" @click="exportExcel('template')" style="margin-right: 5px" size="mini">下载模板</el-button>
                    <el-button slot="reference" @click="exportExcel('data')" size="mini">下载数据</el-button>
                  </div>
                  <el-button icon="el-icon-download" circle slot="reference" style="margin-left: 10px"/>
                </el-popover>
              </template>
              <template slot-scope="scope">
                <el-button type="primary" icon="el-icon-edit" circle @click="dialogShow('update')"/>
                <el-button type="danger" icon="el-icon-delete" circle @click="delUser"/>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <!-- 上传窗口 -->
        <el-dialog
          title="上传数据表"
          :visible.sync="uploadDialogShow"
          width="30%"
          :before-close="closeUploadDialog">
          <el-upload
            class="upload-demo" action=""
            :on-change="handleChange"
            :on-exceed="handleExceed"
            :on-remove="handleRemove"
            :limit="1"
            accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel"
            :auto-upload="false">
            <div class="upload-dialog">
              <el-button size="small" type="primary" @change="importExcel(this)" style="margin-right: 20px;">点击上传</el-button>
              <div slot="tip" class="el-upload__tip">只 能 上 传 xlsx / xls 文 件</div>
            </div>
          </el-upload>
          <span slot="footer" class="dialog-footer">
            <el-button @click="uploadDialogShow = false">取 消</el-button>
            <el-button type="primary" @click="uploadDialogShow = false">确 定</el-button>
          </span>
        </el-dialog>
        <!-- 新增和编辑窗口 -->
        <el-dialog :title="dialogCaller == 'update' ? '编辑窗口' : '新增窗口'" :visible.sync="dialogFormVisible">
          <el-form :model="form" class="my-form">
            <el-form-item label="用户名" :label-width="formLabelWidth">
              <p style="margin: 0px">{{ form.name }}</p>
            </el-form-item>
            <el-form-item label="用类型" :label-width="formLabelWidth">
              <el-select v-model="form.typeDesc" filterable placeholder="请选择"
                         size="small" clearable allow-create default-first-option>
                <el-option
                  v-for="(type, index) in typeList"
                  :key="index"
                  :label="type.desc"
                  :value="type.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="积分" :label-width="formLabelWidth">
              <el-input-number size="small" v-model="form.credit" :precision="1" :step="10" clearable></el-input-number>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="cancelBtn">取 消</el-button>
            <el-button type="primary" @click="confirmBtn">确 定</el-button>
          </div>
        </el-dialog>
      </div>
    </div>
  </div>
</template>

<script>
  import MealHeader from "./MealHeader";
  import MainAside from "./MainAside";
  import myData from "../utils/api"
  import {removeDuplication, export2Excel, import2Excel} from '../utils/api';

  export default {
    name: "UserPage",
    components: {
      MealHeader,
      MainAside,
    },
    data() {
      return {
        screenData: {
          name: null,
          minCredit: null,
          maxCredit: null,
          type: null
        },
        userList: [],
        typeDesc: ['管理员', '普通用户', 'vip会员'],
        typeList: [],
        screenState: false,
        inputSwitch: false,  //积分筛选功能是否可用
        tableHeight: 0,
        excelDLShow: false,
        tHeader: ['用户ID', '姓名', '积分', '用户类型值', '用户类型', '注册日期'],
        filterVal: ['id', 'name', 'credit', 'type', 'typeDesc', 'date'],
        uploadDialogShow: false,
        dialogCaller: 'update',
        form: {},
        formLabelWidth: '100px',
        dialogFormVisible: false
      }
    },
    created() {
      this.userList = myData.userList
      this.setTypeList()
      // this.tableHeight = this.$('.right-aside').height() - this.$('.search').height()
    },
    methods: {
      setTypeList() {
        for (let i = 0; i < myData.userList.length; i++) {
          this.typeList.push({
            val: myData.userList[i].type,
            desc: this.typeDesc[myData.userList[i].type]
          })
        }
        removeDuplication(this.typeList, 'val')
      },
      screenBtn() {
        let screenData = this.screenData
        let minCredit = this.inputSwitch == false ? null : screenData.minCredit
        let maxCredit = this.inputSwitch == false ? null : screenData.maxCredit
        if(this.notNull(screenData.name) || this.notNull(minCredit) || this.notNull(maxCredit) || this.notNull(screenData.type)) {
          this.screenState = true
          const _this = this
          const url = myData.url + '/User/screenUser'
          const params = {
            name: screenData.name,
            minCredit: minCredit,
            maxCredit: maxCredit,
            type: screenData.type
          }
          // console.log(params);
          _this.$postJsonRequest(url, params).then(res => {
            _this.userList = res.data
            _this.setUserList()
          })
        }
      },
      cancelScreen() {
        this.userList = myData.userList
        this.setUserList()
        this.screenState = false
        this.screenData = {
          name: null,
          minCredit: null,
          maxCredit: null,
          type: null
        }
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
      exportExcel(params) {
        this.excelDLShow = !this.excelDLShow
        switch (params) {
          case 'template':
            export2Excel(this.tHeader, [], [], 'user_template')
            break
          case 'data':
            export2Excel(this.tHeader, this.filterVal, this.userList, 'user_data')
            break
        }
      },
      currentChange(val) {
        //单选
        this.form = val
      },
      dialogShow() {
        this.dialogFormVisible = true
      },
      delUser() {
        var _this = this
        _this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          const url = myData.url + '/User/delUser'
          _this.$postRequest(url, {id: _this.form.id}).then(res => {
            if(res.data) {
              myData.userList = []
              myData.myVue.$emit('selectUserExceptAdmin')
              setTimeout(() => {
                _this.userList = myData.userList
              }, 500)
              _this.$message({
                type: 'success',
                message: '删除成功!'
              });
            }
          })
        }).catch(() => {
          // _this.$message({
          //     type: 'info',
          //     message: '已取消删除'
          // });
        });
      },
      handleChange(file, fileList){
        this.fileTemp = file.raw
        if(this.fileTemp){
          if((this.fileTemp.type == 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet') || (this.fileTemp.type == 'application/vnd.ms-excel')){
            this.importExcel(this.fileTemp)
          } else {
            this.$message({
              type:'warning',
              message:'附件格式错误，请删除后重新上传！'
            })
          }
        } else {
          this.$message({
            type:'warning',
            message:'请上传附件！'
          })
        }
      },
      handleExceed(files, fileList) {
        this.$message.warning('一次只能上传一个文件');
      },
      handleRemove(file,fileList){
        this.fileTemp = null
      },
      importExcel(obj) {
        let outData = import2Excel(obj, this.tHeader, this.filterVal)
        this.notNull(outData) ? outData : outData = []
        for(let out of outData) {
          for(let user of this.userList) {
            if(out.name != user.name) {
              this.userList.push(user)
            }
          }
        }
      },
      closeUploadDialog(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
      },
      cancelBtn() {
        this.dialogFormVisible = false
      },
      confirmBtn() {
        const _this = this
        const url = myData.url + '/User/updateUser'
        _this.$postJsonRequest(url, _this.form).then(res => {
          if(res.data) {
            myData.userList = []
            myData.myVue.$emit('selectUserExceptAdmin')
          }
        })
        this.dialogFormVisible = false
      }
    },
    watch: {
      userList: {
        handler(nval, oval) {
          for (let i = 0; i < nval.length; i++) {
            this.$set(nval[i], 'typeDesc', this.typeDesc[nval[i].type])
          }
        },
        deep: true
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

  .user-table {
    width: 98%;
    margin: 5px auto 10px auto;
  }

  .upload-dialog {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  }
</style>

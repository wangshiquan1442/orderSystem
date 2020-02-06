<template>
  <div class="">

    <MealHeader></MealHeader>

    <div class="meal-page">
      <MainAside></MainAside>

      <div class="main-content">
        <!-- 搜索菜品和新增菜品-->
        <div class="screen">
          <div class="screen-item">
<!--            <p>菜名：</p>-->
            <el-select v-model="searchMeal.name" filterable placeholder="请选择菜品" size="mini" clearable>
              <el-option
                v-for="(meal, index) in mealList"
                :key="index"
                :label="meal.name"
                :value="meal.name">
              </el-option>
            </el-select>
          </div>
          <div class="screen-item">
<!--            <p>分类：</p>-->
            <el-select v-model="searchMeal.category" filterable placeholder="请选择菜系" size="mini" clearable>
              <el-option
                v-for="(category, index) in categoryList"
                :key="index"
                :label="category"
                :value="category">
              </el-option>
            </el-select>
          </div>
          <div class="screen-item">
<!--            <p style="width: 70px">菜品介绍：</p>-->
            <el-input v-model="searchMeal.desc" placeholder="请输入菜品介绍" size="mini" clearable
                      style="width: 250px"></el-input>
          </div>
          <div class="screen-item">
            <p>价格区间：</p>
            <!--              <el-input v-model="searchMeal.minPrice" placeholder="最低价" size="mini" clearable-->
            <!--                        style="width: 90px"></el-input>-->
            <el-input-number v-model="searchMeal.minPrice" controls-position="right" :min="0" :max="9999"
                             size="mini"
                             style="width: 90px"></el-input-number>
            <p>&nbsp;~&nbsp;</p>
            <el-input-number v-model="searchMeal.maxPrice" controls-position="right" :min="0" :max="9999"
                             size="mini"
                             style="width: 90px"></el-input-number>
            <!--              <el-input v-model="searchMeal.maxPrice" placeholder="最高价" size="mini" clearable-->
            <!--                        style="width: 90px"></el-input>-->
          </div>
          <div class="screen-item">
            <el-button round style="height: 28px" size="mini" @click="searchBtn">搜索菜品</el-button>
            <el-button round style="height: 28px" size="mini" type="danger" @click="cancelSearch"
                       v-if="searchState">
              取消搜索
            </el-button>
          </div>
        </div>

        <el-table :data="searchState == false ? mealList : resultList" border class="meal-table"
                  highlight-current-row @current-change="currentChange" tooltip-effect="dark"
                  @selection-change="selectionChange" id="mealTable" :height="tableHeight"
                  :default-sort="{prop: 'name', order: 'descending'}" lazy>
          <!--选项列-->
          <el-table-column type="selection" width="55" align="center" header-align="center"></el-table-column>
          <!--索引列-->
          <el-table-column type="index" width="50" align="center" header-align="left"></el-table-column>
          <el-table-column prop="name" label="菜名" width="200" sortable>
            <template slot-scope="scope">
              <!-- 菜品图片气泡展示 -->
              <el-popover
                ref="mealPopover"
                placement="bottom"
                width="200"
                trigger="click">
                <el-image
                  style="width: 200px; height: 200px"
                  :src="scope.row.img"
                  fit="cover"></el-image>
                <el-link :underline="false" slot="reference">{{ scope.row.name }}</el-link>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column prop="category" label="分类" width="150" sortable></el-table-column>
          <el-table-column prop="desc" label="介绍"></el-table-column>
          <el-table-column prop="price" label="价钱" width="120" sortable></el-table-column>
<!--          <el-table-column prop="credit" label="积分" width="120" sortable></el-table-column>-->
          <el-table-column label="操作" width="180" align="center" header-align="center">
<!--            <template slot="header" slot-scope="scope">-->
<!--              <el-button round size="small" class="add-meal" @click="dialogShow('add')">新增菜品</el-button>-->
<!--            </template>-->
            <template slot="header" slot-scope="scope">
              <el-button icon="el-icon-plus" circle @click="dialogShow('add')"/>
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
              <el-button type="primary" icon="el-icon-edit" circle @click="dialogShow('update')"></el-button>
              <el-button type="danger" icon="el-icon-delete" circle @click="delMeal"></el-button>
            </template>
            <!--              <el-row>-->
            <!--                <el-button type="primary" icon="el-icon-edit" circle @click="dialogShow('update')"></el-button>-->
            <!--                <el-button type="danger" icon="el-icon-delete" circle @click="delMeal"></el-button>-->
            <!--              </el-row>-->
          </el-table-column>
        </el-table>

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
      </div>
    </div>

    <!-- 新增和编辑菜品窗口 -->
    <el-dialog :title="dialogCaller == 'update' ? '编辑窗口' : '新增窗口'" :visible.sync="dialogFormVisible">
      <el-form :model="form" class="my-form">
        <el-form-item label="菜名" :label-width="formLabelWidth">
          <el-input v-model="form.name" autocomplete="off" size="small" maxlength="20" clearable></el-input>
        </el-form-item>
        <el-form-item label="分类" :label-width="formLabelWidth">
          <el-select v-model="form.category" filterable placeholder="请选择"
                     size="small" clearable allow-create default-first-option>
            <el-option
              v-for="(category, index) in categoryList"
              :key="index"
              :label="category"
              :value="category">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="价钱" :label-width="formLabelWidth">
          <el-input-number size="small" v-model="form.price" :precision="1" :step="1" clearable></el-input-number>
        </el-form-item>
        <el-form-item label="介绍" :label-width="formLabelWidth">
          <div class="el-textarea el-input--small">
            <textarea autocomplete="off" v-model="form.desc" maxlength="100" class="el-textarea__inner"
                      style="height: 70px;"></textarea>
            <span class="el-input__count" style="bottom: -5px; background: #ffffff00">{{ form.desc.length }}/100</span>
          </div>
        </el-form-item>
        <el-form-item label="上传图片" :label-width="formLabelWidth">
          <el-upload
            class="avatar-uploader"
            ref="upload"
            :http-request="customUpLoad"
            action="ftp://127.0.0.1/nginx_images/meal/"
            :auto-upload="false"
            :show-file-list="false"
            :on-change="changeImg"
            :on-success="handleImgSuccess">
            <div slot="tip" style="font-size: 12px; margin: 0px 15px 0px 15px;">只能上传jpg/png文件，且不超过500kb</div>
            <img v-if="this.form.img" :src="this.form.img" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelBtn">取 消</el-button>
        <el-button type="primary" @click="confirmBtn">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import MainAside from "./MainAside";
  import MealHeader from "./MealHeader";
  import myData, {export2Excel, import2Excel, removeDuplication } from '../utils/api'

  export default {
    name: "MealPage",
    components: {
      MainAside,
      MealHeader
    },
    data() {
      return {
        dialogFormVisible: false,
        searchMeal: {
          name: '',
          desc: '',
          category: '',
          price: '',
          minPrice: '',
          maxPrice: ''
        },
        resultList: [],
        mealList: [],
        categoryList: [],
        searchState: false,
        form: {
          id: '',
          name: '',
          category: '',
          price: '',
          desc: '',
          // credit: 0,
          img: '',
        },
        multipleSelection: '',
        formLabelWidth: '100px',
        uploadSuccess: false,
        tableHeight: 0,
        excelDLShow: false,
        tHeader: ['菜名', '分类', '介绍', '价钱'],
        filterVal: ['name', 'category', 'desc', 'price'],
        uploadDialogShow: false,
        dialogCaller: '',
      }
    },
    created() {
      this.selectAllMeal()
    },
    methods: {
      selectAllMeal() {
        //避免数据重复加载
        myData.mealList.length = 0

        const _this = this
        const url = myData.url + '/Meal/selectAllMeal'
        _this.$getRequest(url).then(res => {
          myData.mealList = res.data
          _this.setMealImg()
          _this.$('.main-content').height(_this.$(window).height() - _this.$('.el-header').height())
          _this.tableHeight = _this.$('.main-content').height() - _this.$('.screen').height()
        }).catch(err => {
          console.log(err);
        })
      },
      setMealImg() {
        const _this = this
        let url = ''
        let imgUrl = []
        _this.$getRequest('/mealImg/meal/').then((res) => {
          const pattern = /[a-z]*[0-9]*\w*.jpg|[a-z]*[0-9]*\w*.png|([a-z]*[0-9]*\w*.jpeg)\1/gm
          imgUrl = res.data.match(pattern)
          removeDuplication(imgUrl, null)
          // console.log(imgUrl);
          for (let i = 0; i < myData.mealList.length; i++) {
            let mealId = myData.mealList[i]['id']
            for (let j = 0; j < imgUrl.length; j++) {
              switch (imgUrl[j]) {
                case mealId + '.jpg':
                  url = mealId + '.jpg'
                  break
                case mealId + '.jpeg':
                  url = mealId + '.jpeg'
                  break
                case mealId + '.png':
                  url = mealId + '.png'
                  break
              }
            }
            url == '' ? url = '../../static/img/meal.jpg' : url = 'http://localhost:8090/meal/' + url
            _this.$set(myData.mealList[i], 'img', url)
            url = ''
          }
          _this.mealList.length == 0
          _this.mealList = myData.mealList
          _this.setCategoryList()
        }).catch((err) => {
          console.log(err);
        })
      },
      setCategoryList() {
        if (this.categoryList != 0) {
          this.categoryList.length = 0
        }
        for (let i = 0; i < this.mealList.length; i++) {
          this.categoryList.push(this.mealList[i].category)
        }
        removeDuplication(this.categoryList, null)
      },
      dialogShow(caller) {
        this.dialogCaller = caller
        if (this.dialogCaller == 'add') {
          for (let i in this.form) {
            this.form[i] = ''
          }
        }
        this.dialogFormVisible = true
      },
      changeImg(file) {
        if (this.beforeImgUpload(file)) {
          this.$set(this.form, 'img', URL.createObjectURL(file.raw))
        }
      },
      beforeImgUpload(file) {
        const isJPG = file.raw.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/jpg';
        const isLt500K = file.size / 1024 / 1024 < 0.5;
        if (!isJPG) {
          this.$message.error('上传图片只能是 JPG/JPEG/PNG 格式!');
        }
        if (!isLt500K) {
          this.$message.error('上传图片大小不能超过 500KB!');
        }
        return isJPG && isLt500K;
      },
      handleImgSuccess(res, file) {
        this.uploadSuccess = true
      },
      cancelBtn() {
        this.dialogFormVisible = false
      },
      confirmBtn() {
        const _this = this
        _this.$refs.upload.submit()
        if (this.uploadSuccess == true) {
          for (let i = 0; i < _this.mealList.length; i++) {
            if (_this.mealList[i].mealId == _this.form.id) {
              _this.$set(_this.mealList, i, _this.form)
              break
            }
          }
        }
        const url =  myData.url + (_this.dialogCaller == 'update' ? '/Meal/updateMeal' : '/Meal/insertMeal')
        _this.$postJsonRequest(url, _this.form).then(res => {
          if(res.data) {
            // _this.selectAllMeal()
          }
        })
        this.dialogFormVisible = false
      },
      delMeal() {
        var _this = this
        _this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          const url = myData.url + '/Meal/delMeal'
          _this.$postRequest(url, {id: _this.form.id}).then(res => {
            if(res.data) {
              _this.selectAllMeal()
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
      currentChange(val) {
        //单选
        this.form = val
      },
      selectionChange(val) {
        //多选
        this.multipleSelection = val;
      },
      searchBtn() {
        this.searchState = true
        this.resultList.length = 0
        if (this.searchMeal.category != '') {
          for (let i = 0; i < this.mealList.length; i++) {
            if (this.searchMeal.category == this.mealList[i].category) {
              this.resultList.push(this.mealList[i])
            }
          }
        }
        if (this.searchMeal.desc != '') {
          if (this.resultList.length == 0) {
            for (let i = 0; i < this.mealList.length; i++) {
              if (this.resultList[i].desc.indexOf(this.searchMeal.desc) == 0) {
                this.resultList.push(this.mealList[i])
              }
            }
          } else {
            for (let i = 0; i < this.resultList.length; i++) {
              if (this.resultList[i].desc.indexOf(this.searchMeal.desc) == -1) {
                this.resultList.splice(i, 1)
                i--
              }
            }
          }
        }
        if (this.searchMeal.minPrice != 0) {
          if (this.resultList.length == 0) {
            for (let i = 0; i < this.mealList.length; i++) {
              if (this.searchMeal.minPrice <= this.mealList[i].price) {
                this.resultList.push(this.mealList[i])
              }
            }
          } else {
            for (let i = 0; i < this.resultList.length; i++) {
              if (this.searchMeal.minPrice > this.resultList[i].price) {
                this.resultList.splice(i, 1)
                i--
              }
            }
          }
        }
        if (this.searchMeal.maxPrice != 0) {
          if (this.resultList.length == 0) {
            for (let i = 0; i < this.mealList.length; i++) {
              if (this.searchMeal.maxPrice >= this.mealList[i].price) {
                this.resultList.push(this.mealList[i])
              }
            }
          } else {
            console.log(this.searchMeal.maxPrice);
            for (let i = 0; i < this.resultList.length; i++) {
              if (this.searchMeal.maxPrice < this.resultList[i].price) {
                this.resultList.splice(i, 1)
                i--
              }
            }
          }
        }
        if (this.searchMeal.name != '') {
          if (this.resultList.length == 0) {
            for (let i = 0; i < this.mealList.length; i++) {
              if (this.searchMeal.name == this.mealList[i].name) {
                this.resultList.push(this.mealList[i])
              }
            }
          } else {
            for (let i = 0; i < this.resultList.length; i++) {
              if (this.searchMeal.name != this.mealList[i].name) {
                this.resultList.splice(i, 1)
                i--
              }
            }
          }
        }
      },
      cancelSearch() {
        this.resultList.length = 0
        for (var i in this.searchMeal) {
          this.searchMeal[i] = ''
        }
        this.searchState = false
      },
      customUpLoad(file) {
        const _this = this
        const url = myData.url + '/Meal/uploadImg'
        // const params = new FormData({
        //   pic: file.file,
        //   username: 'shiquan',
        //   password: '1209yuyu',
        //   mealId: _this.form.id,
        // }[0])
        let params = new FormData()
        params.append('pic', file.file)
        params.append('username', 'shiquan')
        params.append('password', '1209yuyu')
        params.append('mealId', _this.form.id)
        // console.log(params);
        _this.$axios({
          method: 'post',
          url: url,
          headers: {'Content-Type': 'multipart/form-data'},
          data: params
        }).then(res => {

        });
      },
      exportExcel(params) {
        this.excelDLShow = !this.excelDLShow
        switch (params) {
          case 'template':
            export2Excel(this.tHeader, [], [], 'meal_template')
            break
          case 'data':
            export2Excel(this.tHeader, this.filterVal, this.mealList, 'meal_data')
            break
        }
      },
      importExcel(obj) {
        let outData = import2Excel(obj, this.tHeader, this.filterVal)
        this.notNull(outData) ? outData : outData = []
        // console.log(outData);
        // for(let out of outData) {
        //   for(let meal of this.mealList) {
        //     if(out.name != meal.name) {
        //       this.mealList.push(meal)
        //     }
        //   }
        // }
      },
      closeUploadDialog(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
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
    },
    watch: {
    }
  }
</script>

<style scoped>
  .meal-table {
    width: 98%;
    height: 100%;
    margin: 5px auto 10px auto;
  }

  .meal-page {
    display: flex;
    justify-content: space-between;
  }

  .add-meal {
    font-weight: bold;
    font-size: 14px;
    color: #909399;
  }

  .avatar-uploader {
    display: flex;
    flex-direction: row;
  }

  .avatar-uploader >>> .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 10px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  .avatar-uploader >>> .el-upload:hover {
    border-color: #409EFF;
  }

  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 150px;
    height: 150px;
    line-height: 150px;
    text-align: center;
  }

  .avatar {
    width: 150px;
    height: 150px;
    border-radius: 10px;
    display: block;
  }

</style>

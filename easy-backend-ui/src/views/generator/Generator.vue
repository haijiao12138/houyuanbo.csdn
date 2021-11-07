<template>
  <div>
    <el-row>
      <el-form
        :inline="true"
        :model="searchForm"
        ref="searchForm"
        class="demo-form-inline"
      >
        <el-form-item label="数据库表名称" prop="tableName">
          <el-input
            v-model.trim="searchForm.tableName"
            placeholder="请输入数据库表名称"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSearch">查 询</el-button>
          <el-button @click="resetSearchForm('searchForm')">重 置</el-button>
        </el-form-item>
      </el-form>
    </el-row>
    <el-row>
      <el-button type="primary" @click="addHandleClick"  v-auth="['/generator/saveOrUpdate']">添加表信息</el-button>
      <el-button type="primary" @click="batchDelete(tableChecked)"  v-auth="['/generator/delete']"
        >批量删除表
      </el-button>
       <el-button type="primary"  v-auth="['/generator/export']" @click="exportExcel">
         导出数据
      </el-button>
      <p/>
    </el-row>

    <el-table
      :data="tableData"
      border
      style="width: 100%"
      :row-style="{ height: '40px' }"
      :cell-style="{ padding: '0px' }"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="95"></el-table-column>
      <el-table-column prop="tableName" label="表名称"></el-table-column>
      <el-table-column
        prop="moduleName"
        label="模块名"
        width="165"
      ></el-table-column>
      <el-table-column
        prop="ignoreFlag"
        label="是否忽略前缀"
        width="125"
        :formatter="availableFormatter"
      ></el-table-column>
      <el-table-column
        prop="ignorePrefix"
        label="前缀"
        width="125"
      ></el-table-column>
      <el-table-column
        prop="createTime"
        label="创建日期"
        width="180"
        :formatter="carTimeFilter"
      >
      </el-table-column>
      <el-table-column
        prop="updateTime"
        sortable
        label="更新日期"
        width="180"
        :formatter="carTimeFilter"
      >
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="210">
        <template slot-scope="scope">
          <el-button
            v-auth="['/generator/autoGenerator']"
            type="primary"
            @click="generatorHandleClick(scope.row)"
            size="mini"
            icon="el-icon-document-add"
          ></el-button>
          <el-button
            v-auth="['/generator/saveOrUpdate']"
            type="success"
            @click="editorHandleClick(scope.row)"
            size="mini"
            icon="el-icon-edit"
          ></el-button>
          <el-button
            v-auth="['/generator/saveOrUpdate']"
            @click="deleteHandleClick(scope.row)"
            type="danger"
            size="mini"
            icon="el-icon-delete"
          ></el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      :current-page="currentPage"
      :page-size="pageSize"
      @current-change="handleCurrentChange"
      background
      layout="prev, pager, next"
      :total="totalCount"
    >
    </el-pagination>

    <!-- 编辑 : 添加 -->
    <el-dialog
      :title="isEditor ? '编辑表格' : '添加表'"
      :visible.sync="dialogAddFormVisible"
    >
      <el-form :model="form" :rules="rules" ref="form">
        <el-input type="hidden" v-model="form.id" autocomplete="off"></el-input>
        <el-form-item
          prop="selectObj"
          :label="isEditor ? '修改表名' : '选择表'"
          :label-width="formLabelWidth"
        >
          <el-select
            prop="selectObj"
            placeholder="请选择表"
            style=""
            v-model="form.tableName"
          >
            <el-option
              v-for="item in objData"
              :key="item.value"
              :value="item"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item
          prop="moduleName"
          :label="isEditor ? '修改模块名' : '模块名'"
          :label-width="formLabelWidth"
        >
          <el-input v-model="form.moduleName" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item
          prop="ignorePrefix"
          label="表格前缀"
          :label-width="formLabelWidth"
        >
          <el-input v-model="form.ignorePrefix" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item
          prop="ignoreFlag"
          label="是否忽略前缀"
          :label-width="formLabelWidth"
        >
          <el-switch
            v-model="form.ignoreFlag"
            active-text="忽略"
            inactive-text="不忽略"
            :active-value="1"
            :inactive-value="0"
          >
          </el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type=" primary" @click="addTable" v-if="!isEditor"
          >添 加
        </el-button>
        <el-button type=" primary" @click="saveOrUpdate" v-if="isEditor"
          >提 交
        </el-button>
        <el-button @click="resetForm('form')" v-if="!isEditor">重 置</el-button>
        <el-button @click="dialogAddFormVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {
  LIST_GENERATOR_URL,
  FIND_GENERATOR,
  ADD_OR_EDITOR_GENERATOR_URL,
  AUTOTOOLS_GENERATOR_URL,
  BATCHDELETE_GENERATOR_URL,
  QUERYTABLENAME_GENERATOR_URL,
  EXPORT_GENERATOR_URL,
} from "../../utils/api.js";
import moment from "moment";

export default {
  data() {
    return {
      edit: true,
      objData: [],
      areasData: [],
      currentPage: 1,
      totalCount: 1,
      pageSize: 10,
      tableData: [],
      selectObj: "",
      searchForm: {
        tableName: "",
        available: "",
      },
      defaultProps: {
        children: "childResources",
        label: "name",
      },
      isEditor: false,
      isGenerator: true,
      drawer: false,
      resourcesData: [],
      checkeds: [],
      tableChecked: [], //被选中的记录
      ids: [], //批量删除的Id
      updateRoleId: 0,
      formLabelWidth: "120px",
      dialogAddFormVisible: false,
      form: {
        id: "",
        moduleName: "",
        tableName: "",
        ignoreFlag: "",
        ignorePrefix: "t_",
        createTime: "",
        updateTime: "",
      },
      rules: {
        tableName: [
          {
            required: true,
            message: "请输入表名称",
            trigger: "blur",
          },
          {
            min: 1,
            max: 30,
            message: "长度在 1 到 30个字符",
            trigger: "blur",
          },
        ],
        moduleName: [
          {
            required: true,
            message: "请输入模块名称",
            trigger: "blur",
          },
          {
            min: 1,
            max: 20,
            message: "长度在 1 到 20个字符",
            trigger: "blur",
          },
        ],
      },
    };
  },
  created: function () {
    this.pageList();
  },
  mounted() {
    this.areasSelect();
  },
  methods: {
    pageList: function () {
      let url =
        LIST_GENERATOR_URL + "/" + this.currentPage + "/" + this.pageSize;
      this.axios({
        method: "POST",
        url: url,
        data: this.$data.searchForm,
      })
        .then((res) => {
          let code = res.data.code;
          if (code == 200) {
            this.$data.tableData = res.data.data.records;
            this.$data.totalCount = res.data.data.total;
            this.$data.currentPage = res.data.data.current;
          } 
        })
        .catch((error) => {
          // this.$message.error('加载资源失败');
          console.log(error);
        });
    },
    availableFormatter: function (row, column, cellValue) {
      return cellValue ? "忽略" : "不忽略";
    },
    carTimeFilter: function (row, column, cellValue) {
      if (cellValue != null) {
        return moment(cellValue).format("YYYY-MM-DD HH:mm:ss");
      }
    },
    handleCurrentChange: function (val) {
      this.$data.currentPage = val;
      this.pageList();
    },
    onSearch: function () {
      this.pageList();
    },
    resetSearchForm: function (formName) {
      console.log(formName);
      this.$nextTick(() => {
        this.$refs[formName].resetFields();
      });
    },
    resetForm: function (formName) {
      console.log(formName + "<-resetForm");
      this.$nextTick(() => {
        this.$refs[formName].resetFields();
      });
    },
    deleteHandleClick: function (row) {
      this.$confirm("此操作将永久删除该信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.delete(row);
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    delete: function (row) {
      let ids = [];
      ids.push(row.id);
      this.axios({
        method: "POST",
        url: BATCHDELETE_GENERATOR_URL,
        data: ids,
      })
        .then((res) => {
          let code = res.data.code;
          if (code == 200) {
            this.pageList();
          }
        })
        .catch((error) => {
          this.$message.error("删除失败");
          console.log(error);
        });
    },
    editorHandleClick: function (row) {
      this.dialogAddFormVisible = true;
      this.isEditor = true;
      let url = FIND_GENERATOR + "/" + row.id;
      this.axios({
        method: "GET",
        url: url,
        data: {},
      })
        .then((res) => {
          let code = res.data.code;
          console.log(JSON.stringify(res.data));
          if (code == 200) {
            this.$data.form = res.data.data;
          } else {
            this.$message.error("加载资源失败");
          }
        })
        .catch((error) => {
          this.$message.error("加载资源失败");
          console.log(error);
        });
    },
    addTable: function () {
      this.form.id = "";
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.axios({
            method: "POST",
            url: ADD_OR_EDITOR_GENERATOR_URL,
            data: this.form,
          })
            .then((res) => {
              let code = res.data.code;
              if (code == 200) {
                this.pageList();
                this.$data.dialogAddFormVisible = false;
              } else {
                this.$message.error("更新失败");
              }
            })
            .catch((error) => {
              this.$message.error("网络错误");
              console.log(error);
            });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    saveOrUpdate: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.axios({
            method: "POST",
            url: ADD_OR_EDITOR_GENERATOR_URL,
            data: this.form,
          })
            .then((res) => {
              let code = res.data.code;
              if (code == 200) {
                this.pageList();
                this.$message.success("更新表:" + this.form.tableName + "成功");
                this.$data.dialogAddFormVisible = false;
              } else {
                this.$message.error("更新失败");
              }
            })
            .catch((error) => {
              this.$message.error("网络错误");
              console.log(error);
            });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    addHandleClick: function () {
      this.$data.isEditor = false;
      this.$data.dialogAddFormVisible = true;
      this.form.tableName = "";
      this.$nextTick(() => {
        this.$refs["form"].resetFields();
      });
    },
    generatorHandleClick: function (row) {
        this.$confirm("是否确认生成" + row.tableName + "的数据?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
        .then(() => {
          this.generatorTools(row.id);
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "取消生成",
          });
        });
    },
    generatorTools: function (id) {
      let url = AUTOTOOLS_GENERATOR_URL + "/" + id;
      this.axios({
        method: "GET",
        url: url,
        data: {},
      })
        .then((res) => {
          let code = res.data.code;
          if (code == 200) {
            this.dialogGeneratorFormVisible = false;
            this.$message.success("生成成功");
          } else {
            this.$message.error("加载资源失败");
          }
        })
        .catch((error) => {
          this.$message.error("加载资源失败");
          console.log(error);
        });
    },
    updateAuth() {},
    handleSelectionChange(val) {
      this.tableChecked = val;
    },
    closeDrawer() {},
    batchDelete: function (rows) {
      let _this = this;
      if (rows.length <= 0) {
        this.$message.warning("请先选择需要删除的数据");
        return;
      }
      let ids = [];
      rows.forEach((element) => {
        console.log(element);
        ids.push(element.id);
      });
      _this
        .$confirm("是否确定删除选中的id为" + ids + "的数据?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
        .then(() => {
          this.axios({
            method: "POST",
            url: BATCHDELETE_GENERATOR_URL,
            dataType: "json",
            data: ids,
          })
            .then((res) => {
              let code = res.data.code;
              this.$message.success("删除成功");
              if (code == 200) {
                this.pageList();
              }
            })
            .catch((error) => {
              this.$message.error("加载资源失败");
              console.log(error);
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    exportExcel: function () {
      let url = EXPORT_GENERATOR_URL ;
      this.axios({
        method: "GET",
        url: url,
        data: {},
      })
        .then((res) => {
          let code = res.data.code;
          if (code == 200) {
            console.log(res.data.data);
            console.log(process.env.BASE_URL);
            window.location.href =  process.env.VUE_APP_BASEURL + "/profile/download/" + res.data.data;
          } 
        })
        .catch((error) => {
          console.log(error);
        });
    },
    areasSelect: function () {
      this.axios({
        method: "POST",
        url: QUERYTABLENAME_GENERATOR_URL,
      })
        .then((res) => {
          let code = res.data.code;
          if (code == 200) {
            this.objData = res.data.data;
            console.log(this.objData);
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    optionChange() {
      console.log(this.selectObj);
    },
  },
};
</script>
<style scoped>
</style>
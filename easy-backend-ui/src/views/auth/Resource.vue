<template>
  <div v-loading="loading">
    <el-row>
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>权限管理</el-breadcrumb-item>
        <el-breadcrumb-item>权限列表</el-breadcrumb-item>
      </el-breadcrumb>
      <p />
    </el-row>
    <el-row>
      <el-form
        :inline="true"
        :model="searchForm"
        ref="searchForm"
        class="demo-form-inline"
      >
        <el-form-item label="资源名称" prop="name">
          <el-input v-model.trim="searchForm.name" placeholder="资源名称"></el-input>
        </el-form-item>
        <el-form-item label="资源类型" prop="type">
          <el-select v-model="searchForm.type" placeholder="资源类型">
            <el-option label="全部" value=""></el-option>
            <el-option label="菜单" :value="0"></el-option>
            <el-option label="按钮" :value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否可用" prop="available">
          <el-select v-model="searchForm.available" placeholder="是否可用">
            <el-option label="全部" value=""></el-option>
            <el-option label="可用" :value="1"></el-option>
            <el-option label="不可用" :value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSearch">查 询</el-button>
          <el-button @click="resetSearchForm('searchForm')">重 置</el-button>
        </el-form-item>
      </el-form>
    </el-row>
    <el-row>
      <el-button type="primary" @click="addHandleClick">添加</el-button>
      <el-button type="primary" @click="dialogBatchVisible = true">自动创建资源</el-button>
      <p />
    </el-row>

    <el-table
      :data="tableData"
      row-key="id"
      border
      style="width: 100%"
      :tree-props="{ children: 'childResources' }"
    >
      <el-table-column prop="id" fixed label="ID" width="100">
      </el-table-column>
      <el-table-column prop="name" label="资源名称" width="180">
      </el-table-column>
      <el-table-column prop="orderNum" width="180" label="排序">
      </el-table-column>
      <el-table-column prop="permission" label="权限"> </el-table-column>
      <el-table-column prop="url" label="链接"> </el-table-column>
      <el-table-column
        prop="type"
        width="80"
        :formatter="typeFormatter"
        label="类型"
      >
      </el-table-column>
      <el-table-column
        prop="available"
        width="80"
        :formatter="availableFormatter"
        label="是否可用"
      >
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="150">
        <template slot-scope="scope">
          <el-button type="text" size="small">查看</el-button>
          <el-button
            type="text"
            @click="editorHandleClick(scope.row)"
            size="small"
            >编辑</el-button
          >
          <el-button
            @click="deleteHandleClick(scope.row)"
            type="text"
            v-auth="['/resource/delete']"
            size="small"
            >删除</el-button
          >
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

    <el-dialog title="添加资源" :visible.sync="dialogAddFormVisible">
      <el-form :model="form" :rules="rules" ref="form">
        <el-input type="hidden" v-model="form.id" autocomplete="off"></el-input>
        <el-form-item
          prop="parentId"
          label="父类栏目"
          :label-width="formLabelWidth"
          v-show="true"
        >
          <el-select v-model="form.parentId" placeholder="请选择父类资源">
            <el-option label="顶级栏目" :value="1"></el-option>
            <el-option
              v-for="resource in resources"
              :key="resource.id"
              v-show="resource.id != form.id"
              :label="resource.name"
              :value="resource.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          prop="name"
          label="资源名称"
          :label-width="formLabelWidth"
        >
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item
          prop="orderNum"
          label="排序"
          :label-width="formLabelWidth"
        >
          <el-input
            v-model.number="form.orderNum"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item
          prop="permission"
          label="权限字段"
          :label-width="formLabelWidth"
        >
          <el-input v-model="form.permission" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="url" label="链接地址" :label-width="formLabelWidth">
          <el-input v-model="form.url" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item
          prop="available"
          label="是否可用"
          :label-width="formLabelWidth"
        >
          <el-switch
            v-model="form.available"
            active-text="可用"
            inactive-text="不可用"
          >
          </el-switch>
        </el-form-item>
        <el-form-item
          prop="type"
          label="菜单类型"
          :label-width="formLabelWidth"
        >
          <el-select v-model="form.type" placeholder="选择菜单类型">
            <el-option label="菜单" :value="0"></el-option>
            <el-option label="按钮" :value="1"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form')" v-if="!isEditor">重 置</el-button>
        <el-button @click="dialogAddFormVisible = false" v-if="isEditor"
          >取 消</el-button
        >
        <el-button type=" primary" @click="saveOrUpdate">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="自动生成资源" :visible.sync="dialogBatchVisible">
      <el-form :model="form" :rules="rules" ref="form">
        <el-input type="hidden" v-model="form.id" autocomplete="off"></el-input>
        <el-form-item
                prop="parentId"
                label="父类栏目"
                :label-width="formLabelWidth"
                v-show="form.type != 1"
        >
          <el-select v-model="form.parentId" placeholder="请选择父类资源">
            <el-option label="顶级栏目" :value="1"></el-option>
            <el-option
                    v-for="resource in resources"
                    :key="resource.id"
                    v-show="resource.id != form.id"
                    :label="resource.name"
                    :value="resource.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
                prop="name"
                label="资源名称"
                :label-width="formLabelWidth"
        >
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item
                prop="orderNum"
                label="排序"
                :label-width="formLabelWidth"
        >
          <el-input
                  v-model.number="form.orderNum"
                  autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item
                prop="permission"
                label="权限字段"
                :label-width="formLabelWidth"
        >
          <el-input v-model="form.permission" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item
                prop="available"
                label="是否可用"
                :label-width="formLabelWidth"
        >
          <el-switch
                  v-model="form.available"
                  active-text="可用"
                  inactive-text="不可用"
          >
          </el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form')" v-if="!isEditor">重 置</el-button>
        <el-button @click="dialogBatchVisible = false" v-if="isEditor"
        >取 消</el-button
        >
        <el-button type=" primary" @click="batchCreate">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  LOAD_RESOURCES_URL,
  LIST_RESOURCES_URL,
  ADD_OR_EDITOR_RESOURCES_URL,
  DELETE_RESOURCES_URL,
  FIND_RESOURCES_URL,
  BATCH_CREATE_URL,
} from "../../utils/api.js";
export default {
  data() {
    return {
      searchForm: {
        name: "",
        type: "",
        available: "",
      },
      multipleSelection: [],
      tableData: [],
      resources: [],
      dialogFormVisible: false,
      dialogAddFormVisible: false,
      dialogBatchVisible: false,
      isEditor: false,
      // 默认显示第几页
      currentPage: 1,
      // 总条数，根据接口获取数据长度(注意：这里不能为空)
      totalCount: 1,
      pageSize: 10,
      form: {
        id: "",
        name: "",
        parentId: "",
        orderNum: 1,
        permission: "",
        available: true,
        type: "",
        url: "",
        desc: "",
      },
      formLabelWidth: "120px",
      rules: {
        name: [
          {
            required: true,
            message: "请输入资源名称",
            trigger: "blur",
          },
          {
            min: 1,
            max: 20,
            message: "长度在 1 到 20个字符",
            trigger: "blur",
          },
        ],
        permission: [
          {
            required: true,
            message: "请输入权限名称",
            trigger: "blur",
          },
        ],
        url: [
          {
            required: false,
            message: "请输入访问地址",
            trigger: "blur",
          },
        ],
        parentId: [
          {
            required: true,
            message: "请选择父类栏目",
            trigger: "change",
          },
        ],
        type: [
          {
            required: true,
            message: "请选择资源类型",
            trigger: "change",
          },
        ],
        orderNum: [
          {
            type: "number",
            message: "排序必须为数字值",
            trigger: "blur",
          },
          {
            pattern: /^(0|[1-9]\d?|100)$/,
            message: "范围在0-100",
            trigger: "blur",
          },
        ],
      },
	  loading: true
    };
  },
  created: function () {
    this.loadResources();
    this.listResources();
  },
  methods: {
    loadResources: function (val) {
      if (!val) {
        val = 1;
      }
      let url = LOAD_RESOURCES_URL + "/" + val;
      this.axios({
        method: "GET",
        url: url,
        data: this.$data.searchForm,
      })
        .then((res) => {
          let code = res.data.code;
          if (code == 200) {
            this.$data.resources = res.data.data;
          } else {
            this.$message.error("加载资源失败");
          }
        })
        .catch((error) => {
          // this.$message.error('加载资源失败');
          console.log(error);
        });
    },
    listResources: function () {
		this.$data.loading = true;
      let url =
        LIST_RESOURCES_URL + "/" + this.currentPage + "/" + this.pageSize;
      this.axios({
        method: "POST",
        url: url,
        data: this.$data.searchForm,
      })
        .then((res) => {
			this.$data.loading = false;
          console.log(res.data);
          let code = res.data.code;
          console.log(JSON.stringify(res.data));
          if (code == 200) {
            this.$data.tableData = res.data.data.records;
            this.$data.totalCount = res.data.data.total;
            this.$data.currentPage = res.data.data.current;
          } else {
            this.$message.error("加载资源失败");
          }
        })
        .catch((error) => {
          // this.$message.error('加载资源失败');
          console.log(error);
        });
    },
    editorHandleClick: function (row) {
      this.$data.isEditor = true;
      this.resetForm("form");
      this.$data.dialogAddFormVisible = true;
      console.log(row.id);
      let url = FIND_RESOURCES_URL + "/" + row.id;
      this.axios({
        method: "GET",
        url: url,
        data: {},
      })
        .then((res) => {
          console.log(res.data);
          let code = res.data.code;
          console.log(JSON.stringify(res.data));
          if (code == 200) {
            this.$data.form = res.data.data;
          } else {
            this.$message.error("加载资源失败");
          }
        })
        .catch((error) => {
          // this.$message.error('加载资源失败');
          console.log(error);
        });
    },
    addHandleClick: function () {
      this.$data.isEditor = false;
      this.$data.dialogAddFormVisible = true;
      this.$data.form.id = 0;
      this.$nextTick(() => {
        this.$refs["form"].resetFields();
      });
    },
    handleCurrentChange: function (val) {
      console.log(`当前页: ${val}`);
      this.$data.currentPage = val;
      this.listResources();
    },
    saveOrUpdate: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.axios({
            method: "POST",
            url: ADD_OR_EDITOR_RESOURCES_URL,
            data: this.form,
          })
            .then((res) => {
              console.log(res.data);
              let code = res.data.code;
              console.log(JSON.stringify(res.data));
              if (code == 200) {
                this.listResources();
                this.$data.dialogAddFormVisible = false;
              }else if(code == 20004){
                this.$message.error('请先修改数据在更新');
              }
            })
            .catch((error) => {
              // this.$message.error('加载资源失败');
              console.log(error);
            });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    batchCreate: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.axios({
            method: "POST",
            url: BATCH_CREATE_URL,
            data: this.form,
          })
                  .then((res) => {
                    console.log(res.data);
                    let code = res.data.code;
                    console.log(JSON.stringify(res.data));
                    if (code == 200) {
                      this.listResources();
                      this.dialogBatchVisible = false;
                    }else if(code == 20003){
                      this.$message.error('已经存在相同资源');
                    }
                  })
                  .catch((error) => {
                    // this.$message.error('加载资源失败');
                    console.log(error);
                  });
        } else {
          console.log("error submit!!");
          return false;
        }
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
      let id = row.id;

      let url = DELETE_RESOURCES_URL + "?ids=" + id;

      this.axios({
        method: "GET",
        url: url,
        data: {},
      })
        .then((res) => {
          console.log(res.data);
          let code = res.data.code;
          console.log(JSON.stringify(res.data));
          if (code == 200) {
            this.listResources();
          } else if (code == 20001) {
            this.$message.warning("请先删除子项");
          } else if (code == 20002) {
            this.$message.warning("资源已经使用，请在角色管理中解绑后，在操作");
          }
        })
        .catch((error) => {
          // this.$message.error('加载资源失败');
          console.log(error);
        });
    },
    typeFormatter: function (row, column, cellValue) {
      return cellValue == 0 ? "菜单" : "按钮";
    },
    availableFormatter: function (row, column, cellValue) {
      return cellValue ? "可用" : "不可用";
    },
    resetForm: function (formName) {
      console.log(formName);
      this.$nextTick(() => {
        this.$refs[formName].resetFields();
      });
    },
    resetSearchForm: function (formName) {
      console.log(formName);
      this.$nextTick(() => {
        this.$refs[formName].resetFields();
        this.onSearch();
      });
    },
    onSearch: function () {
      console.log(JSON.stringify(this.$data.searchForm));
      this.listResources();
    },
  },
};
</script>

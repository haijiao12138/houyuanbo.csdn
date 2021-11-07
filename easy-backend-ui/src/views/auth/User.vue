<template>
    <div v-loading="loading">
        <el-row>
            <el-form
                    :inline="true"
                    :model="searchForm"
                    ref="searchForm"
                    class="demo-form-inline"
            >
                <el-form-item label="用户名" prop="username">
                    <el-input
                            v-model.trim="searchForm.username"
                            placeholder="用户名"
                    ></el-input>
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
            <el-button type="primary" @click="addHandleClick" v-auth="['/user/saveOrUpdate']">添加</el-button>
            <el-button type="primary" @click="batchDelete(tableChecked)" v-auth="['/user/delete']">批量删除</el-button>
            <p/>
        </el-row>
        <el-table :data="tableData" border style="width: 100%" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="id" label="ID"></el-table-column>
            <el-table-column prop="username" label="用户名">
            </el-table-column>
            <el-table-column
                    prop="available"
                    label="是否可用"
                    :formatter="availableFormatter"
            >
            </el-table-column>
            <el-table-column prop="sexType" label="性别" :formatter="sexFormatter" width="60">
            </el-table-column>
            <el-table-column prop="email" label="邮箱" width="180"></el-table-column>
            <el-table-column prop="tel" label="电话" width="150"></el-table-column>
            <el-table-column  prop="roles"  label="角色" width="150">
                <template slot-scope="scope">
                <span
                    v-for='role in scope.row.roles' :key="role.id"
                    >{{role.name}}
                </span>
                </template>
            </el-table-column>


            <el-table-column
                    prop="updateTime"
                    label="更新日期"
                    width="180"
                    :formatter="carTimeFilter"
            >
            </el-table-column>
            <el-table-column
                    prop="createTime"
                    label="创建日期"
                    width="180"
                    :formatter="carTimeFilter"
            >
            </el-table-column>
            <el-table-column fixed="right" label="操作" width="150">
                <template slot-scope="scope">
                    <el-button
                            type="text"
                            @click="modifyPasswordHandleClick(scope.row)"
                            size="small"
                    >修改密码
                    </el-button>
                    <el-button
                            type="text"
                            @click="roleHandleClick(scope.row)"
                            size="small"
                    >设置权限
                    </el-button>
                    <el-button
                            type="text"
                            @click="editorHandleClick(scope.row)"
                            size="small"
                            v-auth="['/user/saveOrUpdate']"
                    >编辑
                    </el-button
                    >
                    <el-button
                            @click="deleteHandleClick(scope.row)"
                            type="text"
                            v-auth="['/user/delete']"
                            size="small"
                    >删除
                    </el-button
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

        <el-dialog
                title="设置权限"
                :visible.sync="authVisible"
                width="30%">
          <span>
              <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll"
                           @change="handleCheckAllChange">全选</el-checkbox>
              <div style="margin: 15px 0;"></div>
              <el-checkbox-group v-model="checkedRoles" @change="handleCheckedRolesChange">
                <el-checkbox v-for="role in roles" :label="role.id"  :key="role.id">{{role.description}}</el-checkbox>
              </el-checkbox-group>
          </span>
            <span slot="footer" class="dialog-footer">
            <el-button @click="authVisible = false">取 消</el-button>
            <el-button type="primary" @click="changeRolesClick">确 定</el-button>
          </span>
        </el-dialog>

        <el-dialog
                title="修改密码"
                :visible.sync="modifyPasswordVisible"
                width="30%">
          <span>
              <el-input v-model.trim="password" type="password"
                        autocomplete="off" placeholder="请输入新的密码"></el-input>
          </span>
            <span slot="footer" class="dialog-footer">
            <el-button @click="modifyPasswordVisible = false">取 消</el-button>
            <el-button type="primary" @click="modifyPasswordClick">确 定</el-button>
          </span>
        </el-dialog>

        <el-dialog
                :title="isEditor ? '编辑' : '添加'"
                :visible.sync="dialogAddFormVisible"
        >
            <el-form :model="form" :rules="rules" ref="form">
                <el-input type="hidden" v-model="form.id" autocomplete="off"></el-input>
                <el-form-item
                        prop="username"
                        label="用户名称"
                        :label-width="formLabelWidth"
                >
                    <el-input v-model="form.username" autocomplete="off"></el-input>
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
                        prop="sexType"
                        label="性别"
                        :label-width="formLabelWidth"
                >
                    <el-switch
                            v-model="form.sexType"
                            active-text="女"
                            inactive-text="男"
                            :active-value=1
                            :inactive-value=0
                    >
                    </el-switch>
                </el-form-item>
                <el-form-item
                        prop="email"
                        label="邮箱"
                        :label-width="formLabelWidth"
                >
                    <el-input v-model="form.email" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item
                        prop="tel"
                        label="电话"
                        :label-width="formLabelWidth"
                >
                    <el-input v-model="form.tel" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="resetForm('form')" v-if="!isEditor">重 置</el-button>
                <el-button @click="dialogAddFormVisible = false" v-if="isEditor"
                >取 消
                </el-button>
                <el-button type=" primary" @click="saveOrUpdate">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import {
        MODIFY_PASSWORD,
        LIST_USER_URL,
        FIND_USER_URL,
        ADD_OR_EDITOR_USER_URL,
        DELETE_USER_URL,
        LOAD_ROLES_URL,
        UPDATE_USER_AND_ROLE,
    } from "../../utils/api.js";
    import moment from "moment";

    export default {
        data() {
            return {
                roleOptions: [],
                checkAll: false,
                checkedRoles: [],
                roles: '',
                isIndeterminate: true,
                loading: true,
                // 默认显示第几页
                currentPage: 1,
                // 总条数，根据接口获取数据长度(注意：这里不能为空)
                totalCount: 1,
                pageSize: 10,
                tableData: [],
                tableChecked: [],//被选中的记录
                ids: [],//批量删除的id
                authUserId: '', // 定义需要设置角色的用户id
                searchForm: {
                    username: "",
                    available: ""
                },
                isEditor: false,
                dialogAddFormVisible: false,
                authVisible: false,
                modifyPasswordVisible:false,
                password:'',
                form: {
                    id: "",
                    username: "",
                    available: true,
                    sexType: "",
                    email: "",
                    tel: "",
                    createtime: "",
                    updatetime: ""
                },
                formLabelWidth: '120px',
                rules: {
                    username: [{
                        required: true,
                        message: '请输入用户名称',
                        trigger: 'blur'
                    },
                        {
                            min: 2,
                            max: 20,
                            message: '长度在 2 到 20个字符',
                            trigger: 'blur'
                        }
                    ],
                    available: [{
                        required: true
                    }],
                    sexType: [{
                        required: true
                    }],
                    email: [{
                        pattern: /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/,
                        required: false,
                        message: '请输入正确的邮箱格式',
                        trigger: 'blur'
                    }],
                    tel: [{
                        required: false,
                        message: '请输入用户联系方式',
                        trigger: 'blur'
                    }]
                }
            };
        },
        created: function () {
            this.pageUser();
            this.loadAllRoles();
        },
        methods: {
            loadAllRoles: function(){ //加载全部可用的角色资源
                let url = LOAD_ROLES_URL;
                this.axios({
                    method: "POST",
                    url: url
                })
                    .then((res) => {
                        this.$data.loading = false;
                        let code = res.data.code;
                        console.log(JSON.stringify(res.data));
                        if (code == 200) {
                            this.roles = res.data.data;
                            this.roleOptions = this.roles;
                        }
                    })
                    .catch((error) => {
                        console.log(error);
                    });
            },
            pageUser: function () {
                this.$data.loading = true;
                let url = LIST_USER_URL + "/" + this.currentPage + "/" + this.pageSize;
                this.axios({
                    method: "POST",
                    url: url,
                    data: this.$data.searchForm,
                })
                    .then((res) => {
                        this.$data.loading = false;
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
            availableFormatter: function (row, column, cellValue) {
                return cellValue ? "可用" : "不可用";
            },
            sexFormatter: function (row, column, cellValue) {
                return cellValue ? "女" : "男";
            },
            carTimeFilter: function (row, column, cellValue) {
                if (cellValue != null) {
                    return moment(cellValue).format("YYYY-MM-DD HH:mm:ss");
                }
            },
            onSearch: function () {
                this.pageUser();
            },
            resetSearchForm: function (formName) {
                console.log(formName);
                this.$nextTick(() => {
                    this.$refs[formName].resetFields();
                    this.onSearch();
                });
            },
            addHandleClick: function () {
                this.$data.isEditor = false;
                this.$data.dialogAddFormVisible = true
                this.$data.form.id = 0;
                this.$nextTick(() => {
                    this.$refs['form'].resetFields();
                })
            },
            saveOrUpdate: function () {
                if (this.$data.isEditor == false) {
                    this.$data.form.createtime = new Date();
                    this.$data.form.updatetime = new Date();
                } else {
                    this.$data.form.updatetime = new Date();
                }
                this.$refs['form'].validate((valid) => {
                    if (valid) {
                        this.axios({
                            method: 'POST',
                            url: ADD_OR_EDITOR_USER_URL,
                            data: this.form
                        }).then(res => {
                            console.log(res.data);
                            let code = res.data.code
                            console.log(JSON.stringify(res.data))
                            if (code == 200) {
                                this.pageUser();
                                this.$data.dialogAddFormVisible = false
                            } else {
                                this.$message.error('操作失败');
                            }

                        }).catch(error => {
                            console.log(error);
                        });
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            editorHandleClick: function (row) {
                console.log(row);
                this.isEditor = true;
                this.dialogAddFormVisible = true;
                let url = FIND_USER_URL + "/" + row.id;
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
            deleteHandleClick: function (row) {
                this.$confirm('此操作将永久删除该信息, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$data.ids.push(row.id)
                    this.delete()
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            delete: function () {
                let data = this.$data.ids
                console.log(data)
                let urlChild = ''
                data.forEach((e) => {
                    urlChild += 'ids=' + e + '&'
                })
                urlChild = urlChild.substring(0, urlChild.lastIndexOf('&'))
                let url = DELETE_USER_URL + '/?' + urlChild

                this.axios({
                    method: 'GET',
                    url: url,
                    data: {}

                }).then(res => {
                    console.log(res.data);
                    let code = res.data.code
                    console.log(JSON.stringify(res.data))
                    this.$data.ids = new Array()
                    if (code == 200) {
                        this.pageUser();
                    } else {
                        this.$message.error('操作失败');
                    }

                }).catch(error => {
                    console.log(error);
                });
            },
            handleSelectionChange: function (val) {
                console.log("handleSelectionChange--", val);
                this.tableChecked = val;
            },
            batchDelete: function (rows) {
                let _this = this;
                console.log(rows.length);
                if (rows.length <= 0) {
                    this.$message.warning('请先选择需要删除的数据');
                    return;
                }
                _this.$confirm('是否删除选中的所有用户?删除后无法恢复!', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    rows.forEach(element => {
                        _this.ids.push(element.id)
                    })
                    this.delete()
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            handleCurrentChange: function (val) {
                console.log(`当前页: ${val}`);
            },
            handleCheckAllChange(val) {
                this.checkedRoles = val ? this.roleOptions : [];
                this.isIndeterminate = false;
            },
            handleCheckedRolesChange(value) {
                let checkedCount = value.length;
                this.checkAll = checkedCount === this.roles.length;
                this.isIndeterminate = checkedCount > 0 && checkedCount < this.roles.length;
            },
            changeRolesClick: function(){ //改变用户角色
                console.log(JSON.stringify(this.checkedRoles))
                let url = UPDATE_USER_AND_ROLE + "/" +  this.authUserId;
                this.axios({
                    method: "POST",
                    url: url,
                    data:  this.checkedRoles,
                })
                    .then((res) => {
                        console.log(res.data);
                        let code = res.data.code;
                        if (code == 200) {
                            this.authVisible = false;
                            this.$message.success("设置成功");
                            this.pageUser();
                        } else {
                            this.$message.error("加载资源失败");
                        }
                    })
                    .catch((error) => {
                        // this.$message.error('加载资源失败');
                        console.log(error);
                    });
            },
            roleHandleClick: function(row){
               this.checkedRoles = [];
                this.authUserId = '';
                this.authVisible = true;
                let that = this
                row.roles.forEach(function(element){
                    that.checkedRoles.push(element.id);
                });
                this.checkAll = this.checkedRoles.length == this.roles.length;
                this.isIndeterminate = this.checkedRoles.length > 0 && this.checkedRoles.length < this.roles.length;
                console.log(JSON.stringify(this.checkedRoles))
                this.authUserId = row.id
            },
            modifyPasswordHandleClick: function(row){
                this.password = '';
                this.authUserId = row.id
                this.modifyPasswordVisible = true;
            },
            modifyPasswordClick: function(){ //修改密码
                let url = MODIFY_PASSWORD;
                if( this.password.length <= 0){
                    this.$message.warning("密码不能为空");
                    return;
                }
                this.axios({
                    method: "POST",
                    url: url,
                    data: {
                        'id': this.authUserId,
                        'password': this.password
                    },
                }).then((res) => {
                        console.log(res.data);
                        let code = res.data.code;
                        if (code == 200) {
                            this.modifyPasswordVisible = false;
                            this.$message.success("密码修改成功");
                            this.pageUser();
                        }
                    })
                    .catch((error) => {
                        // this.$message.error('加载资源失败');
                        console.log(error);
                    });
            },

        },
    };
</script>

<style>
</style>
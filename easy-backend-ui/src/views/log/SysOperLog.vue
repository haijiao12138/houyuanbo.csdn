<template>
    <div>
                <el-row>
            <el-form :inline="true" :model="searchForm" ref="searchForm" class="demo-form-inline" >
                            <el-form-item label="操作" prop="operation">
                                <el-input v-model.trim="searchForm.operation" placeholder="操作">
                                </el-input>
                            </el-form-item>
                         <el-form-item label="操作时间" prop="createTime">
                             <el-date-picker
                                     v-model.trim="searchDate"
                                     unlink-panels
                                     type="datetimerange"
                                     range-separator="至"
                                     @change="dateChange()"
                                     value-format="yyyy-MM-dd HH:mm:ss"
                                     start-placeholder="开始日期"
                                     end-placeholder="结束日期">
                             </el-date-picker>
                         </el-form-item>
                            <el-form-item label="操作用户" prop="operName">
                                <el-input v-model.trim="searchForm.operName" placeholder="操作用户">
                                </el-input>
                            </el-form-item>
                            <el-form-item label="请求的ip地址" prop="ip">
                                <el-input v-model.trim="searchForm.ip" placeholder="请求的ip地址">
                                </el-input>
                            </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="onSearch">查 询</el-button>
                    <el-button @click="resetSearchForm">重 置</el-button>
                </el-form-item>
            </el-form>
        </el-row>
        <!-- 添加和删除按钮区域 -->
        <el-row>
            <el-button type="primary" @click="exportExcel" v-auth="['/sysOperLog/export']">
                导出数据
            </el-button>
            <p />
        </el-row>
        <!-- 数据列表表单区域 -->
        <el-table :data="tableData" border style="width: 100%" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"> </el-table-column>
                      <el-table-column
                              prop="id"
                              label="ID"
                      ></el-table-column>
                      <el-table-column
                              prop="operation"
                              label="操作"
                      ></el-table-column>
                      <el-table-column
                              prop="businessType"
                              label="业务类型"
                      ></el-table-column>
                      <el-table-column
                              prop="method"
                              label="方法名称"
                      ></el-table-column>
                      <el-table-column
                              prop="createTime"
                              label="操作时间"
                              :formatter="carTimeFilter"
                      ></el-table-column>
                      <el-table-column
                              prop="operName"
                              label="操作用户"
                      ></el-table-column>
                      <el-table-column
                              prop="params"
                              label="参数"
                      ></el-table-column>
                      <el-table-column
                              prop="ip"
                              label="请求的ip地址"
                      ></el-table-column>

        </el-table>
        <!-- 列表分页操作区域 -->
        <el-pagination :current-page="currentPage" :page-size="pageSize" @current-change="handleCurrentChange" background
                       layout="prev, pager, next" :total="totalCount">
        </el-pagination>
        <!-- 编辑和添加页面定义 -->
        <el-dialog :title="isEditor ? '编辑' : '添加'" :visible.sync="dialogAddFormVisible">
            <el-form :model="dialogForm" :rules="rules" ref="dialogForm">
                <el-input type="hidden" v-model="dialogForm.id" autocomplete="off"></el-input>

                             <el-form-item prop="operation" label="操作" :label-width="dialogFormLabelWidth">
                                 <el-input v-model="dialogForm.operation" autocomplete="off"></el-input>
                             </el-form-item>

                             <el-form-item prop="businessType" label="业务类型" :label-width="dialogFormLabelWidth">
                                 <el-input v-model="dialogForm.businessType" autocomplete="off"></el-input>
                             </el-form-item>

                             <el-form-item prop="method" label="方法名称" :label-width="dialogFormLabelWidth">
                                 <el-input v-model="dialogForm.method" autocomplete="off"></el-input>
                             </el-form-item>

                             <el-form-item prop="operName" label="操作用户" :label-width="dialogFormLabelWidth">
                                 <el-input v-model="dialogForm.operName" autocomplete="off"></el-input>
                             </el-form-item>

                             <el-form-item prop="params" label="参数" :label-width="dialogFormLabelWidth">
                                 <el-input v-model="dialogForm.params" autocomplete="off"></el-input>
                             </el-form-item>

                             <el-form-item prop="ip" label="请求的ip地址" :label-width="dialogFormLabelWidth">
                                 <el-input v-model="dialogForm.ip" autocomplete="off"></el-input>
                             </el-form-item>


            </el-form>

            <div slot="footer" class="dialog-footer">
                <el-button @click="resetDialogFormData('dialogForm')" v-if="!isEditor">重 置</el-button>
                <el-button @click="dialogAddFormVisible = false" v-if="isEditor">取 消</el-button>
                <el-button type="primary" @click="saveOrUpdate">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import moment from "moment";
    export default {
        data() {
            return {
                searchDate:"",
                //数据分页
                currentPage: 1,//当前页码
                totalCount: 1,// 总条数，根据接口获取数据长度(注意：这里不能为空)
                pageSize: 10,//页面数据显示数量
                //数据列表表单
                tableData: [],//数据列表表单的数据域
                tableChecked:[],//批量删除时被选中行
                ids:[],//批量删除的id
                //数据更新
                updateUserId: 0,//编辑时要更新的用户id
                //搜索
                searchForm: {
                    operation:"",
                    businessType:"",
                    method:"",
                    operName:"",
                    params:"",
                    ip:"",
                },
                isEditor: false,//标志弹出框是编辑还是添加
                dialogAddFormVisible: false,//编辑表单弹出框是否显示
                dialogForm: {//弹出框的数据域
                    id:"",
                    operation:"",
                    businessType:"",
                    method:"",
                    createTime:"",
                    operName:"",
                    params:"",
                    ip:"",
                },
                dialogFormLabelWidth: '120px',//弹出表单的输入框的宽度
                rules: {//弹出表单的输入规则
                    id:[
                        {
                            required: true,
                            message: "请输入日志主键",
                            trigger: "blur",
                        }
                    ],
                    operation:[
                        {
                            required: true,
                            message: "请输入操作",
                            trigger: "blur",
                        }
                    ],
                    businessType:[
                        {
                            required: true,
                            message: "请输入业务类型",
                            trigger: "blur",
                        }
                    ],
                    method:[
                        {
                            required: true,
                            message: "请输入方法名称",
                            trigger: "blur",
                        }
                    ],
                    createTime:[
                        {
                            required: true,
                            message: "请输入操作时间",
                            trigger: "blur",
                        }
                    ],
                    operName:[
                        {
                            required: true,
                            message: "请输入操作用户",
                            trigger: "blur",
                        }
                    ],
                    params:[
                        {
                            required: true,
                            message: "请输入参数",
                            trigger: "blur",
                        }
                    ],
                    ip:[
                        {
                            required: true,
                            message: "请输入请求的ip地址",
                            trigger: "blur",
                        }
                    ],
                },
                urls:{
                    saveOrUpdate: "/v1"+"/sysOperLog/saveOrUpdate",
                    search: "/v1"+"/sysOperLog/list",
                    delete: "/v1"+"/sysOperLog/delete",
                    find: "/v1/sysOperLog/findById",
                    exportExcel: "/v1/sysOperLog/export",
                },
            };
        },
        created: function () {
            this.pageList();
        },
        methods: {
            //加载列表数据
            pageList: function () {
                let url = this.$data.urls.search + "/" + this.currentPage + "/" + this.pageSize;
                this.axios({
                    method: "POST",
                    url: url,
                    data: this.$data.searchForm,
                }).then((res) => {
                    let code = res.data.code;
                    if (code == 200) {
                        this.$data.tableData = res.data.data.records;
                        this.$data.totalCount = res.data.data.total;
                        this.$data.currentPage = res.data.data.current;
                    }
                }).catch((error) => {
                    console.log(error);
                });
            },
            onSearch: function () {
                this.pageList();
            },
            resetSearchForm: function () {
                //重置表单数据
                this.searchDate = '';
                this.resetDialogFormData('searchForm')
                this.pageList();

            },
            //重置编辑弹出框表单的数据
            resetDialogFormData: function(formName){
                if (this.$refs[formName] !== undefined) {
                    this.$refs[formName].resetFields();
                }
            },
            addHandleClick: function() {
                this.resetDialogFormData('dialogForm');
                this.$data.isEditor = false;
                this.$data.dialogAddFormVisible = true;
            },
            saveOrUpdate: function() {
                this.$refs['dialogForm'].validate((valid) => {
                    if (valid && this.checkDialogForm()) {
                        this.axios({
                            method: 'POST',
                            url: this.$data.urls.saveOrUpdate,
                            data: this.dialogForm
                        }).then(res => {
                            let code = res.data.code
                            if (code == 200) {
                                this.pageList();
                                this.$data.dialogAddFormVisible = false
                            }else if(code == 20004){
                                this.$message.error('请先修改数据在更新');
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
                this.isEditor = true;
                let url = this.$data.urls.find + "/" + row.id;
                this.axios({
                    method: "GET",
                    url: url,
                    data: {},
                }).then((res) => {
                    let code = res.data.code;
                    if (code == 200) {
                        this.resetDialogFormData('dialogForm');
                        this.$data.dialogForm = res.data.data;
                        this.dialogAddFormVisible = true;
                    }
                }).catch((error) => {
                    console.log(error)
                });
            },
            exportExcel: function () {
                let url = this.urls.exportExcel;
                this.axios({
                    method: "GET",
                    url: url,
                    data: {},
                })
                    .then((res) => {
                        let code = res.data.code;
                        if (code == 200) {
                            console.log(res.data.data);
                            window.location.href = process.env.VUE_APP_BASEURL +  "/profile/download/" + res.data.data;
                        }
                    })
                    .catch((error) => {
                        console.log(error);
                    });
            },
            deleteHandleClick: function(row) {
                this.$confirm('此操作将永久删除该信息, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$data.ids.push(row.id);
                    this.delete();
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            //删除ids中对应的
            delete: function() {
                let data = this.$data.ids
                //获得url尾部,要删除的id部分
                let urlChild = '';
                data.forEach((e)=>{
                    urlChild += 'ids=' + e + '&'
                });
                urlChild = urlChild.substring(0,urlChild.lastIndexOf('&'));

                let url = this.$data.urls.delete + '/?' + urlChild
                this.axios({
                    method: 'GET',
                    url: url,
                    data: {}
                }).then(res => {
                    let code = res.data.code;
                    this.$data.ids = new Array();//清空要删除的id数组
                    if (code == 200) {
                        this.pageList();
                    }
                }).catch(error => {
                    console.log(error);
                });
            },
            handleSelectionChange:function(val){
                this.tableChecked=val;
            },
            batchDelete:function(){
                let that = this;
                let _rows = this.$data.tableChecked;
                if(_rows.length <= 0 ){
                    this.$message.warning('请先选择需要删除的数据');
                    return;
                }
                that.$confirm('是否删除选中的所有用户?删除后无法恢复!','提示',{
                    confirmButtonText:'确定',
                    cancelButtonText:'取消',
                    type:'warning'
                }).then(()=>{
                    _rows.forEach(element =>{
                        that.ids.push(element.id)
                    })
                    this.delete();
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            handleCurrentChange: function(val) {
                this.$data.currentPage = val;
                this.pageList();
            },
            checkDialogForm(){
                return true;
            },
            carTimeFilter: function (row, column, cellValue) {
                if (cellValue != null) {
                    return moment(cellValue).format("YYYY-MM-DD HH:mm:ss");
                }
            },
            dateChange: function(){
                this.searchForm.createTimeBegin =  this.searchDate[0];
                this.searchForm.createTimeEnd =  this.searchDate[1];
                console.log(this.searchForm.createTimeBegin + " | " + this.searchForm.createTimeEnd)
            },
        },
    };
</script>

<style>
</style>
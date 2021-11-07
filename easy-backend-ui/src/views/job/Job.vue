<template>
    <div>
                <el-row>
            <el-form :inline="true" :model="searchForm" ref="searchForm" class="demo-form-inline" >
                            <el-form-item label="任务名称" prop="jobName">
                                <el-input v-model.trim="searchForm.jobName" placeholder="">
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
            <el-button type="primary" @click="addHandleClick" v-auth="['/quartz/add']">添加</el-button>
<!--            <el-button type="primary" @click="exportExcel" v-auth="['/quartz/export']">-->
<!--                导出数据-->
<!--            </el-button>-->
            <p />
        </el-row>
        <!-- 数据列表表单区域 -->
        <el-table :data="tableData" border style="width: 100%" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"> </el-table-column>
                <th width="40"></th>
                      <el-table-column
                              prop="jobName"
                              label="任务名称"
                      ></el-table-column>

                <th width="40"></th>
                      <el-table-column
                              prop="jobGroup"
                              label="任务分组"
                      ></el-table-column>

                <th width="40"></th>
                      <el-table-column
                              prop="description"
                              label="描述"
                      ></el-table-column>

                <th width="40"></th>
                      <el-table-column
                              prop="jobClassName"
                              label="执行类"
                      ></el-table-column>

                <th width="40"></th>
                      <el-table-column
                              prop="triggerState"
                              label="状态"
                              :formatter="triggerStateFilter"
                      ></el-table-column>

                <th width="40"></th>
                      <el-table-column
                              prop="cronExpression"
                              label="执行时间"
                      ></el-table-column>

                <th width="40"></th>

            <el-table-column fixed="right" label="操作"  width="220">
                <template slot-scope="scope">
                    <el-button @click="editorHandleClick(scope.row)" type="primary" icon="el-icon-edit"  title="编辑" circle v-auth="['/quartz/add']" size="mini"></el-button>
                    <el-button @click="triggerHandleClick(scope.row)" type="primary" icon="el-icon-video-play"  title="执行一次" circle v-auth="['/quartz/trigger']" size="mini"></el-button>
                    <el-button @click="resumeHandleClick(scope.row)" type="success" icon="el-icon-refresh"  title="恢复任务" circle v-auth="['/quartz/resume']" size="mini"></el-button>
                    <el-button @click="stopHandleClick(scope.row)" type="warning" icon="el-icon-video-pause"  title="停止" circle v-auth="['/quartz/pause']" size="mini"></el-button>
                    <el-button @click="deleteHandleClick(scope.row)" type="danger" icon="el-icon-delete" v-auth="['/quartz/delete']" circle size="mini"></el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 列表分页操作区域 -->
        <el-pagination :current-page="currentPage" :page-size="pageSize" @current-change="handleCurrentChange" background
                       layout="prev, pager, next" :total="totalCount">
        </el-pagination>
        <!-- 编辑和添加页面定义 -->
        <el-dialog :title="isEditor ? '编辑' : '添加'" :visible.sync="dialogAddFormVisible">
            <el-form :model="dialogForm" :rules="rules" ref="dialogForm">
                <el-input type="hidden" v-model="dialogForm.id" autocomplete="off"></el-input>
                             <el-form-item prop="jobName" label="任务名称" :label-width="dialogFormLabelWidth">
                                 <el-input v-model="dialogForm.jobName" autocomplete="off"></el-input>
                             </el-form-item>

                             <el-form-item prop="jobGroup" label="任务分组" :label-width="dialogFormLabelWidth">
                                 <el-input v-model="dialogForm.jobGroup" autocomplete="off"></el-input>
                             </el-form-item>

                             <el-form-item prop="description" label="任务描述" :label-width="dialogFormLabelWidth">
                                 <el-input v-model="dialogForm.description" autocomplete="off"></el-input>
                             </el-form-item>

                             <el-form-item prop="jobClassName" label="执行类" :label-width="dialogFormLabelWidth">
                                 <el-input v-model="dialogForm.jobClassName" autocomplete="off"></el-input>
                             </el-form-item>

                             <el-form-item prop="cronExpression" label="执行时间" :label-width="dialogFormLabelWidth">
                                 <el-popover v-model="cronPopover">
                                     <cron @change="changeCron" @close="cronPopover=false" i18n="cn"></cron>
                                     <el-input slot="reference" @click="cronPopover=true" v-model="dialogForm.cronExpression" placeholder="请输入定时策略"></el-input>
                                 </el-popover>
<!--                                 <el-button  @click="addJobData()">添加job参数</el-button>-->
                             </el-form-item>

<!--                            <el-form-item v-for="(item, index) in dialogForm.jobDataParam" :key="index">-->
<!--                                <input v-model="item.paramName" placeholder="参数名"/>-->
<!--                                <input v-model="item.paramValue" placeholder="参数值" />-->
<!--                                <button @click="delJobParam(index)" icon="close">移除</button>-->
<!--                            </el-form-item>-->
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
    import {cron} from 'vue-cron';
    export default {
        components: { cron },
        data() {
            return {
                cronPopover:false,
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

                },
                isEditor: false,//标志弹出框是编辑还是添加
                dialogAddFormVisible: false,//编辑表单弹出框是否显示
                dialogForm: {//弹出框的数据域

                },
                dialogFormLabelWidth: '120px',//弹出表单的输入框的宽度
                rules: {//弹出表单的输入规则

                    jobName:[
                        {
                            required: true,
                            message: "请输入任务名称",
                            trigger: "blur",
                        }
                    ],
                    jobGroup:[
                        {
                            required: true,
                            message: "请输入任务分组",
                            trigger: "blur",
                        }
                    ],
                    description:[
                        {
                            required: true,
                            message: "请输入任务描述",
                            trigger: "blur",
                        }
                    ],
                    jobClassName:[
                        {
                            required: true,
                            message: "请输入执行类",
                            trigger: "blur",
                        }
                    ],
                    cronExpression:[
                        {
                            required: true,
                            message: "请输入执行时间",
                            trigger: "blur",
                        }
                    ],
                },
                urls:{
                    saveOrUpdate: "/v1"+"/quartz/add",
                    search: "/v1"+"/quartz/list",
                    delete: "/v1"+"/quartz/delete",
                    find: "/v1/quartz/findById",
                    exportExcel: "/v1/quartz/export",
                    pause: "/v1/quartz/pause",
                    trigger: "/v1/quartz/trigger",
                    resume: "/v1/quartz/resume",
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
                this.resetDialogFormData('dialogForm');
                this.$data.dialogForm = row;
                this.dialogAddFormVisible = true;
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
                let url = this.urls.delete;
                this.execHandleClick(url, '是否删除此任务?', row);
            },
            stopHandleClick: function(row) {
                let url = this.urls.pause;
                this.execHandleClick(url, '是否停止此任务?', row);
            },
            triggerHandleClick: function(row) {
                let url = this.urls.trigger;
                this.execHandleClick(url, '执行此任务一次?', row);
            },
            resumeHandleClick: function(row) {
                let url = this.urls.resume;
                this.execHandleClick(url, '是否确定恢复此任务?', row);
            },
            execHandleClick:function (url, msg, row) {
                this.$confirm(msg , '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.axios({
                        method: "POST",
                        url: url,
                        data: {
                            'jobName': row.jobName,
                            'jobGroup': row.jobGroup
                        },
                    }).then((res) => {
                        let code = res.data.code;
                        if (code == 200) {
                            this.$message.success("执行成功");
                            this.pageList();
                        }
                    }).catch((error) => {
                        console.log(error);
                    });
                }).catch(() => {

                });
            },
            handleSelectionChange:function(val){
                this.tableChecked=val;
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
            triggerStateFilter: function (row, column, cellValue) {
                let backVal = cellValue
                if(cellValue == 'PAUSED'){
                    backVal = "暂停";
                }
                if(cellValue == 'ACQUIRED'){
                    backVal = "正在按照规则运行";
                }
                return backVal;
            },
            dateChange: function(){
                this.searchForm.createTimeBegin =  this.searchDate[0];
                this.searchForm.createTimeEnd =  this.searchDate[1];
                console.log(this.searchForm.createTimeBegin + " | " + this.searchForm.createTimeEnd)
            },
            changeCron(val){
                this.dialogForm.cronExpression=val
            },
        },
    };
</script>

<style>
</style>
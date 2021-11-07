<template>
    <div>
        <#-- 搜索区域 -->
        <el-row>
            <el-form :inline="true" :model="searchForm" ref="searchForm" class="demo-form-inline" >
                <#list table.fields as field >
                <#--这些字段不搜索-->
                    <#if (field.propertyName != "id" && field.propertyName != "createTime" && field.propertyName != "updateTime") >
                    <#--时间格式暂时不搜索-->
                        <#if field.type == 'datetime'>
                            <el-form-item label="${field.comment?split("#")[0]}" prop="${field.propertyName}">
                                <el-date-picker type="datetime" v-model.trim="searchForm.${field.propertyName}" placeholder="${field.comment?split("#")[0]}">
                                </el-date-picker>
                            </el-form-item>
                        <#else>
                            <el-form-item label="${field.comment?split("#")[0]}" prop="${field.propertyName}">
                                <el-input v-model.trim="searchForm.${field.propertyName}" placeholder="${field.comment?split("#")[0]}">
                                </el-input>
                            </el-form-item>
                        </#if>
                    </#if>
                     <#if field.propertyName == "createTime">
                         <el-form-item label="${field.comment?split("#")[0]}" prop="createTime">
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
                     </#if>
                </#list>

                <el-form-item>
                    <el-button type="primary" @click="onSearch">查 询</el-button>
                    <el-button @click="resetSearchForm">重 置</el-button>
                </el-form-item>
            </el-form>
        </el-row>
        <#-- 添加和删除按钮区域 -->
        <!-- 添加和删除按钮区域 -->
        <el-row>
            <el-button type="primary" @click="addHandleClick" v-auth="['/${entity?uncap_first}/saveOrUpdate']">添加</el-button>
            <el-button type="primary" @click="batchDelete" v-auth="['/${entity?uncap_first}/delete']">批量删除</el-button>
            <el-button type="primary" @click="exportExcel" v-auth="['/${entity?uncap_first}/export']">
                导出数据
            </el-button>
            <p />
        </el-row>
        <#-- 数据列表表单区域 -->
        <!-- 数据列表表单区域 -->
        <el-table :data="tableData" border style="width: 100%" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"> </el-table-column>

            <#list table.fields as field >
                  <#if field.type == 'datetime'>
                      <el-table-column
                              prop="${field.propertyName}"
                              label="<#if field.propertyName == 'id'>ID<#else>${field.comment?split("#")[0]}</#if>"
                              :formatter="carTimeFilter"
                      ></el-table-column>
                  <#else>
                      <el-table-column
                              prop="${field.propertyName}"
                              label="<#if field.propertyName == 'id'>ID<#else>${field.comment?split("#")[0]}</#if>"
                      ></el-table-column>
                  </#if>
            </#list>

            <el-table-column fixed="right" label="操作" >
                <template slot-scope="scope">
                    <el-button type="text" @click="editorHandleClick(scope.row)" v-auth="['/${entity?uncap_first}/saveOrUpdate']" size="small">编辑</el-button>
                    <el-button @click="deleteHandleClick(scope.row)" type="text" v-auth="['/${entity?uncap_first}/delete']" size="small">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <#-- 列表分页操作区域 -->
        <!-- 列表分页操作区域 -->
        <el-pagination :current-page="currentPage" :page-size="pageSize" @current-change="handleCurrentChange" background
                       layout="prev, pager, next" :total="totalCount">
        </el-pagination>
        <#-- 编辑和添加页面定义 -->
        <!-- 编辑和添加页面定义 -->
        <el-dialog :title="isEditor ? '编辑' : '添加'" :visible.sync="dialogAddFormVisible">
            <el-form :model="dialogForm" :rules="rules" ref="dialogForm">
                <el-input type="hidden" v-model="dialogForm.id" autocomplete="off"></el-input>

                <#list table.fields as field >
                    <#if (field.propertyName != "id" && field.propertyName != "createTime" && field.propertyName != "updateTime")>
                         <#if field.type == 'datetime'>
                             <el-form-item prop="${field.propertyName}" label="${field.comment?split("#")[0]}" :label-width="dialogFormLabelWidth">
                                 <el-date-picker type="datetime" v-model="dialogForm.${field.propertyName}" autocomplete="off"></el-date-picker>
                             </el-form-item>
                         <#else>
                             <el-form-item prop="${field.propertyName}" label="${field.comment?split("#")[0]}" :label-width="dialogFormLabelWidth">
                                 <el-input v-model="dialogForm.${field.propertyName}" autocomplete="off"></el-input>
                             </el-form-item>
                         </#if>

                    </#if>
                </#list>

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
                    <#--定义搜索框的数据域-->
                    <#list table.fields as field>
                    <#--这些字段不搜索-->
                    <#if (field.propertyName != "id" && field.propertyName != "createTime" && field.propertyName != "updateTime") >
                    <#--时间格式暂时不搜索-->
                    <#if field.type == 'datetime'>
                    <#else>
                    ${field.propertyName}:"",
                    </#if>
                    </#if>
                    </#list>
                },
                isEditor: false,//标志弹出框是编辑还是添加
                dialogAddFormVisible: false,//编辑表单弹出框是否显示
                dialogForm: {//弹出框的数据域
                    <#--这里显示表中所有的字段，默认值是空字符串-->
                    <#list table.fields as field >
                    ${field.propertyName}:"",
                    </#list>
                },
                dialogFormLabelWidth: '120px',//弹出表单的输入框的宽度
                rules: {//弹出表单的输入规则
                    <#list table.fields as field >
                    ${field.propertyName}:[
                        {
                            required: true,
                            message: "请输入${field.comment?split("#")[0]}",
                            trigger: "blur",
                        }
                    ],
                    </#list>
                },
                urls:{
                    saveOrUpdate: "/v1"+"/${entity?uncap_first}/saveOrUpdate",
                    search: "/v1"+"/${entity?uncap_first}/list",
                    delete: "/v1"+"/${entity?uncap_first}/delete",
                    find: "/v1/${entity?uncap_first}/findById",
                    exportExcel: "/v1/${entity?uncap_first}/export",
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
                this.$data.isEditor = false;
                this.$data.dialogAddFormVisible = true;
                this.resetDialogFormData('dialogForm');
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
                        this.dialogAddFormVisible = true;
                        this.resetDialogFormData('dialogForm');
                        this.$nextTick(() => {
                            this.$data.dialogForm = res.data.data;
                        })
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
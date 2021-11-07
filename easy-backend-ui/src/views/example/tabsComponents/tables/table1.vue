<template>
    <div>
        <!-- 添加和删除按钮区域 -->
        <el-row>
            <el-button type="primary">添加</el-button>
            <el-button type="primary">批量删除</el-button>
            <el-button type="primary">
                导出数据
            </el-button>
            <p/>
        </el-row>
        <el-table :data="tableData" border style="width: 100%" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"> </el-table-column>

            <el-table-column
                    prop="id"
                    label="ID"
            ></el-table-column>

            <th width="40"></th>
            <el-table-column
                    prop="name"
                    label="姓名"
            ></el-table-column>

            <th width="40">姓名</th>
            <el-table-column
                    prop="createTime"
                    label="创建时间"
                    :formatter="carTimeFilter"
            ></el-table-column>

            <th width="40">创建时间</th>

            <el-table-column fixed="right" label="操作" >
                <template slot-scope="scope">
                    <el-button type="text" @click="editorHandleClick(scope.row)" v-auth="['/test/saveOrUpdate']" size="small">编辑</el-button>
                    <el-button @click="deleteHandleClick(scope.row)" type="text" v-auth="['/test/delete']" size="small">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 列表分页操作区域 -->
        <el-pagination :current-page="currentPage" :page-size="pageSize" @current-change="handleCurrentChange" background
                       layout="prev, pager, next" :total="totalCount">
        </el-pagination>
    </div>

</template>

<script>
    import moment from "moment";
    export default {
        name: "table1",
        props: ['searchForm', 'searchKey'],
        data(){
            return {
                tableData: [],
                //数据分页
                currentPage: 1,//当前页码
                totalCount: 1,// 总条数，根据接口获取数据长度(注意：这里不能为空)
                pageSize: 10,//页面数据显示数量
                urls:{
                    saveOrUpdate: "/v1"+"/test/saveOrUpdate",
                    search: "/v1"+"/test/list",
                    delete: "/v1"+"/test/delete",
                    find: "/v1/test/findById",
                    exportExcel: "/v1/test/export",
                },
            }
        },
        created: function () {
            this.pageList();
        },
        watch: {
            searchKey: {
                handler(newVal) {
                    console.log(JSON.stringify(newVal));
                    console.log(JSON.stringify(this.searchForm))
                    this.pageList();
                },
                deep: true
            }
        },
        methods:{
            //加载列表数据
            pageList: function () {
                let url = this.$data.urls.search + "/" + this.currentPage + "/" + this.pageSize;
                this.axios({
                    method: "POST",
                    url: url,
                    data: this.searchForm,
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
            carTimeFilter: function (row, column, cellValue) {
                if (cellValue != null) {
                    return moment(cellValue).format("YYYY-MM-DD HH:mm:ss");
                }
            },
        }
    }
</script>

<style scoped>

</style>
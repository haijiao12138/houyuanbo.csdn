<template>
    <div>
        <el-container>
            <el-header>
                <div>
                    <!-- 搜索区域 -->
                    <el-row>
                        <el-form :inline="true"
                                 :model="firstFrom"
                                  v-if="activeName == 'first'" ref="firstFrom" class="demo-form-inline">
                            <el-form-item label="姓名" prop="name">
                                <el-input v-model.trim="firstFrom.name" placeholder="姓名">
                                </el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-button type="primary" @click="firstSearchHandleClick">查 询</el-button>
                                <el-button @click="firstSearchHandleClick">重 置</el-button>
                            </el-form-item>
                        </el-form>
                        <el-form :inline="true" v-if="activeName == 'second'" ref="searchForm" class="demo-form-inline">
                            <el-form-item label="创建时间" prop="createTime">
                                <el-date-picker
                                        unlink-panels
                                        type="datetimerange"
                                        range-separator="至"
                                        @change="dateChange()"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        start-placeholder="开始日期"
                                        end-placeholder="结束日期">
                                </el-date-picker>

                            </el-form-item>

                            <el-form-item>
                                <el-button type="primary">查 询</el-button>
                                <el-button >重 置</el-button>
                            </el-form-item>
                        </el-form>
                    </el-row>

                </div>
            </el-header>
            <el-container>
                <el-main width="200px">
                    <el-tabs v-model="activeName" tab-position="left" @tab-click="handleClick">
                        <el-tab-pane name="first" label="用户管理">
                            <Table1 :searchForm="firstFrom" :searchKey="firstSearchKey"/>
                        </el-tab-pane>
                        <el-tab-pane label="配置管理" name="second">配置管理</el-tab-pane>
                        <el-tab-pane label="角色管理" name="third">角色管理</el-tab-pane>
                        <el-tab-pane label="定时任务补偿" name="fourth">定时任务补偿</el-tab-pane>
                    </el-tabs>
                </el-main>

            </el-container>
        </el-container>

    </div>
</template>

<script>
    import Table1 from "./tables/table1";

    export default {
        name: "Tab1",

        components:{
            Table1
        },
        data() {
            return {
                activeName: 'first',
                firstFrom: {
                },
                firstSearchKey: '',
            }
        },
        methods: {
            handleClick(tab, event) {
                console.log(tab, event);
            },
            firstSearchHandleClick: function () {
                let timestamp = Date.parse(new Date()) + Math.round(Math.random()*100)   // 生成0~5的随机整数;
                this.firstSearchKey = timestamp;
            }
        }
    }
</script>

<style scoped>

</style>
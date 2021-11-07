<template>
	<div v-loading="loading">
			<el-row>
			<el-form :inline="true" :model="searchForm" ref="searchForm" class="demo-form-inline">
				<el-form-item label="角色名称" prop="name">
					<el-input v-model.trim="searchForm.name" placeholder="角色名称"></el-input>
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
			<p />
		</el-row>
		<el-table :data="tableData"  border style="width: 100%">
			<el-table-column type="selection" width="55">
			</el-table-column>
			<el-table-column prop="id" label="ID" width="180">
			</el-table-column>
			<el-table-column prop="name" label="角色名称" width="180">
			</el-table-column>
			<el-table-column prop="description" label="角色描述">
			</el-table-column>
			<el-table-column prop="available" label="是否可用" :formatter="availableFormatter">
			</el-table-column>
			<el-table-column fixed="right" label="操作" width="150">
				<template slot-scope="scope">
					<el-button type="text" size="small" @click="settingAuth(scope.row)">设置权限</el-button>
					<el-button type="text" @click="editorHandleClick(scope.row)" size="small">编辑</el-button>
					<el-button @click="deleteHandleClick(scope.row)" type="text" v-auth="['/role/delete']" size="small">删除</el-button>
				</template>
			</el-table-column>
		</el-table>
        <el-pagination :current-page="currentPage" :page-size="pageSize" @current-change="handleCurrentChange" background
		 layout="prev, pager, next" :total="totalCount">
		</el-pagination>
		<el-drawer title="设置权限" :visible.sync="drawer" :with-header="true" custom-class="tree-drawer">
			<el-tree 
			default-expand-all 
			:default-checked-keys="checkeds" 
			node-key="id" 
			show-checkbox 
			ref="tree"
			:data="resourcesData" 
			:props="defaultProps">
			</el-tree>
			
			<div class="buttons drawer__footer" style="text-align: center;">
			  <el-button @click="closeDrawer">取 消</el-button>
			  <el-button type="primary" @click="updateAuth">设 置</el-button>
			</div>
		</el-drawer>

		<el-dialog :title="isEditor ? '编辑角色' : '添加角色' " :visible.sync="dialogAddFormVisible">
			<el-form :model="form" :rules="rules" ref="form">
				<el-input type="hidden" v-model="form.id" autocomplete="off"></el-input>
				<el-form-item prop="name" label="角色名称" :label-width="formLabelWidth">
					<el-input v-model="form.name" autocomplete="off"></el-input>
				</el-form-item>
			
				<el-form-item prop="description" label="角色描述" :label-width="formLabelWidth">
					<el-input v-model="form.description" autocomplete="off"></el-input>
				</el-form-item>
				<el-form-item prop="available" label="是否可用" :label-width="formLabelWidth">
					<el-switch v-model="form.available" active-text="可用" inactive-text="不可用">
					</el-switch>
				</el-form-item>
				
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="resetForm('form')" v-if="!isEditor">重 置</el-button>
				<el-button @click="dialogAddFormVisible = false" v-if="isEditor">取 消</el-button>
				<el-button type=" primary" @click="saveOrUpdate">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import {
		LIST_ROLES_URL,
		LOAD_ALL_RESOURCES_URL,
		UPDATE_ROLES_RESOURCES_URL,
		FIND_ROLE_URL,
		ADD_OR_EDITOR_ROLE_URL,
		DELETE_ROLE_URL,
		SELECT_ROLE_RESOURCES_URL
	} from '../../utils/api.js'
	export default {
		data() {
			return {
				loading: true,
				// 默认显示第几页
				currentPage: 1,
				// 总条数，根据接口获取数据长度(注意：这里不能为空)
				totalCount: 1,
				pageSize: 10,
				tableData: [],
				defaultProps: {
					children: 'childResources',
					label: 'name'
				},
				isEditor: false,
				drawer: false,
				resourcesData: [],
				checkeds: [],
				updateRoleId: 0,
				formLabelWidth: '120px',
				dialogAddFormVisible: false,
				form: {
					id: '',
					name: '',
					available: true,
					description: "",
				},
				rules: {
					name: [{
							required: true,
							message: '请输入角色名称',
							trigger: 'blur'
						},
						{
							min: 1,
							max: 20,
							message: '长度在 1 到 20个字符',
							trigger: 'blur'
						}
					],
				},
				searchForm: {
					name: '',
					available: ''
				},
			}
		},
		created: function() {
			this.pageRoles();
			this.lookAllResources();
		},
		methods: {
			closeDrawer() {
			   this.drawer = false;
			},
			updateAuth() {
				let resourceIds = [];
				this.$refs.tree.getCheckedNodes().forEach(function(item){
					if(item.parentId != 0){
						resourceIds.push(item.id)
						resourceIds.push(item.parentId)
					}
				});
				let ids = [...new Set(resourceIds)];
				let url = UPDATE_ROLES_RESOURCES_URL + "/" + this.updateRoleId
				this.axios({
					method: 'POST',
					url: url,
					data: ids
				}).then(res => {
					console.log(res.data);
					let code = res.data.code
					if (code == 200) {
						 this.drawer = false;
						 this.pageRoles();
					} else {
						this.$message.error('加载资源失败');
					}
				
				}).catch(error => {
					// this.$message.error('加载资源失败');
					console.log(error);
				});
			},
			pageRoles: function() {
				this.$data.loading = true;
				let url = LIST_ROLES_URL + "/" + this.currentPage + "/" + this.pageSize
				this.axios({
					method: 'POST',
					url: url,
					data: this.$data.searchForm
				}).then(res => {
					this.$data.loading = false;
					let code = res.data.code
					console.log(JSON.stringify(res.data))
					if (code == 200) {
						this.$data.tableData = res.data.data.records;
						this.$data.totalCount = res.data.data.total;
						this.$data.currentPage = res.data.data.current;
						
					} else {
						this.$message.error('加载资源失败');
					}

				}).catch(error => {
					// this.$message.error('加载资源失败');
					console.log(error);
				});
			},
			handleCurrentChange: function(val) {
				console.log(`当前页: ${val}`);
				this.$data.currentPage = val;
				this.pageRoles();
			},
			availableFormatter: function(row, column, cellValue) {
				return cellValue ? '可用' : '不可用';
			},
			lookAllResources: function() {
				this.$data.loading = true;
				let url = LOAD_ALL_RESOURCES_URL 
				this.axios({
					method: 'GET',
					url: url,
					data: {}
				}).then(res => {
					this.$data.loading = false;
					let code = res.data.code
					if (code == 200) {
						// this.drawer = true;
						this.resourcesData = res.data.data
						
			
					} else {
						this.$message.error('加载资源失败');
					}
			
				}).catch(error => {
					// this.$message.error('加载资源失败');
					console.log(error);
				});
			},
			settingAuth: function(row){
                this.$nextTick(() => {
					this.$refs.tree.setCheckedKeys([])
				});
				this.updateRoleId = row.id 
				let url = SELECT_ROLE_RESOURCES_URL + "/" + row.id 
				this.axios({
					method: 'GET',
					url: url,
					data: {}
				}).then(res => {
					let code = res.data.code
					if (code == 200) {
						this.drawer = true;
						let that = this
						that.checkeds = [];
						res.data.data.forEach(function(item){
							if(item.type == 1){
								that.checkeds.push(item.id);
							}
						});
			
					} else {
						this.$message.error('加载资源失败');
					}
			
				}).catch(error => {
					// this.$message.error('加载资源失败');
					console.log(error);
				});
				
			
			},
			editorHandleClick: function(row){
				console.log(row);
				this.dialogAddFormVisible = true
				this.isEditor = true
				let url = FIND_ROLE_URL + "/" + row.id;
				this.axios({
					method: 'GET',
					url: url,
					data: {}
				}).then(res => {
					console.log(res.data);
					let code = res.data.code
					console.log(JSON.stringify(res.data))
					if (code == 200) {
						this.$data.form = res.data.data
					} else {
						this.$message.error('加载资源失败');
					}

				}).catch(error => {
					// this.$message.error('加载资源失败');
					console.log(error);
				});
			},
			saveOrUpdate: function() {
				this.$refs['form'].validate((valid) => {
					if (valid) {
						this.axios({
							method: 'POST',
							url: ADD_OR_EDITOR_ROLE_URL,
							data: this.form
						}).then(res => {
							console.log(res.data);
							let code = res.data.code
							console.log(JSON.stringify(res.data))
							if (code == 200) {
								this.pageRoles();
								this.$data.dialogAddFormVisible = false
							} else {
								this.$message.error('加载资源失败');
							}

						}).catch(error => {
							// this.$message.error('加载资源失败');
							console.log(error);
						});
					} else {
						console.log('error submit!!');
						return false;
					}
				});

			},
			deleteHandleClick: function(row) {
				this.$confirm('此操作将永久删除该信息, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.delete(row)
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消删除'
					});
				});
			},
			delete: function(row) {
				let id = row.id;

				let url = DELETE_ROLE_URL + "?ids=" + id

				this.axios({
					method: 'GET',
					url: url,
					data: {}

				}).then(res => {
					console.log(res.data);
					let code = res.data.code
					console.log(JSON.stringify(res.data))
					if (code == 200) {
						this.pageRoles();
					} 

				}).catch(error => {
					// this.$message.error('加载资源失败');
					console.log(error);
				});
			},
			onSearch: function() {
				this.pageRoles();
			},
			resetSearchForm: function(formName) {
				console.log(formName)
				this.$nextTick(() => {
					this.$refs[formName].resetFields();
					this.onSearch()
				})

			},
			addHandleClick: function() {
				this.$data.isEditor = false;
				this.$data.dialogAddFormVisible = true
				this.$data.form.id = 0;
				this.$nextTick(() => {
					this.$refs['form'].resetFields();
				})
			},
		}
	}
</script>
<style>
.tree-drawer .el-drawer__body{
  padding-bottom: 80px;
}
.tree-drawer .drawer__footer{
  position: absolute;
  width: 100%;
  padding: 20px;
  background: #fff;
  bottom: 0;
  z-index: 9;
}
</style>

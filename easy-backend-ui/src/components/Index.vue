<template>
	<div id="index">
		<el-container>
			<el-header class="main-header">
				<el-row type="flex" class="row-bg" justify="space-between">
					<el-col :span="6">
						<el-row type="flex" class="row-bg">
							<div class="logo">后台管理中心 v1.0</div>
							<div class="btn-show">
								<i class="el-icon-s-fold" v-if="isCollapse == false" @click="show(1)"></i>
								<i class="el-icon-s-unfold" v-else @click="show(2)"></i>
							</div>
						</el-row>
					</el-col>
					<el-col :span="6">
						<div class="admin-info">
							<el-dropdown @command="handleCommand">
								<span class="el-dropdown-link">
									超级管理员：{{ userDetail.username }}
									<i class="el-icon-arrow-down el-icon--right"></i>
								</span>
								<el-dropdown-menu slot="dropdown">
									<el-dropdown-item>个人信息</el-dropdown-item>
									<el-dropdown-item command="logout">注销登陆</el-dropdown-item>
								</el-dropdown-menu>
							</el-dropdown>
						</div>
					</el-col>
				</el-row>
			</el-header>
			<el-container :class="isCollapse ? 'hideSidebar' : ''">
				<Menu :isCollapse="isCollapse" class="sidebar-container" />
				<el-main><router-view></router-view></el-main>
			</el-container>
		</el-container>
	</div>
</template>

<script>
import { AUTH_LOGOUT_URL } from '../utils/api.js';
import Menu from './Menu.vue';
// import Menu from './Sidebar/index.vue';

export default {
	name: 'Index',
	components: {
		Menu
	},
	data: function() {
		return {
			userDetail: {},
			isCollapse: false
		};
	},
	created: function() {
		let jsonStr = sessionStorage.getItem('userDetail');
		this.userDetail = JSON.parse(jsonStr);
	},
	methods: {
		show: function(type) {
			if (type == 1) {
				this.isCollapse = true;
			} else {
				this.isCollapse = false;
			}
		},
		handleCommand: function(command) {
			console.log(command);
			if (command === 'logout') {
				this.logout();
			}
		},
		logout: function() {
			let that = this;
			let api = AUTH_LOGOUT_URL;
			that.axios({
				method: 'GET',
				url: api,
				data: {}
			})
				.then(res => {
					console.log(res.data);
					let code = res.data.code;
					if (code == 200) {
						console.log('logout', res.data);
						sessionStorage.removeItem('userDetail');
						sessionStorage.removeItem('authorities');
						sessionStorage.removeItem('token');
						this.$router.replace({
							path: 'login'
						});
					} else {
						this.$message.error('请输入正确的用户名和密码');
					}
				})
				.catch(error => {
					console.log(error);
				});
		}
	}
};
</script>

<style lang="scss">

</style>

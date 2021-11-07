<template>
	<div class="navbar">
		<hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

		<div class="right-menu">
			<el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
				<div class="avatar-wrapper">
					超级管理员：{{ userDetail.username }}
					<!-- <img :src="avatar+'?imageView2/1/w/80/h/80'" class="user-avatar"> -->
					<i class="el-icon-caret-bottom" />
				</div>
				<el-dropdown-menu slot="dropdown">
					<el-dropdown-item>个人信息</el-dropdown-item>
					<el-dropdown-item divided @click.native="logout">退出登录</el-dropdown-item>
				</el-dropdown-menu>
			</el-dropdown>
		</div>
	</div>
</template>

<script>
import { mapGetters } from 'vuex';
import Hamburger from '@/components/Hamburger';
import { AUTH_LOGOUT_URL } from '@/utils/api.js';

export default {
	components: {
		Hamburger
	},
	computed: {
		...mapGetters(['sidebar', 'avatar', 'device'])
	},
	data: function() {
		return {
			userDetail: {}
		};
	},
	created: function() {
		let jsonStr = sessionStorage.getItem('userDetail');
		this.userDetail = JSON.parse(jsonStr);
	},
	methods: {
		toggleSideBar() {
			this.$store.dispatch('app/toggleSideBar');
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

<style lang="scss" scoped>
.navbar {
	height: 50px;
	overflow: hidden;
	position: relative;
	background: #fff;
	box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

	.hamburger-container {
		line-height: 46px;
		height: 100%;
		float: left;
		cursor: pointer;
		transition: background 0.3s;
		-webkit-tap-highlight-color: transparent;

		&:hover {
			background: rgba(0, 0, 0, 0.025);
		}
	}

	.breadcrumb-container {
		float: left;
	}

	.errLog-container {
		display: inline-block;
		vertical-align: top;
	}

	.right-menu {
		float: right;
		height: 100%;
		line-height: 50px;

		&:focus {
			outline: none;
		}

		.right-menu-item {
			display: inline-block;
			padding: 0 8px;
			height: 100%;
			font-size: 18px;
			color: #5a5e66;
			vertical-align: text-bottom;

			&.hover-effect {
				cursor: pointer;
				transition: background 0.3s;

				&:hover {
					background: rgba(0, 0, 0, 0.025);
				}
			}
		}

		.avatar-container {
			margin-right: 30px;
			.avatar-wrapper:hover {
				background: none;
			}
			.avatar-wrapper {
				position: relative;
				.user-avatar {
					cursor: pointer;
					width: 40px;
					height: 40px;
					border-radius: 10px;
				}

				// .el-icon-caret-bottom {
				//   cursor: pointer;
				//   position: absolute;
				//   right: -20px;
				//   top: 25px;
				//   font-size: 12px;
				// }
			}
		}
	}
}
</style>

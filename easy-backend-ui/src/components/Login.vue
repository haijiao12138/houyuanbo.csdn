<template>
	<div>
		<div class="main">
			<!-- <div class="ad-info" id="login">
				<div class="main-title">NIUA-EASY</div>
				<el-row style="margin-top:30px;">
					<el-input style="margin-top:5px;" v-model.trim="username" placeholder="请输入用户名"></el-input>
					<el-input style="margin-top:5px;" v-model.trim="password" placeholder="请输入密码"></el-input>
				</el-row>
				<el-row style="text-align:center"></el-row>
			</div> -->
			<div class="logo_box">
				<h3>NIUA-EASY</h3>
				<div class="input_outer">
					<span class="u_user"></span>
					<el-input class="text" v-model.trim="username" placeholder="请输入用户名"></el-input>
					<!-- <input name="logname" class="text" style="color: #FFFFFF !important" type="text" placeholder="请输入账户"> -->
				</div>
				<div class="input_outer">
					<span class="us_uer"></span>
					<el-input class="text" type="password"  v-model.trim="password" placeholder="请输入密码"></el-input>
					<!-- <input name="logpass" class="text" style="color: #FFFFFF !important; position:absolute; z-index:100;" value="" type="password" placeholder="请输入密码"> -->
				</div>
				<div class="mb2">
					<el-button class="act-but submit" style="width: 100%;" type="primary" size="medium" v-on:click="login">登录</el-button>
				</div>
			</div>
			<vue-particles
				color="#fff"
				:particleOpacity="0.7"
				:particlesNumber="60"
				shapeType="circle"
				:particleSize="4"
				linesColor="#fff"
				:linesWidth="1"
				:lineLinked="true"
				:lineOpacity="0.4"
				:linesDistance="150"
				:moveSpeed="2"
				:hoverEffect="true"
				hoverMode="grab"
				:clickEffect="true"
				clickMode="push"
				class="lizi"
			></vue-particles>
		</div>
	</div>
</template>
<script>
import { AUTH_LOGIN_URL } from '../utils/api.js';
export default {
	name: 'Login',
	data: function() {
		return {
			username: '',
			password: ''
		};
	},
	methods: {
		login: function() {
			let that = this;
			let api = AUTH_LOGIN_URL;
			if (that.username.length <= 0 && that.password.length <= 0) {
				this.$message.error('请填写的用户名和密码');
				return;
			}
			that.axios({
				method: 'post',
				url: api,
				data: {
					username: this.username,
					password: this.password
				}
			})
				.then(res => {
					console.log(res.data);
					let code = res.data.code;
					if (code == 200) {
						let token = res.data.data.token;
						let authorities = res.data.data.userDetail.authorities; // 获取权限信息
						sessionStorage.setItem('userDetail', JSON.stringify(res.data.data.userDetail));
						sessionStorage.setItem('authorities', JSON.stringify(authorities));
						// console.log(JSON.stringify(authorities))
						sessionStorage.setItem('token', token);
						this.$router.replace({
							path: 'index'
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
.lizi {
	height: 100vh;
	background-image: url(../assets/login/login.jpg);
	background-size: cover;
	background-position: center center;
}
.logo_box {
	width: 350px;
	height: 400px;
	padding: 35px;
	color: #eee;
	position: absolute;
	left: 50%;
	top: 50%;
	margin-left: -175px;
	margin-top: -250px;
	background: rgba(255,255,255,0.1);
	border-radius: 10px;
}
.logo_box h3 {
	text-align: center;
	height: 20px;
	font: 20px 'microsoft yahei', Helvetica, Tahoma, Arial, 'Microsoft jhengHei', sans-serif;
	color: #ffffff;
	height: 20px;
	line-height: 20px;
	padding: 0 0 35px 0;
}
.input_outer {
	height: 46px;
	padding: 0 5px;
	margin-bottom: 30px;
	border-radius: 50px;
	position: relative;
	border: rgba(255, 255, 255, 0.2) 2px solid !important;
		overflow: hidden;
}
.u_user {
    width: 25px;
    height: 25px;
    background-image: url(../assets/login/login_ico.png);
    background-position: -125px 0;
    position: absolute;
    margin: 10px 13px;
}
.us_uer {
    width: 25px;
    height: 25px;
    background-image: url(../assets/login/login_ico.png);
    background-position: -125px -34px;
    position: absolute;
    margin: 10px 13px;
}
.text{
	    width: 220px;
	    height: 46px;
	    outline: none;
	    display: inline-block;
	    margin-left: 50px;
	    border: none;
	    line-height: 46px;
		width: calc(100% - 50px);
}
</style>
<style>
	.text  input.el-input__inner{
		background: transparent !important;
		    border: none !important;
			color: #fff;
	}
	.text input:-webkit-autofill {
	  -webkit-transition-delay: 99999s;
	  -webkit-transition: color 99999s ease-out, background-color 99999s ease-out;
	}
</style>
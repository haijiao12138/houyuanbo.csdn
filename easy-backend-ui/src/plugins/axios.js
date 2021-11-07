import Vue from 'vue'
import axios from 'axios'
import VueAxios from 'vue-axios'
import router from '@/router/router.js'

import {
	// Loading,
	Message
} from 'element-ui'

Vue.use(VueAxios, axios, router)

// 超时时间
axios.defaults.timeout = 5000
// http请求拦截器
// var loadinginstace

axios.interceptors.request.use(config => {
	// element ui Loading方法
	// loadinginstace = Loading.service({
	// 	fullscreen: true
	// })
	let token = sessionStorage.getItem("token")
	
	if (!token) {
		router.replace({
			path: "login"
		});
		// alert(sessionStorage.getItem("token"))
	}
	config.headers = {
		'Authorization': 'Bearer ' + token,
		// 'Content-Type': 'application/x-www-form-urlencoded'
	}
	return config
}, error => {
	// loadinginstace.close()
	Message.error({
		message: '加载超时'
	})
	return Promise.reject(error)
})
// http响应拦截器
axios.interceptors.response.use(data => { // 响应成功关闭loading
	// loadinginstace.close()
	let code = data.data.code;
	if(code == 401){
		Message.warning({
			message: '登录权限失效，请重新登录'
		})
		router.replace({
			path: "login"
		});
	}
    
	if(code == 500){
		Message.error({
			message: '服务端响应错误，请联系管理员'
		})
	}
	if(code == 403){
		Message.warning({
			message: '你无权操作此功能，请联系管理员添加'
		})
	}

	if(code == 20005){
		Message.warning({
			message: '操作太频繁，请稍后'
		})
	}

	return data
}, error => {
	// loadinginstace.close()
	Message.error({
		message: '加载失败'
	})
	return Promise.reject(error)
})

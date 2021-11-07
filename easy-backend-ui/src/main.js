import Vue from 'vue'
import App from './App.vue'
import store from './store'
import './plugins/element.js'
//导入路由
import router from './router/router.js'
//导入axios
import './plugins/axios.js'
import qs from 'qs'
import Auth from './directives/auth'
import VueParticles from 'vue-particles'
Vue.use(VueParticles)
Vue.config.productionTip = false
Vue.prototype.$qs = qs
Vue.use(Auth);
//引入样式
import '@/styles/index.scss' // global css
//引入公共变量
import common from './plugins/common.js'
Vue.prototype.$common = common;



new Vue({
	store,
	router,
	render: h => h(App),
}).$mount('#app')

import Vue from 'vue'
import VueRouter from 'vue-router'
import {
	Notification
} from 'element-ui';
// import Index from "../components/Index.vue"
import Login from "../components/Login.vue"
import Redirect from "../components/kong/Redirect"
import Role from "../views/auth/Role.vue"
import Resource from "../views/auth/Resource.vue"
import User from "../views/auth/User.vue"
import SysOperLog from "../views/log/SysOperLog"
import Analysis from "../views/analysis/Analysis"
import TabsPages from "../views/example/TabsPages";
import Upload from "../views/commons/Upload"
import Generator from "../views/generator/Generator";
import Job from "../views/job/Job";
import Test from "../views/test/Test";
import NotFound from "../views/404.vue"
import Forbidden from "../views/403.vue"
import {
	check,
	isLogin
} from '../utils/auth.js'
import findLast from 'lodash/findLast'

import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
NProgress.configure({
	showSpinner: false
}) // NProgress Configuration

Vue.use(VueRouter)

import constRouters from './constRouters.js'
/* Layout */
import Layout from '@/layout'
Vue.use(VueRouter)

export const routes = [{
		path: "/login",
		hideInMenus: true,
		component: Login
	},
	{
		path: "/index",
		name: '首页',
		hideInMenus: false,
		component: Layout,
		meta: {
			authority: ['/']
		},
		children: [{
				path: "/index",
				name: '权限管理',
				hideInMenus: false,
				component: {
					render: h => h("router-view")
				},
				meta: {
					authority: ['/'],
					icon: 'el-icon-setting'
				},
				children: [{
						path: "/role",
						name: '角色管理',
						hideInMenus: false,
						meta: {
							authority: ['/role']
						},
						component: Role
					},
					{
						path: "/resource",
						name: '资源管理',
						hideInMenus: false,
						meta: {
							authority: ['/resource']
						},
						component: Resource
					},
					{
						path: "/user",
						name: '用户管理',
						hideInMenus: false,
						meta: {
							authority: ['/user']
						},
						component: User
					},
					{
						path: "/sysOperLog",
						name: '操作日志管理',
						hideInMenus: false,
						meta: {
							authority: ['/sysOperLog']
						},
						component: SysOperLog
					},
					{
						path: "/403",
						hideInMenus: true,
						component: Forbidden
					},

				]
			},
			{
				path: "/analysis",
				name: '图表展示',
				hideInMenus: false,
				component: {
					render: h => h("router-view")
				},
				meta: {
					authority: ['/'],
					icon: 'el-icon-data-analysis'
				},
				children: [{
					path: "/analysis",
					name: '图表页',
					hideInMenus: false,
					meta: {
						authority: ['/role']
					},
					component: Analysis
				}, ]
			},
			{
				path: "/example",
				name: '演示项目',
				hideInMenus: false,
				component: {
					render: h => h("router-view")
				},
				meta: {
					authority: ['/'],
					icon: 'el-icon-collection-tag'
				},
				children: [{
					path: "/example",
					name: 'Tabs标签演示',
					hideInMenus: false,
					meta: {
						authority: ['/']
					},
					component: TabsPages
				},


				]
			},
			{
				path: "/other",
				name: '其他特定展示',
				hideInMenus: false,
				component: {
					render: h => h("router-view")
				},
				meta: {
					authority: ['/'],
					icon: 'el-icon-collection-tag'
				},
				children: [{
						path: "/upload",
						name: '上传',
						hideInMenus: false,
						meta: {
							authority: ['/']
						},
						component: Upload
					},


				]
			},
			{
				path: "/generator",
				name: '代码相关',
				hideInMenus: false,
				component: { render: h => h("router-view")},
				meta: { 
					authority: ['/generator'],
					icon: 'el-icon-brush' 
				},
				children: [
					{
						path: "/generator",
						name: '代码生成',
						hideInMenus: false,
						meta: { authority: ['/generator'] },
						component: Generator
					},
					{
						path: "/test",
						name: '测试',
						hideInMenus: false,
						meta: { authority: ['/test'] },
						component: Test
					},
					
				]
			},
			{
				path: "/job",
				name: '定时任务',
				hideInMenus: false,
				component: { render: h => h("router-view")},
				meta: {
					authority: ['/quartz'],
					icon: 'el-icon-brush'
				},
				children: [
					{
						path: "/job",
						name: '任务列表',
						hideInMenus: false,
						meta: { authority: ['/quartz'] },
						component: Job
					}

				]
			},
			{
				path: "/hid",
				name: '演示项目',
				hideInMenus: true,
				component: {
					render: h => h("router-view")
				},
				meta: {
					authority: ['/'],
					icon: 'el-icon-collection-tag'
				},
				children: [{
					path: "/hid/child",
					name: 'Tabs标签演示',
					hideInMenus: true,
					meta: {
						authority: ['/']
					},
					component: TabsPages
				},


				]
			},

		]
	},

	{
		path: "*",
		hideInMenus: true,
		component: NotFound
	},
	{
		path: '/',
		hideInMenus: true,
		redirect: '/index'
	},
	{
		path: "/redirect",
		hideInMenus: true,
		component: Redirect
	},
]

function exportWhiteListFromRouter(router) {
	let res = []
	for (let item of router) {
		res.push(item.path)
		routes.push(item)
	}
	return res
}

// 路由白名单
const whiteList = exportWhiteListFromRouter(constRouters)


var router = new VueRouter({
	mode: "history",
	routes: routes
})

router.beforeEach((to, form, next) => {


	NProgress.start()
	const record = findLast(to.matched, record => record.meta.authority);

	if (record && !check(record.meta.authority)) {
		if (whiteList.indexOf(to.path) !== -1) { //白名单中有的路由，可以继续访问
			// in the free login whitelist, go directly
			next()
			NProgress.done()
		} else { //否则，白名单中没有的路由，跳回首页
			if (!isLogin() && to.path !== '/login') {
				next({
					path: '/login'
				});
				NProgress.done()
			} else if (to.path !== "/403") {
				next({
					path: '/403'
				});
				Notification.error({
					title: '您没有访问权限',
					message: '请联系管理员'
				});
				NProgress.done()
			}
		}
	} else {
		next();
		NProgress.done()
	}

});

export default router;

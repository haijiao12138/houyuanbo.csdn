<template>
	<div>
		<el-scrollbar wrap-class="scrollbar-wrapper">
			<el-menu
				default-active="2"
				class="el-menu-vertical-demo"
				:background-color="$common.maincolor.menuBg"
				:text-color="$common.maincolor.menuText"
				:unique-opened="false"
				:active-text-color="$common.maincolor.menuActiveText"
				:router="true"
				:collapse="isCollapse"
			>
				<el-submenu :index="menu.path" v-for="menu in menus" :key="menu.path">
					<template slot="title">
						<i class="el-icon-location"></i>
						<span slot="title">{{ menu.name }}</span>
					</template>
					<el-menu-item :index="child.path" v-for="child in menu.children" :key="child.path">{{ child.name }}</el-menu-item>
				</el-submenu>
			</el-menu>
		</el-scrollbar>
	</div>
</template>

<script>
import { routes } from '../plugins/router.js';
import { check } from '../utils/auth.js';
export default {
	name: 'Menu',
	props: ['isCollapse'],
	data() {
		return {
			menus: [],
		};
	},
	created: function() {
		this.$data.menus = this.loadMenus(routes[1].children);
	},
	methods: {
		handleSelect(key, keyPath) {
			console.log(key, keyPath);
		},
		loadMenus: function(routes) {
			const menusData = [];
			for (let item of routes) {
				if (item.meta && item.meta.authority && check(item.meta.authority)) {
					if (!item.hideInMenus) {
						if (item.children) {
							item.children = this.loadMenus(item.children);
						}
						menusData.push(item);
					}
				}
			}
			return menusData;
		}
	}
};
</script>

<style>
	
</style>

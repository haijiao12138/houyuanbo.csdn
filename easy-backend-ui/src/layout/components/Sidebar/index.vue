<template>
  <div :class="{'has-logo':showLogo}">
    <logo v-if="showLogo" :collapse="isCollapse" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="variables.menuBg"
        :text-color="variables.menuText"
        :unique-opened="false"
        :active-text-color="variables.menuActiveText"
        :collapse-transition="false"
        mode="vertical"
      >
        <sidebar-item v-for="route in menus" :key="route.path" :item="route" :base-path="route.path" />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Logo from './Logo'
import SidebarItem from './SidebarItem'
import variables from '@/styles/variables.scss'
import { routes } from '@/router/router.js';
import { check } from '@/utils/auth.js';

export default {
  components: { SidebarItem, Logo },
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
  },
  computed: {
    ...mapGetters([
      'sidebar'
    ]),
	
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    showLogo() {
      return this.$store.state.settings.sidebarLogo
    },
    variables() {
      return variables
    },
    isCollapse() {
      return !this.sidebar.opened
    }
  }
}
</script>

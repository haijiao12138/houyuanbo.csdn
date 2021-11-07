### 新建一个modules

`const state = {
  userDetail:{},
  authorities:{},
  token:''
}

const mutations = {
  SET_USERDETAIL:  (state, userDetail) => {
    state.userDetail = userDetail
  },
 SET_AUTHORITIES:  (state, authorities) => {
   state.authorities = authorities
 },
 SET_TOKEN:  (state, token) => {
   state.token = token
 },
}

const actions = {
  setUserDetail({ commit }, userDetail) {
    commit('SET_USERDETAIL', userDetail)
  },
  setAuthorities({ commit }, authorities) {
    commit('SET_AUTHORITIES', authorities)
  },
  setToken({ commit }, token) {
    commit('SET_TOKEN', token)
  },
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}`
### 在getters.js里面写上

`userDetail: state => state.user.userDetail,
  authorities: state => state.user.authorities,
  token: state => state.user.token`

### set方法是this.$store.dispatch('user/setUserDetail', {name:100})    
### get方法是this.$store.getters.userDetail
> 其中modules文件里的namespaced: true  为了解决不同模块命名冲突的问题，将不同模块的namespaced:true，之后在不同页面中引入getter、actions、mutations时，需要加上所属的模块名
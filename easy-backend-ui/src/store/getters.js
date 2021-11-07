const getters = {
  sidebar: state => state.app.sidebar,
  size: state => state.app.size,
  device: state => state.app.device,
  visitedViews: state => state.tagsView.visitedViews,
  userDetail: state => state.user.userDetail,
  authorities: state => state.user.authorities,
  token: state => state.user.token
  // cachedViews: state => state.tagsView.cachedViews,
  // errorLogs: state => state.errorLog.logs
}
export default getters

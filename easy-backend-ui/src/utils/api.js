// 登录注册相关接口
export const AUTH_LOGIN_URL =  "/v1/auth/login";
export const AUTH_LOGOUT_URL =  "/v1/auth/logout"; // 退出系统

//加载权限资源
export const LIST_RESOURCES_URL =  "/v1/resource/listResources";
export const LOAD_RESOURCES_URL =  "/v1/resource/loadResources";
export const ADD_OR_EDITOR_RESOURCES_URL =  "/v1/resource/saveOrUpdate";
export const DELETE_RESOURCES_URL =  "/v1/resource/delete";
export const FIND_RESOURCES_URL =  "/v1/resource/findResource";
export const BATCH_CREATE_URL =  "/v1/resource/batchCreate";

//加载角色信息
export const LOAD_ALL_RESOURCES_URL =  "/v1/role/loadAllResource";
export const LIST_ROLES_URL =  "/v1/role/list";
export const LOAD_ROLES_RESOURCES_URL =  "/v1/role/loadResources";
export const UPDATE_ROLES_RESOURCES_URL =  "/v1/role/updateRoleResources"; // 更新权限绑定
export const FIND_ROLE_URL =  "/v1/role/findRoleById"; // 根据角色id，查询角色信息
export const ADD_OR_EDITOR_ROLE_URL =  "/v1/role/saveOrUpdate"; // 保存和更新角色信息
export const DELETE_ROLE_URL =  "/v1/role/delete";
export const SELECT_ROLE_RESOURCES_URL =  "/v1/role/selectResourceByRoleId"; // 查询角色拥有的全部权限信息，包括菜单和按钮的

//用户管理
export const LIST_USER_URL =  "/v1/user/list";
export const FIND_USER_URL =  "/v1/user/findUserById";
export const ADD_OR_EDITOR_USER_URL =  "/v1/user/saveOrUpdate";
export const DELETE_USER_URL =  "/v1/user/delete";
export const LOAD_ROLES_URL =  "/v1/user/loadAllRoles";
export const UPDATE_USER_AND_ROLE =  "/v1/user/updateUserAndRole";
export const MODIFY_PASSWORD =  "/v1/user/modify-password";



//代码自动生成
export const LIST_GENERATOR_URL =  "/v1/generator/list";
export const DELETE_GENERATOR_URL =  "/v1/generator/delete";
export const FIND_GENERATOR =  "/v1/generator/findGeneratorById";
export const ADD_OR_EDITOR_GENERATOR_URL =  "/v1/generator/saveOrUpdate";
export const AUTOTOOLS_GENERATOR_URL =  "/v1/generator/autoGenerator";
export const BATCHDELETE_GENERATOR_URL =  "/v1/generator/batchDelete";
export const QUERYTABLENAME_GENERATOR_URL =  "/v1/generator/queryAllTableInDb";
export const EXPORT_GENERATOR_URL =  "/v1/generator/export";

 
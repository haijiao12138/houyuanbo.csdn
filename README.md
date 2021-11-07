#### 1、模块说明
niua_admin 模块为业务模块，所有业务相关代码都写在里面

niua_auth  为权限验证模块，所有权限验证相关逻辑都写在里面

niua_common 为公用模块，可以公用的工具类和配置也在里面

niua_core 核心配置组件，项目核心的配置都写在里面

niua_gen 代码生成相关业务

niua_quartz 任务调度相关

#### 2、本地化部署需要注意的细节
admin模块下的yml文件

1）需要修改profile参数，此路径存储相关的上传和下载资源路径，需要根据自己的系统进行配置

2）gen.yml

修改outPath参数，此参数为代码生成的目标路径

#### 3、 初始化数据库
在niua_admin模块下的initsql文件夹下

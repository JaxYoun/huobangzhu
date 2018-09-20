## 项目前端开发指南

### 目录结构

```
├── /dist/           # 项目输出目录
├── /routes/         # server配置
├── /src/            # 项目源码目录
│ ├── /components/   # UI组件及UI相关方法
│ │ ├── skin.less    # 全局样式
│ │ └── vars.less    # 全局样式变量
│ ├── /routes/       # 路由组件
│ │ └── app.js       # 路由入口
│ ├── /models/       # 数据模型
│ ├── /services/     # 数据接口
│ ├── /themes/       # 项目样式
│ ├── /mock/         # 数据mock
│ ├── /utils/        # 工具函数
│ │ ├── config.js    # 项目常规配置
│ │ ├── menu.js      # 菜单及面包屑配置
│ │ ├── config.js    # 项目常规配置
│ │ ├── request.js   # 异步请求函数
│ │ └── theme.js     # 项目需要在js中使用到样式变量
│ ├── route.js       # 路由配置
│ ├── index.js       # 入口文件
│ └── index.html
├── package.json     # 项目信息
├── server.js        # server 服务
├── .eslintrc        # Eslint配置
└── .roadhogrc.js    # roadhog配置
```




### 开发环境部署

克隆项目文件:

    svn: https://192.168.121.130/svn/Projects/HBZ/trunk/hbz-web-ui

进入目录安装依赖:

    需要配置npm地址为本地npm库
    npm config set registry=http://10.0.40.57:8081/repository/tnpm/ 
    
    npm i 或者 yarn

启动项目：

```
npm start 

浏览器打开 http://localhost:8000
```



### 生产（测试）环境部署

克隆项目文件:

    svn: https://192.168.121.130/svn/Projects/HBZ/trunk/hbz-web-ui

进入目录安装依赖:

    需要配置npm地址为本地npm库
    npm config set registry=http://10.0.40.57:8081/repository/tnpm/ 
    
    npm i 或者 yarn

构建项目：

```
npm run build

将会生成dist目录

(使用npm run build 打包项目之后：
    启动项目生产环境的关键目录：
├── /dist/           # 项目输出目录
├── /server/         # server配置
├── /views/          # views
├── /node_modules/   # node_modules 模块文件
├── package.json     # 项目信息
└── server.js        # server 服务
)
```
api配置：
```
./server/config.js

  proxy: 服务器api地址+端口，
  port: 前端服务端口号,

```

启动项目：

```
npm run server 

浏览器打开 http://localhost:前端服务端口号
```

停止项目：
 ```
 npm run stop

 ```

 重启项目：
 ```
 npm run restart

 ```

查看项目运行状态：

```
npm run info
```

查看项目日志：
 ```
 npm run logs
 ```

卸载项目：
 ```
 npm run uninstall
 ```

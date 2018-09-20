const path = require('path')
const express = require('express')
const app = express()
const proxy = require('http-proxy-middleware')
const indexRouter = require('./server/index')
const config = require('./server/config')

// 模板配置
app.set('views', path.join(__dirname, 'views'))
app.set('view engine', 'ejs')

// 静态资源配置
app.use('/', express.static(path.join(__dirname, 'dist')))


// api代理配置
app.use('/api', proxy({
  target: config.proxy,
  changeOrigin: true,
}))

// 路由配置
app.use('/', indexRouter)

// 404
app.use('*', (req, res) => {
  if (!res.headersSent) {
    res.render('404')
  }
})

// 监听端口
app.listen(config.port)

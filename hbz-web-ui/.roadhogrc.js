const path = require('path')
const config = require('./server/config')
const svgSpriteDirs = [
  path.resolve(__dirname, 'src/svg/'),
  require.resolve('antd').replace(/index\.js$/, ''),
]

export default {
  entry: 'src/index.js',
  svgSpriteLoaderDirs: svgSpriteDirs,
  "proxy": {
    "/api": {
      "target": config.proxy1,
      "changeOrigin": true,
    }
  },
  "theme": "./theme.config.js",
  "env": {
      "development": {
        "extraBabelPlugins": [
          "dva-hmr",
          "transform-runtime",
  		    ["import", { "libraryName": "antd", "style": true }]
        ]
      },
      "production": {
        "extraBabelPlugins": [
          "transform-runtime",
  		    ["import", { "libraryName": "antd", "style": true}]
        ]
      }
  }
}

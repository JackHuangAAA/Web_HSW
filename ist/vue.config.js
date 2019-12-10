const path = require('path')
const CompressionWebpackPlugin = require('compression-webpack-plugin')
const CSSSplitWebpackPlugin = require('css-split-webpack-plugin').default


const productionGzipExtensions = ['js', 'css']
const isProduction = process.env.NODE_ENV === 'production'
const resolve = dir => {
  return path.join(__dirname, dir)
}

//静态文件基础路径//设备测试需要把display去掉
const BASE_URL = process.env.NODE_ENV === 'production'
  ? '/'
  : '/'

module.exports = {
  baseUrl: BASE_URL,
  lintOnSave: false,
  chainWebpack: config => {
    //src别名配置
    config.resolve.alias
      .set('@', resolve('src'))
    //less解析
    config.resolve.extensions
      .add('.less')
    //iview es6编译
    config.module
      .rule('compile')
        .test(/iview.src.*?js$/)
        .use('babel')
          .loader('babel-loader');
  },
  configureWebpack: config =>{
    if (isProduction) {
      //解决IE9不能加载4000以上selector问题
      config.plugins.push(new CSSSplitWebpackPlugin({
        size: 4000,
        filename: 'css/[name]-[part].[ext]'
      }))
      //gzip压缩静态文件
      // config.plugins.push(new CompressionWebpackPlugin({
      //   algorithm: 'gzip',
      //   test: new RegExp('\\.(' + productionGzipExtensions.join('|') + ')$'),
      //   threshold: 10240,
      //   minRatio: 0.8
      // }))
    }
  },
  // 打包时不生成.map文件
  productionSourceMap: false,
  //开发配置
  devServer: {
    proxy: {
      "/ist": {
        target: "http://localhost:7003",
        secure: false,
        pathRewrite: { "^/ist": "" }
      },
      "/socket.io": {
        target: "http://localhost:7004",
        secure: false,
        changeOrigin: true,
        ws: true
      }
    }
  },
  css: {
    loaderOptions: {
      //less开启js支持
      less: {
        javascriptEnabled: true
      }
    }
  }
};

const path = require('path')
const CompressionWebpackPlugin = require('compression-webpack-plugin')
const CSSSplitWebpackPlugin = require('css-split-webpack-plugin').default

const productionGzipExtensions = ['js', 'css']
const isProduction = process.env.NODE_ENV === 'production'
const resolve = dir => {
  return path.join(__dirname, dir)
}

//静态文件基础路径
const BASE_URL = process.env.NODE_ENV === 'production' ? './' : '/'

module.exports = {
  publicPath: BASE_URL,
  lintOnSave: false,
  chainWebpack: config => {
    //热更新
    config.resolve.symlinks(true)
    //src别名配置
    config.resolve.alias
      .set('@', resolve('src'))
      .set('_c', resolve('src/components'))
      .set('_a', resolve('src/assets'))
    //less解析
    config.resolve.extensions.add('.less')
    //iview es6编译
    config.module
      .rule('compile')
      .test(/iview.src.*?js$/)
      .use('babel')
      .loader('babel-loader')
  },
  configureWebpack: {
    entry: {
      vendor: ['vue', 'vue-i18n', 'vue-router', 'vuex', 'axios', 'iview']
    },
    devtool: true
  },
  configureWebpack: config => {
    if (isProduction) {
      //解决IE9不能加载4000以上selector问题
      config.plugins.push(
        new CSSSplitWebpackPlugin({
          size: 4000,
          filename: 'css/[name]-[part].[ext]'
        })
      )
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
      '/vcc': {
        target: 'http://localhost:9998',
        secure: false,
        pathRewrite: { '^/vcc': '' }
      },
      '/socket.io': {
        target: 'http://localhost:9990',
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
}

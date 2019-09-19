'use strict';

const webpack = require('webpack');
const merge = require('webpack-merge');
const baseConfig = require('./webpack.base.conf');

module.exports = merge(baseConfig, {
  mode: 'development',

  devServer: {
    clientLogLevel: 'debug',
    hot: true,
    contentBase: 'dist',
    compress: true,
    host: 'localhost',
    port: 8080,
    open: false,
    overlay: { warnings: false, errors: true },
    publicPath: '/',
    quiet: true,
  },

  module: {
    rules: [
      {
        test: /\.css$/,
        use: [
          'vue-style-loader',
          'css-loader'
        ]
      }, {
        test: /\.styl(us)?$/,
        use: [
          'vue-style-loader',
          'css-loader',
          'stylus-loader'
        ]
      }
    ]
  },

  plugins: [
    new webpack.HotModuleReplacementPlugin()
  ]
});

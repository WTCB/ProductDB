'use strict';

var webpack = require('webpack');
var HtmlWebpackPlugin = require('html-webpack-plugin');

/**
 * Get configuration for Webpack
 *
 * @see http://webpack.github.io/docs/configuration
 *      https://github.com/petehunt/webpack-howto
 *
 * @param {boolean} release True if configuration is intended to be used in
 * a release mode, false otherwise
 * @return {object} Webpack configuration
 */
module.exports = {
  externals: {
    'jquery': 'jQuery'
  },
  
  entry: {
    app: './front/src/app.js'
  },

  output: {
    filename: 'app.js',
    path: './front/build/',
    publicPatch: './build/'
  },

  debug: true,
  devtool: false,

  stats: {
    colors: true,
    reasons: true
  },

  plugins: [
    new HtmlWebpackPlugin({template: 'front/src/assets/index.html'}),
    new webpack.ProvidePlugin({
      '$': "jquery",
      'jQuery': "jquery",
      'Popper': 'popper.js',
      'window.jQuery': 'jquery'
    })
  ],

  resolve: {
    extensions: ['', '.webpack.js', '.web.js', '.js', '.jsx']
  },

  module: {
    loaders: [
      {
        test: /\.css$/,
        loader: 'style!css'
      },
      {
        test:  /\.(jpe?g|png|gif|svg)$/i,
        loader: 'file-loader?name=[name].[ext]'
      },
      {
        test: /\.js|\.jsx/,
        exclude: /node_modules|bower_components/,
        loader: 'babel'
      }
    ]
  }
};

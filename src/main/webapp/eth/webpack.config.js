const webpack = require('webpack');
const path = require('path');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin')

module.exports = {
  entry: {
    app: path.resolve(__dirname, 'app.js'),
    index: path.resolve(__dirname, 'public/js/index.js'),
    contractDetails: path.resolve(__dirname, 'public/js/contractDetails.js'),
    contractSign: path.resolve(__dirname, 'public/js/contractSign.js'),
    newContract: path.resolve(__dirname, 'public/js/newContract.js'),
    signature: path.resolve(__dirname, 'public/js/signature.js'),
    personalData: path.resolve(__dirname, 'public/js/personalData.js'),
    login: path.resolve(__dirname, 'public/js/login.js'),
    register: path.resolve(__dirname, 'public/js/register.js'),
  },
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: '[name]Dist.js'
  },

  plugins: [
    // new CopyWebpackPlugin([
    //   {
    //     from: path.resolve(__dirname, 'views/index.html'),
    //     to: path.resolve(__dirname, 'build/index.html')
    //   },
    //   {
    //     from:path.resolve(__dirname, 'views/contractDetails.html'),
    //     to: path.resolve(__dirname, 'build/contractDetails.html')
    //   },
    //   {
    //     from: path.resolve(__dirname, 'views/contractSign.html'),
    //     to: path.resolve(__dirname, 'build/contactSign.html')
    //   },
    //   {
    //     from: path.resolve(__dirname, 'views/newContract.html'),
    //     to: path.resolve(__dirname, 'build/newContract.html')
    //   },
    //   {
    //     from: path.resolve(__dirname, 'views/signature.html'),
    //     to: path.resolve(__dirname, 'build/signature.html')
    //   },
    //   {
    //     from: path.resolve(__dirname, 'views/login.html'),
    //     to: path.resolve(__dirname, 'build/login.html')
    //   },
    //   {
    //     from: path.resolve(__dirname, 'views/register.html'),
    //     to: path.resolve(__dirname, 'build/register.html')
    //   },
    // ]),
    new CleanWebpackPlugin(),
    // new HtmlWebpackPlugin(),
    new webpack.ProvidePlugin({
      $: 'jquery',
      jQuery: 'jquery',
      jquery: 'jquery',
      'windows.jQuery': 'jquery',
      Popper: ['popper.js', 'default']
    })
  ],

  devtool: 'source-map',

  module: {
    rules: [
      {
        test: /\.css$/,
        use: [
          {
            loader: 'style-loader'
          },
          {
            loader: 'css-loader',
            options: {
              modules: true
            }
          }
        ]
      },
      {
        test: /\.js?$/,
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader',
          options: {
            presets: ['es2015']
          }
        }
      },
      {
        test: /\.(png|jpg|gif|svg)$/,
        use: [
          {
            loader: 'url-loader',
            options: {
              limit: 8192
            }
          }
        ]
      }
    ]
  },

  devServer: {
    host: '127.0.0.1',
    port: '88899'
  }
};


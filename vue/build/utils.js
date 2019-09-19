'use strict';

const path = require('path');
const config = require('../config');

module.exports = {
  resolve: function (dir) {
    return path.join(__dirname, '..', dir)
  },

  assetsPath: function (_path) {
    return path.posix.join(config.build.assetsSubDirectory, _path)
  }
};

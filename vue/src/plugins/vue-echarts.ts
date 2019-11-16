import Vue from 'vue';
// @ts-ignore
import ECharts from 'vue-echarts';
import 'echarts/lib/component/legend';
import 'echarts/lib/component/legend/ScrollableLegendView';
import 'echarts/lib/component/legend/ScrollableLegendModel';
import 'echarts/lib/component/tooltip';
import 'echarts/lib/component/title';
import 'echarts/lib/theme/dark';
// const echarts = require('echarts');

Vue.component('v-echarts', ECharts);

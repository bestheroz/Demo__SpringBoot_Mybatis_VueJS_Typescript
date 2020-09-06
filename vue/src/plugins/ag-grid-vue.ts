import '@/scss/ag-grid-vue.scss';
import '../../node_modules/ag-grid-community/dist/styles/ag-grid.css';
import '../../node_modules/ag-grid-community/dist/styles/ag-theme-alpine.css';

import Vue from 'vue';
import { AgGridVue } from 'ag-grid-vue';

Vue.component('AgGridVue', AgGridVue);

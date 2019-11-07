import Vue from 'vue';
import VueSweetalert2 from 'vue-sweetalert2';

// If you don't need the styles, do not connect
import 'sweetalert2/dist/sweetalert2.min.css';

import Swal from 'sweetalert2/dist/sweetalert2.js';

let timerInterval: any;

Vue.use(VueSweetalert2, {
  timer: 5000,
  html: '<b>5</b> 초 후에 자동으로 닫힙니다.',
  onBeforeOpen: () => {
    if (Swal.getTimerLeft()) {
      timerInterval = setInterval(() => {
        // @ts-ignore
        Swal.getContent().querySelector('b').textContent = (
          +(Swal.getTimerLeft() || 0) / 1000
        ).toFixed(0);
      }, 1002);
    } else if (Swal.getContent().querySelector('#swal2-content')) {
      Swal.getContent().querySelector('#swal2-content')!.innerHTML = '';
    }
  },
  onClose: () => {
    timerInterval && clearInterval(timerInterval);
    timerInterval = undefined;
  },
});

import 'sweetalert2/dist/sweetalert2.css';
import '@/scss/sweetalert.scss';
import { SweetAlertResult } from 'sweetalert2';
import Swal from 'sweetalert2/src/sweetalert2.js';
import { AxiosError } from 'axios';

let timerInterval: any;

const countdownDialog = () => {
  if (Swal.getContent() !== null) {
    setTimeout(() => {
      if (Swal.getTimerLeft()) {
        timerInterval = setInterval(() => {
          try {
            Swal.getContent()!.querySelector('b')!.textContent = (
              +(Swal.getTimerLeft() || 0) / 1000
            ).toFixed(0);
          } catch (e) {
            timerInterval && clearInterval(timerInterval);
            timerInterval = undefined;
          }
        }, 1002);
      } else if (Swal.getContent()!.querySelector('#swal2-content')) {
        Swal.getContent()!.querySelector('#swal2-content')!.innerHTML = '';
      }
    }, 400);
  }
};

export function alertInfo(message: string, timer = 3500): void {
  Swal.mixin({
    customClass: {
      confirmButton: 'info',
    },
    buttonsStyling: false,
  });

  Swal.fire({
    icon: 'info',
    confirmButtonText: message,
    timer: timer,
    html: '<b>3</b> 초 후에 자동으로 닫힙니다.',
    onBeforeOpen: countdownDialog,
    onClose: () => {
      timerInterval && clearInterval(timerInterval);
      timerInterval = undefined;
    },
  });
}

export function alertSuccess(message: string, timer = 350000): void {
  Swal.mixin({
    customClass: {
      confirmButton: 'success',
    },
    buttonsStyling: false,
  });
  Swal.fire({
    icon: 'success',
    confirmButtonText: message,
    timer: timer,
    html: '<b>3</b> 초 후에 자동으로 닫힙니다.',
    onBeforeOpen: countdownDialog,
    onClose: () => {
      timerInterval && clearInterval(timerInterval);
      timerInterval = undefined;
    },
  });
}

export function alertWarning(message: string): void {
  Swal.mixin({
    customClass: {
      confirmButton: 'warning',
    },
    buttonsStyling: false,
  });

  Swal.fire({
    icon: 'warning',
    confirmButtonText: message,
  });
}

export function alertError(e: string | AxiosError): void {
  Swal.mixin({
    customClass: {
      confirmButton: 'error',
    },
    buttonsStyling: false,
  });
  let message;
  if (
    typeof e === 'string' ||
    !(e.response && e.response.data && e.response.data.message)
  ) {
    message = e;
  } else {
    message = e.response.data.message;
  }
  Swal.fire({
    icon: 'error',
    confirmButtonText: message,
  });
}

export async function confirm(
  title: string,
  text = '',
  confirmButtonText = '확인',
  cancelButtonText = '취소',
): Promise<SweetAlertResult> {
  return await Swal.fire({
    title: title,
    text: text,
    icon: 'question',
    showCancelButton: true,
    confirmButtonText: confirmButtonText,
    cancelButtonText: cancelButtonText,
  });
}

export async function confirmDelete(
  title = '삭제 하시겠습니까?',
  text = '',
): Promise<SweetAlertResult> {
  return await Swal.fire({
    title: title,
    text: text,
    icon: 'question',
    showCancelButton: true,
    confirmButtonText: '삭제 하겠습니다',
    cancelButtonText: '취소',
  });
}

import '@sweetalert2/theme-dark/dark.scss';
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
  Swal.fire({
    icon: 'info',
    confirmButtonColor: '#00CAE3',
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
export function alertSuccess(message: string, timer = 3500): void {
  Swal.fire({
    icon: 'success',
    confirmButtonColor: '#4caf50',
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
  Swal.fire({
    icon: 'warning',
    confirmButtonColor: '#FFC107',
    confirmButtonText: message,
  });
}
export function alertError(e: string | AxiosError): void {
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
    confirmButtonColor: '#E91E63',
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
    confirmButtonColor: '#4caf50',
    confirmButtonText: confirmButtonText,
    cancelButtonColor: '#E91E63',
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
    confirmButtonColor: '#4caf50',
    confirmButtonText: '삭제 하겠습니다',
    cancelButtonColor: '#E91E63',
    cancelButtonText: '취소',
  });
}

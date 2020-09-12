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
  Swal.fire({
    icon: 'info',
    confirmButtonColor: 'var(--v-info-base)',
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
    confirmButtonColor: 'var(--v-success-base)',
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
    confirmButtonColor: 'var(--v-warning-base)',
    confirmButtonText: message,
  });
}

export function alertError(e: string | AxiosError): void {
  let message;
  if (typeof e === 'string' || !e?.response?.data?.message) {
    message = e;
  } else {
    message = e.response.data.message;
  }
  Swal.fire({
    icon: 'error',
    confirmButtonColor: 'var(--v-error-base)',
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
    confirmButtonColor: 'var(--v-primary-base)',
    confirmButtonText: confirmButtonText,
    cancelButtonColor: 'var(--v-secondary-base)',
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
    confirmButtonColor: 'var(--v-primary-base)',
    confirmButtonText: '삭제 하겠습니다',
    cancelButtonColor: 'var(--v-secondary-base)',
    cancelButtonText: '취소',
  });
}

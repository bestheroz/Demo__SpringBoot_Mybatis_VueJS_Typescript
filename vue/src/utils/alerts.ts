import "sweetalert2/dist/sweetalert2.css";
import "@/scss/sweetalert.scss";
import Swal, { SweetAlertResult } from "sweetalert2";

const Toast = Swal.mixin({
  toast: true,
  position: "top",
  showCloseButton: true,
  showConfirmButton: false,
  didOpen: (toast) => {
    toast.addEventListener("mouseenter", Swal.stopTimer);
    toast.addEventListener("mouseleave", Swal.resumeTimer);
  },
});

export function toastSuccess(message: string, timer = 2000): void {
  Toast.fire({
    icon: "success",
    iconColor: "var(--v-primary-base)",
    timer: timer,
    timerProgressBar: !!timer,
    title: message,
  });
}
export function toastInfo(message: string, timer = 5000): void {
  Toast.fire({
    icon: "info",
    iconColor: "var(--v-info-base)",
    timer: timer,
    timerProgressBar: !!timer,
    title: message,
  });
}
export function toastWarning(message: string, timer = 3000): void {
  Toast.fire({
    icon: "warning",
    iconColor: "var(--v-warning-base)",
    timer: timer,
    timerProgressBar: !!timer,
    title: message,
  });
}
export function toastError(message: string, timer?: number): void {
  Toast.fire({
    icon: "error",
    iconColor: "var(--v-error-base)",
    timer: timer,
    timerProgressBar: !!timer,
    title: message,
  });
}
export function toastCloseAll(): void {
  Toast.close();
}

export async function confirm(
  title: string,
  text?: string,
  confirmButtonText = "확인",
  cancelButtonText = "취소",
): Promise<SweetAlertResult> {
  return await Swal.fire({
    title: title,
    text: text,
    icon: "question",
    showConfirmButton: true,
    confirmButtonColor: "var(--v-primary-base)",
    confirmButtonText: confirmButtonText,
    showCancelButton: true,
    cancelButtonColor: "var(--v-secondary-base)",
    cancelButtonText: cancelButtonText,
  });
}

export async function confirmDelete(
  text?: string,
  title = "삭제 하시겠습니까?",
  confirmButtonText = "확인",
  cancelButtonText = "취소",
): Promise<SweetAlertResult> {
  return await Swal.fire({
    title: title,
    text: text,
    icon: "question",
    showConfirmButton: true,
    confirmButtonColor: "var(--v-error-base)",
    confirmButtonText: confirmButtonText,
    showCancelButton: true,
    cancelButtonColor: "var(--v-secondary-base)",
    cancelButtonText: cancelButtonText,
  });
}

export async function prompt(
  title: string,
  text?: string,
  inputPlaceholder = "값을 입력하세요",
): Promise<string> {
  return await Swal.fire({
    input: "text",
    title: title,
    text: text,
    icon: "question",
    inputPlaceholder: inputPlaceholder,
    showCancelButton: true,
    allowEscapeKey: false,
    allowOutsideClick: false,
    inputValidator(inputValue: string): string {
      if (!inputValue) {
        return "값을 입력하세요";
      } else {
        return "";
      }
    },
  }).then((result) => {
    return result.value;
  });
}

export async function promptPassword(
  title: string,
  text?: string,
  inputPlaceholder = "비밀번호를 입력 해 주세요",
): Promise<string> {
  return await Swal.fire({
    input: "password",
    title: title,
    text: text,
    icon: "question",
    // prettier-ignore
    iconHtml: "<i class=\"mdi mdi-keyboard-outline\"></i>",
    inputPlaceholder: inputPlaceholder,
    showCancelButton: true,
    allowEscapeKey: false,
    allowOutsideClick: false,
    inputValidator(inputValue: string): string {
      if (!inputValue) {
        return "비밀번호를 입력하세요";
      } else {
        return "";
      }
    },
  }).then((result) => {
    return result.value;
  });
}

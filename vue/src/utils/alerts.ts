import "sweetalert2/dist/sweetalert2.css";
import "@/scss/sweetalert.scss";
import { SweetAlertResult } from "sweetalert2";
import Swal from "sweetalert2/src/sweetalert2.js";

const Toast = Swal.mixin({
  toast: true,
  position: "bottom-end",
  showConfirmButton: true,
  confirmButtonText: "X",
  didOpen: (toast) => {
    toast.addEventListener("mouseenter", Swal.stopTimer);
    toast.addEventListener("mouseleave", Swal.resumeTimer);
  },
});

export function toastSuccess(message: string, timer = 5000): void {
  Toast.fire({
    icon: "success",
    iconColor: "var(--v-success-base)",
    timer: timer,
    timerProgressBar: !!timer,
    title: message,
    confirmButtonColor: "var(--v-success-base)",
  });
}
export function toastInfo(message: string, timer = 5000): void {
  Toast.fire({
    icon: "info",
    iconColor: "var(--v-info-base)",
    timer: timer,
    timerProgressBar: !!timer,
    title: message,
    confirmButtonColor: "var(--v-info-base)",
  });
}
export function toastWarning(message: string, timer?: number): void {
  Toast.fire({
    icon: "warning",
    iconColor: "var(--v-warning-base)",
    timer: timer,
    timerProgressBar: !!timer,
    title: message,
    confirmButtonColor: "var(--v-warning-base)",
  });
}
export function toastError(message: string, timer?: number): void {
  Toast.fire({
    icon: "error",
    iconColor: "var(--v-error-base)",
    timer: timer,
    timerProgressBar: !!timer,
    title: message,
    confirmButtonColor: "var(--v-error-base)",
  });
}
export function toastCloseAll(): void {
  Toast.close();
}

export async function confirm(
  title: string,
  text = "",
  confirmButtonText = "확인",
  cancelButtonText = "취소",
): Promise<SweetAlertResult> {
  return await Toast.fire({
    title: title,
    text: text,
    icon: "question",
    showCancelButton: true,
    confirmButtonColor: "var(--v-primary-base)",
    confirmButtonText: confirmButtonText,
    cancelButtonColor: "var(--v-secondary-base)",
    cancelButtonText: cancelButtonText,
  });
}

export async function confirmDelete(
  title = "삭제 하시겠습니까?",
  text = "",
): Promise<SweetAlertResult> {
  return await Toast.fire({
    title: title,
    text: text,
    icon: "question",
    showCancelButton: true,
    confirmButtonColor: "var(--v-primary-base)",
    confirmButtonText: "삭제 하겠습니다",
    cancelButtonColor: "var(--v-secondary-base)",
    cancelButtonText: "취소",
  });
}

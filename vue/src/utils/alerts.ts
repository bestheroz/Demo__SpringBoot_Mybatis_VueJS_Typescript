import "sweetalert2/dist/sweetalert2.css";
import "@/scss/sweetalert.scss";
import { SweetAlertResult } from "sweetalert2";
import Swal from "sweetalert2/src/sweetalert2.js";

export async function confirm(
  title: string,
  text = "",
  confirmButtonText = "확인",
  cancelButtonText = "취소",
): Promise<SweetAlertResult> {
  return await Swal.fire({
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
  return await Swal.fire({
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

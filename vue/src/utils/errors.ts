import router from "@/router";

export async function errorPage(statusCode: number | string): Promise<void> {
  if (
    !["/", "/login", "/error", "/error/403", "/error/404"].includes(
      router.currentRoute.path,
    )
  ) {
    await router.push(`/error/${statusCode}`);
  }
}

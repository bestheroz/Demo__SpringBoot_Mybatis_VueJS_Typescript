import router from '@/router';

export async function errorPage(statusCode: number | string) {
  if (
    !['/', '/login', '/error', '/error/403', '/error/404'].includes(
      router.currentRoute.path,
    )
  ) {
    await router.push(`/error/${statusCode}`);
  }
}

import envs from '@/constants/envs';
import router from '@/router';
import { axiosInstance } from '@/utils/apis';

export async function logout() {
  try {
    await axiosInstance.delete(`${envs.API_HOST}api/auth/logout`);
  } catch (e) {
    console.error(e);
  }
  await router.push('/login');
}

export function refreshToken(accessToken: string) {
  window.localStorage.setItem('accessToken', accessToken);
}

export function saveToken(token: {
  accessToken: string;
  refreshToken: string;
}) {
  window.localStorage.setItem('accessToken', token.accessToken);
  window.localStorage.setItem('refreshToken', token.refreshToken);
}

export async function needLogin() {
  if (router.currentRoute.path !== '/login') {
    await router.push('/login?login=need');
  }
}

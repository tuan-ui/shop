/* eslint-disable @typescript-eslint/no-explicit-any */
import { API, standardResponse } from '../utils/middleware';

type StandardResponse<T = any> = { success: boolean; message: T };

export async function refreshToken(token: string) {
  const url = '/auth/refreshToken';
  const params = { refreshToken: token };

  try {
    const response = await API.post(url, params);
    return response.data;
  } catch (error: any) {
    console.error(error.message);
    return {};
  }
}

export async function login(username: string, password: string) {
  const url = '/auth/login';
  const params = { username, password };
  console.debug('[auth] login called for', username);
  try {
    const response = await API.post(url, params);
    console.debug('[auth] login response', response?.status, response?.data);
    const { object, status, message } = response.data;

    if (status === 200 && object) {
      if (object.validate2FAResult) {
        return {
          username: object.username,
          requires2FA: object.validate2FAResult,
          status,
          message,
        };
      }
      if (object.absoluteExp) {
        localStorage.setItem('absoluteExp', object.absoluteExp.toString());
      }

      return {
        token: object.token,
        refreshToken: object.refreshToken,
        username: object.username,
        fullName: object.fullName,
        email: object.email,
        phone: object.phone,
        roles: object.roles,
        userId: object.user_id,
        status,
        message,
      };
    }

    if (status === 401) {
      return { status, message };
    }

    return {
      token: object.token,
      refreshToken: object.refreshToken,
      username: object.username,
      fullName: object.fullName,
      email: object.email,
      statusAccount: object.status,
      phone: object.phone,
      roles: object.roles,
      userId: object.user_id,
      status: object?.status,
      message,
    };
  } catch (error: any) {
    console.error('Login error:', error);
    return {
      status: error.response?.data?.status || 500,
      message: 'Lỗi hệ thống khi đăng nhập',
    };
  }
}

export async function changePassword(
  staffPassword: string,
  userId: string | number,
  userNewPassword: string
) {
  const url = '/auth/changePassword';
  const params = { staffPassword, userId, userNewPassword };
  try {
    const response = await API.post(url, params);
    return response.data;
  } catch (error: any) {
    return standardResponse(false, error.response?.data);
  }
}
let cachedAvatar: string | null = null;
let cachedUserId: string | number | null = null;

export async function getUserImageNav(
  userId: string | number,
  type: string,
  cache?: string | null
) {
  if (cache) {
    cachedAvatar = cache;
    return cache;
  }
  if (cachedUserId === userId) return cachedAvatar;

  const url = `/users/getImage?userId=${userId}&type=${type}`;
  try {
    const response = await API.get(url, { responseType: 'blob' as any });
    const contentType = response.headers['content-type'];
    if (contentType && (contentType as string).startsWith('image/')) {
      cachedAvatar = window.URL.createObjectURL(response.data);
      cachedUserId = userId;
      return cachedAvatar;
    }
    cachedAvatar = null;
    cachedUserId = userId;
    return null;
  } catch (error: any) {
    console.error('Error fetching image:', error);
    cachedAvatar = null;
    cachedUserId = userId;
    return null;
  }
}

export function logOut() {
  localStorage.removeItem('accessToken');
  localStorage.removeItem('refreshToken');
  cachedAvatar = null;
  cachedUserId = null;
}


export async function forgetPassword(username: string, phone: string) {
  const url = '/auth/forgetPassword';
  const params = { username, phone };
  try {
    const response = await API.post(url, params);
    return response.data;
  } catch (error: any) {
    return standardResponse(false, error.response?.data);
  }
}

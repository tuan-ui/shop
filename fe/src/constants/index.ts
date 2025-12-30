import { PATH_AUTH, PATH_ERROR, PATH_SYSTEM, PATH_HOME } from './routes.ts';

const DASHBOARD_ITEMS = [
  { title: 'sidebar.role', path: PATH_SYSTEM.role },
  { title: 'sidebar.user', path: PATH_SYSTEM.user },
];

const AUTHENTICATION_ITEMS = [
  { title: 'sign in', path: PATH_AUTH.signin },
  { title: 'sign up', path: PATH_AUTH.signup },
  { title: 'welcome', path: PATH_AUTH.welcome },
  { title: 'verify email', path: PATH_AUTH.verifyEmail },
  { title: 'password reset', path: PATH_AUTH.passwordReset },
  { title: 'account deleted', path: PATH_AUTH.accountDelete },
];

const ERROR_ITEMS = [
  { title: '400', path: PATH_ERROR.error400 },
  { title: '403', path: PATH_ERROR.error403 },
  { title: '404', path: PATH_ERROR.error404 },
  { title: '500', path: PATH_ERROR.error500 },
  { title: '503', path: PATH_ERROR.error503 },
];

export {
  PATH_AUTH,
  PATH_ERROR,
  DASHBOARD_ITEMS,
  AUTHENTICATION_ITEMS,
  ERROR_ITEMS,
  PATH_SYSTEM,
  PATH_HOME,
};

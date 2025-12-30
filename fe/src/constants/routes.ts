function path(root: string, sublink: string) {
  return `${root}${sublink}`;
}
const ROOTS_AUTH = '/auth';
const ROOTS_ERRORS = '/errors';
const ROOTS_SYSTEM = '/system';
const ROOTS_DOCS = '/docs';
const ROOTS_DND = '/dnd';

export const PATH_HOME = {
  root: ROOTS_SYSTEM,
};

export const PATH_SYSTEM = {
  root: ROOTS_SYSTEM,
  role: path(ROOTS_SYSTEM, '/roles'),
  user: path(ROOTS_SYSTEM, '/users'),
};

export const PATH_AUTH = {
  root: ROOTS_AUTH,
  signin: path(ROOTS_AUTH, '/signin'),
  signup: path(ROOTS_AUTH, '/signup'),
  passwordReset: path(ROOTS_AUTH, '/password-reset'),
  passwordConfirm: path(ROOTS_AUTH, '/password-confirmation'),
  welcome: path(ROOTS_AUTH, '/welcome'),
  verifyEmail: path(ROOTS_AUTH, '/verify-email'),
  accountDelete: path(ROOTS_AUTH, '/account-delete'),
  passwordResetResult: path(ROOTS_AUTH, '/passwordResetResult'),
};

export const PATH_ERROR = {
  root: ROOTS_ERRORS,
  error400: path(ROOTS_ERRORS, '/400'),
  error403: path(ROOTS_ERRORS, '/403'),
  error404: path(ROOTS_ERRORS, '/404'),
  error500: path(ROOTS_ERRORS, '/500'),
  error503: path(ROOTS_ERRORS, '/503'),
};

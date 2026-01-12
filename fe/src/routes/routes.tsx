import {
  createBrowserRouter,
  useNavigate,
  useLocation,
  Outlet,
} from 'react-router-dom';
import { useEffect } from 'react';
import { notification } from 'antd';
import { refreshToken } from '../api/authenticationApi';
import { useTranslation } from 'react-i18next';
import {
  Error400Page,
  Error403Page,
  Error404Page,
  Error500Page,
  Error503Page,
  ErrorPage,
  PasswordResetPage,
  SignInPage,
  SignUpPage,
  DefaultRolePage,
  DefaultUserPage,
} from '../pages';
import React, { ReactNode, Suspense } from 'react';
import { PATH_SYSTEM } from '../constants';
import ProtectedRoute from './ProtectedRoute';
import { PasswordResetResultPage } from '../pages/admin/authentication/ForgotPasswordModal';
import { DashboardLayout } from '../pages/admin/dashboards';
import Login from '../pages/client/Login';
import Home from '../pages/client/Home';

// Custom scroll restoration function

type PageProps = {
  children: ReactNode;
};

// Create an HOC to wrap your route components with ScrollToTop
const PageWrapper = ({ children }: PageProps) => {
  const navigate = useNavigate();
  const location = useLocation();
  const { t } = useTranslation();

  useEffect(() => {
    const verifyToken = async () => {
      const token = localStorage.getItem('token');
      const refresh = localStorage.getItem('refreshToken');

      if (!token) {
        navigate('/');
        return;
      }

      try {
        const [, payloadBase64] = token.split('.');
        const payload = JSON.parse(atob(payloadBase64));
        const now = Math.floor(Date.now() / 1000);

        // Token hết hạn
        if (payload.exp && payload.exp < now) {
          if (refresh) {
            try {
              const res = await refreshToken(refresh);
              if (res.object?.accessToken) {
                localStorage.setItem('token', res.object.accessToken);
                if (res.refreshToken) {
                  localStorage.setItem('refreshToken', res.refreshToken);
                }
                navigate(PATH_SYSTEM.root);
              } else {
                notification.error({
                  message: t('error.connectTimeOut'),
                  description: t('error.expiredToken'),
                });
                localStorage.removeItem('token');
                localStorage.removeItem('refreshToken');
                navigate('/');
                return;
              }
            } catch (error) {
              notification.error({
                message: t('error.connectTimeOut'),
                description: t('error.expiredToken'),
              });
              localStorage.removeItem('token');
              localStorage.removeItem('refreshToken');
              navigate('/');
              return;
            }
          } else {
            notification.error({
              message: t('error.connectTimeOut'),
              description: t('error.expiredToken'),
            });
            localStorage.removeItem('token');
            localStorage.removeItem('refreshToken');
            navigate('/');
            return;
          }
        } else {
          const path = location?.pathname || '';
          const isGuestPath =
            path === '/' || path.startsWith('/auth') || path === '';
          if (isGuestPath) {
            navigate(PATH_SYSTEM.root);
          }
        }
      } catch (err) {
        notification.error({
          message: t('error.connectTimeOut'),
          description: t('error.expiredToken'),
        });
        localStorage.removeItem('token');
        localStorage.removeItem('refreshToken');
        navigate('/');
        return;
      }
    };

    verifyToken();
  }, [navigate]);

  return <>{children}</>;
};

// Create the router
const router = createBrowserRouter([
  {
    path: '/',
    // Redirect to /client/login by default
    element: <Login />,
    errorElement: <ErrorPage />,
    children: [],
  },
  {
    path: '/admin/system',
    element: <PageWrapper children={<DashboardLayout />} />,
    children: [],
  },
  {
    path: '/admin/system',
    element: <PageWrapper children={<DashboardLayout />} />,
    errorElement: <ErrorPage />,
    children: [
      {
        index: true,
        path: 'default',
        element: <DashboardLayout />,
      },
      {
        path: 'roles',
        element: (
          <ProtectedRoute required="ROLE">
            <DefaultRolePage />
          </ProtectedRoute>
        ),
      },
      {
        path: 'users',
        element: (
          <ProtectedRoute required="USER">
            <DefaultUserPage />
          </ProtectedRoute>
        ),
      },
    ],
  },
  {
    path: '/admin/auth',
    errorElement: <ErrorPage />,
    children: [
      // {
      //   path: 'signup',
      //   element: <SignUpPage />,
      // },
      {
        path: 'signin',
        element: <SignInPage />,
      },
      {
        path: 'password-reset',
        element: <PasswordResetPage />,
      },
      {
        path: 'passwordResetResult',
        element: <PasswordResetResultPage />,
      },
    ],
  },
  {
    path: 'errors',
    errorElement: <ErrorPage />,
    children: [
      {
        path: '400',
        element: <Error400Page />,
      },
      {
        path: '403',
        element: <Error403Page />,
      },
      {
        path: '404',
        element: <Error404Page />,
      },
      {
        path: '500',
        element: <Error500Page />,
      },
      {
        path: '503',
        element: <Error503Page />,
      },
    ],
  },
  {
    path: '/admin',
    element: <DashboardLayout />,
    children: [
      {
        index: true,
        path: '',
        element: <DashboardLayout />,
      },
    ],
  },
  {
    path: '/client',
    children: [
      {
        path: 'login',
        element: <Login />,
      },
      {
        path: 'home',
        element: <Home />,
      },
    ],
  },
]);

export default router;

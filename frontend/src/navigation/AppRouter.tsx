// ================================================================
// Router principal de la aplicación.
// ================================================================

import { lazy, Suspense, useEffect } from 'react'
import {
  createBrowserRouter,
  RouterProvider,
  Navigate,
  useLocation,
  Outlet,
} from 'react-router-dom'
import { useAuthStore } from '../store/authStore'
import Layout from '../shared/components/Layout'
import Loader from '../shared/components/Loader'
import LandingPage from '../features/landing/pages/LandingPage'
import AnalysisPage from '../features/analysis/pages/AnalysisPage'
import HistoryPage from '../features/history/pages/HistoryPage'
import HelpPage from '../features/help/pages/HelpPage'
import LoginPage from '../features/auth/pages/LoginPage'
import RegisterPage from '../features/auth/pages/RegisterPage'
import AdminPage from '../features/admin/pages/AdminPage'

const ResultsPage = lazy(() => import('../features/results/pages/ResultsPage'))

const resultsElement = (
  <Suspense fallback={<Loader text="Cargando resultados..." />}>
    <ResultsPage />
  </Suspense>
)

function ProtectedRoute() {
  const isAuthenticated = useAuthStore((s) => s.isAuthenticated)
  const location = useLocation()

  if (!isAuthenticated) {
    return (
      <Navigate
        to="/login"
        state={{ from: location.pathname }}
        replace
      />
    )
  }
  return <Outlet />
}

function UnauthorizedWatcher() {
  const logout = useAuthStore((s) => s.logout)

  useEffect(() => {
    const handler = () => {
      logout()
      window.location.href = '/login'
    }
    window.addEventListener('energiai:unauthorized', handler)
    return () => window.removeEventListener('energiai:unauthorized', handler)
  }, [logout])

  return null
}

const router = createBrowserRouter([
  {
    path: '/',
    element: (
      <>
        <UnauthorizedWatcher />
        <Layout />
      </>
    ),
    children: [
      { index: true, element: <LandingPage /> },
      { path: 'ayuda', element: <HelpPage /> },
      {
        element: <ProtectedRoute />,
        children: [
          { path: 'analizar', element: <AnalysisPage /> },
          { path: 'historial', element: <HistoryPage /> },
          // Panel de admin — protegido por autenticación.
          // La visibilidad del link en el navbar se controla por rol (JWT).
          { path: 'admin', element: <AdminPage /> },
        ],
      },
      // ✅ Solo ruta con ID. El backend responde GET /api/analisis/{id}
      { path: 'resultados/:id', element: resultsElement },
    ],
  },
  { path: '/login', element: <LoginPage /> },
  { path: '/registro', element: <RegisterPage /> },
])

export default function AppRouter() {
  return <RouterProvider router={router} />
}
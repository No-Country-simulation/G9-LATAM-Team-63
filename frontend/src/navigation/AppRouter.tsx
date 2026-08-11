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

// ResultsPage se carga bajo demanda: incluye los gráficos de Recharts
// y no debe penalizar la carga inicial del sitio.
const ResultsPage = lazy(() => import('../features/results/pages/ResultsPage'))

const resultsElement = (
  <Suspense fallback={<Loader text="Cargando resultados..." />}>
    <ResultsPage />
  </Suspense>
)

// ─── ProtectedRoute ──────────────────────────────────────────────────────────
// Redirige a /login con { from: location.pathname } si no hay sesión activa.
// LoginPage lee ese estado para volver a la ruta protegida tras el login.
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

// ─── UnauthorizedWatcher ─────────────────────────────────────────────────────
// Escucha el evento custom lanzado por client.ts cuando recibe un 401.
// Usa el store directamente (sin hooks de react-router para evitar dep circular).
function UnauthorizedWatcher() {
  const logout = useAuthStore((s) => s.logout)

  useEffect(() => {
    const handler = () => {
      logout()
      // Redireccionamos recargando — el router ya tomará la ruta protegida y mandará a /login
      window.location.href = '/login'
    }
    window.addEventListener('energiai:unauthorized', handler)
    return () => window.removeEventListener('energiai:unauthorized', handler)
  }, [logout])

  return null
}

// ─── Router ──────────────────────────────────────────────────────────────────
const router = createBrowserRouter([
  // Layout principal (con Navbar y Footer)
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
      // Rutas protegidas — requieren sesión
      {
        element: <ProtectedRoute />,
        children: [
          { path: 'analizar', element: <AnalysisPage /> },
          { path: 'historial', element: <HistoryPage /> },
        ],
      },
      // /resultados no requiere auth (redirige solo si no hay result en store)
      { path: 'resultados', element: resultsElement },
      { path: 'resultados/:id', element: resultsElement },
    ],
  },
  // Auth — sin Layout (sin Navbar/Footer)
  { path: '/login', element: <LoginPage /> },
  { path: '/registro', element: <RegisterPage /> },
])

export default function AppRouter() {
  return <RouterProvider router={router} />
}

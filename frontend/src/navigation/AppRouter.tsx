import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import Layout from '../shared/components/Layout'
import LandingPage from '../features/landing/pages/LandingPage'
import AnalysisPage from '../features/analysis/pages/AnalysisPage'
import ResultsPage from '../features/results/pages/ResultsPage'
import HistoryPage from '../features/history/pages/HistoryPage'
import HelpPage from '../features/help/pages/HelpPage'

const router = createBrowserRouter([
  {
    path: '/',
    element: <Layout />,
    children: [
      { index: true, element: <LandingPage /> },
      { path: 'analizar', element: <AnalysisPage /> },
      { path: 'resultados', element: <ResultsPage /> },
      { path: 'resultados/:id', element: <ResultsPage /> },
      { path: 'historial', element: <HistoryPage /> },
      { path: 'ayuda', element: <HelpPage /> },
    ],
  },
])

export default function AppRouter() {
  return <RouterProvider router={router} />
}

import { useEffect } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useAnalysisStore } from '../../../store/analysisStore'
import ClassificationCard from '../components/ClassificationCard'
import CostEstimate from '../components/CostEstimate'
import RecommendationsList from '../components/RecommendationsList'
import Button from '../../../shared/components/Button'
import Loader from '../../../shared/components/Loader'

export default function ResultsPage() {
  const navigate = useNavigate()
  const result = useAnalysisStore((s) => s.result)

  useEffect(() => {
    if (!result) {
      navigate('/analizar', { replace: true })
    }
  }, [result, navigate])

  if (!result) {
    return <Loader text="Cargando resultados..." />
  }

  return (
    <section className="results-page">
      <div className="container">
        <div className="results-page__header">
          <div className="section-label">Resultados</div>
          <h1 className="section-title">
            Tu perfil energético:{' '}
            <span className="gradient-text">{result.categoria}</span>
          </h1>
          <p className="section-subtitle" style={{ margin: '0 auto' }}>
            Basado en los datos ingresados, estos son tus resultados de eficiencia energética.
          </p>
        </div>

        <div className="results-grid">
          <ClassificationCard result={result} />
          <CostEstimate result={result} />
          <RecommendationsList result={result} />
        </div>

        <div className="results-actions">
          <Button to="/analizar" variant="primary">
            Nuevo análisis
          </Button>
          <Link to="/" className="btn-secondary">
            Volver al inicio
          </Link>
        </div>
      </div>
    </section>
  )
}

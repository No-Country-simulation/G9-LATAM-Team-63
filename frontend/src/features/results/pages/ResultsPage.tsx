import { useEffect } from 'react'
import { useNavigate, useParams, Link } from 'react-router-dom'
import { useAnalysisStore } from '../../../store/analysisStore'
import { findHistoryEntry } from '../../../data/services/historyService'
import type { AnalysisInput, AnalysisResult } from '../../../data/types/analysis'
import ClassificationCard from '../components/ClassificationCard'
import CostEstimate from '../components/CostEstimate'
import InputSummary from '../components/InputSummary'
import ClassDistribution from '../components/ClassDistribution'
import SavingsProjection from '../components/SavingsProjection'
import Equivalences from '../components/Equivalences'
import RecommendationsList from '../components/RecommendationsList'
import ReportActions from '../components/ReportActions'
import Button from '../../../shared/components/Button'
import Loader from '../../../shared/components/Loader'
import { Icon } from '../../../shared/components/Icons'

export default function ResultsPage() {
  const navigate = useNavigate()
  const { id } = useParams<{ id: string }>()
  const storeResult = useAnalysisStore((s) => s.result)
  const storeInput = useAnalysisStore((s) => s.lastInput)

  const isDetail = Boolean(id)
  // localStorage es síncrono: la búsqueda se resuelve en el propio render
  const historyEntry = id ? findHistoryEntry(id) : undefined

  const result: AnalysisResult | null = isDetail ? historyEntry?.result ?? null : storeResult
  const input: AnalysisInput | null = isDetail ? historyEntry?.input ?? null : storeInput

  // Sin ID: redirigir si no hay resultado en el store
  useEffect(() => {
    if (!isDetail && !storeResult) {
      navigate('/analizar', { replace: true })
    }
  }, [isDetail, storeResult, navigate])

  if (isDetail && !historyEntry) {
    return (
      <section className="results-page">
        <div className="container">
          <div className="results-notfound">
            <div className="results-notfound__icon">
              <Icon name="alert" size={40} />
            </div>
            <h1 className="section-title">Análisis no encontrado</h1>
            <p className="section-subtitle" style={{ margin: '0 auto' }}>
              No pudimos encontrar el análisis solicitado en tu historial.
            </p>
            <div className="results-actions">
              <Button to="/historial">Ver historial</Button>
              <Link to="/" className="btn-secondary">Volver al inicio</Link>
            </div>
          </div>
        </div>
      </section>
    )
  }

  if (!result) {
    return <Loader text="Cargando resultados..." />
  }

  const fecha = isDetail && historyEntry
    ? new Date(historyEntry.created_at).toLocaleString('es-PE', {
        day: '2-digit',
        month: 'long',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
      })
    : new Date().toLocaleString('es-PE', {
        day: '2-digit',
        month: 'long',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
      })

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
          <div className="results-page__meta">
            <span>Análisis #{result.idAnalisis}</span>
            <span className="results-page__meta-sep">·</span>
            <span>{fecha}</span>
          </div>
        </div>

        <div className="results-grid">
          <ClassificationCard result={result} />
          <CostEstimate result={result} />
          {input && <InputSummary input={input} />}
          <ClassDistribution result={result} />
          <SavingsProjection result={result} />
          {input && <Equivalences input={input} />}
          <RecommendationsList result={result} />
          <ReportActions input={input} result={result} fecha={fecha} />
        </div>

        <div className="results-actions">
          <Button to="/analizar" variant="primary">
            Nuevo análisis
          </Button>
          <Button to="/historial" variant="secondary">
            Ver historial
          </Button>
          <Link to="/" className="btn-secondary">
            Volver al inicio
          </Link>
        </div>
      </div>
    </section>
  )
}

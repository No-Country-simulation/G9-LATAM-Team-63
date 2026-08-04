import ProfileBadge from '../../../shared/components/ProfileBadge'
import type { AnalysisResult } from '../../../data/types/analysis'

interface Props {
  result: AnalysisResult
}

export default function ClassificationCard({ result }: Props) {
  return (
    <div className="glass-card result-card">
      <p className="result-card__label">Clasificación energética</p>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <div>
          <div className="result-card__value">{result.categoria}</div>
          <div className="result-card__sub">
            <strong style={{ color: 'var(--color-accent-primary)' }}>
              {Math.round(result.probabilidad * 100)}%
            </strong>{' '}
            de confianza del modelo
          </div>
        </div>
        <ProfileBadge profile={result.categoria} />
      </div>
      <div className="prob-bar" style={{ marginTop: 16 }}>
        <div
          className="prob-bar__fill"
          style={{ width: `${Math.round(result.probabilidad * 100)}%` }}
        />
      </div>
    </div>
  )
}

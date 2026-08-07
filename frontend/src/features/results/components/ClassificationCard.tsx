import type { CSSProperties } from 'react'
import ProfileBadge from '../../../shared/components/ProfileBadge'
import type { AnalysisResult } from '../../../data/types/analysis'

interface Props {
  result: AnalysisResult
}

export default function ClassificationCard({ result }: Props) {
  const pct = Math.round(result.probabilidad * 100)
  const gaugeStyle = { '--gauge-pct': `${pct * 3.6}deg` } as CSSProperties

  return (
    <div className="glass-card result-card">
      <p className="result-card__label">Clasificación energética</p>
      <div className="result-class">
        <div className="result-class__info">
          <div className="result-card__value">{result.categoria}</div>
          <div className="result-card__sub">
            <strong style={{ color: 'var(--color-accent-primary)' }}>{pct}%</strong>{' '}
            de confianza del modelo
          </div>
          <ProfileBadge profile={result.categoria} />
        </div>
        <div className="gauge" style={gaugeStyle} role="img" aria-label={`Confianza del ${pct}%`}>
          <div className="gauge__inner">
            <span>{pct}%</span>
          </div>
        </div>
      </div>
      <div className="prob-bar" style={{ marginTop: 16 }}>
        <div className="prob-bar__fill" style={{ width: `${pct}%` }} />
      </div>
    </div>
  )
}

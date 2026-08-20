import { TARIFA_KWH } from '../../../config/constants'
import type { AnalysisResult } from '../../../data/types/analysis'

interface Props {
  result: AnalysisResult
}

export default function CostEstimate({ result }: Props) {
  const mensual = result.costo_estimado_mensual

  return (
    <div className="glass-card result-card" style={{ borderColor: 'rgba(30, 64, 175, 0.15)' }}>
      <p className="result-card__label">Estimación financiera</p>
      <div className="result-card__value" style={{ color: 'var(--color-accent-primary)', fontSize: '2.2rem' }}>
        ${mensual.toFixed(2)}
      </div>
      <div className="result-card__sub">
        costo estimado mensual · @${TARIFA_KWH}/kWh
      </div>
    </div>
  )
}

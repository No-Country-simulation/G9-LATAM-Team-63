import { TARIFA_KWH } from '../../../config/constants'
import type { AnalysisResult } from '../../../data/types/analysis'

interface Props {
  result: AnalysisResult
}

export default function CostEstimate({ result }: Props) {
  const ahorroPotencial = (result.costo_estimado_mensual * 0.18).toFixed(2)

  return (
    <div className="glass-card result-card" style={{ borderColor: 'rgba(30, 64, 175, 0.15)' }}>
      <p className="result-card__label">Estimación financiera</p>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <div>
          <div className="result-card__value" style={{ color: 'var(--color-accent-primary)', fontSize: '2.2rem' }}>
            ${result.costo_estimado_mensual.toFixed(2)}
          </div>
          <div className="result-card__sub">
            costo estimado mensual · @${TARIFA_KWH}/kWh
          </div>
        </div>
        <div style={{ textAlign: 'right' }}>
          <span style={{ fontFamily: 'var(--font-primary)', fontSize: '1.2rem', fontWeight: 700, color: 'var(--color-accent-success)', display: 'block' }}>
            -${ahorroPotencial}
          </span>
          <span style={{ fontSize: '0.75rem', color: 'var(--color-text-secondary)' }}>ahorro posible</span>
        </div>
      </div>
    </div>
  )
}

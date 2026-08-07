import { TARIFA_KWH } from '../../../config/constants'
import { savingsRate } from '../../../data/services/insights'
import type { AnalysisResult } from '../../../data/types/analysis'

interface Props {
  result: AnalysisResult
}

export default function CostEstimate({ result }: Props) {
  const rate = savingsRate(result.categoria)
  const mensual = result.costo_estimado_mensual
  const anual = mensual * 12
  const ahorroMensual = mensual * rate
  const ahorroAnual = ahorroMensual * 12

  return (
    <div className="glass-card result-card" style={{ borderColor: 'rgba(30, 64, 175, 0.15)' }}>
      <p className="result-card__label">Estimación financiera</p>
      <div className="result-card__value" style={{ color: 'var(--color-accent-primary)', fontSize: '2.2rem' }}>
        ${mensual.toFixed(2)}
      </div>
      <div className="result-card__sub">
        costo estimado mensual · @${TARIFA_KWH}/kWh
      </div>
      <div className="cost-details">
        <div className="cost-details__row">
          <span>Proyección anual</span>
          <strong>${anual.toFixed(2)}</strong>
        </div>
        <div className="cost-details__row cost-details__row--savings">
          <span>
            Ahorro potencial{' '}
            <em>({Math.round(rate * 100)}%)</em>
          </span>
          <strong>
            -${ahorroMensual.toFixed(2)}/mes · -${ahorroAnual.toFixed(2)}/año
          </strong>
        </div>
      </div>
    </div>
  )
}

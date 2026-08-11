import { Icon, type IconName } from '../../../shared/components/Icons'
import { buildEquivalences } from '../../../data/services/insights'
import type { AnalysisInput } from '../../../data/types/analysis'

interface Props {
  input: AnalysisInput
}

export default function Equivalences({ input }: Props) {
  const eq = buildEquivalences(input.consumoKwh)

  const tiles: { icon: IconName; value: string; label: string; tone: string }[] = [
    { icon: 'cloud', value: `${eq.co2Kg.toFixed(0)} kg`, label: 'de CO₂ emitidos por mes', tone: 'tone-blue' },
    { icon: 'leaf', value: eq.arboles.toFixed(1), label: 'árboles necesarios para absorber ese CO₂ en un año', tone: 'tone-green' },
    { icon: 'tv', value: `${eq.horasTv.toFixed(0)} horas`, label: 'de TV LED encendida equivalen a tu consumo mensual', tone: 'tone-purple' },
    { icon: 'car', value: `${eq.kmAuto.toFixed(0)} km`, label: 'recorridos en auto eléctrico con tu consumo mensual', tone: 'tone-amber' },
  ]

  return (
    <div className="glass-card result-card result-card--full">
      <p className="result-card__label">Impacto en contexto</p>
      <div className="equivalence-grid">
        {tiles.map((tile) => (
          <div className="equivalence-tile" key={tile.label}>
            <span className={`equivalence-tile__icon equivalence-tile__icon--${tile.tone}`}>
              <Icon name={tile.icon} size={20} />
            </span>
            <div>
              <div className="equivalence-tile__value">{tile.value}</div>
              <div className="equivalence-tile__label">{tile.label}</div>
            </div>
          </div>
        ))}
      </div>
      <p className="equivalence-foot">
        Estimaciones con factores de referencia (EPA y promedios de eficiencia de equipos).
      </p>
    </div>
  )
}

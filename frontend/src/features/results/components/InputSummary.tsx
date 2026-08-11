import { Icon, type IconName } from '../../../shared/components/Icons'
import { compareToAverage, annualized } from '../../../data/services/insights'
import { PROMEDIO_KWH_MENSUAL, TARIFA_KWH } from '../../../config/constants'
import type { AnalysisInput } from '../../../data/types/analysis'

interface Props {
  input: AnalysisInput
}

const inmuebleIcon: Record<AnalysisInput['tipoInmueble'], IconName> = {
  Casa: 'house',
  Apartamento: 'building',
  Local: 'store',
  Oficina: 'building',
}

export default function InputSummary({ input }: Props) {
  const { pct } = compareToAverage(input.consumoKwh)
  const arribaPromedio = pct > 0

  const items: { icon: IconName; label: string; value: string }[] = [
    { icon: 'zap', label: 'Consumo mensual', value: `${input.consumoKwh} kWh` },
    { icon: 'plug', label: 'Equipos', value: String(input.cantidadEquipos) },
    { icon: inmuebleIcon[input.tipoInmueble], label: 'Tipo de inmueble', value: input.tipoInmueble },
    { icon: 'users', label: 'Habitantes', value: String(input.numeroHabitantes) },
    { icon: 'clock', label: 'Horas alto consumo', value: `${input.horasAltoConsumo} h/día` },
    { icon: 'building', label: 'Antigüedad', value: `${input.antiguedadInmueble} años` },
    { icon: 'zap', label: 'Horario pico', value: input.usoHorarioPico ? 'Sí' : 'No' },
    { icon: 'flame', label: 'Calefacción eléctrica', value: input.calefaccion ? 'Sí' : 'No' },
    { icon: 'snowflake', label: 'Aire acondicionado', value: input.aireAcondicionado ? 'Sí' : 'No' },
  ]

  return (
    <div className="glass-card result-card result-card--full">
      <div className="result-card__head">
        <p className="result-card__label">Tu consumo registrado</p>
        <span
          className={`avg-chip ${arribaPromedio ? 'avg-chip--warn' : 'avg-chip--ok'}`}
        >
          {arribaPromedio
            ? `${pct}% sobre el promedio de ${PROMEDIO_KWH_MENSUAL} kWh`
            : `${Math.abs(pct)}% bajo el promedio de ${PROMEDIO_KWH_MENSUAL} kWh`}
        </span>
      </div>
      <div className="input-chips">
        {items.map((item) => (
          <div className="input-chip" key={item.label}>
            <Icon name={item.icon} size={16} />
            <div>
              <span className="input-chip__label">{item.label}</span>
              <span className="input-chip__value">{item.value}</span>
            </div>
          </div>
        ))}
      </div>
      <p className="input-summary__foot">
        Costo anual equivalente: <strong>${annualized(input.consumoKwh * TARIFA_KWH).toFixed(2)}</strong>
      </p>
    </div>
  )
}

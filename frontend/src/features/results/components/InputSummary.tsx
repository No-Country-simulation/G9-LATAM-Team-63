import { Icon, type IconName } from '../../../shared/components/Icons'
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
      <p className="result-card__label">Tu consumo registrado</p>
      <div className="input-chips input-chips--3col">
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
    </div>
  )
}

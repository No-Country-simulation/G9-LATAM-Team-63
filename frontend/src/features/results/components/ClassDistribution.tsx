import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Cell,
  LabelList,
  ResponsiveContainer,
} from 'recharts'
import type { AnalysisResult } from '../../../data/types/analysis'

interface Props {
  result: AnalysisResult
}

const CATEGORY_ORDER = ['Eficiente', 'Moderado', 'Ineficiente'] as const

const CATEGORY_COLORS: Record<(typeof CATEGORY_ORDER)[number], string> = {
  Eficiente: 'var(--color-accent-success)',
  Moderado: 'var(--color-accent-warn)',
  Ineficiente: 'var(--color-accent-danger)',
}

interface DistributionItem {
  name: (typeof CATEGORY_ORDER)[number]
  value: number
  isWinner: boolean
}

// Distribución de probabilidades por clase que devuelve el modelo de
// ciencia de datos (campo "distancias"). Si el backend aún no lo envía,
// el componente no se renderiza (fallback al gauge tradicional).
export default function ClassDistribution({ result }: Props) {
  const distancias = result.distancias
  if (!distancias) return null

  const data: DistributionItem[] = CATEGORY_ORDER.map((cat) => ({
    name: cat,
    value: Math.round((distancias[cat] ?? 0) * 100),
    isWinner: cat === result.categoria,
  }))

  return (
    <div className="glass-card result-card result-card--full">
      <p className="result-card__label">Probabilidad por perfil (modelo)</p>
      <div className="class-distribution">
        <ResponsiveContainer width="100%" height={170}>
          <BarChart
            data={data}
            layout="vertical"
            margin={{ top: 0, right: 44, left: 0, bottom: 0 }}
          >
            <XAxis type="number" domain={[0, 100]} hide />
            <YAxis
              type="category"
              dataKey="name"
              width={92}
              axisLine={false}
              tickLine={false}
              tick={{
                fill: 'var(--color-text-secondary)',
                fontSize: 13,
                fontWeight: 600,
              }}
            />
            <Bar dataKey="value" radius={[0, 8, 8, 0]} barSize={26}>
              {data.map((entry) => (
                <Cell
                  key={entry.name}
                  fill={CATEGORY_COLORS[entry.name]}
                  fillOpacity={entry.isWinner ? 1 : 0.3}
                />
              ))}
              <LabelList
                dataKey="value"
                position="right"
                formatter={(label: React.ReactNode) => `${label}%`}
                style={{
                  fill: 'var(--color-text-secondary)',
                  fontSize: 12,
                  fontWeight: 700,
                }}
              />
            </Bar>
          </BarChart>
        </ResponsiveContainer>
      </div>
      <p className="equivalence-foot">
        Probabilidad asignada por el modelo a cada perfil. La barra destacada es tu clasificación.
      </p>
    </div>
  )
}

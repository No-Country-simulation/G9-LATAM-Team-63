import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from 'recharts'
import { savingsRate } from '../../../data/services/insights'
import type { AnalysisResult } from '../../../data/types/analysis'

interface Props {
  result: AnalysisResult
}

const MONTHS = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic']

interface TooltipRow {
  dataKey?: string | number
  name?: string | number
  value?: string | number
  color?: string
}

interface MoneyTooltipProps {
  active?: boolean
  payload?: TooltipRow[]
  label?: string | number
}

function MoneyTooltip({ active, payload, label }: MoneyTooltipProps) {
  if (!active || !payload?.length) return null
  return (
    <div className="chart-tooltip">
      <span className="chart-tooltip__label">{label}</span>
      {payload.map((entry) => (
        <span className="chart-tooltip__row" key={String(entry.dataKey)}>
          <i style={{ background: entry.color }} />
          {entry.name}: <strong>${Number(entry.value).toFixed(2)}</strong>
        </span>
      ))}
    </div>
  )
}

export default function SavingsProjection({ result }: Props) {
  const rate = savingsRate(result.categoria)
  const monthly = Number(result.costo_estimado_mensual.toFixed(2))
  const optimized = Number((monthly * (1 - rate)).toFixed(2))
  const annualSavings = (monthly - optimized) * 12

  const data = MONTHS.map((mes) => ({
    mes,
    Actual: monthly,
    Optimizado: optimized,
  }))

  return (
    <div className="glass-card result-card result-card--full">
      <div className="result-card__head">
        <p className="result-card__label">Proyección de ahorro anual</p>
        <span className="projection-badge">
          Ahorro estimado: <strong>-${annualSavings.toFixed(2)}/año</strong>
        </span>
      </div>

      <div className="projection-chart">
        <ResponsiveContainer width="100%" height={250}>
          <BarChart data={data} margin={{ top: 5, right: 8, left: -10, bottom: 0 }} barGap={4}>
            <CartesianGrid strokeDasharray="3 3" stroke="var(--color-border)" vertical={false} />
            <XAxis
              dataKey="mes"
              tick={{ fill: 'var(--color-text-muted)', fontSize: 11 }}
              axisLine={false}
              tickLine={false}
            />
            <YAxis
              tick={{ fill: 'var(--color-text-muted)', fontSize: 11 }}
              axisLine={false}
              tickLine={false}
              tickFormatter={(value: number) => `$${value}`}
              width={52}
            />
            <Tooltip content={<MoneyTooltip />} cursor={{ fill: 'rgba(96, 165, 250, 0.06)' }} />
            <Legend wrapperStyle={{ fontSize: 12, paddingTop: 8 }} />
            <Bar
              dataKey="Actual"
              name="Costo actual"
              fill="var(--color-accent-primary)"
              radius={[4, 4, 0, 0]}
              maxBarSize={14}
              isAnimationActive
              animationDuration={700}
            />
            <Bar
              dataKey="Optimizado"
              name="Con optimización"
              fill="var(--color-accent-success)"
              radius={[4, 4, 0, 0]}
              maxBarSize={14}
              isAnimationActive
              animationDuration={700}
            />
          </BarChart>
        </ResponsiveContainer>
      </div>
    </div>
  )
}

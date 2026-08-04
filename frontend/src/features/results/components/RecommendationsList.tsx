import type { AnalysisResult } from '../../../data/types/analysis'

interface Props {
  result: AnalysisResult
}

export default function RecommendationsList({ result }: Props) {
  return (
    <div className="glass-card result-card result-card--full">
      <p className="result-card__label">Recomendaciones de optimización</p>
      <ul className="result-card__recs">
        {result.recomendaciones.map((rec: string, i: number) => (
          <li key={i}>{rec}</li>
        ))}
      </ul>
    </div>
  )
}

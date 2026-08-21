import { Icon } from '../../../shared/components/Icons'
import Button from '../../../shared/components/Button'
import { buildReportText } from '../../../data/services/insights'
import type { AnalysisInput, AnalysisResult } from '../../../data/types/analysis'

interface Props {
  input: AnalysisInput | null
  result: AnalysisResult
  fecha: string
}

export default function ReportActions({ input, result, fecha }: Props) {
  const text = buildReportText(input, result, fecha)

  const handleDownload = () => {
    const blob = new Blob([text], { type: 'text/plain;charset=utf-8' })
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `informe-energia-${result.idAnalisis}.txt`
    a.click()
    URL.revokeObjectURL(url)
  }

  return (
    <div className="glass-card result-card result-card--full report-actions">
      <p className="result-card__label">Descargar informe</p>
      <div className="report-actions__row">
        <Button variant="secondary" onClick={handleDownload}>
          <span className="report-actions__btn">
            <Icon name="download" size={16} />
            Descargar informe
          </span>
        </Button>
      </div>
    </div>
  )
}

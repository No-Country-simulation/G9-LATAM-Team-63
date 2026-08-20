import type { AnalysisInput, AnalysisResult } from '../types/analysis'

function fmt(n: number, digits = 2): string {
  return n.toLocaleString('es-PE', {
    minimumFractionDigits: digits,
    maximumFractionDigits: digits,
  })
}

function booleanLabel(value: boolean): string {
  return value ? 'Sí' : 'No'
}

export function buildReportText(
  input: AnalysisInput | null,
  result: AnalysisResult,
  fecha: string,
): string {
  const monthly = result.costo_estimado_mensual

  const lines = [
    'INFORME ENERGÉTICO — EnergiAI',
    `ID de análisis: ${result.idAnalisis}`,
    `Fecha: ${fecha}`,
    '',
    `Perfil energético: ${result.categoria} (confianza ${Math.round(result.probabilidad * 100)}%)`,
    `Costo estimado mensual: $${fmt(monthly)}`,
  ]

  if (input) {
    lines.push(
      '',
      'Datos registrados:',
      `- Consumo: ${input.consumoKwh} kWh/mes`,
      `- Cantidad de equipos: ${input.cantidadEquipos}`,
      `- Número de habitantes: ${input.numeroHabitantes}`,
      `- Horas de alto consumo: ${input.horasAltoConsumo} h/día`,
      `- Tipo de inmueble: ${input.tipoInmueble}`,
      `- Antigüedad del inmueble: ${input.antiguedadInmueble} años`,
      `- Consumo en horario pico: ${booleanLabel(input.usoHorarioPico)}`,
      `- Calefacción eléctrica: ${booleanLabel(input.calefaccion)}`,
      `- Aire acondicionado: ${booleanLabel(input.aireAcondicionado)}`,
    )
  }

  lines.push('', 'Recomendaciones:')
  result.recomendaciones.forEach((rec) => lines.push(`- ${rec}`))

  lines.push('', 'Generado con EnergiAI — Consumo Inteligente.')
  return lines.join('\n')
}

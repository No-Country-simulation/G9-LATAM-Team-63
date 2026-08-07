import type { AnalysisInput, AnalysisResult } from '../types/analysis'
import { PROMEDIO_KWH_MENSUAL } from '../../config/constants'

// Porcentaje de ahorro potencial según la categoría energética.
// Referencias orientativas usadas para la proyección financiera.
export const SAVINGS_RATE: Record<AnalysisResult['categoria'], number> = {
  Eficiente: 0.05,
  Moderado: 0.15,
  Ineficiente: 0.3,
}

export function savingsRate(categoria: AnalysisResult['categoria']): number {
  return SAVINGS_RATE[categoria]
}

export function annualized(value: number): number {
  return value * 12
}

export function compareToAverage(consumoKwh: number): {
  diff: number
  pct: number
} {
  const diff = consumoKwh - PROMEDIO_KWH_MENSUAL
  const pct = Math.round((diff / PROMEDIO_KWH_MENSUAL) * 100)
  return { diff, pct }
}

// Equivalencias de impacto — valores orientativos de referencia:
// factor de emisión EPA ~0.387 kg CO2/kWh, árbol absorbe ~21 kg CO2/año,
// TV LED ~60 W, auto eléctrico ~15 kWh/100 km.
export function buildEquivalences(consumoKwh: number): {
  co2Kg: number
  arboles: number
  horasTv: number
  kmAuto: number
} {
  const co2Kg = consumoKwh * 0.387
  return {
    co2Kg,
    arboles: co2Kg / 21,
    horasTv: consumoKwh / 0.06,
    kmAuto: consumoKwh / 0.15,
  }
}

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
  const rate = savingsRate(result.categoria)
  const monthly = result.costo_estimado_mensual
  const annual = monthly * 12
  const savingsAnnual = annual * rate

  const lines = [
    'INFORME ENERGÉTICO — EnergiAI',
    `ID de análisis: ${result.idAnalisis}`,
    `Fecha: ${fecha}`,
    '',
    `Perfil energético: ${result.categoria} (confianza ${Math.round(result.probabilidad * 100)}%)`,
    `Costo estimado mensual: $${fmt(monthly)}`,
    `Costo estimado anual: $${fmt(annual)}`,
    `Ahorro potencial anual: -$${fmt(savingsAnnual)} (${Math.round(rate * 100)}%)`,
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

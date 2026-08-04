import type { AnalysisInput, AnalysisResult } from '../types/analysis'
import { TARIFA_KWH } from '../../config/constants'

export function estimateCost(consumoKwh: number): number {
  return Number((consumoKwh * TARIFA_KWH).toFixed(2))
}

export function classifyProfile(input: AnalysisInput): AnalysisResult {
  const score = calculateScore(input)
  let categoria: AnalysisResult['categoria']
  let probabilidad: number

  if (score >= 70) {
    categoria = 'Eficiente'
    probabilidad = Number((score / 100).toFixed(2))
  } else if (score >= 40) {
    categoria = 'Moderado'
    probabilidad = Number(((100 - score) / 100).toFixed(2))
  } else {
    categoria = 'Ineficiente'
    probabilidad = Number(((100 - score) / 100).toFixed(2))
  }

  const recomendaciones = generateRecommendations(input, categoria)
  const costo_estimado_mensual = estimateCost(input.consumoKwh)

  return { categoria, probabilidad, recomendaciones, costo_estimado_mensual }
}

function calculateScore(input: AnalysisInput): number {
  let score = 100

  if (input.consumoKwh > 500) score -= 25
  else if (input.consumoKwh > 300) score -= 10

  if (input.usoHorarioPico) score -= 15

  if (input.cantidadEquipos > 15) score -= 15
  else if (input.cantidadEquipos > 8) score -= 8

  if (input.horasAltoConsumo > 10) score -= 20
  else if (input.horasAltoConsumo > 6) score -= 10

  if (input.tipoInmueble === 'Local' || input.tipoInmueble === 'Oficina') score -= 10

  return Math.max(0, score)
}

function generateRecommendations(input: AnalysisInput, categoria: string): string[] {
  const recs: string[] = []

  if (input.usoHorarioPico) {
    recs.push('Reducir el uso de equipos durante los horarios pico (18–22 h)')
  }

  if (input.cantidadEquipos > 8) {
    recs.push('Evaluar equipos con alto consumo energético para posible reemplazo')
  }

  if (input.horasAltoConsumo > 6) {
    recs.push('Distribuir las actividades de mayor consumo a lo largo del día')
  }

  if (input.consumoKwh > 400) {
    recs.push('Revisar aislamiento térmico y eficiencia de climatización')
  }

  if (categoria === 'Ineficiente') {
    recs.push('Considerar una auditoría energética profesional')
    recs.push('Reemplazar electrodomésticos por modelos de alta eficiencia (A++ o superior)')
  }

  recs.push('Monitorear el consumo semanalmente para detectar cambios en los patrones de uso')

  return recs.slice(0, 5)
}

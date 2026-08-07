import { api } from './client'
import type {
  AnalysisInput,
  AnalysisResult,
  HistorialEntryDto,
} from '../types/analysis'

// Rutas reales del backend (base: /api) → POST /api/analisis,
// GET /api/analisis/historial, GET /api/analisis/{id}
export function postAnalysis(input: AnalysisInput): Promise<AnalysisResult> {
  return api.post<AnalysisResult>('/analisis', input)
}

export function getHistory(): Promise<HistorialEntryDto[]> {
  return api.get<HistorialEntryDto[]>('/analisis/historial')
}

export function getResult(id: string): Promise<HistorialEntryDto> {
  return api.get<HistorialEntryDto>(`/analisis/${id}`)
}

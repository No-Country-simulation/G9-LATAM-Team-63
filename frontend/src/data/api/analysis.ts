import { api } from './client'
import type { AnalysisInput, AnalysisResult, HistoryEntry } from '../types/analysis'

// Rutas reales del backend (base: /api) → POST /api/analisis, GET /api/analisis/{id}
export function postAnalysis(input: AnalysisInput): Promise<AnalysisResult> {
  return api.post<AnalysisResult>('/analisis', input)
}

export function getResult(id: string): Promise<HistoryEntry> {
  return api.get<HistoryEntry>(`/analisis/${id}`)
}

export function getHistory(): Promise<HistoryEntry[]> {
  return api.get<HistoryEntry[]>('/historial')
}

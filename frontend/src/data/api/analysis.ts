// ================================================================
// Capa de comunicación con el backend para el módulo de Análisis.
// Centraliza las peticiones HTTP al endpoint /api/analisis.
// ================================================================

import { api } from './client'
import type {
  AnalysisInput,
  AnalysisResult,
  HistorialEntryDto,
} from '../types/analysis'

// ================================================================
// 1. Crear Análisis Energético
// POST /api/analisis → 201 Created
// El backend calcula categoría, probabilidad y recomendaciones.
// Se asocia automáticamente al usuario autenticado vía JWT.
// ================================================================
export function postAnalysis(input: AnalysisInput): Promise<AnalysisResult> {
  return api.post<AnalysisResult>('/analisis', input)
}

// ================================================================
// 2. Listar Historial del Usuario
// GET /api/analisis/historial → 200 OK
// Filtrado por usuario autenticado, ordenado del más reciente al más antiguo.
// ================================================================
export function getHistory(): Promise<HistorialEntryDto[]> {
  return api.get<HistorialEntryDto[]>('/analisis/historial')
}

// ================================================================
// 3. Consultar Análisis por ID
// GET /api/analisis/{id} → 200 OK | 403 si no pertenece al usuario
// ================================================================
export function getResult(id: string): Promise<HistorialEntryDto> {
  return api.get<HistorialEntryDto>(`/analisis/${id}`)
}
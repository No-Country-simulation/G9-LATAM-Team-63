import { useMutation } from '@tanstack/react-query'
import { postAnalysis } from '../api/analysis'
import type { AnalysisInput, AnalysisResult } from '../types/analysis'
import { useAnalysisStore } from '../../store/analysisStore'
import type { ApiError } from '../api/client'

const HISTORY_KEY = 'energiai_history'

function saveToHistory(input: AnalysisInput, result: AnalysisResult): void {
  try {
    const raw = localStorage.getItem(HISTORY_KEY)
    const existing = raw ? JSON.parse(raw) : []
    const entry = {
      id: String(result.idAnalisis),
      input,
      result,
      created_at: new Date().toISOString(),
    }
    // Insertar al inicio, limitar a 50 entradas
    const updated = [entry, ...existing].slice(0, 50)
    localStorage.setItem(HISTORY_KEY, JSON.stringify(updated))
  } catch {
    // localStorage no disponible — continuar sin historial
  }
}

export function useAnalysisMutation() {
  const setResult = useAnalysisStore((s) => s.setResult)

  return useMutation<AnalysisResult, ApiError, AnalysisInput>({
    // Sin fallback: el análisis es 100% real o falla con error visible
    mutationFn: (input) => postAnalysis(input),
    onSuccess: (data, input) => {
      setResult(data)
      saveToHistory(input, data)
    },
  })
}

// ================================================================
// Hook de mutación para crear análisis energético.
// Envía los datos al backend (POST /api/analisis) y persiste
// el resultado en el store global + invalida el caché del historial.
// ================================================================

import { useMutation, useQueryClient } from '@tanstack/react-query'
import { postAnalysis } from '../api/analysis'
import type { AnalysisInput, AnalysisResult } from '../types/analysis'
import { useAnalysisStore } from '../../store/analysisStore'
import type { ApiError } from '../api/client'

export function useAnalysisMutation() {
  const setResult = useAnalysisStore((s) => s.setResult)
  const queryClient = useQueryClient()

  return useMutation<AnalysisResult, ApiError, AnalysisInput>({
    // Delega 100% al backend. Sin fallback local.
    mutationFn: (input) => postAnalysis(input),
    onSuccess: (data) => {
      setResult(data)
      // Fuerza refetch del historial para mostrar el análisis recién creado
      queryClient.invalidateQueries({ queryKey: ['history'] })
    },
  })
}
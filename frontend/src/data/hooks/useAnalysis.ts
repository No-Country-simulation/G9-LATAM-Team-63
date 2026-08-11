import { useMutation, useQueryClient } from '@tanstack/react-query'
import { postAnalysis } from '../api/analysis'
import type { AnalysisInput, AnalysisResult } from '../types/analysis'
import { useAnalysisStore } from '../../store/analysisStore'
import type { ApiError } from '../api/client'

export function useAnalysisMutation() {
  const setResult = useAnalysisStore((s) => s.setResult)
  const queryClient = useQueryClient()

  return useMutation<AnalysisResult, ApiError, AnalysisInput>({
    // Sin fallback: el análisis es 100% real o falla con error visible
    mutationFn: (input) => postAnalysis(input),
    onSuccess: (data) => {
      setResult(data)
      // El historial vive en el backend: invalidar la query para que
      // HistoryPage muestre el análisis recién creado
      queryClient.invalidateQueries({ queryKey: ['history'] })
    },
  })
}

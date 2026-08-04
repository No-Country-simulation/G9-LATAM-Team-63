import { useMutation } from '@tanstack/react-query'
import { postAnalysis } from '../api/analysis'
import type { AnalysisInput, AnalysisResult } from '../types/analysis'
import { useAnalysisStore } from '../../store/analysisStore'
import { classifyProfile } from '../services/analysisService'

export function useAnalysisMutation() {
  const setResult = useAnalysisStore((s) => s.setResult)

  return useMutation<AnalysisResult, Error, AnalysisInput>({
    mutationFn: async (input) => {
      try {
        return await postAnalysis(input)
      } catch {
        return classifyProfile(input)
      }
    },
    onSuccess: (data) => setResult(data),
  })
}

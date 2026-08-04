import { create } from 'zustand'
import type { AnalysisInput, AnalysisResult } from '../data/types/analysis'

interface AnalysisState {
  result: AnalysisResult | null
  lastInput: AnalysisInput | null
  setResult: (result: AnalysisResult) => void
  setLastInput: (input: AnalysisInput) => void
  clear: () => void
}

export const useAnalysisStore = create<AnalysisState>((set) => ({
  result: null,
  lastInput: null,
  setResult: (result) => set({ result }),
  setLastInput: (lastInput) => set({ lastInput }),
  clear: () => set({ result: null, lastInput: null }),
}))

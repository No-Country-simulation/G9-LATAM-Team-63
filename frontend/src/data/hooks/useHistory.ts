import { useQuery } from '@tanstack/react-query'
import type { HistoryEntry } from '../types/analysis'

const HISTORY_KEY = 'energiai_history'

function loadHistoryFromStorage(): HistoryEntry[] {
  try {
    const raw = localStorage.getItem(HISTORY_KEY)
    return raw ? JSON.parse(raw) : []
  } catch {
    return []
  }
}

// El historial se mantiene local (localStorage) — no existe endpoint /historial en el backend
export function useHistory() {
  return useQuery<HistoryEntry[]>({
    queryKey: ['history'],
    queryFn: loadHistoryFromStorage,
    // No hay fetching de red — siempre sincrónico desde localStorage
    staleTime: 0,
  })
}

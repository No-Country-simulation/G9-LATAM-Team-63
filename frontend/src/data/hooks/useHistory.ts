import { useQuery } from '@tanstack/react-query'
import type { HistoryEntry } from '../types/analysis'
import { getHistoryEntries } from '../services/historyService'

// El historial se mantiene local (localStorage) — no existe endpoint /historial en el backend
export function useHistory() {
  return useQuery<HistoryEntry[]>({
    queryKey: ['history'],
    queryFn: getHistoryEntries,
    // No hay fetching de red — siempre sincrónico desde localStorage
    staleTime: 0,
  })
}

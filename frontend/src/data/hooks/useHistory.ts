import { useQuery } from '@tanstack/react-query'
import type { HistoryEntry } from '../types/analysis'
import { getHistory } from '../api/analysis'
import { mapHistorialEntry } from '../services/historyService'

// El historial se consulta al backend (GET /api/analisis/historial),
// que devuelve solo los análisis del usuario autenticado vía JWT
export function useHistory() {
  return useQuery<HistoryEntry[]>({
    queryKey: ['history'],
    queryFn: async () => (await getHistory()).map(mapHistorialEntry),
    staleTime: 60_000,
  })
}

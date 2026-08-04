import { useQuery } from '@tanstack/react-query'
import { getHistory } from '../api/analysis'
import type { HistoryEntry } from '../types/analysis'

export function useHistory() {
  return useQuery<HistoryEntry[]>({
    queryKey: ['history'],
    queryFn: getHistory,
  })
}

import type { HistoryEntry } from '../types/analysis'

const HISTORY_KEY = 'energiai_history'

// El historial se mantiene local (localStorage) — no existe endpoint /historial en el backend
export function getHistoryEntries(): HistoryEntry[] {
  try {
    const raw = localStorage.getItem(HISTORY_KEY)
    return raw ? (JSON.parse(raw) as HistoryEntry[]) : []
  } catch {
    return []
  }
}

export function findHistoryEntry(id: string): HistoryEntry | undefined {
  return getHistoryEntries().find((entry) => entry.id === id)
}

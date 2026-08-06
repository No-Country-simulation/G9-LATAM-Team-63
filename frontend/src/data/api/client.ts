import { API_BASE_URL } from '../../config/constants'

// ─── Error personalizado ────────────────────────────────────────────────────
// Captura el shape de error del backend:
//   400 → { "error": "...", "detalles": { "<campo>": "<mensaje>" } }
//   401 → { "error": "..." }
//   500 → { "error": "..." }
export class ApiError extends Error {
  status: number
  details?: Record<string, string>

  constructor(message: string, status: number, details?: Record<string, string>) {
    super(message)
    this.name = 'ApiError'
    this.status = status
    this.details = details
  }
}

// ─── Cliente HTTP ───────────────────────────────────────────────────────────
interface RequestConfig {
  method?: string
  body?: unknown
  headers?: Record<string, string>
}

const TOKEN_KEY = 'energiai_token'
const USERNAME_KEY = 'energiai_username'

async function request<T>(endpoint: string, config: RequestConfig = {}): Promise<T> {
  const { method = 'GET', body, headers = {} } = config

  // Inyectar token si existe
  const token = localStorage.getItem(TOKEN_KEY)
  const authHeaders: Record<string, string> = token
    ? { Authorization: `Bearer ${token}` }
    : {}

  const options: RequestInit = {
    method,
    headers: {
      'Content-Type': 'application/json',
      ...authHeaders,
      ...headers,
    },
  }

  if (body) {
    options.body = JSON.stringify(body)
  }

  const response = await fetch(`${API_BASE_URL}${endpoint}`, options)

  // Manejar 401: limpiar sesión y notificar al router
  if (response.status === 401) {
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(USERNAME_KEY)
    window.dispatchEvent(new CustomEvent('energiai:unauthorized'))
    throw new ApiError('Sesión expirada. Por favor, inicia sesión nuevamente.', 401)
  }

  if (!response.ok) {
    const errorBody = await response.json().catch(() => ({}))
    // El backend puede enviar { error: string, detalles: Record<string, string> }
    const message: string =
      errorBody.error || errorBody.message || `Error HTTP ${response.status}`
    const details: Record<string, string> | undefined =
      errorBody.detalles ?? undefined
    throw new ApiError(message, response.status, details)
  }

  return response.json()
}

export const api = {
  get: <T>(endpoint: string) => request<T>(endpoint),
  post: <T>(endpoint: string, body: unknown) => request<T>(endpoint, { method: 'POST', body }),
}

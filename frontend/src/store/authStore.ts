import { create } from 'zustand'

const TOKEN_KEY = 'energiai_token'
const USERNAME_KEY = 'energiai_username'

interface AuthState {
  token: string | null
  username: string | null
  isAuthenticated: boolean
  login: (token: string, username: string) => void
  logout: () => void
}

function getInitialToken(): string | null {
  if (typeof window === 'undefined') return null
  return localStorage.getItem(TOKEN_KEY)
}

function getInitialUsername(): string | null {
  if (typeof window === 'undefined') return null
  return localStorage.getItem(USERNAME_KEY)
}

const initialToken = getInitialToken()
const initialUsername = getInitialUsername()

export const useAuthStore = create<AuthState>((set) => ({
  token: initialToken,
  username: initialUsername,
  isAuthenticated: !!initialToken,

  login: (token, username) => {
    localStorage.setItem(TOKEN_KEY, token)
    localStorage.setItem(USERNAME_KEY, username)
    set({ token, username, isAuthenticated: true })
  },

  logout: () => {
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(USERNAME_KEY)
    set({ token: null, username: null, isAuthenticated: false })
  },
}))

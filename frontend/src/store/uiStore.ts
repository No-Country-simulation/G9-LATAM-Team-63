import { create } from 'zustand'

export type Theme = 'light' | 'dark'

const THEME_KEY = 'energiai-theme'

function getInitialTheme(): Theme {
  if (typeof window === 'undefined') return 'light'
  const saved = localStorage.getItem(THEME_KEY)
  if (saved === 'light' || saved === 'dark') return saved
  return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'
}

interface UiState {
  isMobileMenuOpen: boolean
  toggleMobileMenu: () => void
  closeMobileMenu: () => void
  theme: Theme
  toggleTheme: () => void
}

export const useUiStore = create<UiState>((set, get) => ({
  isMobileMenuOpen: false,
  toggleMobileMenu: () => set((s) => ({ isMobileMenuOpen: !s.isMobileMenuOpen })),
  closeMobileMenu: () => set({ isMobileMenuOpen: false }),

  theme: getInitialTheme(),

  toggleTheme: () => {
    const next: Theme = get().theme === 'dark' ? 'light' : 'dark'

    const apply = () => {
      document.documentElement.classList.toggle('dark', next === 'dark')
      localStorage.setItem(THEME_KEY, next)
    }

    if ('startViewTransition' in document) {
      document.startViewTransition(apply)
    } else {
      apply()
    }

    set({ theme: next })
  },
}))
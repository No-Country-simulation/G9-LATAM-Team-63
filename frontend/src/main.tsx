import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'

/* Design System */
import './styles/design-system.css'
import './styles/animations.css'

/* Theme (debe ir al final para que sus overrides ganen) */
import './styles/theme.css'

/* Shared Components */
import './styles/components/shared/buttons.css'
import './styles/components/shared/glass-card.css'
import './styles/components/shared/profile-badge.css'
import './styles/components/shared/loader.css'

/* Feature Components */
import './styles/components/navbar.css'
import './styles/components/hero.css'
import './styles/components/features.css'
import './styles/components/stats.css'
import './styles/components/how-it-works.css'
import './styles/components/api-demo.css'
import './styles/components/tech-stack.css'
import './styles/components/use-cases.css'
import './styles/components/cta.css'
import './styles/components/footer.css'
import './styles/components/form.css'
import './styles/components/results.css'
import './styles/components/history.css'
import './styles/components/auth.css'

import App from './App.tsx'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <App />
  </StrictMode>,
)

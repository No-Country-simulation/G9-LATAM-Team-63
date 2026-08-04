import { useEffect, useState } from 'react'
import { Link, useLocation } from 'react-router-dom'
import Logo from '../../../shared/components/Logo'
import Button from '../../../shared/components/Button'
import ThemeToggle from '../../../shared/components/ThemeToggle'
import { useUiStore } from '../../../store/uiStore'

export default function Navbar() {
  const [scrolled, setScrolled] = useState(false)
  const location = useLocation()
  const isHome = location.pathname === '/'
  const closeMobileMenu = useUiStore((s) => s.closeMobileMenu)

  useEffect(() => {
    const handleScroll = () => setScrolled(window.scrollY > 40)
    window.addEventListener('scroll', handleScroll)
    return () => window.removeEventListener('scroll', handleScroll)
  }, [])

  return (
    <nav className={`navbar ${scrolled ? 'scrolled' : ''}`} role="navigation" aria-label="Navegación principal">
      <div className="container">
        <div className="navbar__inner">
          <Link to="/" className="navbar__logo" aria-label="EnergiAI - Inicio" onClick={closeMobileMenu}>
            <Logo />
          </Link>

          <ul className="navbar__links">
            <li>
              {isHome ? (
                <a href="#features">Características</a>
              ) : (
                <Link to="/#features">Características</Link>
              )}
            </li>
            <li>
              {isHome ? (
                <a href="#how-it-works">¿Cómo funciona?</a>
              ) : (
                <Link to="/#how-it-works">¿Cómo funciona?</Link>
              )}
            </li>
            <li><Link to="/analizar">Analizar consumo</Link></li>
            <li><Link to="/ayuda">Ayuda</Link></li>
          </ul>

          <div className="navbar__cta">
            <ThemeToggle />
            <Button to="/analizar" variant="primary">
              Analizar consumo
            </Button>
          </div>
        </div>
      </div>
    </nav>
  )
}

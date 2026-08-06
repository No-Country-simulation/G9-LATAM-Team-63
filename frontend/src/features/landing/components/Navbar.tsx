import { useEffect, useState } from 'react'
import { Link, useLocation, useNavigate } from 'react-router-dom'
import Logo from '../../../shared/components/Logo'
import Button from '../../../shared/components/Button'
import ThemeToggle from '../../../shared/components/ThemeToggle'
import { useUiStore } from '../../../store/uiStore'
import { useAuthStore } from '../../../store/authStore'

export default function Navbar() {
  const [scrolled, setScrolled] = useState(false)
  const location = useLocation()
  const navigate = useNavigate()
  const isHome = location.pathname === '/'
  const closeMobileMenu = useUiStore((s) => s.closeMobileMenu)
  const { isAuthenticated, username, logout } = useAuthStore()

  useEffect(() => {
    const handleScroll = () => setScrolled(window.scrollY > 40)
    window.addEventListener('scroll', handleScroll)
    return () => window.removeEventListener('scroll', handleScroll)
  }, [])

  const handleLogout = () => {
    logout()
    navigate('/')
    closeMobileMenu()
  }

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

            {isAuthenticated ? (
              /* Usuario con sesión */
              <div className="navbar__user">
                <span className="navbar__username" title={`Conectado como ${username}`}>
                  {username}
                </span>
                <button
                  type="button"
                  className="navbar__logout"
                  onClick={handleLogout}
                >
                  Cerrar sesión
                </button>
              </div>
            ) : (
              /* Sin sesión — un solo punto de entrada */
              <Button to="/login" variant="secondary">
                Iniciar sesión
              </Button>
            )}
          </div>
        </div>
      </div>
    </nav>
  )
}

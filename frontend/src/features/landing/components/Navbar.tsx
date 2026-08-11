import { useEffect, useRef, useState } from 'react'
import { Link, useLocation } from 'react-router-dom'
import Logo from '../../../shared/components/Logo'
import Button from '../../../shared/components/Button'
import ThemeToggle from '../../../shared/components/ThemeToggle'
import { Icon } from '../../../shared/components/Icons'
import { useUiStore } from '../../../store/uiStore'
import { useAuthStore } from '../../../store/authStore'

export default function Navbar() {
  const [scrolled, setScrolled] = useState(false)
  const [userMenuOpen, setUserMenuOpen] = useState(false)
  const userMenuRef = useRef<HTMLDivElement>(null)
  const location = useLocation()
  const isHome = location.pathname === '/'
  const closeMobileMenu = useUiStore((s) => s.closeMobileMenu)
  const { isAuthenticated, username, logout } = useAuthStore()

  useEffect(() => {
    const handleScroll = () => setScrolled(window.scrollY > 40)
    window.addEventListener('scroll', handleScroll)
    return () => window.removeEventListener('scroll', handleScroll)
  }, [])

  // Cerrar el submenú de usuario al hacer clic fuera de él
  useEffect(() => {
    if (!userMenuOpen) return
    const handleClickOutside = (event: MouseEvent) => {
      if (userMenuRef.current && !userMenuRef.current.contains(event.target as Node)) {
        setUserMenuOpen(false)
      }
    }
    document.addEventListener('mousedown', handleClickOutside)
    return () => document.removeEventListener('mousedown', handleClickOutside)
  }, [userMenuOpen])

  const handleLogout = () => {
    setUserMenuOpen(false)
    closeMobileMenu()
    logout()
    // Redirección dura a la portada: garantiza salir de rutas protegidas
    // sin que ProtectedRoute redirija a /login por la carrera de estados
    // entre el cierre de sesión y la navegación del router.
    window.location.href = '/'
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
              /* Usuario con sesión — submenú al presionar el usuario */
              <div className="navbar__user" ref={userMenuRef}>
                <button
                  type="button"
                  className={`navbar__user-btn${userMenuOpen ? ' is-open' : ''}`}
                  onClick={() => setUserMenuOpen((open) => !open)}
                  aria-haspopup="menu"
                  aria-expanded={userMenuOpen}
                  aria-label={`Menú de usuario: ${username}`}
                >
                  <span className="navbar__username" title={`Conectado como ${username}`}>
                    {username}
                  </span>
                  <Icon name="chevron-down" size={14} className="navbar__caret" />
                </button>

                {userMenuOpen && (
                  <div className="navbar__user-menu" role="menu">
                    <Link
                      to="/historial"
                      className="navbar__user-item"
                      role="menuitem"
                      onClick={() => {
                        setUserMenuOpen(false)
                        closeMobileMenu()
                      }}
                    >
                      <Icon name="clipboard" size={16} />
                      Mi historial
                    </Link>
                    <button
                      type="button"
                      className="navbar__user-item navbar__user-item--danger"
                      role="menuitem"
                      onClick={handleLogout}
                    >
                      <Icon name="logout" size={16} />
                      Cerrar sesión
                    </button>
                  </div>
                )}
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

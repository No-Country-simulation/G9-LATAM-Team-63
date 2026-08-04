import { Link } from 'react-router-dom'
import Logo from '../../../shared/components/Logo'

export default function Footer() {
  return (
    <footer className="footer" role="contentinfo">
      <div className="container">
        <div className="footer__inner">
          <Link to="/" className="footer__logo" aria-label="EnergiAI - Inicio">
            <Logo showTagline={false} />
          </Link>

          <p className="footer__copy">
            © 2026 EnergiAI · Hackathon ONE G9 LATAM · Tarifa de referencia: $0.75/kWh
          </p>

          <ul className="footer__links">
            <li><a href="#features">Características</a></li>
            <li><Link to="/analizar">API</Link></li>
            <li><Link to="/ayuda">Ayuda</Link></li>
          </ul>
        </div>
      </div>
    </footer>
  )
}

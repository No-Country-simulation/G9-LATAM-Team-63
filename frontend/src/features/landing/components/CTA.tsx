import { Link } from 'react-router-dom'
import { Icon } from '../../../shared/components/Icons'

const trustItems = [
  { icon: 'lock' as const, text: 'Datos seguros' },
  { icon: 'zap' as const, text: 'Resultados instantáneos' },
  { icon: 'leaf' as const, text: 'Impacto sostenible' },
  { icon: 'cloud' as const, text: 'Powered by OCI' },
]

export default function CTA() {
  return (
    <section id="cta" className="cta-section">
      <div className="container">
        <div className="cta-section__inner">
          <div className="cta-section__glow" aria-hidden="true" />

          <p className="cta-section__eyebrow" aria-hidden="true">
            Empieza hoy mismo
          </p>

          <h2 className="cta-section__title">
            Tu energía, tu ahorro,{' '}
            <span className="gradient-text">tu decisión</span>
          </h2>

          <p className="cta-section__subtitle">
            Miles de usuarios ya transformaron sus hábitos energéticos con la
            inteligencia de EnergiAI. Es gratis, es rápido y los resultados
            son inmediatos.
          </p>

          <div className="cta-section__actions">
            <Link to="/analizar" className="btn-primary">
              Analizar mi consumo gratis
            </Link>
            <a href="#demo" className="btn-secondary">
              Ver documentación API
            </a>
          </div>

          <div className="cta-trust" aria-label="Indicadores de confianza">
            {trustItems.map((item) => (
              <div key={item.text} className="cta-trust__item">
                <Icon name={item.icon} size={16} />
                {item.text}
              </div>
            ))}
          </div>
        </div>
      </div>
    </section>
  )
}

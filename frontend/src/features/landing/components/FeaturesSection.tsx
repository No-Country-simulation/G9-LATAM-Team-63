import { features } from '../data/features'
import { FeatureIcon } from '../../../shared/components/Icons'

export default function FeaturesSection() {
  return (
    <section id="features" className="features">
      <div className="container">
        <div className="features__header">
          <div className="section-label">¿Por qué EnergiAI?</div>
          <h2 className="section-title">
            Todo lo que necesitas para{' '}
            <span className="gradient-text">optimizar tu energía</span>
          </h2>
          <p className="section-subtitle" style={{ margin: '0 auto' }}>
            Una plataforma completa que transforma datos de consumo en información
            clara, recomendaciones accionables y ahorros reales.
          </p>
        </div>

        <div className="features__grid stagger-children">
          {features.map((f) => (
            <div key={f.title} className="glass-card feature-card fade-in-up">
              <FeatureIcon name={f.icon} className={f.iconClass} />
              <h3 className="feature-card__title">{f.title}</h3>
              <p className="feature-card__description">{f.description}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}

import { techs } from '../data/techs'
import { TechIcon, Icon } from '../../../shared/components/Icons'

export default function TechStack() {
  return (
    <section id="tech" className="tech-section">
      <div className="container">
        <div className="tech-section__header">
          <div className="section-label">Stack Tecnológico</div>
          <h2 className="section-title">
            Construido con tecnologías{' '}
            <span className="gradient-text">de vanguardia</span>
          </h2>
          <p className="section-subtitle" style={{ margin: '0 auto' }}>
            Cada capa de la solución fue seleccionada para garantizar rendimiento,
            escalabilidad y precisión en los resultados.
          </p>
        </div>

        <div className="tech-grid stagger-children">
          {techs.map((t) => (
            <div key={t.name} className="glass-card tech-card fade-in-up">
              <TechIcon name={t.icon} />
              <h3 className="tech-card__name">{t.name}</h3>
              <p className="tech-card__desc">{t.desc}</p>
            </div>
          ))}
        </div>

        <div className="glass-card oci-highlight" role="complementary" aria-label="Integración con Oracle Cloud Infrastructure">
          <div className="oci-highlight__icon" aria-hidden="true">
            <Icon name="cloud" size={28} />
          </div>
          <div>
            <h3 className="oci-highlight__title">
              Integración completa con Oracle Cloud Infrastructure (OCI)
            </h3>
            <p className="oci-highlight__desc">
              La solución utiliza <strong style={{ color: 'var(--color-accent-secondary)' }}>Object Storage</strong> para
              almacenar modelos serializados, <strong style={{ color: 'var(--color-accent-secondary)' }}>OCI Compute</strong> para
              alojar la API Spring Boot y <strong style={{ color: 'var(--color-accent-secondary)' }}>OCI Functions</strong> para
              procesamiento de análisis en batch.
            </p>
          </div>
          <div className="oci-highlight__badges">
            {['Object Storage', 'OCI Compute', 'OCI Functions'].map((s) => (
              <span key={s} className="oci-badge">
                <Icon name="check" size={12} style={{ marginRight: 4, verticalAlign: 'middle' }} />
                {s}
              </span>
            ))}
          </div>
        </div>
      </div>
    </section>
  )
}

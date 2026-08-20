import { techs } from '../data/techs'
import { TechIcon } from '../../../shared/components/Icons'

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

      </div>
    </section>
  )
}

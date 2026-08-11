import { cases } from '../data/cases'
import { AvatarIcon } from '../../../shared/components/Icons'

export default function UseCases() {
  return (
    <section className="usecases">
      <div className="container">
        <div className="usecases__header">
          <div className="section-label">Casos de uso</div>
          <h2 className="section-title">
            Un perfil para cada{' '}
            <span className="gradient-text">patrón de consumo</span>
          </h2>
          <p className="section-subtitle" style={{ margin: '0 auto' }}>
            Ejemplos ilustrativos de cómo el análisis clasifica distintos
            tipos de inmueble y genera recomendaciones accionables.
          </p>
        </div>

        <div className="usecases__grid stagger-children">
          {cases.map((c) => (
            <div key={c.title} className="glass-card usecase-card fade-in-up">
              <div className="usecase-card__top">
                <AvatarIcon name={c.avatar} />
                <span className={`profile-badge ${c.profileClass}`} aria-label={`Perfil: ${c.profile}`}>
                  {c.profile}
                </span>
              </div>

              <h3 className="usecase-card__title">{c.title}</h3>
              <p className="usecase-card__description">{c.description}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}

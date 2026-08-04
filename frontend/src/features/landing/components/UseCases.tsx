import { cases } from '../data/cases'
import { AvatarIcon } from '../../../shared/components/Icons'

export default function UseCases() {
  return (
    <section className="usecases">
      <div className="container">
        <div className="usecases__header">
          <div className="section-label">Casos de uso</div>
          <h2 className="section-title">
            Resultados reales,{' '}
            <span className="gradient-text">impacto comprobado</span>
          </h2>
          <p className="section-subtitle" style={{ margin: '0 auto' }}>
            Hogares, restaurantes y negocios ya están reduciendo sus costos
            energéticos con las recomendaciones de EnergiAI.
          </p>
        </div>

        <div className="usecases__grid stagger-children">
          {cases.map((c) => (
            <div key={c.author} className="glass-card usecase-card fade-in-up">
              <div className="usecase-card__top">
                <AvatarIcon name={c.avatar} />
                <div>
                  <span className="usecase-card__savings" aria-label={`${c.savings} de ${c.savingsLabel}`}>
                    {c.savings}
                  </span>
                  <span className="usecase-card__savings-label">{c.savingsLabel}</span>
                </div>
              </div>

              <blockquote className="usecase-card__quote">{c.quote}</blockquote>

              <div className="usecase-card__footer">
                <div>
                  <div className="usecase-card__author">{c.author}</div>
                  <div className="usecase-card__role">{c.role}</div>
                </div>
                <span className={`profile-badge ${c.profileClass}`} aria-label={`Perfil: ${c.profile}`}>
                  {c.profile}
                </span>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}

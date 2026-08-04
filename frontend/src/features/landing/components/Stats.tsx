import { stats } from '../data/stats'

export default function Stats() {
  return (
    <section className="stats-section" aria-label="Estadísticas de EnergiAI">
      <div className="container">
        <div className="stats-grid stagger-children">
          {stats.map((s) => (
            <div key={s.label} className="stat-card fade-in-up" role="group" aria-label={`${s.label}: ${s.number}`}>
              <div
                className="stat-card__number"
                style={{
                  background: s.gradient,
                  WebkitBackgroundClip: 'text',
                  WebkitTextFillColor: 'transparent',
                  backgroundClip: 'text',
                }}
              >
                {s.number}
              </div>
              <div className="stat-card__label">{s.label}</div>
              <div className="stat-card__desc">{s.desc}</div>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}

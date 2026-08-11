import Button from '../../../shared/components/Button'
import { Icon } from '../../../shared/components/Icons'

export default function Hero() {
  return (
    <section id="hero" className="hero">
      <div className="hero__bg-grid" aria-hidden="true" />
      <div className="hero__orb1" aria-hidden="true" />
      <div className="hero__orb2" aria-hidden="true" />

      <div className="container">
        <div className="hero__inner">
          <div className="hero__content">
            <div className="hero__badge" aria-label="Powered by Machine Learning">
              <span className="hero__badge-dot" aria-hidden="true" />
              Analiza tu consumo con IA
            </div>

            <h1 className="hero__title">
              Convierte tus datos de consumo en{' '}
              <span>ahorro real</span>
            </h1>

            <p className="hero__description">
              EnergiAI analiza tus hábitos energéticos usando Machine Learning para
              clasificar tu perfil, identificar desperdicios y recomendarte acciones
              concretas que reducen tu factura mes a mes.
            </p>

            <div className="hero__actions">
              <Button to="/analizar" variant="primary">
                Analizar mi consumo
              </Button>
              <a href="#how-it-works" className="btn-secondary">
                Cómo funciona
              </a>
            </div>
          </div>

          <div className="hero__visual">
            <div className="dashboard__float-badge float-badge--savings" aria-hidden="true">
              <div className="float-badge__icon float-badge__icon--blue">
                <Icon name="dollar" size={16} />
              </div>
              <div>
                <span className="float-badge__value">-$47/mes</span>
                <span className="float-badge__label">Ahorro potencial</span>
              </div>
            </div>

            <div className="dashboard__float-badge float-badge--alert" aria-hidden="true">
              <div className="float-badge__icon float-badge__icon--amber">
                <Icon name="alert" size={16} />
              </div>
              <div>
                <span className="float-badge__value" style={{ color: 'var(--color-accent-warn)' }}>Pico detectado</span>
                <span className="float-badge__label">18:00 – 22:00 h</span>
              </div>
            </div>

            <div className="hero__dashboard" role="presentation" aria-label="Vista previa del análisis energético">
              <div className="dashboard__header">
                <div>
                  <p className="dashboard__title">Tu perfil energético</p>
                  <strong style={{ fontFamily: 'var(--font-primary)', fontSize: '1rem', color: 'var(--color-text-primary)' }}>
                    Análisis IA
                  </strong>
                </div>
                <div className="dashboard__badge" style={{ color: 'var(--color-accent-warn)', background: 'rgba(217, 119, 6, 0.12)' }}>
                  <Icon name="check" size={12} style={{ marginRight: 4, verticalAlign: 'middle' }} />
                  Moderado
                </div>
              </div>

              <div className="dashboard__gauge-wrapper">
                <svg className="gauge-svg" viewBox="0 0 200 120" aria-label="Confianza del modelo: 70%">
                  <defs>
                    <linearGradient id="gaugeGradient" x1="0%" y1="0%" x2="100%" y2="0%">
                      <stop offset="0%" stopColor="#1e40af" />
                      <stop offset="100%" stopColor="#2563eb" />
                    </linearGradient>
                  </defs>
                  <path d="M 20 110 A 80 80 0 1 1 180 110" className="gauge__track" />
                  <path d="M 20 110 A 80 80 0 1 1 180 110" className="gauge__fill" />
                  <text x="100" y="95" textAnchor="middle" className="gauge__label">70</text>
                  <text x="100" y="112" textAnchor="middle" className="gauge__sublabel">% confianza</text>
                </svg>
              </div>

              <div className="dashboard__metrics">
                <div className="metric-tile metric-tile--blue">
                  <span className="metric-tile__value">420 kWh</span>
                  <span className="metric-tile__label">Consumo</span>
                </div>
                <div className="metric-tile metric-tile--sky">
                  <span className="metric-tile__value">$315</span>
                  <span className="metric-tile__label">Costo est.</span>
                </div>
                <div className="metric-tile metric-tile--amber">
                  <span className="metric-tile__value">15%</span>
                  <span className="metric-tile__label">Ahorro</span>
                </div>
              </div>

              <div aria-label="Distribución de consumo por equipo">
                <div className="energy-bar">
                  <span className="energy-bar__label">Climatiz.</span>
                  <div className="energy-bar__track">
                    <div className="energy-bar__fill" style={{ width: '68%', background: 'linear-gradient(90deg, #1e40af, #2563eb)' }} />
                  </div>
                  <span className="energy-bar__value" style={{ color: 'var(--color-accent-primary)' }}>68%</span>
                </div>
                <div className="energy-bar">
                  <span className="energy-bar__label">Iluminac.</span>
                  <div className="energy-bar__track">
                    <div className="energy-bar__fill" style={{ width: '22%', background: 'linear-gradient(90deg, #2563eb, #3b82f6)' }} />
                  </div>
                  <span className="energy-bar__value" style={{ color: 'var(--color-accent-secondary)' }}>22%</span>
                </div>
                <div className="energy-bar">
                  <span className="energy-bar__label">Otros</span>
                  <div className="energy-bar__track">
                    <div className="energy-bar__fill" style={{ width: '10%', background: 'linear-gradient(90deg, #d97706, #dc2626)' }} />
                  </div>
                  <span className="energy-bar__value" style={{ color: 'var(--color-accent-warn)' }}>10%</span>
                </div>
              </div>

              <div className="dashboard__classification">
                <div className="classification__tag">
                  <Icon name="check" size={14} style={{ marginRight: 6, verticalAlign: 'middle' }} />
                  Moderado
                </div>
                <span className="classification__info">Clasificación IA · 70% confianza</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  )
}

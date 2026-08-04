export default function ApiDemo() {
  return (
    <section id="demo" className="demo-section">
      <div className="container">
        <div className="demo-section__header">
          <div className="section-label">Demo API</div>
          <h2 className="section-title">
            Integración simple,{' '}
            <span className="gradient-text">resultados inmediatos</span>
          </h2>
          <p className="section-subtitle" style={{ margin: '0 auto' }}>
            Una sola llamada POST te devuelve clasificación, probabilidad,
            recomendaciones y estimación financiera en formato JSON listo para consumir.
          </p>
        </div>

        <div className="demo-section__inner">
          <div className="demo__input-card" role="region" aria-label="Ejemplo de request a la API">
            <div className="demo__card-header">
              <div className="demo__card-dot" />
              <div className="demo__card-dot" />
              <div className="demo__card-dot" />
              <span className="demo__card-title">POST /api/analisis</span>
            </div>
            <pre className="code-block" aria-label="JSON de entrada de la API">
              <code>
                <span className="code-bracket">{'{'}</span>{'\n'}
                {'  '}<span className="code-key">"consumoKwh"</span>:{' '}
                <span className="code-value-num">420</span>,{'\n'}
                {'  '}<span className="code-key">"usoHorarioPico"</span>:{' '}
                <span className="code-value-bool">true</span>,{'\n'}
                {'  '}<span className="code-key">"cantidadEquipos"</span>:{' '}
                <span className="code-value-num">10</span>,{'\n'}
                {'  '}<span className="code-key">"tipoInmueble"</span>:{' '}
                <span className="code-value">"Casa"</span>,{'\n'}
                {'  '}<span className="code-key">"horasAltoConsumo"</span>:{' '}
                <span className="code-value-num">8</span>{'\n'}
                <span className="code-bracket">{'}'}</span>
              </code>
            </pre>

            <div className="demo-endpoints">
              <p className="demo-endpoints__label">Endpoints disponibles</p>
              <div className="demo-endpoints__list">
                {[
                  { method: 'POST', path: '/api/analisis', color: '#1e40af' },
                  { method: 'GET', path: '/api/analisis/{id}', color: '#2563eb' },
                  { method: 'POST', path: '/api/auth/login', color: '#7c3aed' },
                ].map((ep) => (
                  <div key={ep.path} className="demo-endpoint">
                    <span
                      className="demo-endpoint__method"
                      style={{
                        color: ep.color,
                        background: `${ep.color}18`,
                        border: `1px solid ${ep.color}40`,
                      }}
                    >
                      {ep.method}
                    </span>
                    <code className="demo-endpoint__path">{ep.path}</code>
                  </div>
                ))}
              </div>
            </div>
          </div>

          <div className="demo__output" role="region" aria-label="Ejemplos de respuesta de la API">
            <div className="output-card">
              <p className="output-card__label">Clasificación energética</p>
              <div className="output-card__content">
                <div>
                  <div className="output-card__main" style={{ color: 'var(--color-accent-danger)' }}>
                    Ineficiente
                  </div>
                  <div className="output-card__sub">
                    <span style={{ fontFamily: 'var(--font-primary)', fontWeight: 600, color: 'var(--color-accent-primary)' }}>
                      81%
                    </span>{' '}
                    de confianza del modelo
                  </div>
                </div>
                <span className="profile-badge profile-badge--ineficiente" aria-label="Perfil Ineficiente">
                  Ineficiente
                </span>
              </div>
              <div className="prob-bar" aria-label="Barra de probabilidad al 81%">
                <div className="prob-bar__fill" style={{ width: '81%' }} />
              </div>
            </div>

            <div className="output-card">
              <p className="output-card__label">Recomendaciones de optimización</p>
              <ul className="recs-list" aria-label="Lista de recomendaciones">
                <li>Reducir el uso de equipos durante los horarios pico (18–22 h)</li>
                <li>Evaluar equipos con alto consumo energético para reemplazo</li>
                <li>Distribuir actividades de mayor consumo a lo largo del día</li>
                <li>Instalar termostato inteligente en sistema de climatización</li>
              </ul>
            </div>

            <div className="output-card" style={{ borderColor: 'rgba(30, 64, 175, 0.15)' }}>
              <p className="output-card__label">Estimación financiera</p>
              <div className="output-card__content">
                <div>
                  <div className="output-card__main" style={{ color: 'var(--color-accent-primary)', fontSize: '1.8rem' }}>
                    $315.00
                  </div>
                  <div className="output-card__sub">costo estimado mensual · @$0.75/kWh</div>
                </div>
                <div style={{ textAlign: 'right' }}>
                  <span style={{ fontFamily: 'var(--font-primary)', fontSize: '1rem', fontWeight: 700, color: 'var(--color-accent-primary)', display: 'block' }}>
                    -$56
                  </span>
                  <span style={{ fontSize: '0.7rem', color: 'var(--color-text-secondary)' }}>ahorro posible</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  )
}

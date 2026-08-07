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
                <span className="code-value-num">8</span>,{'\n'}
                {'  '}<span className="code-key">"numeroHabitantes"</span>:{' '}
                <span className="code-value-num">3</span>,{'\n'}
                {'  '}<span className="code-key">"antiguedadInmueble"</span>:{' '}
                <span className="code-value-num">10</span>,{'\n'}
                {'  '}<span className="code-key">"calefaccion"</span>:{' '}
                <span className="code-value-bool">false</span>,{'\n'}
                {'  '}<span className="code-key">"aireAcondicionado"</span>:{' '}
                <span className="code-value-bool">true</span>{'\n'}
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

          <div className="demo__input-card" role="region" aria-label="Ejemplo de respuesta de la API">
            <div className="demo__card-header">
              <div className="demo__card-dot" />
              <div className="demo__card-dot" />
              <div className="demo__card-dot" />
              <span className="demo__card-title">200 · Respuesta</span>
            </div>
            <pre className="code-block" aria-label="JSON de respuesta de la API">
              <code>
                <span className="code-bracket">{'{'}</span>{'\n'}
                {'  '}<span className="code-key">"categoria"</span>:{' '}
                <span className="code-value">"Moderado"</span>,{'\n'}
                {'  '}<span className="code-key">"probabilidad"</span>:{' '}
                <span className="code-value-num">0.7</span>,{'\n'}
                {'  '}<span className="code-key">"recomendaciones"</span>:{' '}
                <span className="code-bracket">[</span>{'\n'}
                {'    '}<span className="code-value">"Identificar oportunidades de ahorro en horarios pico"</span>,{'\n'}
                {'    '}<span className="code-value">"Revisar equipos individuales con mayor consumo para optimizar el ratio"</span>{'\n'}
                {'  '}<span className="code-bracket">]</span>,{'\n'}
                {'  '}<span className="code-key">"costo_estimado_mensual"</span>:{' '}
                <span className="code-value-num">315.0</span>,{'\n'}
                {'  '}<span className="code-key">"idAnalisis"</span>:{' '}
                <span className="code-value-num">1</span>{'\n'}
                <span className="code-bracket">{'}'}</span>
              </code>
            </pre>

            <div className="demo-endpoints">
              <p className="demo-endpoints__label">Contrato de respuesta</p>
              <div className="demo-endpoints__list">
                <div className="demo-endpoint">
                  <span className="demo-endpoint__method" style={{ color: '#059669', background: '#05966918', border: '1px solid #05966940' }}>
                    ML
                  </span>
                  <code className="demo-endpoint__path">probabilidad por perfil (distancias) con el modelo de ciencia de datos</code>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  )
}

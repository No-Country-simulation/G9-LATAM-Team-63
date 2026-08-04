import { Link } from 'react-router-dom'

const faqs = [
  {
    q: '¿Cómo se calcula mi perfil energético?',
    a: 'Utilizamos modelos de Machine Learning entrenados con datos de consumo reales. Analizamos tu consumo mensual, cantidad de equipos, horarios de uso y tipo de inmueble para clasificar tu perfil en Eficiente, Moderado o Ineficiente.',
  },
  {
    q: '¿Qué tan precisa es la clasificación?',
    a: 'Nuestros modelos alcanzan un 97% de precisión en clasificación de perfiles, validados con datos de consumo de hogares y pequeños establecimientos de la región.',
  },
  {
    q: '¿Cómo se estima el costo mensual?',
    a: 'Usamos una tarifa de referencia de $0.75/kWh, basada en el promedio de la región. Multiplicamos tu consumo mensual en kWh por esta tarifa para obtener una estimación clara y transparente.',
  },
  {
    q: '¿Mis datos están seguros?',
    a: 'Sí. Todos los datos se procesan de forma anónima y se almacenan de manera segura en Oracle Cloud Infrastructure, cumpliendo con estándares de protección de datos.',
  },
  {
    q: '¿Puedo integrar la API con mi sistema?',
    a: 'Sí. La API REST está documentada y lista para integrar. Un solo endpoint POST /api/analisis devuelve toda la información en formato JSON.',
  },
  {
    q: '¿El análisis es gratuito?',
    a: 'Sí, completamente gratuito. Puedes realizar todos los análisis que necesites sin costo alguno.',
  },
]

export default function HelpPage() {
  return (
    <section className="history-page" style={{ paddingTop: '120px' }}>
      <div className="container">
        <div className="history-page__header">
          <div className="section-label">Ayuda</div>
          <h1 className="section-title">
            Preguntas{' '}
            <span className="gradient-text">frecuentes</span>
          </h1>
          <p className="section-subtitle" style={{ margin: '0 auto' }}>
            Todo lo que necesitas saber sobre EnergiAI y cómo aprovechar al máximo
            el análisis de consumo energético.
          </p>
        </div>

        <div className="results-grid" style={{ maxWidth: 800 }}>
          {faqs.map((faq, i) => (
            <div
              key={i}
              className="glass-card result-card result-card--full"
              style={{ padding: 'var(--space-md) var(--space-lg)' }}
            >
              <h3 style={{
                fontFamily: 'var(--font-primary)',
                fontSize: '1rem',
                fontWeight: 600,
                marginBottom: 8,
                color: 'var(--color-text-primary)',
              }}>
                {faq.q}
              </h3>
              <p style={{ fontSize: '0.88rem', color: 'var(--color-text-secondary)', lineHeight: 1.7 }}>
                {faq.a}
              </p>
            </div>
          ))}
        </div>

        <div className="results-actions">
          <Link to="/analizar" className="btn-primary">
            Analizar mi consumo
          </Link>
        </div>
      </div>
    </section>
  )
}

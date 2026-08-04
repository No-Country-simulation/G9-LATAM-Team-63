import ConsumptionForm from '../components/ConsumptionForm'

export default function AnalysisPage() {
  return (
    <section className="form-page">
      <div className="container">
        <div className="form-page__header">
          <div className="section-label">Análisis energético</div>
          <h1 className="section-title">
            Analiza tu{' '}
            <span className="gradient-text">consumo eléctrico</span>
          </h1>
          <p className="section-subtitle" style={{ margin: '0 auto' }}>
            Completa los siguientes datos y descubre tu perfil energético con
            recomendaciones personalizadas.
          </p>
        </div>

        <div className="form-card">
          <h2 className="form-card__title">Datos de consumo</h2>
          <p className="form-card__subtitle">
            Toda la información es procesada de forma anónima y segura.
          </p>
          <ConsumptionForm />
        </div>
      </div>
    </section>
  )
}

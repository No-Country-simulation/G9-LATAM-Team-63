import { steps } from '../data/steps'
import { StepIcon } from '../../../shared/components/Icons'

export default function HowItWorks() {
  return (
    <section id="how-it-works" className="how-it-works">
      <div className="how-it-works__bg" aria-hidden="true" />

      <div className="container">
        <div className="how-it-works__header">
          <div className="section-label">¿Cómo funciona?</div>
          <h2 className="section-title">
            Tres pasos hacia la{' '}
            <span className="gradient-text">eficiencia</span>
          </h2>
          <p className="section-subtitle" style={{ margin: '0 auto' }}>
            En menos de 60 segundos obtendrás un diagnóstico completo de tu consumo
            eléctrico con recomendaciones prioritarias para empezar a ahorrar.
          </p>
        </div>

        <div className="steps">
          {steps.map((step, index) => (
            <div key={step.number} style={{ display: 'contents' }}>
              <div className="step glass-card fade-in-up">
                <div className="step__number" aria-label={`Paso ${step.number}`}>
                  <StepIcon name={step.icon} />
                  <span className="step__badge" aria-hidden="true">
                    {step.number}
                  </span>
                </div>
                <h3 className="step__title">{step.title}</h3>
                <p className="step__description">{step.description}</p>
              </div>

              {index < steps.length - 1 && (
                <div className="step-connector" aria-hidden="true">
                  <svg viewBox="0 0 40 40" fill="none">
                    <path
                      d="M8 20 H32 M24 12 L32 20 L24 28"
                      stroke="currentColor"
                      strokeWidth="2"
                      strokeLinecap="round"
                      strokeLinejoin="round"
                    />
                  </svg>
                </div>
              )}
            </div>
          ))}
        </div>

        <div className="steps-cta">
          <a href="/analizar" className="btn-primary">
            Analizar mi consumo ahora →
          </a>
        </div>
      </div>
    </section>
  )
}

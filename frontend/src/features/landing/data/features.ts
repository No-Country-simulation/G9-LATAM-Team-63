import type { IconName } from '../../../shared/components/Icons'

interface Feature {
  icon: IconName
  iconClass: string
  title: string
  description: string
}

export const features: Feature[] = [
  {
    icon: 'chart',
    iconClass: 'feature-card__icon--blue',
    title: 'Diagnóstico completo',
    description:
      'Analizamos tu consumo mensual, horarios de uso, cantidad de equipos y tipo de inmueble para generar un perfil energético detallado con precisión de IA.',
  },
  {
    icon: 'lightbulb',
    iconClass: 'feature-card__icon--sky',
    title: 'Recomendaciones personalizadas',
    description:
      'Recibe sugerencias concretas y accionables para reducir tu consumo, desde ajustes de horarios hasta recomendaciones de equipos eficientes.',
  },
  {
    icon: 'dollar',
    iconClass: 'feature-card__icon--amber',
    title: 'Estimación financiera',
    description:
      'Conoce exactamente cuánto estás gastando y proyecta tu ahorro potencial con base en una tarifa de referencia transparente de $0.75/kWh.',
  },
  {
    icon: 'robot',
    iconClass: 'feature-card__icon--purple',
    title: 'Clasificación por IA',
    description:
      'Modelos de Machine Learning entrenados con datos reales clasifican tu perfil en Eficiente, Moderado o Ineficiente con alta precisión.',
  },
  {
    icon: 'plug',
    iconClass: 'feature-card__icon--green',
    title: 'Identificación de desperdicios',
    description:
      'Detectamos equipos y hábitos que más impactan tu factura, incluyendo consumo en horarios pico y uso ineficiente de climatización.',
  },
  {
    icon: 'cloud',
    iconClass: 'feature-card__icon--teal',
    title: 'Infraestructura en la nube',
    description:
      'Desplegado sobre Oracle Cloud Infrastructure garantizando disponibilidad, escalabilidad y seguridad de tus datos energéticos.',
  },
]

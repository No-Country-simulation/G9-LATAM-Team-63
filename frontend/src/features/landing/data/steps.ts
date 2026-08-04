import type { IconName } from '../../../shared/components/Icons'

interface Step {
  number: string
  icon: IconName
  title: string
  description: string
}

export const steps: Step[] = [
  {
    number: '01',
    icon: 'clipboard',
    title: 'Ingresa tus datos',
    description:
      'Completa un formulario simple con tu consumo mensual en kWh, cantidad de equipos, horarios de uso y tipo de inmueble.',
  },
  {
    number: '02',
    icon: 'zap',
    title: 'Análisis inteligente',
    description:
      'Nuestro motor de IA procesa tu información y clasifica tu perfil energético utilizando modelos predictivos de Machine Learning.',
  },
  {
    number: '03',
    icon: 'trend-up',
    title: 'Resultados y acciones',
    description:
      'Obtén tu clasificación, estimación de costos, factores clave de consumo y recomendaciones prioritarias para empezar a ahorrar.',
  },
]

import type { IconName } from '../../../shared/components/Icons'

interface UseCase {
  avatar: IconName
  title: string
  description: string
  profile: 'Eficiente' | 'Moderado' | 'Ineficiente'
  profileClass: string
}

// Escenarios ilustrativos de clasificación, coherentes con las reglas del
// análisis energético. No son testimonios ni datos de usuarios reales.
export const cases: UseCase[] = [
  {
    avatar: 'house',
    title: 'Casa familiar',
    description:
      'Consumo de 420 kWh con 10 equipos y aire acondicionado, concentrado en horario pico (18–22 h). Se recomienda desplazar el consumo y revisar los equipos de mayor gasto.',
    profile: 'Moderado',
    profileClass: 'profile-badge--moderado',
  },
  {
    avatar: 'store',
    title: 'Local comercial',
    description:
      'Más de 7 horas diarias de alto consumo y un ratio de kWh por equipo elevado. Se sugiere reducir las horas pico y evaluar el reemplazo de equipos antiguos.',
    profile: 'Ineficiente',
    profileClass: 'profile-badge--ineficiente',
  },
  {
    avatar: 'building',
    title: 'Oficina',
    description:
      'Consumo contenido por equipo y pocas horas de alta demanda. El perfil se mantiene con buenas prácticas: solo requiere monitoreo periódico del consumo.',
    profile: 'Eficiente',
    profileClass: 'profile-badge--eficiente',
  },
]

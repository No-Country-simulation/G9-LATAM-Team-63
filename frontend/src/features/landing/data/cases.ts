import type { IconName } from '../../../shared/components/Icons'

interface UseCase {
  avatar: IconName
  savings: string
  savingsLabel: string
  quote: string
  author: string
  role: string
  profile: 'Eficiente' | 'Moderado' | 'Ineficiente'
  profileClass: string
}

export const cases: UseCase[] = [
  {
    avatar: 'house',
    savings: '$73',
    savingsLabel: 'ahorro mensual',
    quote:
      '"Descubrí que mi aire acondicionado representaba el 62% de mi consumo en horas pico. Con los cambios sugeridos por EnergiAI reduje mi factura un 35% en el primer mes."',
    author: 'María Rodríguez',
    role: 'Residencial · Casa familiar, Buenos Aires',
    profile: 'Moderado',
    profileClass: 'profile-badge--moderado',
  },
  {
    avatar: 'store',
    savings: '$214',
    savingsLabel: 'ahorro mensual',
    quote:
      '"Nuestro restaurante pasó de perfil Ineficiente a Eficiente en 3 meses. La IA detectó que dejábamos equipos encendidos fuera de horario. Ahora ahorramos $214 al mes."',
    author: 'Carlos Méndez',
    role: 'Comercio · Restaurante, Ciudad de México',
    profile: 'Eficiente',
    profileClass: 'profile-badge--eficiente',
  },
  {
    avatar: 'building',
    savings: '$89',
    savingsLabel: 'ahorro mensual',
    quote:
      '"Integramos la API en menos de una hora. Ahora tenemos análisis automáticos en nuestro dashboard interno. Las recomendaciones son claras, prácticas y fáciles de implementar."',
    author: 'Ana Torres',
    role: 'Tecnología · Startup, Lima',
    profile: 'Eficiente',
    profileClass: 'profile-badge--eficiente',
  },
]

// Estadísticas verificables con el funcionamiento real de la aplicación:
// perfiles del modelo, variables del análisis, lógica de ahorro y API local.
export const stats = [
  {
    number: '3',
    label: 'Perfiles de eficiencia',
    desc: 'Eficiente, Moderado e Ineficiente',
    gradient: 'var(--gradient-accent)',
  },
  {
    number: '9',
    label: 'Variables analizadas',
    desc: 'consumo, equipos, horarios e inmueble',
    gradient: 'linear-gradient(135deg, #2563eb, #3b82f6)',
  },
  {
    number: '5–30%',
    label: 'Ahorro potencial',
    desc: 'estimado según el perfil obtenido',
    gradient: 'linear-gradient(135deg, #3b82f6, #6366f1)',
  },
  {
    number: '<1s',
    label: 'Tiempo de respuesta',
    desc: 'de la API REST local',
    gradient: 'linear-gradient(135deg, #6366f1, #7c3aed)',
  },
]
